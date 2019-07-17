package service.acatch;

import domain.Catch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
    public Catch findById(String id) {
        return Optional.ofNullable(manager.find(Catch.class, id)).orElse(null);
    }

    @Override
    public List<Catch> findAll() {
        return Optional.ofNullable(manager.createNamedQuery("catch.findAll", Catch.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response save(Catch source) {
        manager.persist(source);
        LOG.info("Catch {} has been created.", source.toString());
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(Catch source, String id) {
        Optional.ofNullable(manager.find(Catch.class, id)).ifPresent(aCatch -> {
            aCatch.setVariety(source.getVariety());
            aCatch.setWeight(source.getWeight());
            manager.merge(aCatch);
            LOG.info("Catch '{}' has been updated.", id);
        });
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Catch.class, id)).ifPresent(aCatch -> {
            manager.remove(aCatch);
            LOG.info("Catch '{}' has been deleted.", id);
        });
    }

}
