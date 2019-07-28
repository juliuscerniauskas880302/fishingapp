package dao.arrival;

import domain.Arrival;
import service.exception.ResourceNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
public class ArrivalDAOImpl implements ArrivalDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Arrival findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(entityManager.find(Arrival.class, id))
                .orElse(null);
    }

    @Override
    public List<Arrival> findAll() {
        return Optional.ofNullable(entityManager.createNamedQuery("arrival.findAll", Arrival.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public void save(Arrival arrival) {
        entityManager.persist(arrival);
        entityManager.flush();
    }

    @Override
    public void update(Arrival arrival) {
        entityManager.merge(arrival);
        entityManager.flush();
    }

    @Override
    public void deleteById(String id) {
        Optional<Arrival> arrivalOptional = Optional.ofNullable(entityManager.find(Arrival.class, id));
        if (arrivalOptional.isPresent()) {
            entityManager.remove(arrivalOptional.get());
        } else throw new ResourceNotFoundException("Cannot find Arrival with id: " + id);
    }
}
