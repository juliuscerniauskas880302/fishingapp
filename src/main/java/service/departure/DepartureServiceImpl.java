package service.departure;

import domain.Departure;

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
public class DepartureServiceImpl implements DepartureService {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Departure findById(String id) {
        return Optional.ofNullable(manager.find(Departure.class, id)).orElse(null);
    }

    @Override
    public List<Departure> findAll() {
        return Optional.ofNullable(manager.createNamedQuery("departure.findAll", Departure.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response save(Departure source) {
        manager.persist(source);
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(Departure source, String id) {
        Optional.ofNullable(manager.find(Departure.class, id)).ifPresent(departure -> {
            departure.setPort(source.getPort());
            departure.setDate(source.getDate());
            manager.merge(departure);
        });
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Departure.class, id)).ifPresent(departure ->
                manager.remove(departure));
    }

}