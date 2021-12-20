package test03shapes;

import model.Shader;
import model.Mesh;
import model.Texture;
import org.lwjgl.glfw.GLFW;
import shapes.Square;
import shapes.Shape;
import window.MainWindow;

import static org.lwjgl.opengl.GL46.*;

public class TestShapesSquare {
    private static Shader shaderSetup() throws Exception {
        String vertexShader = """
                #version 330 core
                layout(location = 0) in vec3 position;
                layout(location = 1) in vec3 normal;
                layout(location = 2) in vec2 uv;
                
                out vec2 uvCoord;
                
                void main(){
                    uvCoord = uv;
                    gl_Position.xyz = position;
                }
                                """;
        String fragmentShader = """
                #version 330 core
                uniform sampler2D diffuseTexture;
                
                in vec2 uvCoord;
                
                out vec3 color;
                
                void main(){
                    color = vec3(texture(diffuseTexture, uvCoord));
                }
                                """;
        return new Shader(vertexShader, fragmentShader);
    }

    private static Mesh setupMesh(Shader shader) {
        Shape square = new Square(0.6f);
        Texture texture = new Texture("th06.png");
        Mesh mesh = new Mesh(
                square.getVertices(),
                square.getIndices(),
                texture,
                shader
        );
        mesh.setup();
        return mesh;
    }

    private static void render(Mesh mesh) {
        mesh.draw();
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow(640, 480);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        try {
            Shader shader = shaderSetup();
            Mesh mesh = setupMesh(shader);

            while (!GLFW.glfwWindowShouldClose(mainWindow.getWindow())) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

                mesh.bind();
                render(mesh);

                mainWindow.swapBuffer();

                GLFW.glfwPollEvents();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}