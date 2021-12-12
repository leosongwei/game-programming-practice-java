package Test_01_Triangle;

import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL46.*;

import Window.MainWindow;
import Shader.Shader;

public class Triangle {

    private static int shaderSetup() throws Exception {
        String vertexShader = """
                #version 330 core
                layout(location = 0) in vec2 vertexPosition_modelspace;
                void main(){
                    gl_Position.xy = vertexPosition_modelspace;
                    gl_Position.z = 0.0;
                    gl_Position.w = 1.0;
                }
                """;
        String fragmentShader = """
                #version 330 core
                out vec3 color;
                void main(){
                color = vec3(1,0,0); // red
                }
                """;
        return Shader.createProgramWithShaderStrings(vertexShader, fragmentShader);
    }

    private static void renderModelSetup() {

    }

    private static void render() {

    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow(640, 480);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        try{
            int program = shaderSetup();
            System.out.printf("Program: %d\n", program);

            while ( !GLFW.glfwWindowShouldClose(mainWindow.getWindow()) ) {
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

                GLFW.glfwSwapBuffers(mainWindow.getWindow()); // swap the color buffers

                // Poll for window events. The key callback above will only be
                // invoked during this call.
                GLFW.glfwPollEvents();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
