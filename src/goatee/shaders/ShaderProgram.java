package goatee.shaders;

import goatee.Main;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class ShaderProgram {
    private static List<ShaderProgram> programs = new ArrayList<ShaderProgram>();

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    public ShaderProgram(String vertexFile, String fragmentFile) {
        programs.add(this);
        String loc = "src/goatee/shaders/";
        vertexShaderID = loadShader(loc + vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(loc + fragmentFile, GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();

        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);

        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        //  GL20.glBindAttribLocation(programID, 1, "position");

        // System.out.println(GL20.glGetAttribLocation(programID, "position"));

    }

    private static int loadShader(String file, int type) {

        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader r = new BufferedReader(new FileReader(file));
            String line;
            while (((line = r.readLine()) != null))
                shaderSource.append(line).append("\n");
            r.close();
        } catch (IOException e) {
            System.out.println("Couldn't read file!");
            e.printStackTrace();
        }

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            int logLength = GL20.glGetShaderi(shaderID, GL20.GL_INFO_LOG_LENGTH);
            System.out.println("Shader compilation error: " + GL20.glGetShaderInfoLog(shaderID, logLength));
            Main.exit();
        }
        return shaderID;
    }

    public static void cleanUp() {
        for (ShaderProgram p : programs) {
            GL20.glDetachShader(p.programID, p.vertexShaderID);
            GL20.glDetachShader(p.programID, p.fragmentShaderID);
            GL20.glDeleteProgram(p.programID);
        }
    }

    protected void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    protected abstract void bindAttributes();

    protected int getUniformLocation(String s) {
        return GL20.glGetUniformLocation(programID, s);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrixBuffer);

    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public int getProgramID() {
        return programID;
    }
}
