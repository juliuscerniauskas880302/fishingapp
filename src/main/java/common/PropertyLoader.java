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
        // TODO add file stream to read properties from file
    }

    public static String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }

}
