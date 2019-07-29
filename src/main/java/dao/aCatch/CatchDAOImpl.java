package dao.aCatch;

import domain.Catch;
import exception.ResourceLockedException;
import exception.ResourceNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
public class CatchDAOImpl implements CatchDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Catch findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(entityManager.find(Catch.class, id))
                .orElse(null);
    }

    @Override
    public List<Catch> findAll() {
        return Optional.ofNullable(entityManager.createNamedQuery("catch.findAll", Catch.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public void save(Catch aCatch) {
        entityManager.persist(aCatch);
        entityManager.flush();
    }

    @Override
    public void update(Catch aCatch) throws ResourceNotFoundException, ResourceLockedException {
        entityManager.merge(aCatch);
        entityManager.flush();
    }

    @Override
    public void deleteById(String id) {
        Optional<Catch> arrivalOptional = Optional.ofNullable(entityManager.find(Catch.class, id));
        if (arrivalOptional.isPresent()) {
            entityManager.remove(arrivalOptional.get());
        } else throw new ResourceNotFoundException("Cannot find Catch with id: " + id);
    }
}
