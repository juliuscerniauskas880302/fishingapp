package service.config;

import domain.config.Configuration;

import javax.ws.rs.core.Response;
import java.util.List;

public interface ConfigService {

    Response add(Configuration configuration);
    void delete(String key);
    void update(String key, String value, String description);
    String getValueByKey(String key, String defaultValue);
    List<Configuration> getAll();

}
