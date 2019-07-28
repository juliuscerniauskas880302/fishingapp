package dao.departure;

import domain.Departure;
import service.exception.ResourceNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
public class DepartureDAOImpl implements DepartureDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Departure findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(entityManager.find(Departure.class, id))
                .orElse(null);
    }

    @Override
    public List<Departure> findAll() {
        return Optional.ofNullable(entityManager.createNamedQuery("departure.findAll", Departure.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public void save(Departure departure) {
        entityManager.persist(departure);
        entityManager.flush();
    }

    @Override
    public void update(Departure departure) {
        entityManager.merge(departure);
        entityManager.flush();
    }

    @Override
    public void deleteById(String id) {
        Optional<Departure> departureOptional = Optional.ofNullable(entityManager.find(Departure.class, id));
        if (departureOptional.isPresent()) {
            entityManager.remove(departureOptional.get());
        } else throw new ResourceNotFoundException("Cannot find Arrival with id: " + id);
    }
}
