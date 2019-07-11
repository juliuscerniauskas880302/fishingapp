package service.config;

import domain.config.Configuration;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateful
public class ConfigServiceImpl implements ConfigService {
    private static final String FIND_CONFIG_BY_KEY = "SELECT C.* FROM CONFIGURATION C where C.KEY = ?1";
    private static final String FIND_ALL_CONFIG = "SELECT * FROM CONFIGURATION";

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Response add(Configuration configuration) {
        manager.persist(configuration);
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    public void delete(String key) {
        Optional.ofNullable(manager.createNativeQuery(FIND_CONFIG_BY_KEY, Configuration.class).setParameter(1, key)).ifPresent(
                query -> manager.remove(query.getResultList().get(0))
        );
    }

    @Override
    public void update(String key, String value, String description) {
        Optional.ofNullable(manager.createNativeQuery(FIND_CONFIG_BY_KEY, Configuration.class).setParameter(1, key)).ifPresent(
                query -> {
                    Configuration config = (Configuration) query.getResultList().get(0);
                    config.setDescription(description);
                    config.setValue(value);
                    manager.merge(config);
                }
        );
    }

    @Override
    public String getValueByKey(String key, String defaultValue) {
        TypedQuery<Configuration> nativeQuery = (TypedQuery<Configuration>) manager.createNativeQuery(FIND_CONFIG_BY_KEY, Configuration.class)
                .setParameter(1, key);
        return nativeQuery.getResultList().stream().findFirst().map(Configuration::getValue).orElse(defaultValue);
    }

    @Override
    public List<Configuration> getAll() {
        return Optional.ofNullable(manager.createNativeQuery(FIND_ALL_CONFIG, Configuration.class)
                .getResultList()).orElse(Collections.emptyList());
    }
}
