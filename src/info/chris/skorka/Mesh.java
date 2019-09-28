package info.chris.skorka;

import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

public class Mesh {

    private Shader shader;
    private Transformation transformation = new Transformation();

    private Vertex[] vertices;
    private int[] indices;
    private int vertexBufferID, positionBufferID, indexBufferID, textureID;

    private float[]colors = {
            252/255f, 250/255f, 242/255f, 1f,
            135/255f, 119/255f, 61/255f,  1f,
            31/255f,  166/255f, 87/255f,  1f,
            157/255f, 204/255f, 131/255f, 1f,
            204/255f, 204/255f, 131/255f, 1f,
    };

    public static Mesh fromStochasticFractalHeightMap(float left, float right, float bottom, float top, float height, int countX, int countY){

        Random random = new Random(0x123456789ABCDEFL);

        float dx = (right - left) / (countX - 1);
        float dy = (top - bottom) / (countY - 1);

        Vertex[] vertices = new Vertex[countY * countX];
        int[] indices = new int[(countX-1) * (countY-1) * 4];

        for(int ix = 0; ix < countX; ix++){
            float x = left + ix * dx;
            for(int iy = 0; iy < countY; iy++){
                float y = bottom + iy * dy;
                float z = random.nextFloat() * height;

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



        return new Mesh(vertices, indices);
    }

    public Mesh(Vertex[]  vertices, int[] indices){
        this.vertices = vertices;
        this.indices = indices;

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

        FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(colors.length);
        textureBuffer.put(colors).flip();

        textureID = GL46.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_1D, textureID);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexParameteri(GL11.GL_TEXTURE_1D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_1D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage1D(GL11.GL_TEXTURE_1D, 0, GL11.GL_RGBA, colors.length/4, 0,GL11.GL_RGBA, GL11.GL_FLOAT, textureBuffer);
        GL11.glBindTexture(GL11.GL_TEXTURE_1D, textureID);
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
        GL15.glBindTexture(GL11.GL_TEXTURE_1D, textureID);

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

    public void setShader(Shader shader) {
        this.shader = shader;
    }

    public void setTransformation(Transformation transformation) {
        this.transformation = transformation;
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
