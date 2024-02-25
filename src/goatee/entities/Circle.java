package goatee.entities;

import org.lwjgl.util.vector.Vector3f;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class Circle extends Entity {

    public int points;
    public float r;


    public Circle() {
        super();
    }

    public Circle(int points, int r) {
        this.points = points;
        this.r = r;
        createVertices();
    }


    public void createVertices() {
        //x = rcos(theta), y = rsin(theta)
        //in points, r
        List<Vector3f> vertices = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Float> tCoords = new ArrayList<>();

        double angle = (2 * Math.PI) / points;
        vertices = new ArrayList<>();
        indices = new ArrayList<>();

        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(5); //prevents sin(0) of being 2.2323E-14

        int index = 0;
        for (int a = 0; a <= points; a++) {
            float i = (float) (a * angle);
            if (index < points) { //prevents addition of 360, as it is already zero and added
                float x = Float.parseFloat(format.format(r * Math.cos(i)));
                float y = Float.parseFloat(format.format(r * Math.sin(i)));
                vertices.add((new Vector3f(x, y, 0)));

                indices.add(index);
                if (index + 1 >= points) //adds very last index as 0
                    indices.add(0);
                else indices.add(index + 1);

                index++;
            }
        }
        loadModel(vertices, indices, normals, tCoords, "Circle");
    }

}
