package goatee.entities;

import goatee.Main;
import goatee.shaders.StaticShader;
import org.lwjgl.util.vector.Vector3f;

public class Light extends Sphere {

    public Light(float x, float y, float z) {
        super(1, 36, 18);
        setColor(1, 1, 0.85f);
        setScale(1);
        setPosition(10, 5, 10);
        setShader(StaticShader.sunShader);

    }


    public void handleTransMat2() {
        mat.setIdentity();
        Vector3f s = Main.s.position;
        rotation.z = 270;
        //  u.rotationMatrix(10, "Z", new Vector3f(0, 0, 0));


        //Vector3f v = rotateY(Main.timeF, position, s);
        // mat.scale(scale.x, scale.y, scale.z);

        // mat.rotate(rotation.z, 0, 0, 1);
        //  position = mat.getNewPos();

        //   Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), transMat, transMat);

        // Matrix4f.translate(position.negate(null), transMat, transMat);
        // Vector3f v = new Vector3f(s.x, s.y + 3, s.z);
        // Matrix4f.translate(v, transMat, transMat);
        // setPosition(s.x, s.y + 3, s.z);
        //  rotateZ(Math.max(1.5f * Math.max(u.sin(Main.timeF / 4), 0), 0.2f));


//        System.out.println(mat);
//        System.out.println(mat);
//        System.out.println("--------------------------------------------------");//

        //  Matrix4f m = new Matrix4f();
//        m.m00 = position.x;
//        m.m11 = position.y;
//        m.m22 = position.z;
//        Matrix4f v = Matrix4f.mul(m, transMat, null);
        // System.out.println(v);
        //  setPosition(transMat * position);
        //   Matrix4f.rotate((float) Math.sin(Main.time), new Vector3f(0, 1, 0), transMat, transMat);
        //  Matrix4f.rotate((float) Math.cos(Main.time), new Vector3f(1, 0, 0), transMat, transMat);

        //  Matrix4f.translate(position, transMat, transMat);

    }


}
