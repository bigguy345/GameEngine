package goatee;

import goatee.constants.Resources;
import goatee.entities.*;
import goatee.renderEngine.Camera;
import goatee.renderEngine.ModelRenderer;
import goatee.renderEngine.VAOLoader;
import goatee.shaders.ShaderHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.time.Duration;
import java.time.Instant;

public class Main {
    public static int width = 1920;
    public static int height = 1080;
    public static Instant beginTime;
    public static Duration runTime = Duration.ofSeconds(0);
    public static float timeSinceStart;
    public static Sphere s;
    public static Light light;
    public static Entity miku;
    private static int FPS_CAP = 240;

    public static void main(String[] args) {
        beginTime = Instant.now();
        createDisplay();

        ModelRenderer renderer = new ModelRenderer();
        ShaderHelper.loadShaders();
        float scale = 0.5f;

        float[] vertices = {-0.5f, 0.5f, 0f, //V0
                -0.5f, -0.5f, 0f, //V1

                0.5f, -0.5f, 0f, //V2
                .5f, 0.5f, 0f, //V3

                //  -0.5f, -0.5f, 0f,
                //0f, 0.5f, 0f,
                //    0.5f, -0.5f, 0f
        };
        int[] indices = {0, 1, 2, 2, 3, 0};
        float[] textureCoords = {0, 0, 0, 1, 1, 1, 1, 0};
        Entity e = new Entity(0, 0f, 1.5f);
        e.loadModel(vertices, indices, new float[10], textureCoords, "catscreen");
        e.getModel().setTexture(Resources.TEXTURE_DIRECTORY + "cat.png", "png");
        e.shaderProgram = ShaderHelper.entityShader;

        Cube c = new Cube(0.8f, 0, 1.7f);


        s = new Sphere(1f, 72, 36);
        s.setPosition(-2f, 5f, 2f);
        s.setScale(scale, scale, scale);
        //s.getModel().setTexture(VAOLoader.loadTexture("4"));

        light = new Light(0, 5, 0);
        // light.getModel().setTexture("sun");

        Entity uvsphere = new Entity(1.7f, 0, 1.5f);
        uvsphere.loadModel(Resources.MODEL_DIRECTORY + "cube.obj");
        uvsphere.setScale(scale, scale, scale);
        Line.createAxisLines();

        miku = new Entity(0, 0, 0f);
        miku.loadModel(Resources.MODEL_DIRECTORY + "hatsune.obj");
        miku.setScale(0.1f);
        miku.setColor(1, 1, 1f);

        while (!Display.isCloseRequested()) {
            Camera.updateControls();
            Entity.updateAll();
            renderer.render();
            updateDisplay();
            updateTime();
        }
        exit();
    }

    public static void createDisplay() {
        ContextAttribs attribs = new ContextAttribs(3, 2);
        attribs.withForwardCompatible(true);
        attribs.withProfileCore(true);
        try {
            DisplayMode dm = new DisplayMode(width, height);
            Display.setLocation(50, 50);
            Display.setDisplayMode(dm);
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("Game Engine");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        GL11.glViewport(0, 0, width, height);
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
    }

    public static void updateTime() {
        runTime = Duration.between(beginTime, Instant.now());
        timeSinceStart = (float) (runTime.toMillis()) / 1000;
    }

    public static void exit() {

        System.out.println("(INFO): Exiting from main.");
        VAOLoader.cleanUp();
        ShaderHelper.cleanUp();
        Display.destroy();
        System.exit(0);
    }


    public static void createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        int vboid = GL15.glGenBuffers();
        GL30.glBindVertexArray(vaoID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboid);


        float[] vertices = new float[]{-0.5f, -0.5f, 0f, 0.5f, -0.5f, 0f, 0f, 0.5f, 0f};
        FloatBuffer b = BufferUtils.createFloatBuffer(vertices.length);
        b.put(vertices);
        b.flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, b, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);


        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoID);
        GL15.glDeleteBuffers(vboid);

    }
}