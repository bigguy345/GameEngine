package goatee.shaders;

import goatee.Main;
import goatee.entities.Entity;
import goatee.entities.Light;
import goatee.renderEngine.Camera;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class StaticShader extends ShaderProgram {
    public static StaticShader instance;
    public static StaticShader entityShader = new StaticShader("vertexShader.vert", "fragmentShader.frag");
    public static StaticShader lineShader = new StaticShader("lineVert.vsh", "lineFrag.frag");
    public static StaticShader sunShader = new StaticShader("sunVert.glsl", "sunFrag.glsl");

    private String vertexFile;
    private String fragFile;
    private int uniform_res;
    private int uniform_MVP;
    private int uniform_time;
    private int uniform_lightPosition;
    private int uniform_lightColor;
    private int uniform_transMat;
    private int uniform_hasTexture;
    private int uniform_color;


    public StaticShader(String vertexFile, String fragFile) {
        super(vertexFile, fragFile);
        this.vertexFile = vertexFile;
        this.fragFile = fragFile;
        instance = this;
        getAllUniformLocations();
        start();
        loadRes();
        stop();


    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    public void getAllUniformLocations() {
        uniform_res = super.getUniformLocation("res");
        uniform_MVP = super.getUniformLocation("mvp");
        uniform_time = super.getUniformLocation("time");
        uniform_lightPosition = super.getUniformLocation("lightPosition");
        uniform_lightColor = super.getUniformLocation("lightColor");
        uniform_transMat = super.getUniformLocation("transMat");
        uniform_hasTexture = super.getUniformLocation("hasTexture");
        uniform_color = super.getUniformLocation("objColor");


    }

    public void loadRes() {
        GL20.glUniform2f(uniform_res, Main.width, Main.height);
    }


    public void setTexture(boolean has) {
        GL20.glUniform1f(uniform_hasTexture, has ? 1 : 0);

    }

    public void loadMVP(Entity e) {
        loadMVP(e.mat.getMatrix());
        GL20.glUniform3f(uniform_color, e.getColor().x, e.getColor().y, e.getColor().z);

    }

    //    public void loadMVP(Vector3f translation, Vector3f rotation, Vector3f scale) {
//        GL20.glUniform1f(uniform_time, Main.time);
//
//        loadLight(Main.light);
//        loadTransMat(translation, rotation, scale);
//        super.loadMatrix(uniform_MVP, Camera.createMVP(transMat));
//    }
    public void loadMVP(Matrix4f transMat) {
        GL20.glUniform1f(uniform_time, Main.timeF);

        loadLight(Main.light);
        loadTransMat(transMat);
        super.loadMatrix(uniform_MVP, Camera.createMVP(transMat));
    }

    public void loadLight(Light l) {
        GL20.glUniform3f(uniform_lightPosition, l.position.x, l.position.y, l.position.z);
        GL20.glUniform3f(uniform_lightColor, l.color.x, l.color.y, l.color.z);
    }

    public void loadTransMat(Matrix4f transMat) {
        super.loadMatrix(uniform_transMat, transMat);
    }


}
