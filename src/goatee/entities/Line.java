package goatee.entities;

import goatee.models.Model;
import goatee.renderEngine.Camera;
import goatee.renderEngine.VAOLoader;
import goatee.shaders.ShaderHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Line {
    public static List<Line> lines = new ArrayList<>();
    public static boolean enableDebugAxis = true;
    public static int index = 0;
    public Vector3f startPos, endPos, color;
    public Vector3f position, rotation, scale;
    public float[] vertices;
    // public static StaticShader shader = new StaticShader("line.vert", "line.frag");
    public Model lineModel;
    public boolean debugAxis;

    public Matrix4f transMat = new Matrix4f();

    public Line(Vector3f start, Vector3f end, Vector3f col, boolean debugAxis) {
        this.color = col;
        this.startPos = start;
        this.endPos = end;
        this.debugAxis = debugAxis;
        vertices = new float[]{start.x, start.y, start.z, end.x, end.y, end.z};
        int[] indices = new int[]{0, 1};
        lineModel = VAOLoader.loadToVao(vertices, indices, new float[]{0, 0, 0, 0}, new float[6], "Line" + index++);
        lines.add(this);

        this.position = new Vector3f(0, 0, 0);
        this.rotation = new Vector3f(0, 0, 0);
        this.scale = new Vector3f(1f, 1f, 1f);

    }

    public static void drawAllLines() {
        for (Line l : lines) {
            //  l.setPos(Camera.position.x, Camera.position.y  -1, Camera.position.z);
            if (l.debugAxis && !enableDebugAxis) {
            } else
                l.draw();
        }
    }

    public static void createAxisLines() {
        float yOffset = 0;
        Line LX = new Line(new Vector3f(-100, yOffset, 0), new Vector3f(100, yOffset, 0), new Vector3f(1f, 0, 0), true);
        Line LY = new Line(new Vector3f(0, -100 + yOffset, 0), new Vector3f(0, 100 + yOffset, 0), new Vector3f(0, 1f, 0), true);
        Line LZ = new Line(new Vector3f(0, yOffset, -100), new Vector3f(0, yOffset, 100), new Vector3f(0, 0, 1f), true);

        //  Line lx = new Line(new Vector3f(0, 1, 0), new Vector3f(10, 0, 0), new Vector3f(1f, 0, 0), true);
        // Line ly = new Line(new Vector3f(0, 1, 0), new Vector3f(0, 10, 0), new Vector3f(0, 1f, 0), true);
        // Line lz = new Line(new Vector3f(0, 1, 0), new Vector3f(0, 0, 10), new Vector3f(0, 0, 1f), true);


    }

    public void draw() {
        Matrix4f mat = transMat;
        Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), mat, mat);
        Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), mat, mat);
        Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), mat, mat);

        ShaderHelper.useShader(ShaderHelper.lineShader, shader -> {
            ShaderHelper.uniformMatrix4x4("model", mat);
            ShaderHelper.uniformMatrix4x4("view", Camera.getViewMatrix());
            ShaderHelper.uniformMatrix4x4("projection", Camera.getProjectionMatrix());
            ShaderHelper.uniformMatrix4x4("mvp", Camera.createMVP(mat));
            ShaderHelper.uniformVec3("color", color.x, color.y, color.z);
        });

        GL30.glBindVertexArray(lineModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL11.glDrawElements(GL11.GL_LINES, 2, GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        ShaderHelper.releaseShader();

    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public void setPos(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);
    }

    public void setPos(Vector3f pos) {
        this.position = pos;
    }
}
