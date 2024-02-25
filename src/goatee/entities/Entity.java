package goatee.entities;

import goatee.Main;
import goatee.models.Model;
import goatee.renderEngine.VAOLoader;
import goatee.shaders.StaticShader;
import goatee.utils.TransMat;
import goatee.utils.u;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    public static List<Entity> entities = new ArrayList<>();
    public Model m;
    public Vector3f position = new Vector3f();
    public Vector3f rotation = new Vector3f();
    public Vector3f scale = new Vector3f();
    public Vector3f color = new Vector3f();
    public boolean renderPass = false;
    public TransMat mat = new TransMat();
    private StaticShader shaderProg;

    public Entity() {
        entities.add(this);
        setRotation(0, 0, 0);
        setScale(1, 1, 1);
        setColor(1, 0, 0);
    }

    public Entity(float x, float y, float z) {
        this();
        setPosition(x, y, z);

    }

    public static void updateAll() {
        for (Entity e : entities) {
            e.tick();
        }
    }

    public void tick() {
        handleTransMat();
        if (this instanceof Light) {
            rotateZ(Main.timeF * 20, 15.0f, Main.miku.position);
        } else if (this instanceof Sphere) {
            rotateZ(Main.timeF * 120, 2f, Main.light.position);
        }

    }

    public void handleTransMat() {
        mat.setIdentity(); //sets everything to the center, 1x scale, 0,0,0 translation from origin with no rotation
        mat.translate(position);

        mat.scale(scale.x, scale.y, scale.z);
        mat.rotate(rotation.x, 1, 0, 0);
        mat.rotate(rotation.y, 0, 1, 0);
        mat.rotate(rotation.z, 0, 0, 1);
    }

    public void rotateY(float a, float r, Vector3f origin) {
        a = u.toRad(a);
        float x = origin.x + (r * u.cos(a));
        float z = origin.z + (r * u.sin(a));
        setPosition(x, origin.y, z);
    }

    public void rotateZ(float a, float r, Vector3f origin) {
        a = u.toRad(a);
        float x = origin.x + (r * u.cos(a));
        float y = origin.y + (r * u.sin(a));
        setPosition(x, y, origin.z);
    }

    public void rotateX(float a, float r, Vector3f origin) {
        a = u.toRad(a);
        float y = origin.y + (r * u.cos(a));
        float z = origin.z + (r * u.sin(a));
        setPosition(origin.x, y, z);
    }

    public void move(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;

    }

    public void rotate(Vector3f rot) {
        rotation.x += rot.x;
        rotation.y += rot.y;
        rotation.z += rot.z;


    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void setScale(float x, float y, float z) {
        scale.x = x;
        scale.y = y;
        scale.z = z;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void loadModel(List<Vector3f> vertices, List<Integer> indices, List<Vector3f> normals, List<Float> tCoords, String modelName) {
        this.m = VAOLoader.loadToVao(vertices, indices, normals, tCoords, modelName);
        setShader(StaticShader.entityShader);
    }

    public void loadModel(float[] vertices, int[] indices, float[] normals, float[] tCoords, String modelName) {
        this.m = VAOLoader.loadToVao(vertices, indices, normals, tCoords, modelName);
        setShader(StaticShader.entityShader);
    }

    public void loadModel(String modelName) {
        this.m = VAOLoader.loadFromObj(modelName);
        setShader(StaticShader.entityShader);
    }

    public boolean hasTexture() {
        return m.getTexture() != -1;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Model getModel() {
        return m;
    }

    public void setModel(Model m) {
        this.m = m;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(float s) {
        setScale(s, s, s);
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(float r, float g, float b) {
        this.color.set(r, g, b);
    }

    public String toString() {
        return this.getClass().getName() + "[" + position.x + ", " + position.y + ", " + position.z + "] ";
    }

    public void lookAt(Vector3f center) {
        Vector3f direction = Vector3f.sub(center, position, null);
        Vector3f v = direction.normalise(null);
        rotation.x = (float) Math.toDegrees(Math.asin(v.y));

        rotation.y = (float) Math.toDegrees(Math.atan2(v.x, v.z));
        //  GL11.glRotatef(180, rotation.x, rotation.y, rotation.z);
        //System.out.println(rotation);
    }

    public Vector3f directionFromRotation(float pPitch, float pYaw) {
        float f = (float) Math.cos(-pYaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float f1 = (float) Math.sin(-pYaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float f2 = (float) -Math.cos(-pPitch * ((float) Math.PI / 180F));
        float f3 = (float) Math.sin(-pPitch * ((float) Math.PI / 180F));
        return new Vector3f((f1 * f2), f3, (f * f2));
    }

    public boolean hasModel() {
        return m != null || m.getVaoID() != -1;
    }

    public StaticShader getShader() {
        return shaderProg;
    }

    public void setShader(StaticShader s) {
        shaderProg = s;
    }

    public void setRenderPass(boolean bo) {
        renderPass = bo;
    }
}
