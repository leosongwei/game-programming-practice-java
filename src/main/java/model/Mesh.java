package model;

import static org.lwjgl.opengl.GL46.*;

public class Mesh {
    private final float[] vertices; // n*8: x,y,z,nx,ny,nz,u,v
    private final int[] indices;
    private final TexturePack texturePack;
    private final Shader shader;
    private int vao;
    private int vbo;
    private int ebo;

    public Mesh(float[] verticesArray, int[] indicesArray, TexturePack texturePackObject, Shader shaderObject) {
        vertices = verticesArray;
        indices = indicesArray;
        texturePack = texturePackObject;
        shader = shaderObject;
    }

    public void setup() {
        vao = glGenVertexArrays();
        glBindVertexArray(vao);

        vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        int size = (3+3+2) * 4;
        // bind x, y, z
        glVertexAttribPointer(0, 3, GL_FLOAT, false, size, 0);
        glEnableVertexAttribArray(0);
        // bind nx, ny, nz
        glVertexAttribPointer(1, 3, GL_FLOAT, false, size, 3 * 4);
        glEnableVertexAttribArray(1);
        // bind u, v
        glVertexAttribPointer(2, 2, GL_FLOAT, false, size, 6 * 4);
        glEnableVertexAttribArray(2);
    }

    public void unload() {
        glDeleteVertexArrays(vao);
        glDeleteBuffers(vbo);
        glDeleteBuffers(ebo);
    }

    public void draw() {
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
    }

    public void bind() {
        glBindVertexArray(vao);
        shader.use();
        texturePack.bind(shader);
    }
}
