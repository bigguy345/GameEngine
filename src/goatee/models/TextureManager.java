package goatee.models;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.PNGDecoder;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

public class TextureManager {
    public static HashMap<String, Texture> textureMap = new HashMap<>();

    public static Texture bindTexture(String textureLoc) {
        Texture tex = textureMap.get(textureLoc);

        if (tex == null)
            tex = loadTexture(textureLoc);


        if (tex != null)
            GL11.glBindTexture(GL_TEXTURE_2D, tex.id);

        return tex;
    }

    public static Texture loadTexture(String textureLoc) {

        Texture tex;
        try {
            FileInputStream in = new FileInputStream(textureLoc);
            PNGDecoder image = new PNGDecoder(in);
            ByteBuffer imageBuffer = ByteBuffer.allocateDirect(4 * image.getHeight() * image.getWidth());
            image.decode(imageBuffer, image.getWidth() * 3, PNGDecoder.RGBA);
            imageBuffer.flip();

            int id = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, image.getWidth(), image.getHeight(), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, imageBuffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            tex = new Texture(id, textureLoc, image.getWidth(), image.getHeight());
            textureMap.put(textureLoc, tex);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return tex;
    }
}
