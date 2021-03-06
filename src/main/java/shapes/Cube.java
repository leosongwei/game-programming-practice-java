package shapes;

public class Cube implements Shape {
    private final float[] vertices;
    private final int[] indices;

    public Cube() {
        vertices = new float[]{
                -0.5f, -0.5f, -0.5f, +0.0f, +0.0f, -1.0f, +0.0f, +0.0f,
                +0.5f, -0.5f, -0.5f, +0.0f, +0.0f, -1.0f, +1.0f, +0.0f,
                +0.5f, +0.5f, -0.5f, +0.0f, +0.0f, -1.0f, +1.0f, +1.0f,
                +0.5f, +0.5f, -0.5f, +0.0f, +0.0f, -1.0f, +1.0f, +1.0f,
                -0.5f, +0.5f, -0.5f, +0.0f, +0.0f, -1.0f, +0.0f, +1.0f,
                -0.5f, -0.5f, -0.5f, +0.0f, +0.0f, -1.0f, +0.0f, +0.0f,
                -0.5f, -0.5f, +0.5f, +0.0f, +0.0f, +1.0f, +0.0f, +0.0f,
                +0.5f, -0.5f, +0.5f, +0.0f, +0.0f, +1.0f, +1.0f, +0.0f,
                +0.5f, +0.5f, +0.5f, +0.0f, +0.0f, +1.0f, +1.0f, +1.0f,
                +0.5f, +0.5f, +0.5f, +0.0f, +0.0f, +1.0f, +1.0f, +1.0f,
                -0.5f, +0.5f, +0.5f, +0.0f, +0.0f, +1.0f, +0.0f, +1.0f,
                -0.5f, -0.5f, +0.5f, +0.0f, +0.0f, +1.0f, +0.0f, +0.0f,
                -0.5f, +0.5f, +0.5f, -1.0f, +0.0f, +0.0f, +1.0f, +0.0f,
                -0.5f, +0.5f, -0.5f, -1.0f, +0.0f, +0.0f, +1.0f, +1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f, +0.0f, +0.0f, +0.0f, +1.0f,
                -0.5f, -0.5f, -0.5f, -1.0f, +0.0f, +0.0f, +0.0f, +1.0f,
                -0.5f, -0.5f, +0.5f, -1.0f, +0.0f, +0.0f, +0.0f, +0.0f,
                -0.5f, +0.5f, +0.5f, -1.0f, +0.0f, +0.0f, +1.0f, +0.0f,
                +0.5f, +0.5f, +0.5f, +1.0f, +0.0f, +0.0f, +1.0f, +0.0f,
                +0.5f, +0.5f, -0.5f, +1.0f, +0.0f, +0.0f, +1.0f, +1.0f,
                +0.5f, -0.5f, -0.5f, +1.0f, +0.0f, +0.0f, +0.0f, +1.0f,
                +0.5f, -0.5f, -0.5f, +1.0f, +0.0f, +0.0f, +0.0f, +1.0f,
                +0.5f, -0.5f, +0.5f, +1.0f, +0.0f, +0.0f, +0.0f, +0.0f,
                +0.5f, +0.5f, +0.5f, +1.0f, +0.0f, +0.0f, +1.0f, +0.0f,
                -0.5f, -0.5f, -0.5f, +0.0f, -1.0f, +0.0f, +0.0f, +1.0f,
                +0.5f, -0.5f, -0.5f, +0.0f, -1.0f, +0.0f, +1.0f, +1.0f,
                +0.5f, -0.5f, +0.5f, +0.0f, -1.0f, +0.0f, +1.0f, +0.0f,
                +0.5f, -0.5f, +0.5f, +0.0f, -1.0f, +0.0f, +1.0f, +0.0f,
                -0.5f, -0.5f, +0.5f, +0.0f, -1.0f, +0.0f, +0.0f, +0.0f,
                -0.5f, -0.5f, -0.5f, +0.0f, -1.0f, +0.0f, +0.0f, +1.0f,
                -0.5f, +0.5f, -0.5f, +0.0f, +1.0f, +0.0f, +0.0f, +1.0f,
                +0.5f, +0.5f, -0.5f, +0.0f, +1.0f, +0.0f, +1.0f, +1.0f,
                +0.5f, +0.5f, +0.5f, +0.0f, +1.0f, +0.0f, +1.0f, +0.0f,
                +0.5f, +0.5f, +0.5f, +0.0f, +1.0f, +0.0f, +1.0f, +0.0f,
                -0.5f, +0.5f, +0.5f, +0.0f, +1.0f, +0.0f, +0.0f, +0.0f,
                -0.5f, +0.5f, -0.5f, +0.0f, +1.0f, +0.0f, +0.0f, +1.0f
        };
        indices = new int[36];
        for (int i = 0; i < 36; i++) {
            indices[i] = i;
        }

    }

    public float[] getVertices() {
        return vertices;
    }

    public int[] getIndices() {
        return indices;
    }
}