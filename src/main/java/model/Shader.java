package model;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL46.*;

public class Shader {
    private final int programID;

    public Shader(String vertexShaderCode, String fragmentShaderCode) throws Exception {
        int vertexShaderProgram = compileShaderFromString(GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShaderProgram = compileShaderFromString(GL_FRAGMENT_SHADER, fragmentShaderCode);

        programID = glCreateProgram();
        linkShaders(programID, vertexShaderProgram, fragmentShaderProgram);
    }

    public void use() {
        glUseProgram(programID);
    }

    public int getUniformLocation(String name) {
        return glGetUniformLocation(programID, name);
    }

    public void uniformMatrix4fv(String name, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            uniformMatrix4fv(name, matrix, stack);
        }
    }

    public void uniformMatrix4fv(String name, Matrix4f matrix, MemoryStack stack) {
        FloatBuffer buffer = matrix.get(stack.mallocFloat(16));
        uniformMatrix4fv(name, buffer);
    }

    public void uniformMatrix4fv(String name, FloatBuffer buffer) {
        int position = getUniformLocation(name);
        glUniformMatrix4fv(position, false, buffer);
    }

    public void uniform3fv(String name, Vector3f vector, MemoryStack stack) {
        FloatBuffer buffer = vector.get(stack.mallocFloat(3));
        uniform3fv(name, buffer);
    }

    public void uniform3fv(String name, FloatBuffer buffer) {
        int position = getUniformLocation(name);
        glUniform3fv(position, buffer);
    }

    private static int compileShaderFromString(int shaderType, String shaderProgram) throws Exception {
        int shaderID = glCreateShader(shaderType);
        if (shaderID == 0) {
            throw new Exception("Unable to glCreateShader");
        }

        glShaderSource(shaderID, shaderProgram);
        glCompileShader(shaderID);

        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0) {
            String log = glGetShaderInfoLog(shaderID);
            System.out.printf("%s\n", log);
            throw new Exception(String.format("shader compile failed: %s", log));
        }

        return shaderID;
    }

    private static void linkShaders(int programID, int vertexShaderProgram, int fragmentShaderProgram) throws Exception {
        glAttachShader(programID, vertexShaderProgram);
        glAttachShader(programID, fragmentShaderProgram);
        glLinkProgram(programID);

        glDetachShader(programID, vertexShaderProgram);
        glDetachShader(programID, fragmentShaderProgram);

        glDeleteShader(vertexShaderProgram);
        glDeleteShader(fragmentShaderProgram);

        if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
            System.out.printf("Shader program link error: %s\n", glGetProgramInfoLog(programID));
            throw new Exception("Shader program link error");
        }
    }

}
