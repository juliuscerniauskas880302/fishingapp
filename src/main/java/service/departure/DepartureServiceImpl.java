package service.departure;

import domain.Departure;
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
public class DepartureServiceImpl implements DepartureService {
    private static final Logger LOG = LogManager.getLogger(DepartureServiceImpl.class);

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Departure findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(manager.find(Departure.class, id))
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Departure with id: " + id));
    }

    @Override
    public List<Departure> findAll() {
        return Optional.ofNullable(manager.createNamedQuery("departure.findAll", Departure.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public void save(Departure source) {
        manager.persist(source);
        try {
            manager.flush();
            LOG.info("Departure {} has been created.", source.toString());
        } catch (Exception ex) {
            LOG.error("unable to persist Departure {}. {}.", source.getId(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void update(Departure source, String id) throws ResourceNotFoundException, ResourceLockedException {
        Optional.ofNullable(manager.find(Departure.class, id)).map(departure -> {
            departure.setPort(source.getPort());
            departure.setDate(source.getDate());
            manager.merge(departure);
            try {
                manager.flush();
                LOG.info("Departure '{}' has been updated.", id);
            } catch (OptimisticLockException ex) {
                LOG.error("Departure resource {} is locked", id);
                throw new ResourceLockedException("Departure resource with id: " + id + " is locked");
            }
            return Response.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Cannot find Departure with id: " + id));
    }

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Departure.class, id)).ifPresent(departure -> {
            manager.remove(departure);
            LOG.info("Departure '{}' has been deleted.", id);
        });
    }

}