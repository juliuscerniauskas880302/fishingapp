package service.acatch;

import domain.Catch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.exception.ResourceNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
public class CatchServiceImpl implements CatchService {
    private static final Logger LOG = LogManager.getLogger(CatchServiceImpl.class);

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Catch findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(manager.find(Catch.class, id))
                .orElseThrow(()-> new ResourceNotFoundException("Cannot find Catch with id: " + id));
    }

    @Override
    public List<Catch> findAll() {
        return Optional.ofNullable(manager.createNamedQuery("catch.findAll", Catch.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public Response save(Catch source) {
        manager.persist(source);
        LOG.info("Catch {} has been created.", source.toString());
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    public void update(Catch source, String id) {
        Optional.ofNullable(manager.find(Catch.class, id)).ifPresent(aCatch -> {
            aCatch.setVariety(source.getVariety());
            aCatch.setWeight(source.getWeight());
            manager.merge(aCatch);
            LOG.info("Catch '{}' has been updated.", id);
        });
    }

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Catch.class, id)).ifPresent(aCatch -> {
            manager.remove(aCatch);
            LOG.info("Catch '{}' has been deleted.", id);
        });
    }

}
