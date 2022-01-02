package test03shapes;

import model.Mesh;
import model.Shader;
import model.Texture;
import model.TexturePack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import shapes.Cube;
import shapes.Shape;
import window.MainWindow;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL46.*;

public class TestShapesCube {
    private static Shader shaderSetup() throws Exception {
        String vertexShader = """
                #version 330 core
                layout(location = 0) in vec3 position;
                layout(location = 1) in vec3 normal;
                layout(location = 2) in vec2 uv;
                
                uniform mat4 projectionLook;
                uniform mat4 modelTransform;
                
                out vec2 uvCoord;
                
                void main(){
                    uvCoord = uv;
                    gl_Position = projectionLook * modelTransform * vec4(position, 1.0);
                }
                                """;
        String fragmentShader = """
                #version 330 core
                uniform sampler2D TEXTURE0;
                uniform sampler2D TEXTURE1;
                
                in vec2 uvCoord;
                
                out vec3 color;
                
                void main(){
                    vec3 color0 = vec3(texture(TEXTURE0, uvCoord));
                    vec3 color1 = vec3(texture(TEXTURE1, uvCoord));
                    color = color0 * uvCoord.x + color1 * (1-uvCoord.x);
                }
                                """;
        return new Shader(vertexShader, fragmentShader);
    }

    private static Mesh setupMesh(Shader shader) throws Exception {
        Shape cube = new Cube();
        TexturePack texture = new TexturePack(new String[]{"th06.png", "container2.png"});
        Mesh mesh = new Mesh(
                cube.getVertices(),
                cube.getIndices(),
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
        MainWindow mainWindow = new MainWindow(1024, 768);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        try {
            Shader shader = shaderSetup();
            Mesh mesh = setupMesh(shader);

            Vector3f eyePosition = new Vector3f(0.0f, 0.0f, 10.0f);

            float angle = 0.0f;

            while (!GLFW.glfwWindowShouldClose(mainWindow.getWindow())) {
                Matrix4f projectionLook = new Matrix4f().perspective(
                        (float)Math.toRadians(20.0),
                        ((float)mainWindow.getWidth()) / ((float)mainWindow.getHeight()),
                        0.1f,
                        200.0f
                ).lookAt(
                        eyePosition,
                        new Vector3f(0.0f, 0.0f, 0.0f),
                        new Vector3f(0.0f, 1.0f, 0.0f)
                );

                try (MemoryStack stack = MemoryStack.stackPush()) {
                    FloatBuffer perspectiveMatrix = projectionLook.get(stack.mallocFloat(16));
                    glUniformMatrix4fv(
                            shader.getUniformLocation("projectionLook"),
                            false,
                            perspectiveMatrix
                    );

                    angle = (angle + 1f) % 360.0f;

                    Vector3f translation = new Vector3f(0f, 0f, 2f).rotateY((float)Math.toRadians(angle));

                    FloatBuffer boxTranslation = new Matrix4f()
                            .translation(translation)
                            .get(stack.mallocFloat(16));

                    glUniformMatrix4fv(
                            shader.getUniformLocation("modelTransform"),
                            false,
                            boxTranslation
                    );
                }

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
