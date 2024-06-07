package goatee.models;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.PNGDecoder;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class Texture {

    public int id;

    public Texture(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Texture loadTexture(String filename) {
        String loc = "src/goatee/";

        ByteBuffer imageBuffer;
        PNGDecoder image;
        try {
            image = new PNGDecoder(Texture.class.getResourceAsStream(filename));
            imageBuffer = ByteBuffer.allocateDirect(4 * image.getHeight() * image.getWidth());
            image.decode(imageBuffer, 0, PNGDecoder.RGBA);
        } catch (IOException e) {
            //   e.printStackTrace();
            return null;
        }
        imageBuffer.flip();

        int id = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        return new Texture(id);
    }

}
