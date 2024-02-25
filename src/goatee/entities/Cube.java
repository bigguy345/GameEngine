package goatee.entities;

import goatee.shaders.StaticShader;

public class Cube extends Entity {
    public static Cube c;
    int[] indices;
    float[] vertices;

    public Cube(float x, float y, float z) {
        super(x, y, z);
        c = this;
        float z1 = 0.5f;
        vertices = new float[]{0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0,
                0, 0, z1, 0, 1, z1,
                1, 0, z1, 1, 1, z1

        };
//        indices = new int[]{
//                0, 1,  1, 3,  2, 2,  0, 2,
//                0, 4,  4, 5,  5, 2,  2, 0,
//                4, 6,  6, 7,  7, 5,  5, 4,
//                7, 3,  3, 1,  1, 6,  6, 7,
//                5, 7,  7, 3,  3, 2,  2, 5,
//                0, 1,  1, 6,  6, 4,  4, 0
//        };


        indices = new int[]{
                0, 1, 2, 2, 3, 1,
                0, 4, 5, 0, 2, 5,
                2, 5, 7, 2, 3, 7,
                7, 3, 1, 7, 6, 1,
                4, 6, 5, 5, 6, 7,
                0, 6, 5, 0, 1, 6,


        };

        loadModel(vertices, indices, new float[]{0, 1f}, new float[6], "Cube");
        setShader(StaticShader.lineShader);

    }
}
