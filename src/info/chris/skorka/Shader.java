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

    /**
     * create a shader from the vertex and fragment shader source files
     * @param shaderBaseBath file path of the shade's without the .vs.glsl or .fs.glsl extension
     */
    public Shader(String shaderBaseBath){

        // read shader sources
        vertexShaderSource = File.readFileToString(shaderBaseBath + ".vs.glsl");
        fragmentShaderSource = File.readFileToString(shaderBaseBath + ".fs.glsl");

        // register programs
        programID = GL20.glCreateProgram();
        vertexShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        fragmentShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        // compile vertex shader
        GL20.glShaderSource(vertexShaderID, vertexShaderSource);
        GL20.glCompileShader(vertexShaderID);

        // check for compilation errors
        if(GL20.glGetShaderi(vertexShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Failed compiling '" + shaderBaseBath + ".vs.glsl'");
            System.err.println(GL20.glGetShaderInfoLog(vertexShaderID, GL20.glGetShaderi(vertexShaderID, GL20.GL_INFO_LOG_LENGTH)));
            return;
        }

        // compile fragment shader
        GL20.glShaderSource(fragmentShaderID, fragmentShaderSource);
        GL20.glCompileShader(fragmentShaderID);

        // check for compilation errors
        if(GL20.glGetShaderi(fragmentShaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Failed compiling '" + shaderBaseBath + ".fs.glsl'");
            System.err.println(GL20.glGetShaderInfoLog(fragmentShaderID, GL20.glGetShaderi(fragmentShaderID, GL20.GL_INFO_LOG_LENGTH)));
            return;
        }

        // attach shade's to the program
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);

        // link and validate program as a whole
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);

        // check for linking errors
        if(GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.println("Failed linking shader '" + shaderBaseBath + "'");
            System.err.println(GL20.glGetProgramInfoLog(programID, GL20.glGetProgrami(programID, GL20.GL_INFO_LOG_LENGTH)));
            return;
        }

        // check for validation errors
        if(GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == GL11.GL_FALSE) {
            System.err.println("Failed validating shader '" + shaderBaseBath + "'");
            System.err.println(GL20.glGetProgramInfoLog(programID, GL20.glGetProgrami(programID, GL20.GL_INFO_LOG_LENGTH)));
            return;
        }

        // delete shaders after linking them to the program
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);

        available = true;
    }

    /**
     * get the location of a uniform
     * @param name uniform name
     * @return uniform location
     */
    public int getUniformLocation(String name) {
        return GL20.glGetUniformLocation(programID, name);
    }

    /**
     * set a mat4 uniform value
     * @param name
     * @param floatArray
     */
    public void setUniform(String name, float[] floatArray){
        FloatBuffer matrix = MemoryUtil.memAllocFloat(floatArray.length);
        matrix.put(floatArray).flip();
        GL20.glUniformMatrix4fv(getUniformLocation(name), true, matrix);
    }

    /**
     * bind shader program
     */
    public void bind(){
        if(available)
            GL20.glUseProgram(programID);
    }

    /**
     * unbind shader program
     */
    public void unbind(){
        GL20.glUseProgram(0);
    }

    /**
     * destroy shader program
     */
    public void destroy(){
        GL20.glDeleteProgram(programID);
    }

}
