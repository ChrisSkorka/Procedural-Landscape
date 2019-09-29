package info.chris.skorka;

import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

public class Mesh {

    private static final int NOISE_TEXTURE_SIZE = 100;

    private Shader shader;
    private Transformation transformation = new Transformation();

    private Vertex[] vertices;
    private int[] indices;
    private int vertexBufferID, positionBufferID, indexBufferID, heightTextureID, noiseTextureID;

    private float[] heightTexture;
    private float[] noiseTexture;

    public static Mesh fromPerlinNoiseHeightMap(float left, float right, float bottom, float top, float height, int countX, int countY, float[]texture){

        Noise noise = new Noise(5, 0, height);

        float dx = (right - left) / (countX - 1);
        float dy = (top - bottom) / (countY - 1);

        Vertex[] vertices = new Vertex[countY * countX];
        int[] indices = new int[(countX-1) * (countY-1) * 4];

        for(int ix = 0; ix < countX; ix++){
            float x = left + ix * dx;
            for(int iy = 0; iy < countY; iy++){
                float y = bottom + iy * dy;

                float z = (float) noise.sample(x, y);

                int iv = iy * countX + ix;

                vertices[iv] = new Vertex(x, y, z);
            }
        }

        // construct quads indices
        for(int ix = 0; ix < countX-1; ix++){
            for(int iy = 0; iy < countY-1; iy++){

                int iv = 4 * (iy * (countX - 1) + ix);

                indices[iv + 0] = (iy + 0) * countX + ix + 0;
                indices[iv + 1] = (iy + 0) * countX + ix + 1;
                indices[iv + 2] = (iy + 1) * countX + ix + 1;
                indices[iv + 3] = (iy + 1) * countX + ix + 0;
            }
        }



        return new Mesh(vertices, indices, texture);
    }

    public static Mesh fromPlane(float left, float right, float bottom, float top, float height, float[]texture){
        return new Mesh(new Vertex[]{
                new Vertex(left, bottom, height),
                new Vertex( right, bottom, height),
                new Vertex( right,  top, height),
                new Vertex(left,  top, height),
        }, new int[]{
                0,1,2,3
        }, texture);
    }

    public Mesh(Vertex[]  vertices, int[] indices, float[] heightTexture){
        this.vertices = vertices;
        this.indices = indices;
        this.heightTexture = heightTexture;
        this.noiseTexture = getNoiseTexture(NOISE_TEXTURE_SIZE, NOISE_TEXTURE_SIZE);

        vertexBufferID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vertexBufferID);

        float[] positionData = new float[vertices.length * 3];
        for(int i = 0; i < vertices.length; i++){
            positionData[3*i + 0] = vertices[i].getX();
            positionData[3*i + 1] = vertices[i].getY();
            positionData[3*i + 2] = vertices[i].getZ();
        }

        positionBufferID = storeAttributeData(positionData, 0, 3);

        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
        indicesBuffer.put(indices).flip();

        indexBufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBufferID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        // height heightTexture
        FloatBuffer heightTextureBuffer = MemoryUtil.memAllocFloat(heightTexture.length);
        heightTextureBuffer.put(heightTexture).flip();

        heightTextureID = GL46.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_1D, heightTextureID);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexParameteri(GL11.GL_TEXTURE_1D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_1D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage1D(GL11.GL_TEXTURE_1D, 0, GL11.GL_RGBA, heightTexture.length/4, 0,GL11.GL_RGBA, GL11.GL_FLOAT, heightTextureBuffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_1D, 0);

        // noise heightTexture
        FloatBuffer noiseTextureBuffer = MemoryUtil.memAllocFloat(noiseTexture.length);
        noiseTextureBuffer.put(noiseTexture).flip();

        noiseTextureID = GL46.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, noiseTextureID);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, NOISE_TEXTURE_SIZE, NOISE_TEXTURE_SIZE, 0,GL11.GL_RGBA, GL11.GL_FLOAT, noiseTextureBuffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    private int storeAttributeData(float[] data, int index, int size){

        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();

        int bufferID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        return bufferID;
    }

    public void render(){
        GL30.glBindVertexArray(vertexBufferID);
        GL30.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBufferID);
        GL15.glActiveTexture(GL13.GL_TEXTURE0);
        GL15.glBindTexture(GL11.GL_TEXTURE_1D, heightTextureID);
        GL15.glActiveTexture(GL13.GL_TEXTURE1);
        GL15.glBindTexture(GL11.GL_TEXTURE_2D, noiseTextureID);

        if(shader != null) {
            shader.bind();
            shader.setUniform("transformation", transformation.toFlatArray());
        }
        GL11.glDrawElements(GL11.GL_QUADS, indices.length, GL11.GL_UNSIGNED_INT, 0);
        if(shader != null)
            shader.unbind();

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    private float[] getNoiseTexture(int x, int y){

        Random random = new Random();
        float[] texture = new float[x*y*4];

        for(int i = 0; i < x*y; i++){
            float v = random.nextFloat();
            texture[4*i + 0] = v;
            texture[4*i + 1] = v;
            texture[4*i + 2] = v;
            texture[4*i + 3] = 1;
        }

        return texture;
    }

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
    }

    public void set1DTexture(float[] texture){
        this.heightTexture = texture;
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public void setIndices(int[]indices) {
        this.indices = indices;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }

    public int getVertexBufferID() {
        return vertexBufferID;
    }

    public int getPositionBufferID() {
        return positionBufferID;
    }

    public int getIndexBufferID() {
        return indexBufferID;
    }

}
