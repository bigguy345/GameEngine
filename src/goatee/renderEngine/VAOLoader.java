package goatee.renderEngine;

import goatee.models.Model;
import goatee.utils.u;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;


public class VAOLoader {
    private static List<Integer> vaos = new ArrayList<Integer>();
    private static List<Integer> vbos = new ArrayList<Integer>();
    private static List<Integer> textures = new ArrayList<Integer>();

    public static Model loadToVao(float[] positions, int[] indices, float[] normal, float[] textureCoords, String modelName) {
        for (Model m : Model.modelEntityMap.keySet()) {
            if (m.modelName.equals(modelName))
                return m;
        }

        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, 3, positions);
        storeDataInAttributeList(1, 2, textureCoords);
        storeDataInAttributeList(2, 3, normal);

        u.unbindVAO();
        return new Model(vaoID, indices.length, modelName);
    }

    public static Model loadToVao(List<Vector3f> vertices, List<Integer> indices, List<Vector3f> normals, List<Float> tCoords, String modelName) {
        int[] i = new int[indices.size()];
        for (int j = 0; j < i.length; j++)
            i[j] = indices.get(j);

        float[] t = new float[tCoords.size()];
        for (int j = 0; j < t.length; j++)
            t[j] = tCoords.get(j);

        return loadToVao(u.vectorToFloatArray(vertices), i, u.vectorToFloatArray(normals), t, modelName);
    }

    private static int createVAO() {
        int vaoID = GL30.glGenVertexArrays(); //each VAO has unique id
        GL30.glBindVertexArray(vaoID); //change current active VAO to this vaoID
        vaos.add(vaoID);
        return vaoID;
    }

    private static void storeDataInAttributeList(int attId, int coordsize, float[] data) {
        int vboID = GL15.glGenBuffers(); //make new buffer and bind if
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        vbos.add(vboID);
        FloatBuffer b = u.storeDataInFloatBuffer(data);

        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, b, GL15.GL_STATIC_DRAW); //store data in buffer
        //store buffer in attribute list
        GL20.glVertexAttribPointer(attId, coordsize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    public static int loadTexture(String filename) {
        Texture texture = null;
        try {
            texture = TextureLoader.getTexture("png", new FileInputStream(("res/" + filename + ".png")));
        } catch (IOException e) {
            //  e.printStackTrace();
        }
        int textureID = texture.getTextureID();
        textures.add(textureID);
        return textureID;
    }

    private static void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);

        IntBuffer b = u.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, b, GL15.GL_STATIC_DRAW);

    }

    public static Model loadFromObj(String file) {
        for (Model m : Model.modelEntityMap.keySet()) {
            if (m.modelName.equals(file))
                return m;
        }

        List<Float> vertices = new ArrayList<>(), unsortedtcoords = new ArrayList<>(), unsortednormals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<Float> sortedtcoords = new ArrayList<>(), sortednormals = new ArrayList<>();

        try {
            String loc = "res/";
            BufferedReader r = new BufferedReader(new FileReader(loc + file));

            String l;
            while ((l = r.readLine()) != null) {

                if (l.startsWith("v ")) {
                    String[] s = l.split(" ");
                    for (int i = 1; i < s.length; i++)
                        vertices.add(Float.parseFloat(s[i]));

                } else if (l.startsWith("vn ")) {
                    String[] s = l.split(" ");
                    for (int i = 1; i < s.length; i++)
                        unsortednormals.add(Float.parseFloat(s[i]));


                } else if (l.startsWith("vt ")) {
                    String[] s = l.split(" ");
                    for (int i = 1; i < s.length; i++)
                        unsortedtcoords.add(Float.parseFloat(s[i]));


                } else if (l.startsWith("f ")) {
                    String[] rmvSpace = l.replace("f", "").replaceFirst(" ", "").split(" ");

                    for (int i = 0; i < rmvSpace.length; i++) {
                        // 5/1/1, 3/2/1, 1/3/1
                        String[] rmvSlash = rmvSpace[i].split("/");
                        indices.add(Integer.parseInt(rmvSlash[0]) - 1);

                        sortedtcoords.add(unsortedtcoords.get(Integer.parseInt(rmvSlash[1]) - 1));
                        sortednormals.add(unsortednormals.get(Integer.parseInt(rmvSlash[2]) - 1));
                        //  System.out.println(unsortednormals.get(Integer.parseInt(rmvSlash[2])));
                    }

                }

            }
            if (vertices.isEmpty())
                throw new IllegalArgumentException("No vertices were found in file!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        float[] v = new float[vertices.size()];
        for (int j = 0; j < v.length; j++)
            v[j] = vertices.get(j);

        int[] i = new int[indices.size()];
        for (int j = 0; j < i.length; j++)
            i[j] = indices.get(j);

        float[] tc = new float[sortedtcoords.size()];
        for (int j = 0; j < tc.length; j++)
            tc[j] = sortedtcoords.get(j);

        float[] n = new float[sortednormals.size()];
        for (int j = 0; j < n.length; j++) {
            n[j] = sortednormals.get(j);
            //  System.out.println(n[j]);

        }
        // System.out.println(sortednormals.size());
        return loadToVao(v, i, tc, n, file);
    }

    public static void cleanUp() { //memory management, cleans up vaos and vbos from memory
        for (int a : vaos)
            GL30.glDeleteVertexArrays(a);
        for (int a : vbos)
            GL15.glDeleteBuffers(a);
        for (int a : textures)
            GL11.glDeleteTextures(a);
    }


}
