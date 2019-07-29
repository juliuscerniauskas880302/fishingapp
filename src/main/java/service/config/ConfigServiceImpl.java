package service.config;

import domain.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.inject.Model;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Model
@SuppressWarnings("unchecked")
public class ConfigServiceImpl implements ConfigService {
    private static final Logger LOG = LogManager.getLogger(ConfigServiceImpl.class);
    private static final String FIND_CONFIG_BY_KEY = "SELECT C.* FROM CONFIGURATION C where C.KEY = ?1";
    private static final String FIND_ALL_CONFIG = "SELECT * FROM CONFIGURATION";

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Response add(Configuration configuration) {
        manager.persist(configuration);
        manager.flush();
        LOG.info("Configuration {} has been created", configuration.toString());
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    public void delete(String key) {
        try {
            Configuration singleResult = (Configuration) manager.createNativeQuery(FIND_CONFIG_BY_KEY, Configuration.class)
                    .setParameter(1, key).getSingleResult();
            manager.remove(singleResult);
            manager.flush();
            LOG.info("Configuration '{}' has been deleted", key);
        } catch (NoResultException ex) {
            LOG.error("Configuration '{}' does not exists", key);
        }
    }

    @Override
    public void update(String key, String value, String description) {
        try {
            Configuration singleResult = (Configuration) manager.createNativeQuery(FIND_CONFIG_BY_KEY, Configuration.class)
                    .setParameter(1, key).getSingleResult();
            singleResult.setValue(value);
            singleResult.setDescription(description);
            manager.merge(singleResult);
            manager.flush();
            LOG.info("Configuration '{}' updated", key);
        } catch (NoResultException ex) {
            LOG.error("Configuration '{}' does not exists", key);
        }
    }

    @Override
    public String getValueByKey(String key, String defaultValue) {
        try {
            return ((Configuration) manager.createNativeQuery(FIND_CONFIG_BY_KEY, Configuration.class)
                    .setParameter(1, key).getSingleResult()).getValue();
        } catch (NoResultException ex) {
            return defaultValue;
        }
    }

    @Override
    public List<Configuration> getAll() {
        return Optional.ofNullable(manager.createNativeQuery(FIND_ALL_CONFIG, Configuration.class)
                .getResultList()).orElse(Collections.emptyList());
    }
}
