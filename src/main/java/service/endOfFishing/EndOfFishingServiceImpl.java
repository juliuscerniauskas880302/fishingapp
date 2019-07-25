package service.endOfFishing;

import domain.EndOfFishing;
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
public class EndOfFishingServiceImpl implements EndOfFishingService {
    private static final Logger LOG = LogManager.getLogger(EndOfFishingServiceImpl.class);

    @PersistenceContext
    private EntityManager manager;

    @Override
    public EndOfFishing findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(manager.find(EndOfFishing.class, id))
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find EndOfFishing with id: " + id));
    }

    @Override
    public List<EndOfFishing> findAll() {
        return Optional.ofNullable(manager.createNamedQuery("endOfFishing.findAll", EndOfFishing.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public void save(EndOfFishing source) {
        manager.persist(source);
        try {
            manager.flush();
            LOG.info("EndOfFishing {} has been created.", source.toString());
        } catch (Exception ex) {
            LOG.error("unable to persist EndOfFishing {}. {}.", source.getId(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void update(EndOfFishing source, String id) throws ResourceNotFoundException, ResourceLockedException {
        Optional.ofNullable(manager.find(EndOfFishing.class, id)).map(endOfFishing -> {
            endOfFishing.setDate(source.getDate());
            manager.merge(endOfFishing);
            try {
                manager.flush();
                LOG.info("EndOfFishing '{}' has been updated.", id);
            } catch (OptimisticLockException ex) {
                LOG.error("EndOfFishing resource {} is locked", id);
                throw new ResourceLockedException("EndOfFishing resource with id: " + id + " is locked");
            }
            return Response.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Cannot find Departure with id: " + id));
    }

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(EndOfFishing.class, id)).ifPresent(endOfFishing -> {
            manager.remove(endOfFishing);
            LOG.info("EndOfFishing '{}' has been deleted.", id);
        });
    }

}
