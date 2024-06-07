package goatee.renderEngine;

import goatee.entities.Line;
import goatee.models.Model;
import org.lwjgl.opengl.GL11;

public class ModelRenderer {
    public static boolean bo;

    public ModelRenderer() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_FRONT);
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.1f, 0.1f, 0.1f, 1f);
        GL11.glDepthFunc(GL11.GL_LESS);
        Camera.createProjView();
    }


    public void render() {
        prepare();
        Model.renderAllModels();
        Line.drawAllLines();

    }


}
