package service.endOffFishing;

import domain.EndOfFishing;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateful
@Slf4j
public class EndOffFishingServiceImpl implements EndOffFishingService {
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
        log.info("EndOffFishing {} has been created.", source.toString());
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    public void update(EndOfFishing source, String id) {
        Optional.ofNullable(manager.find(EndOfFishing.class, id)).ifPresent(endOfFishing -> {
            endOfFishing.setDate(source.getDate());
            log.info("EndOffFishing '{}' has been updated.", id);
            manager.merge(endOfFishing);
        });
    }

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(EndOfFishing.class, id)).ifPresent(endOfFishing ->
                manager.remove(endOfFishing));
        log.info("EndOffFishing '{}' has been deleted.", id);
    }
}
