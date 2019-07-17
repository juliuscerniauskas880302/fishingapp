package service.endOfFishing;

import domain.EndOfFishing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
public class EndOfFishingServiceImpl implements EndOfFishingService {
    private static final Logger LOG = LogManager.getLogger(EndOfFishingServiceImpl.class);

    @PersistenceContext
    private EntityManager manager;

    @Override
    public EndOfFishing findById(String id) {
        return Optional.ofNullable(manager.find(EndOfFishing.class, id)).orElse(null);
    }

    @Override
    public List<EndOfFishing> findAll() {
        return Optional.ofNullable(manager.createNamedQuery("endOfFishing.findAll", EndOfFishing.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public Response save(EndOfFishing source) {
        manager.persist(source);
        LOG.info("EndOfFishing {} has been created.", source.toString());
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    public void update(EndOfFishing source, String id) {
        Optional.ofNullable(manager.find(EndOfFishing.class, id)).ifPresent(endOfFishing -> {
            endOfFishing.setDate(source.getDate());
            LOG.info("EndOfFishing '{}' has been updated.", id);
            manager.merge(endOfFishing);
        });
    }

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(EndOfFishing.class, id)).ifPresent(endOfFishing ->
                manager.remove(endOfFishing));
        LOG.info("EndOfFishing '{}' has been deleted.", id);
    }

}
