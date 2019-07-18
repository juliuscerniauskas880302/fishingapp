package service.arrival;

import domain.Arrival;
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
public class ArrivalServiceImpl implements ArrivalService {
    private static final Logger LOG = LogManager.getLogger(ArrivalServiceImpl.class);

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
        LOG.info("Arrival {} has been added to db.", source.getId());
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
            LOG.info("Arrival '{}' has been updated.", id);
        });
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Arrival.class, id)).ifPresent(arrival -> {
            manager.remove(arrival);
            LOG.info("Arrival '{}' has been deleted.", id);
        });
    }

}
