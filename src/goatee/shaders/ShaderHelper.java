package goatee.shaders;

import goatee.Main;
import goatee.constants.Resources;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

public abstract class ShaderHelper {
    private static List<Integer> programs = new ArrayList<>();
    public static int currentProgram;

    public static int entityShader;
    public static int lineShader;
    public static int sunShader;


    public static void loadShaders() {
        entityShader = createProgram("entity.vert", "entity.frag");
        lineShader = createProgram("line.vert", "line.frag");
        sunShader = createProgram("sun.vert", "sun.frag");
    }

    public static int createProgram(String vertexFile, String fragmentFile) {
        int vertexShaderID = createShader(Resources.SHADER_DIRECTORY + vertexFile, GL20.GL_VERTEX_SHADER);
        int fragmentShaderID = createShader(Resources.SHADER_DIRECTORY + fragmentFile, GL20.GL_FRAGMENT_SHADER);
        int programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);

        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);

        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);

        bindAttribute(0, "vertexPosition");
        bindAttribute(1, "textureCoords");
        bindAttribute(2, "vertexNormals");

        programs.add(programID);
        return programID;
    }

    private static int createShader(String file, int type) {

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

    public static void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(currentProgram, attribute, variableName);
    }

    public static void loadUniforms(int shader, IShaderUniform uniforms) {
        uniforms.load(shader);
    }

    public static void useShader(int shader, IShaderUniform uniforms) {
        GL20.glUseProgram(currentProgram = shader);

        if (shader != 0) { //loads all uniforms
            uniform1f("time", Main.timeSinceStart);
            uniformVec2("u_resolution", Main.width, Main.height);

            if (uniforms != null)
                loadUniforms(shader, uniforms);
        }
    }

    public static void useShader(int shader) {
        useShader(shader, null);
    }

    public static void releaseShader() {
        useShader(0);
    }

    public static void cleanUp() {
        for (Integer p : programs) {
            GL20.glDeleteProgram(p);
        }
    }

    //////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //Uniform helpers
    public static void uniform1f(String name, float x) {
        int uniformLocation = ARBShaderObjects.glGetUniformLocationARB(currentProgram, name);
        ARBShaderObjects.glUniform1fARB(uniformLocation, x);
    }

    public static void uniformArray(String name, float[] array) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length);
        buffer.put(array);
        buffer.flip();

        int uniformLocation = ARBShaderObjects.glGetUniformLocationARB(currentProgram, name);
        ARBShaderObjects.glUniform1ARB(uniformLocation, buffer);
    }

    public static void uniformArray(String name, int[] array) {
        IntBuffer buffer = BufferUtils.createIntBuffer(array.length);
        buffer.put(array);
        buffer.flip();
        int uniformLocation = ARBShaderObjects.glGetUniformLocationARB(currentProgram, name);
        ARBShaderObjects.glUniform1ARB(uniformLocation, buffer);
    }

    public static void uniformVec2(String name, float x, float y) {
        int uniformLocation = ARBShaderObjects.glGetUniformLocationARB(currentProgram, name);
        ARBShaderObjects.glUniform2fARB(uniformLocation, x, y);
    }

    public static void uniformVec3(String name, float x, float y, float z) {
        int uniformLocation = ARBShaderObjects.glGetUniformLocationARB(currentProgram, name);
        ARBShaderObjects.glUniform3fARB(uniformLocation, x, y, z);
    }

    public static void uniformVec4(String name, float x, float y, float z, float w) {
        int uniformLocation = ARBShaderObjects.glGetUniformLocationARB(currentProgram, name);
        ARBShaderObjects.glUniform4fARB(uniformLocation, x, y, z, w);
    }

    public static void uniformMatrix4x4(String name, Matrix4f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        matrix.store(buffer);
        buffer.flip();

        int uniformLocation = ARBShaderObjects.glGetUniformLocationARB(currentProgram, name);
        GL20.glUniformMatrix4(uniformLocation, false, buffer);

    }

    public static void loadTextureUnit(int textureUnit, int textureID) {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
        GL11.glBindTexture(GL_TEXTURE_2D, textureID);
        GL13.glActiveTexture(0);
    }

    public static void uniformTexture(String name, int textureUnit, int textureID) {
        ShaderHelper.loadTextureUnit(textureUnit, textureID);
        int uniformLocation = ARBShaderObjects.glGetUniformLocationARB(currentProgram, name);
        ARBShaderObjects.glUniform1iARB(uniformLocation, textureUnit);
    }

    public static void uniformTextureResolution(String name, int textureID) {
        int previousTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        GL11.glBindTexture(GL_TEXTURE_2D, textureID);

        float width = GL11.glGetTexLevelParameterf(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        float height = GL11.glGetTexLevelParameterf(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        uniformVec2(name, width, height);

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, previousTexture);
    }

    public static void uniformColor(String name, int color, float alpha) {
        float r = (color >> 16 & 255) / 255f;
        float g = (color >> 8 & 255) / 255f;
        float b = (color & 255) / 255f;
        uniformVec4(name, r, g, b, alpha);
    }
}
