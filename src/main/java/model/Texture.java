package model;

import org.lwjgl.stb.STBImage;
import static org.lwjgl.opengl.GL46.*;

public class Texture {
    private final int textureID;
    private final int width;
    private final int height;

    public Texture(String filepath) {
        int[] xRef = {0};
        int[] yRef = {0};
        int[] channels_in_file = {};
        java.nio.ByteBuffer buffer = STBImage.stbi_load(filepath, xRef, yRef, channels_in_file, 3);
        assert buffer != null;
        width = xRef[0];
        height = yRef[0];

        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINE);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);

        STBImage.stbi_image_free(buffer);
    }

    public void bind(int textureNumber){
        glActiveTexture(textureNumber);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public int getWidth() {return width;}
    public int getHeight() {return height;}
}
