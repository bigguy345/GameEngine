package goatee.models;

public class Texture {

    public int id, width, height;
    public String resourceLocation;

    public Texture(int id, String resource, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.resourceLocation = resource;
    }


    public int getId() {
        return id;
    }

  
}
