package service.departure;

import domain.Departure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class DepartureServiceImpl implements DepartureService {
    private static final Logger LOG = LoggerFactory.getLogger(DepartureServiceImpl.class);

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
        LOG.info("Departure {} has been created.", source.toString());
        return Response.status(Response.Status.CREATED).build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void update(Departure source, String id) {
        Optional.ofNullable(manager.find(Departure.class, id)).ifPresent(departure -> {
            departure.setPort(source.getPort());
            departure.setDate(source.getDate());
            LOG.info("Departure '{}' has been updated.", id);
            manager.merge(departure);
        });
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Departure.class, id)).ifPresent(departure ->
                manager.remove(departure));
        LOG.info("Departure '{}' has been deleted.", id);
    }

}