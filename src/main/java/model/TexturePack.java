package model;

import java.util.ArrayList;
import static org.lwjgl.opengl.GL46.*;

public class TexturePack {
    private final ArrayList<Texture> textures = new ArrayList<Texture>();

    public TexturePack(String[] texturePaths) throws Exception {
        if(texturePaths.length > 5) {
            throw new Exception("TexturePack(): Too much textures!");
        }

        for(String path: texturePaths) {
            Texture texture = new Texture(path);
            textures.add(texture);
        }
    }

    public void bind() {
        int[] glTextureNumbers = {
                GL_TEXTURE0, GL_TEXTURE1, GL_TEXTURE2, GL_TEXTURE3, GL_TEXTURE4
        };

        for(int i=0; i<textures.size(); i++) {
            int textureNumber = glTextureNumbers[i];
            Texture texture = textures.get(i);
            texture.bind(textureNumber);
        }
    }
}
