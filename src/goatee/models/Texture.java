package goatee.models;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.PNGDecoder;

import java.io.IOException;
import java.nio.ByteBuffer;

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

        ByteBuffer b;
        PNGDecoder decoder;
        try {
            decoder = new PNGDecoder(Texture.class.getResourceAsStream(filename));
            b = ByteBuffer.allocateDirect(4 * decoder.getHeight() * decoder.getWidth());
            decoder.decode(b, 0, PNGDecoder.RGBA);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        b.flip();

        int id = GL11.glGenTextures();
        GL11.glBindTexture(id, GL11.GL_TEXTURE_2D);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, b);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        return new Texture(id);
    }
}
