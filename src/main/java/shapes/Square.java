package shapes;

import model.Mesh;
import model.Shader;
import model.Texture;

public class Square implements Shape {
    private final float[] vertices;
    private final float[] indices;

    public Square(float w) {
        vertices = new float[]{
                // x, y, z, nx, ny, nz, u, v
                0.0f, w, 0.0f,       0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
                w, w, 1.0f,          0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
                0.0f, 0.0f, 0.0f,    0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
                w, 0.0f, 1.0f,       0.0f, 0.0f, -1.0f, 1.0f, 0.0f
        };
        indices = new float[]{0, 3, 1, 0, 2, 3};
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getIndices() {
        return indices;
    }
}
