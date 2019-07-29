package utilities;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public class ObjectMapper<T extends Object> {
    com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    public String serialize(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public T deSerialize(String serialized, Class<T> tClass) {
        try {
            return objectMapper.readValue(serialized, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
