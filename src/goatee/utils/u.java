package goatee.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class u {


    public static IntBuffer toIntBuffer(int[] data) {
        IntBuffer b = BufferUtils.createIntBuffer(data.length);
        b.put(data);
        b.flip(); //prepares buffer to be read from, as it expects to be written to;
        return b;
    }

    public static FloatBuffer toFloatBuffer(float[] data) {
        FloatBuffer b = BufferUtils.createFloatBuffer(data.length);
        b.put(data);
        b.flip(); //prepares buffer to be read from, as it expects to be written to;
        return b;
    }

    public static void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    public static void bindTexture(int textureid) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureid);
    }

    public static float[] vectorToFloatArray(List<Vector3f> v) {
        float[] f = new float[v.size() * 3];
        int index = 0;
        for (int i = 0; i < f.length; i++) {
            if (i % 3 == 0) {
                f[i] = v.get(index).x;
                f[i + 1] = v.get(index).y;
                f[i + 2] = v.get(index).z;
                index++;
            }
        }

        return f;
    }

    public static float sin(float a) {
        return (float) Math.sin(a);
    }

    public static float cos(float a) {
        return (float) Math.cos(a);
    }

    public static float toRad(float a) {
        return (float) Math.toRadians(a);
    }

    public static float toDeg(float a) {
        return (float) Math.toDegrees(a);
    }

    public static Matrix4f rotationMatrix(float a, String axis, Vector3f originPoint) {
        a = toRad(a);
        Vector3f pos = null;
        Matrix4f m = new Matrix4f();
        if (axis.toLowerCase().equals("z")) {
            m.m00 = u.cos(a);
            m.m01 = -u.sin(a);
            m.m10 = u.sin(a);
            m.m11 = u.cos(a);

            m.m03 = originPoint.x;
            m.m13 = originPoint.y;
            m.m23 = originPoint.z;

            float x = u.cos(a) * pos.x + -u.sin(a) * pos.y + originPoint.x * 1;
            float y = u.sin(a) * pos.x + u.cos(a) * pos.y;
            System.out.println(m);
            return m;
        }
        return m;
    }

    public static Vector3f mulV4byM4(Matrix4f mat, Vector3f v) {
        float x = mat.m00 * v.getX() + mat.m01 * v.getY() + mat.m02 * v.getZ() + mat.m03;
        float y = mat.m10 * v.getX() + mat.m11 * v.getY() + mat.m12 * v.getZ() + mat.m13;
        float z = mat.m20 * v.getX() + mat.m21 * v.getY() + mat.m22 * v.getZ() + mat.m23;
        return new Vector3f(x, y, z);
    }
}
