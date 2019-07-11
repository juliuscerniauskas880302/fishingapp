package service.config;

import domain.config.Configuration;

import javax.ejb.Stateful;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateful
public class ConfigServiceImpl implements ConfigService {
    private static final String FIND_VALUE_BY_KEY = "SELECT c.VALUE FROM CONFIGURATION c WHERE c.KEY = ?1";
    private static final String FIND_CONFIG_BY_KEY = "SELECT * FROM CONFIGURATION WHERE KEY = ?1";
    private static final String FIND_ALL_CONFIG = "SELECT * FROM CONFIGURATION";

    @PersistenceContext
    private EntityManager manager;


    @Override
    public void add(Configuration configuration) {
        manager.persist(configuration);
    }

    @Override
    public void delete(String key) {
        Optional.ofNullable(manager.createNativeQuery(FIND_CONFIG_BY_KEY, Configuration.class)
                .setParameter(1, key)).ifPresent(r -> {
            Configuration config = (Configuration) r.getSingleResult();
            manager.remove(config);
        });
    }

    @Override
    public void update(String key, String value, String description) {
        Optional.ofNullable(manager.createNativeQuery(FIND_CONFIG_BY_KEY, Configuration.class)
                .setParameter(1, key)).ifPresent(r -> {
            Configuration config = (Configuration) r.getSingleResult();
            config.setValue(value);
            config.setDescription(description);
            manager.merge(config);
        });
    }

    @Override
    public String getOne(String key, String defaultValue) {
        return Optional.ofNullable(manager.createNativeQuery(FIND_VALUE_BY_KEY, Configuration.class)
                .setParameter(1, key)
                .setMaxResults(1)
                .getResultList().stream().findFirst().toString()).orElse(defaultValue);
    }

    @Override
    public List<Configuration> getAll() {
        return Optional.ofNullable(manager.createNativeQuery(FIND_ALL_CONFIG, Configuration.class)
                .getResultList()).orElse(Collections.emptyList());
    }
}
