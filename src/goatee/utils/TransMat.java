package goatee.utils;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class TransMat {
    public Matrix4f m = new Matrix4f();
    public Vector3f currentPos;
    public Vector3f newPos = new Vector3f();

    public TransMat() {

    }

    public Vector3f getNewPos() {
        return newPos;
    }

    public void setCurrentPos(Vector3f v) {
        currentPos = v;
    }

    public void translate(Vector3f v) {
        Matrix4f.translate(v, m, m);
        if (currentPos != null) {
            // currentPos.translate(v.x, v.y, v.z);
            newPos.translate(v.x + currentPos.x, v.y + currentPos.y, v.z + currentPos.z);
            System.out.println("currewnt pos " + currentPos);
        }
    }

    public void translate(float x, float y, float z) {
        translate(new Vector3f(x, y, z));
    }

    public void scale(Vector3f v) {
        Matrix4f.scale(v, m, m);
    }

    public void scale(float x, float y, float z) {
        scale(new Vector3f(x, y, z));
    }

    public void scale(float s) {
        scale(s, s, s);
    }

    public void rotate(float angle, Vector3f v) {
        Matrix4f.rotate(u.toRad(angle), v, m, m);
    }

    public void rotate(float angle, float x, float y, float z) {
        rotate(angle, new Vector3f(x, y, z));
    }

    public Matrix4f getMatrix() {
        return m;
    }

    public void setIdentity() {
        m.setIdentity();
    }

    public String toString() {
        return m.toString();
    }
}
