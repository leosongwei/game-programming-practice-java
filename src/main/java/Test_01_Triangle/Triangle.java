package Test_01_Triangle;

import Window.MainWindow;
import org.lwjgl.glfw.GLFW;
import static org.lwjgl.opengl.GL46.*;

public class Triangle {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();

        while ( !GLFW.glfwWindowShouldClose(mainWindow.getWindow()) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            GLFW.glfwSwapBuffers(mainWindow.getWindow()); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            GLFW.glfwPollEvents();
        }
    }
}
