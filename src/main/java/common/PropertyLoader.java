package common;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Singleton
@Startup
public class PropertyLoader {
    private static Properties properties;

    @PostConstruct
    public void init() {
        try (InputStream input = new FileInputStream("C:\\dev\\fishingapp\\src\\main\\resources\\application.properties")) {
            properties = new Properties();
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

}
