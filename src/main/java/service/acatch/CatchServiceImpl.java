package service.acatch;

import domain.Catch;
import lombok.extern.slf4j.Slf4j;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateful
@Slf4j
public class CatchServiceImpl implements CatchService {
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
        log.info("Catch {} has been created.", source.toString());
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(Catch source, String id) {
        Optional.ofNullable(manager.find(Catch.class, id)).ifPresent(aCatch -> {
            aCatch.setVariety(source.getVariety());
            aCatch.setWeight(source.getWeight());
            manager.merge(aCatch);
            log.info("Catch '{}' has been updated.", id);
        });
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Catch.class, id)).ifPresent(aCatch ->
                manager.remove(aCatch));
        log.info("Catch '{}' has been deleted.", id);
    }
}
