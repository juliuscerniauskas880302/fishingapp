package service.logbook;

import domain.Logbook;

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
public class LogbookServiceImpl implements LogbookService {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Logbook findById(String id) {
        return Optional.ofNullable(manager.find(Logbook.class, id)).orElse(null);
    }

    @Override
    public List<Logbook> findAll() {
        return Optional.ofNullable(manager.createNamedQuery("logbook.findAll", Logbook.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response save(Logbook source) {
        manager.persist(source);
        return Response.status(Response.Status.OK).build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(Logbook source, String id) {
        Optional.ofNullable(manager.find(Logbook.class, id)).ifPresent(logbook -> {
            logbook.setCommunicationType(source.getCommunicationType());
            logbook.setArrival(source.getArrival());
            logbook.setCatches(source.getCatches());
            logbook.setDeparture(source.getDeparture());
            logbook.setEndOfFishing(source.getEndOfFishing());
            manager.merge(logbook);
        });
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Logbook.class, id)).ifPresent(logbook ->
                manager.remove(logbook));
    }
}