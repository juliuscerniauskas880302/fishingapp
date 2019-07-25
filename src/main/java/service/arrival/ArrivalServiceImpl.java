package service.arrival;

import domain.Arrival;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.exception.ResourceLockedException;
import service.exception.ResourceNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
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
    public Arrival findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(manager.find(Arrival.class, id))
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Arrival with id: " + id));
    }

    @Override
    public List<Arrival> findAll() {
        return Optional.ofNullable(manager.createNamedQuery("arrival.findAll", Arrival.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public void save(Arrival source) {
        manager.persist(source);
        try {
            manager.flush();
            LOG.info("Arrival {} has been created.", source.getId());
        } catch (Exception ex) {
            LOG.info("Unable to persist Arrival {}. {}.", source.getId(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void update(Arrival source, String id) throws ResourceNotFoundException, ResourceLockedException {
        Optional.ofNullable(manager.find(Arrival.class, id)).map(arrival -> {
            arrival.setPort(source.getPort());
            arrival.setDate(source.getDate());
            manager.merge(arrival);
            try {
                manager.flush();
                LOG.info("Arrival '{}' has been updated.", id);
            } catch (OptimisticLockException ex) {
                LOG.error("Arrival resource {} is locked", id);
                throw new ResourceLockedException("Arrival resource with id: " + id + " is locked");
            }
            return Response.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Cannot find Arrival with id: " + id));
    }

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Arrival.class, id)).ifPresent(arrival -> {
            manager.remove(arrival);
            LOG.info("Arrival '{}' has been deleted.", id);
        });
    }
}
