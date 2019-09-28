package info.chris.skorka;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Shader {

    private String vertexShaderSource;
    private String fragmentShaderSource;

    private int vertexShaderID, fragmentShaderID, programID;

    private boolean available = false;

    public Shader(String shaderBaseBath){

        // read shader sources
        vertexShaderSource = File.readFileToString(shaderBaseBath + ".vs.glsl");
        fragmentShaderSource = File.readFileToString(shaderBaseBath + ".fs.glsl");

        // register programs
        programID = GL20.glCreateProgram();
        vertexShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        fragmentShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        GL20.glShaderSource(vertexShaderID, vertexShaderSource);
        GL20.glCompileShader(vertexShaderID);

        if(GL20.glGetShaderi(vertexShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Failed compiling '" + shaderBaseBath + ".vs.glsl'");
            return;
        }

        GL20.glShaderSource(fragmentShaderID, fragmentShaderSource);
        GL20.glCompileShader(fragmentShaderID);

        if(GL20.glGetShaderi(fragmentShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Failed compiling '" + shaderBaseBath + ".fs.glsl'");
            return;
        }

        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);

        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);

        if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Failed linking shader '" + shaderBaseBath + "'");
            return;
        }

        if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Failed validating shader '" + shaderBaseBath + "'");
            return;
        }

        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);

        available = true;
    }

    public int getUniformLocation(String name) {
        return GL20.glGetUniformLocation(programID, name);
    }

    public void setUniform(String name, float[] floatArray){
        FloatBuffer matrix = MemoryUtil.memAllocFloat(floatArray.length);
        matrix.put(floatArray).flip();
        GL20.glUniformMatrix4fv(getUniformLocation(name), true, matrix);
    }

    public void bind(){
        if(available)
            GL20.glUseProgram(programID);
    }

    public void unbind(){
        GL20.glUseProgram(0);
    }

    public void destroy(){
        GL20.glDeleteProgram(programID);
    }

}
