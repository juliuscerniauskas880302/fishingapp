package service.acatch;

import domain.Catch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.exception.ResourceLockedException;
import service.exception.ResourceNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
public class CatchServiceImpl implements CatchService {
    private static final Logger LOG = LogManager.getLogger(CatchServiceImpl.class);

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Catch findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(manager.find(Catch.class, id))
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Catch with id: " + id));
    }

    @Override
    public List<Catch> findAll() {
        return Optional.ofNullable(manager.createNamedQuery("catch.findAll", Catch.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public void save(Catch source) throws Exception {
        manager.persist(source);
        try {
            manager.flush();
            LOG.info("Catch {} has been created.", source.getId());
        } catch (Exception ex) {
            LOG.info("Unable to persist Catch {}. {}.", source.getId(), ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void update(Catch source, String id) throws ResourceNotFoundException, ResourceLockedException {
        Optional.ofNullable(manager.find(Catch.class, id)).map(aCatch -> {
            aCatch.setVariety(source.getVariety());
            aCatch.setWeight(source.getWeight());
            manager.merge(aCatch);
            try {
                manager.flush();
                LOG.info("Catch {} has been updated.", id);
            } catch (OptimisticLockException ex) {
                LOG.error("Catch resource {} is locked", id);
                throw new ResourceLockedException("Catch resource with id: " + id + " is locked");
            }
            return aCatch;
        }).orElseThrow(() -> new ResourceNotFoundException("Cannot find Catch with id: " + id));
    }

    @Override
    public void deleteById(String id) {
        Optional.ofNullable(manager.find(Catch.class, id)).ifPresent(aCatch -> {
            manager.remove(aCatch);
            LOG.info("Catch '{}' has been deleted.", id);
        });
    }
}
