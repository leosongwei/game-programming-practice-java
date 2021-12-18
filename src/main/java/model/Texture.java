package model;

import org.lwjgl.stb.STBImage;

import static org.lwjgl.opengl.GL46.*;

import org.lwjgl.system.MemoryUtil;
import utils.ResourceFile;

import java.nio.ByteBuffer;

public class Texture {
    private final int textureID;
    private final int width;
    private final int height;

    public Texture(String filepath) {
        int[] xRef = {0};
        int[] yRef = {0};
        int[] channels_in_file = {0};
        ResourceFile resource = new ResourceFile();
        byte[] bytes = resource.getAsBytes(filepath);
        ByteBuffer fileBuffer = MemoryUtil.memAlloc(bytes.length);
        fileBuffer.put(0, bytes);
        ByteBuffer buffer = STBImage.stbi_load_from_memory(fileBuffer, xRef, yRef, channels_in_file, 3);
        MemoryUtil.memFree(fileBuffer);
        if (buffer == null) {
            System.out.println(STBImage.stbi_failure_reason());
        }
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

    public void bind(int textureNumber) {
        glActiveTexture(textureNumber);
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
