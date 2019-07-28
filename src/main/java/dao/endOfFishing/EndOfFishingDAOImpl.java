package dao.endOfFishing;

import domain.EndOfFishing;
import service.exception.ResourceNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
public class EndOfFishingDAOImpl implements EndOfFishingDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EndOfFishing findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(entityManager.find(EndOfFishing.class, id))
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Arrival with id: " + id));
    }

    @Override
    public List<EndOfFishing> findAll() {
        return Optional.ofNullable(entityManager.createNamedQuery("endOfFishing.findAll", EndOfFishing.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public void save(EndOfFishing endOfFishing) {
        entityManager.persist(endOfFishing);
        entityManager.flush();
    }

    @Override
    public void update(EndOfFishing endOfFishing) {
        entityManager.merge(endOfFishing);
        entityManager.flush();
    }

    @Override
    public void deleteById(String id) {
        Optional<EndOfFishing> endOfFishingOptional = Optional.ofNullable(entityManager.find(EndOfFishing.class, id));
        if (endOfFishingOptional.isPresent()) {
            entityManager.remove(endOfFishingOptional.get());
        } else throw new ResourceNotFoundException("Cannot find Arrival with id: " + id);
    }
}
