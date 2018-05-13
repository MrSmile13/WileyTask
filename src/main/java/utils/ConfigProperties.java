package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigProperties {

    protected static FileInputStream fis;
    protected static Properties PROPERTIES;

    static {
        try {
            fis = new FileInputStream("src/main/resources/config.properties");
            PROPERTIES = new Properties();
            PROPERTIES.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
}
