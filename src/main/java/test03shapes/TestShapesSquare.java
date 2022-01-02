package test03shapes;

import model.Shader;
import model.Mesh;
import model.Texture;
import model.TexturePack;
import org.lwjgl.glfw.GLFW;
import shapes.Square;
import shapes.Shape;
import window.MainWindow;

import static org.lwjgl.opengl.GL46.*;

public class TestShapesSquare {
    // https://learnopengl-cn.github.io/02%20Lighting/04%20Lighting%20maps/
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
                uniform sampler2D TEXTURE0;
                
                in vec2 uvCoord;
                
                out vec3 color;
                
                void main(){
                    color = vec3(texture(TEXTURE0, uvCoord));
                }
                                """;
        return new Shader(vertexShader, fragmentShader);
    }

    private static Mesh setupMesh(Shader shader) throws Exception {
        Shape square = new Square(0.6f);
        TexturePack texture = new TexturePack(new String[]{"th06.png"});
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
