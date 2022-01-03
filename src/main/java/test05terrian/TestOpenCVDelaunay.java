package test05terrian;

import model.Mesh;
import model.Shader;
import model.TexturePack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;
import org.opencv.core.Core;
import org.opencv.core.MatOfFloat6;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.imgproc.Subdiv2D;
import window.MainWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;


public class TestOpenCVDelaunay {
    private static Shader meshShader() throws Exception {
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
                    color = vec3(1.0, 1.0, 1.0);
                }
                                """;
        return new Shader(vertexShader, fragmentShader);
    }

    private static void render() throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        /*
        Point[] points = {
                new Point(1.0, 1.0),
                new Point(1.0, 8.0),
                new Point(4.0, 4.0),
                new Point(9.0, 11.0),
                new Point(10.0, 1.0),
                new Point(5.0, 4.0),
        };
        */
        ArrayList<Point> points = new ArrayList<Point>();
        Random randomGenerator = new Random(23333);
        for(int i=0; i<200; i++){
            points.add(new Point(randomGenerator.nextFloat()*10, randomGenerator.nextFloat()*10));
        }

        Subdiv2D triangulator = new Subdiv2D(new Rect(
                new Point(0.0, 0.0), new Point(10.0, 10.0)
        ));

        for (var point : points) {
            triangulator.insert(point);
        }
        MatOfFloat6 triangleList = new MatOfFloat6();
        triangulator.getTriangleList(triangleList);
        System.out.println(Arrays.toString(triangleList.toArray()));

        float[] triangles = triangleList.toArray();
        int length = triangles.length / 2;
        float[] vertices = new float[length * 8];
        for (int i = 0; i < length; i++) {
            int offset = i * 8;
            vertices[offset] = triangles[i * 2];
            vertices[offset + 1] = triangles[i * 2 + 1];
            vertices[offset + 2] = 0.0f;

            vertices[offset + 3] = 0.0f;
            vertices[offset + 4] = 0.0f;
            vertices[offset + 5] = 1.0f;

            vertices[offset + 6] = 0.0f;
            vertices[offset + 7] = 0.0f;
        }
        int[] indices = new int[length];
        for (int i = 0; i < length; i++) {
            indices[i] = i;
        }
        System.out.printf("length:%d \n", length);

        MainWindow mainWindow = new MainWindow(1024, 768);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        Shader shader = meshShader();

        Mesh mesh = new Mesh(
                vertices, indices, new TexturePack(new String[]{}), shader
        );
        mesh.setup();

        Vector3f eyePosition = new Vector3f(5, 5, 20);
        int rotation = 0;
        while (!GLFW.glfwWindowShouldClose(mainWindow.getWindow())) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            try (MemoryStack stack = MemoryStack.stackPush()) {
                Matrix4f projectionLook = new Matrix4f().perspective(
                        (float) Math.toRadians(30),
                        ((float) mainWindow.getWidth()) / ((float) mainWindow.getHeight()),
                        0.1f,
                        200.0f
                ).lookAt(
                        eyePosition,
                        new Vector3f(5, 5, 0),
                        new Vector3f(0, 1, 0)
                );

                mesh.bind();
                shader.uniformMatrix4fv("projectionLook",
                        projectionLook.get(stack.mallocFloat(16)));
                shader.uniformMatrix4fv("modelTransform",
                        new Matrix4f().get(stack.mallocFloat(16)));
                mesh.draw();
            }
            mainWindow.swapBuffer();
            GLFW.glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        try {
            render();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
