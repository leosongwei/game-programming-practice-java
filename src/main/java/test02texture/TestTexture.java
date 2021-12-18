package test02texture;

import org.lwjgl.glfw.GLFW;
import shader.Shader;
import window.MainWindow;
import model.Texture;

import static org.lwjgl.opengl.GL46.*;

public class TestTexture {

    private static int shaderSetup() throws Exception {
        String vertexShader = """
                #version 330 core
                layout(location = 0) in vec2 modelCoord;
                layout(location = 1) in vec2 texCoord;
                out vec2 fragTexCoord;
                void main(){
                    fragTexCoord = texCoord;
                    gl_Position.xy = modelCoord;
                    gl_Position.z = 0.0;
                    gl_Position.w = 1.0;
                }
                                """;
        String fragmentShader = """
                #version 330 core
                uniform sampler2D quadTexture;
                in vec2 fragTexCoord;
                out vec3 color;
                void main(){
                    color = vec3(texture(quadTexture, fragTexCoord));
                }
                                """;
        int program = Shader.createProgramWithShaderStrings(vertexShader, fragmentShader);
        glUseProgram(program);
        return program;
    }

    private static void renderModelSetup() {
        float[] vertices = {
                // x, y, u, v
                -0.9f, 0.9f, 0.0f, 1.0f,
                0.9f, 0.9f, 1.0f, 1.0f,
                -0.9f, -0.9f, 0.0f, 0.0f,
                0.9f, -0.9f, 1.0f, 0.0f
        };
        int[] eboIndices = {0, 3, 1, 0, 2, 3};

        int vertexArray = glGenVertexArrays();
        glBindVertexArray(vertexArray);

        int vertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, eboIndices, GL_STATIC_DRAW);

        glVertexAttribPointer(0, 2, GL_FLOAT, false, 4 * 4, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 4 * 4, 2 * 4);
        glEnableVertexAttribArray(1);

        glBindVertexArray(vertexArray);

        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    }

    private static void render() {
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow(640, 480);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        try {
            int program = shaderSetup();
            renderModelSetup();
            System.out.printf("Program: %d\n", program);
            Texture texture = new Texture("th06.png");
            texture.bind(GL_TEXTURE0);
            glUniform1i(glGetUniformLocation(program, "quadTexture"),0);

            while (!GLFW.glfwWindowShouldClose(mainWindow.getWindow())) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

                render();

                mainWindow.swapBuffer();

                // Poll for window events. The key callback above will only be
                // invoked during this call.
                GLFW.glfwPollEvents();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
