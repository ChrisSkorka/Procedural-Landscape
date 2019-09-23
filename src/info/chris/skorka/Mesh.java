package info.chris.skorka;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

public class Mesh {

    private Shader shader;

    private Vertex[] vertices;
    private int[] indecies;
    private int vao, pbo, ibo;

    public static Mesh fromStochasticFractalHeightMap(float left, float right, float bottom, float top, float height, int countX, int countY, Shader shader){

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



        return new Mesh(vertices, indices, shader);
    }

    public Mesh(Vertex[]  vertices, int[] indices, Shader shader){
        this.vertices = vertices;
        this.indecies = indices;
        this.shader = shader;

        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3); // TODO change 3 to appropriate value
        float[] positionData = new float[vertices.length * 3]; // TODO change 3 to appropriate value
        for(int i = 0; i < vertices.length; i++){
            positionData[3*i + 0] = vertices[i].getX();
            positionData[3*i + 1] = vertices[i].getY();
            positionData[3*i + 2] = vertices[i].getZ();
        }
        positionBuffer.put(positionData).flip();

        pbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, pbo);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, positionBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indecies.length);
        indicesBuffer.put(indices).flip();

        ibo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void render(){
        GL30.glBindVertexArray(vao);
        GL30.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
        if(shader != null)
            shader.bind();
        GL11.glDrawElements(GL11.GL_QUADS, indecies.length, GL11.GL_UNSIGNED_INT, 0); // TODO confirm GL_UNSIGNED_INT
        if(shader != null)
            shader.unbind();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL30.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public int[] getIndecies() {
        return indecies;
    }

    public int getVao() {
        return vao;
    }

    public int getPbo() {
        return pbo;
    }

    public int getIbo() {
        return ibo;
    }

}
