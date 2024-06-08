package goatee.models;

import goatee.Main;
import goatee.entities.Entity;
import goatee.renderEngine.Camera;
import goatee.renderEngine.ModelRenderer;
import goatee.shaders.ShaderHelper;
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
    public Texture texture;
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
                if (!e.shouldRender)
                    continue;

                ShaderHelper.useShader(e.shaderProgram, shader -> {
                    ShaderHelper.uniformVec3("lightColor", Main.light.color.x, Main.light.color.y, Main.light.color.z);
                    ShaderHelper.uniformVec3("lightPosition", Main.light.position.x, Main.light.position.y, Main.light.position.z);

                    ShaderHelper.uniformMatrix4x4("model", e.modelMatrix.getMatrix());
                    ShaderHelper.uniformMatrix4x4("view", Camera.getViewMatrix());
                    ShaderHelper.uniformMatrix4x4("projection", Camera.getProjectionMatrix());
                    ShaderHelper.uniformMatrix4x4("mvp", Camera.createMVP(e.modelMatrix.getMatrix()));
                    ShaderHelper.uniform1f("hasTexture", e.hasTexture() ? 1 : 0);

                    ShaderHelper.uniformVec3("objColor", e.getColor().x, e.getColor().y, e.getColor().z);


                });
                if (e.hasTexture()) {
                    GL13.glActiveTexture(GL13.GL_TEXTURE0);
                    u.bindTexture(m.getTexture().id);
                } else {
                    u.bindTexture(0);
                }
                // GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
                GL11.glDrawElements(GL11.GL_TRIANGLES, m.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
                ShaderHelper.releaseShader();
            }

            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL20.glDisableVertexAttribArray(2);
            GL30.glBindVertexArray(0);
            ModelRenderer.bo = true;
        }
    }

    public int getVaoID() {
        return vaoID;
    }


    public int getVertexCount() {
        return vertexCount;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(String filename, String format) {
        texture = TextureManager.bindTexture(filename);
    }

}
