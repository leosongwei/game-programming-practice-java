package Shader;

import static org.lwjgl.opengl.GL46.*;

public class Shader {
    public static int compileShaderFromString(int shaderType, String shaderProgram) throws Exception {
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

    public static int createProgramWithShaderStrings(String vertexShader, String fragmentShader) throws Exception {
        int programID = 0;
        int vertexShaderProgram = Shader.compileShaderFromString(GL_VERTEX_SHADER, vertexShader);
        int fragmentShaderProgram = Shader.compileShaderFromString(GL_FRAGMENT_SHADER, fragmentShader);

        programID = glCreateProgram();
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

        return programID;
    }
}
