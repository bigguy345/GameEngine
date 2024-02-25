package goatee.entities;

import org.lwjgl.util.vector.Vector3f;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Sphere extends Circle {
    public int stacks, sectors;


    public Sphere(float r, int stacks, int sectors) {
        super();
        this.stacks = stacks;
        this.sectors = sectors;
        this.r = r;

        createVertices();
    }


    public void setLongLat(int stacks, int sectors) {
        this.stacks = stacks;
        this.sectors = sectors;
    }

    public List<Integer> createIndices() {
        List<Integer> indices = new ArrayList<>();
        int k1 = 0;
        int k2 = k1 + sectors + 1;
        for (int i = 0; i <= sectors; i++) {
            if (i == 1) k1 = 1;
            if (i == 0) k2 = k1 + 1;

            for (int j = 0; j <= stacks; j++) {
                if (i == 0) { //northernmost point
                    indices.add(k1);
                    indices.add(k2);
                    indices.add(k2 + 1);
                    k2++;
                    continue;
                } else if (i == sectors - 1) { //southernmost point
                    indices.add(k1);
                    indices.add(k1 + 1);
                    indices.add(k2);
                    k1++;
                    continue;
                }  //all sectors and stacks in between

                indices.add(k1);
                indices.add(k2);
                indices.add(k1 + 1);

                indices.add(k1 + 1);
                indices.add(k2);
                indices.add(k2 + 1);

                k1++;
                k2++;
            }
            if (i == 0) k1++;
        }
        return indices;
    }

    public void createVertices() {
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Float> tCoords = new ArrayList<>();

        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(5); //prevents sin(0) of being 2.2323E-14

        float stackStep = (float) (2 * Math.PI / stacks);
        float sectorStep = (float) (Math.PI / sectors);
        float lengthInv = 1.0f / r;
        float nx, ny, nz, s, t;

        for (int i = 0; i <= sectors; i++) {
            float phi = (float) ((Math.PI / 2) - (i * sectorStep));

            for (int j = 0; j <= stacks; j++) {
                float theta = j * stackStep;
                float x = Float.parseFloat(format.format(r * Math.cos(theta) * Math.cos(phi)));
                float z = Float.parseFloat(format.format(r * Math.sin(theta) * Math.cos((phi))));
                float y = Float.parseFloat(format.format(r * Math.sin(phi)));
                vertices.add(new Vector3f(x, y, z));

                nx = x * lengthInv;
                ny = y * lengthInv;
                nz = z * lengthInv;
                normals.add(new Vector3f(nx, ny, nz));

                s = (float) j / stacks;
                t = (float) i / sectors;
                tCoords.add(s);
                tCoords.add(t);

                if (i == 0 || i == sectors) break;
            }

        }

        loadModel(vertices, createIndices(), normals, tCoords, "Sphere");
    }


}
