package ejb.impl;

import domain.Departure;
import ejb.DepartureEJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class DepartureEBJImpl implements DepartureEJB {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<Departure> findAll() {
        Query q = em.createQuery("SELECT d FROM Departure d");
        return q.getResultList();
    }

    @Override
    public void create(Departure departure) {
        em.persist(departure);
    }

    @Override
    public void update(Long id, Departure body) {
        Departure departure = em.find(Departure.class, id);
        if(departure != null) {
            departure.setDate(body.getDate());
            departure.setPort(body.getPort());
            em.merge(departure);
        }
    }

    @Override
    public void remove(Long id) {
        Departure departure = em.find(Departure.class, id);
        em.remove(departure);
    }
}
