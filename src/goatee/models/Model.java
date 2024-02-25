package goatee.models;

import goatee.entities.Entity;
import goatee.renderEngine.VAOLoader;
import goatee.utils.u;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {
    public static Map<Model, List<Entity>> modelEntityMap = new HashMap<>();
    public int textureID = -1;
    public String modelName;
    private int vaoID = -1;
    private int vertexCount;


    public Model(int vaoId, int vertexCount, String modelName) {
        this.vaoID = vaoId;
        this.vertexCount = vertexCount;
        this.modelName = modelName;
        modelEntityMap.put(this, new ArrayList<>());
    }

    public static void processModelEntityMap(Entity e) {
        if (e.hasModel() && !modelEntityMap.get(e.getModel()).contains(e)) {
            modelEntityMap.get(e.getModel()).add(e);
        }
    }

    public static void renderAllModels() {
        for (Entity e : Entity.entities)
            Model.processModelEntityMap(e);


        for (Model m : modelEntityMap.keySet()) {
            GL30.glBindVertexArray(m.getVaoID());
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1);
            GL20.glEnableVertexAttribArray(2);

            for (Entity e : modelEntityMap.get(m)) {
                if (e.renderPass) continue;
                e.getShader().start();
                e.getShader().loadMVP(e);
                if (e.hasTexture()) {
                    e.getShader().setTexture(true);
                    GL13.glActiveTexture(GL13.GL_TEXTURE0);
                    u.bindTexture(m.getTexture());
                } else {
                    e.getShader().setTexture(false);
                    u.bindTexture(0);
                }
                // GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
                GL11.glDrawElements(GL11.GL_TRIANGLES, m.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
                e.getShader().stop();
            }

            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL20.glDisableVertexAttribArray(2);
            GL30.glBindVertexArray(0);
        }
    }

    public int getVaoID() {
        return vaoID;
    }


    public int getVertexCount() {
        return vertexCount;
    }

    public int getTexture() {
        return textureID;
    }

    public void setTexture(String filename) {
        textureID = VAOLoader.loadTexture(filename);
    }

}
