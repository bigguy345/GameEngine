package goatee.renderEngine;

import goatee.Main;
import goatee.entities.Line;
import goatee.utils.TransMat;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;


public class Camera {
    public static Vector3f position = new Vector3f(0, 0, 0);
    public static int FOV = 100;
    public static float aspect_ratio;
    public static float near = 0.0001f, far = 2000;
    public static float yaw = 230, pitch = 0, roll, PITCH_MAX = 90;
    public static Matrix4f mvp;
    public static Matrix4f projViewMatrix;
    public static Matrix4f projectionMatrix;
    public static Matrix4f viewMatrix;
    public static int press = 0;

    public Camera() {

    }

    public static boolean kd(int key) {
        return Keyboard.isKeyDown(key);
    }

    public static boolean kp(int key) {
        if (Keyboard.isKeyDown(key) && press == 0) {
            press = 60;
            return Keyboard.isKeyDown(key);
        }
        return false;
    }

    public static Vector3f mv(boolean strafe, boolean diagonal, float speed) {
        if (diagonal) {
            float angle = yaw;
            float dx = (float) (-Math.sin((angle * Math.PI) / 180) * speed);
            float dz = (float) (Math.cos((angle * Math.PI) / 180) * speed);

            float angle2 = yaw + 90;
            float dx2 = (float) (-Math.sin((angle2 * Math.PI) / 180) * speed);
            float dz2 = (float) (Math.cos((angle2 * Math.PI) / 180) * speed);

            Vector3f v = null;
            if (kd(Keyboard.KEY_W) && kd(Keyboard.KEY_D))
                v = new Vector3f(dx + dx2, 0, dz + dz2);
            else if (kd(Keyboard.KEY_W) && kd(Keyboard.KEY_A))
                v = new Vector3f(dx - dx2, 0, dz - dz2);
            else if (kd(Keyboard.KEY_S) && kd(Keyboard.KEY_D))
                v = new Vector3f(-dx + dx2, 0, -dz + dz2);
            else if (kd(Keyboard.KEY_S) && kd(Keyboard.KEY_A))
                v = new Vector3f(-dx - dx2, 0, -dz - dz2);

            return v;
        }


        float angle = !strafe ? yaw : yaw + 90;
        float dx = (float) (-Math.sin((angle * Math.PI) / 180) * speed);
        //double dy = Math.sin((pitch * Math.PI) / 180) * distance;
        float dz = (float) (Math.cos((angle * Math.PI) / 180) * speed);
        Vector3f v = null;
        boolean pstvDir = kd(Keyboard.KEY_W) || kd(Keyboard.KEY_D);
        if ((kd(Keyboard.KEY_S) || kd(Keyboard.KEY_A)) && !pstvDir)
            v = new Vector3f(-dx, 0, -dz);
        else if (!(kd(Keyboard.KEY_S) || kd(Keyboard.KEY_A)) && pstvDir)
            v = new Vector3f(dx, 0, dz);
        else
            v = new Vector3f(0, 0, 0);

        return v;
    }

