package utils;

import java.io.InputStream;

public class ResourceFile {
    public byte[] getAsBytes(String filepath) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream stream = classLoader.getResourceAsStream(filepath);
            assert stream != null;

            return stream.readAllBytes();
        } catch (Exception e) {
            return null;
        }
    }
}
