package service.arrival;

import domain.Arrival;
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
public class ArrivalServiceImpl implements ArrivalService {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Arrival findById(String id) {
        return Optional.ofNullable(manager.find(Arrival.class, id)).orElse(null);
    }

    @Override
    public List<Arrival> findAll() {
        return Optional.ofNullable(manager.createNamedQuery("arrival.findAll", Arrival.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response save(Arrival source) {
        log.info("Arrival {} has been added to db.", source.toString());
        manager.persist(source);
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(Arrival source, String id) {
        Optional.ofNullable(manager.find(Arrival.class, id)).ifPresent(arrival -> {
            arrival.setPort(source.getPort());
            arrival.setDate(source.getDate());
            manager.merge(arrival);
            log.info("Arrival '{}' has been updated.", id);
        });
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Arrival.class, id)).ifPresent(arrival ->
                manager.remove(arrival));
        log.info("Arrival '{}' has been deleted.", id);
    }
}
