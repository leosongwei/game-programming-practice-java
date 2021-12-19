package model;

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
