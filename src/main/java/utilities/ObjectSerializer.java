package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class ObjectSerializer<T extends Object> {
    private static final Logger LOG = LogManager.getLogger(ObjectSerializer.class);

    public String serialize(T object) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(object);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            LOG.error("Error occur during serialization. {}. {}.", e.getCause(), e.getMessage());
        }
        return null;
    }

    public T deSerialize(String serialized, Class<T> tClass) {
        byte[] data = Base64.getDecoder().decode(serialized);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            T object = (T) objectInputStream.readObject();
            return object;
        } catch (Exception e) {
            LOG.error("Error occur during serialization. {}. {}.", e.getCause(), e.getMessage());
        }
        return null;
    }
}
