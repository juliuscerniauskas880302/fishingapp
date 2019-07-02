package ejb.impl;

import domain.Arrival;
import ejb.ArrivalEJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ArrivalEJBImpl implements ArrivalEJB {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<Arrival> findAll() {
        Query q = em.createQuery("SELECT a FROM Arrival a");
        return q.getResultList();
    }

    @Override
    public void create(Arrival arrival) {
        em.persist(arrival);
    }

    @Override
    public void update(Long id, Arrival body) {
        Arrival arrival = em.find(Arrival.class, id);
        arrival.setPort(body.getPort());
        arrival.setDate(body.getDate());
        em.merge(arrival);
    }

    @Override
    public void remove(Long id) {
        Arrival arrival = em.find(Arrival.class, id);
        em.remove(arrival);
    }
}
