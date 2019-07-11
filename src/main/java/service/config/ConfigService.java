package service.config;

import domain.config.Configuration;

import java.util.List;

public interface ConfigService {
    void add(Configuration configuration);
    void delete(String key);
    void update(String key, String value, String description);
    String getOne(String key, String defaultValue);
    List<Configuration> getAll();
}