    public static void updateControls() {

        if (kd(Keyboard.KEY_ESCAPE)) {
            Main.exit();
            return;
        }

        if (kd(Keyboard.KEY_TAB)) {
            Mouse.setGrabbed(false);
            return;
        }


        boolean zMovement = kd(Keyboard.KEY_W) || kd(Keyboard.KEY_S);
        boolean xMovement = kd(Keyboard.KEY_D) || kd(Keyboard.KEY_A);
        float speed = 0.005f * 2f;
        boolean diagonalMovement = zMovement && xMovement;
        float diagonalSpeed = 2f * 0.5f;

        if (diagonalMovement) {
            Vector3f mv = mv(false, true, speed * diagonalSpeed);
            position.x -= mv.x;
            position.z -= mv.z;
        } else if (zMovement || xMovement) {
            Vector3f mv = mv(xMovement, false, speed);
            position.x -= mv.x;
            position.z -= mv.z;
        }


        float verticalSpeed = 0.015f * 0.5f;
        if (kd(Keyboard.KEY_LSHIFT))
            position.y -= verticalSpeed;
        if (kd(Keyboard.KEY_SPACE))
            position.y += verticalSpeed;

        float speed2 = 0.041f;
        if (kd(Keyboard.KEY_UP))
            Main.light.move(0, speed2, 0);
        if (kd(Keyboard.KEY_N))
            Main.miku.move(0, speed2, 0);
        if (kd(Keyboard.KEY_M))
            Main.miku.move(0, -speed2, 0);


        if (kd(Keyboard.KEY_DOWN))
            Main.light.move(0, -speed2, 0);
        if (kd(Keyboard.KEY_LEFT))
            Main.light.move(-speed2, 0, 0);
        if (kd(Keyboard.KEY_RIGHT))
            Main.light.move(speed2, 0, 0);
        if (kd(Keyboard.KEY_V))
            Main.s.move(0, 0, speed2);
        if (kd(Keyboard.KEY_B))
            Main.s.move(0, 0, -speed2);


        if (kd(Keyboard.KEY_C))
            Main.s.rotation.x -= 1;
        if (kd(Keyboard.KEY_F))
            Main.s.rotation.y += 0.1f;
        if (kd(Keyboard.KEY_G))
            Main.s.rotation.z -= 1;
        if (kp(Keyboard.KEY_F5))
            Line.enableDebugAxis = !Line.enableDebugAxis;
        if (kd(Keyboard.KEY_G)) {
            position.z = -1;
            position.y = 0;
            position.x = 0;
            pitch = 0;
            yaw = 180;
        }
        if (Main.timeSinceStart > 0.5) {
            pitch -= (Mouse.getDY() * 0.05);
            yaw += (Mouse.getDX() * 0.05);
        }

        checkPosLimits();


        Mouse.setGrabbed(true);


    }

    public static void checkPosLimits() {
        if (pitch >= PITCH_MAX)
            pitch = PITCH_MAX;
        if (pitch <= -PITCH_MAX)
            pitch = -PITCH_MAX;

        if (position.y <= 0)
            position.y = 0;
        if (press > 0)
            press--;
    }

    public static Matrix4f createProjView() {
        Matrix4f pv = new Matrix4f();
        Matrix4f.mul(getProjectionMatrix(), getViewMatrix(), pv);

        projViewMatrix = pv;
        return pv;
    }

    public static Matrix4f createMVP(Matrix4f transMat) {
        Matrix4f m = new Matrix4f();
        Matrix4f.mul(projViewMatrix, transMat, m);

        mvp = m;
        return m;
    }

    public static Matrix4f getProjectionMatrix() {
        if (projectionMatrix == null) {
            projectionMatrix = new Matrix4f();
            aspect_ratio = (float) Main.width / Main.height;
            float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2F))) * aspect_ratio);
            float x_scale = y_scale / aspect_ratio;
            float frustum_length = far - near;

            projectionMatrix.m00 = x_scale;
            projectionMatrix.m11 = y_scale;
            projectionMatrix.m22 = -((far + near) / frustum_length);
            projectionMatrix.m23 = -1;
            projectionMatrix.m32 = -((2 * near * far) / frustum_length);
            projectionMatrix.m33 = 0;
        }

        return projectionMatrix;
    }

    public static Matrix4f getViewMatrix() {
        Vector3f negativeCameraPos = new Vector3f(-position.getX(), -position.getY(), -position.getZ());
        TransMat view = new TransMat();

        view.rotate(pitch, 1, 0, 0);
        view.rotate(yaw, 0, 1, 0);
        view.rotate(roll, 0, 0, 1);
        view.translate(negativeCameraPos);
        return view.getMatrix();
    }


    public static Vector3f getLookVec() {
        float xzLen = (float) Math.cos(pitch);
        float x = (float) (xzLen * Math.cos(yaw));
        float y = (float) Math.sin(pitch);
        float z = (float) (xzLen * Math.sin(-yaw));
        return new Vector3f(x, y, z);
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f center, Vector3f up) {

        Vector3f f = Vector3f.sub(center, eye, null).normalise(null);
        Vector3f u = up.normalise(null);
        Vector3f s = Vector3f.cross(f, u, null).normalise(null);
        u = Vector3f.cross(s, f, null);

        Matrix4f result = new Matrix4f();
        result.setIdentity();
        result.m00 = s.x;
        result.m10 = s.y;
        result.m20 = s.z;
        result.m01 = u.x;
        result.m11 = u.y;
        result.m21 = u.z;
        result.m02 = -f.x;
        result.m12 = -f.y;
        result.m22 = -f.z;

        return Matrix4f.translate(new Vector3f(-eye.x, -eye.y, -eye.z), result, null);
    }
}
