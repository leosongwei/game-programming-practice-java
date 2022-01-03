package test04lightmapping;

import model.Mesh;
import model.Shader;
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
import static org.lwjgl.glfw.GLFW.*;

public class TestLightMapping {
    private static Shader shaderSetupLightSource() throws Exception {
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

    private static Shader shaderSetupBox() throws Exception {
        String vertexShader = """
                #version 330 core
                layout(location = 0) in vec3 position;
                layout(location = 1) in vec3 normal;
                layout(location = 2) in vec2 uv;
                
                uniform mat4 projectionLook;
                uniform mat4 modelTransform;
                
                out vec2 fragUV;
                out vec3 fragNormal;
                out vec3 fragPos;
                
                void main(){
                    fragUV = uv;
                    fragNormal = mat3(transpose(inverse(modelTransform))) * normal;
                    fragPos = vec3(modelTransform * vec4(position, 1.0));
                    gl_Position = projectionLook * modelTransform * vec4(position, 1.0);
                }
                                """;
        String fragmentShader = """
                #version 330 core
                uniform sampler2D TEXTURE0;
                uniform sampler2D TEXTURE1;
                uniform vec3 lightPos;
                uniform vec3 eyePos;
                
                in vec2 fragUV;
                in vec3 fragNormal;
                in vec3 fragPos;
                
                out vec3 color;
                
                void main(){
                    vec3 lightColor = vec3(1.0, 1.0, 1.0);
                    vec3 colorAmbient = lightColor * texture(TEXTURE0, fragUV).rgb * 0.2;
                    
                    vec3 lightDir = normalize(lightPos - fragPos);
                    vec3 normal = normalize(fragNormal);
                    float diffuse = max(dot(normal, lightDir), 0.0);
                    vec3 colorDiffuse = lightColor * diffuse * texture(TEXTURE0, fragUV).rgb;
                    
                    vec3 viewDir = normalize(eyePos - fragPos);
                    vec3 reflectDir = reflect(-lightDir, normal);
                    float shininess = 30;
                    float spec = pow(max(dot(viewDir, reflectDir), 0.0), shininess);
                    vec3 colorSpecular = lightColor * spec * texture(TEXTURE1, fragUV).rgb;
                    
                    color = colorAmbient + colorDiffuse + colorSpecular;
                }
                                """;
        return new Shader(vertexShader, fragmentShader);
    }

    private static Mesh setupMesh(Shader shader) throws Exception {
        Shape cube = new Cube();
        TexturePack texturePack = new TexturePack(
                new String[]{"container2.png", "container2_specular.png"}
        );
        Mesh mesh = new Mesh(
                cube.getVertices(),
                cube.getIndices(),
                texturePack,
                shader
        );
        mesh.setup();
        return mesh;
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow(1024, 768);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        try {
            Shader boxShader = shaderSetupBox();
            Mesh boxMesh = setupMesh(boxShader);
            Shader lightSourceShader = shaderSetupLightSource();
            Mesh lightSourceMesh = setupMesh(lightSourceShader);

            Vector3f lightSourcePosition = new Vector3f(0.7f,1f,0.7f);
            Vector3f boxPosition = new Vector3f(0f, 0f, 0f);

            float angleX = 0f;
            float angleY = 0f;
            float fovy = 70f;

            while (!GLFW.glfwWindowShouldClose(mainWindow.getWindow())) {
                boolean keyW = glfwGetKey(mainWindow.getWindow(), GLFW_KEY_W) == GLFW_PRESS;
                boolean keyS = glfwGetKey(mainWindow.getWindow(), GLFW_KEY_S) == GLFW_PRESS;
                boolean keyA = glfwGetKey(mainWindow.getWindow(), GLFW_KEY_A) == GLFW_PRESS;
                boolean keyD = glfwGetKey(mainWindow.getWindow(), GLFW_KEY_D) == GLFW_PRESS;

                boolean keyADD = glfwGetKey(mainWindow.getWindow(), GLFW_KEY_KP_ADD) == GLFW_PRESS;
                boolean keySUB = glfwGetKey(mainWindow.getWindow(), GLFW_KEY_KP_SUBTRACT) == GLFW_PRESS;

                if(keyW) {
                    angleX = (angleX + 1) % 89;
                }
                if(keyS) {
                    angleX = (angleX - 1) % 89;
                }
                if(keyA) {
                    angleY = (angleY + 1) % 360;
                }
                if(keyD) {
                    angleY = (angleY - 1) % 360;
                }
                if(keyADD) {
                    fovy -= 1;
                }
                if(keySUB) {
                    fovy += 1;
                }

                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

                try (MemoryStack stack = MemoryStack.stackPush()) {

                    float radius = (float)Math.cos(Math.toRadians(angleX));
                    Vector3f eyePosition = new Vector3f(
                            (float) (6 * radius * Math.sin(Math.toRadians(angleY))),
                            (float) (6 * Math.sin(Math.toRadians(angleX))),
                            (float) (6 * radius * Math.cos(Math.toRadians(angleY)))
                    );

                    Vector3f direction = new Vector3f(0f, 0f, 0f).sub(eyePosition);
                    Vector3f right = new Vector3f(1f, 0f, 0f).rotateY((float)Math.toRadians(angleY));
                    Vector3f up = direction.cross(right.negate()).normalize();

                    Matrix4f projectionLook = new Matrix4f().perspective(
                            (float)Math.toRadians(fovy),
                            ((float)mainWindow.getWidth()) / ((float)mainWindow.getHeight()),
                            0.1f,
                            200.0f
                    ).lookAt(
                            eyePosition,
                            boxPosition,
                            up
                    );

                    boxMesh.bind();
                    FloatBuffer perspectiveMatrix = projectionLook.get(stack.mallocFloat(16));
                    glUniformMatrix4fv(
                            boxShader.getUniformLocation("projectionLook"),
                            false,
                            perspectiveMatrix
                    );

                    FloatBuffer boxTranslation = new Matrix4f().get(stack.mallocFloat(16));
                    glUniformMatrix4fv(
                            boxShader.getUniformLocation("modelTransform"),
                            false,
                            boxTranslation
                    );
                    FloatBuffer lightPosition = lightSourcePosition.get(stack.mallocFloat(3));
                    glUniform3fv(
                            boxShader.getUniformLocation("lightPos"),
                            lightPosition
                    );
                    FloatBuffer eyePos = eyePosition.get(stack.mallocFloat(3));
                    glUniform3fv(
                            boxShader.getUniformLocation("eyePos"),
                            eyePos
                    );
                    boxMesh.draw();

                    lightSourceMesh.bind();
                    FloatBuffer lightSourceTranslation = new Matrix4f()
                            .translation(lightSourcePosition)
                            .scale(0.1f)
                            .get(stack.mallocFloat(16));
                    glUniformMatrix4fv(
                            lightSourceShader.getUniformLocation("projectionLook"),
                            false,
                            perspectiveMatrix
                    );
                    glUniformMatrix4fv(
                            lightSourceShader.getUniformLocation("modelTransform"),
                            false,
                            lightSourceTranslation
                    );
                    lightSourceMesh.draw();
                }

                mainWindow.swapBuffer();

                GLFW.glfwPollEvents();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
