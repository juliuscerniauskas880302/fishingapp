package ejb.impl;

import domain.Arrival;
import ejb.ArrivalEJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
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
    public Arrival findById(Long id) {
        return em.find(Arrival.class, id);
    }

    @Override
    public Response create(Arrival arrival) {
        em.persist(arrival);
        return Response.ok("Arrival created").build();
    }

    @Override
    public Response update(Long id, Arrival body) {
        Arrival arrival = em.find(Arrival.class, id);
        if (arrival != null) {
            arrival.setPort(body.getPort());
            arrival.setDate(body.getDate());
            em.merge(arrival);
            return Response.ok("Arrival updated").build();
        }
        return Response.status(404).build();
    }

    @Override
    public Response remove(Long id) {
        Arrival arrival = em.find(Arrival.class, id);
        if (arrival != null) {
            em.remove(arrival);
            return Response.ok("Arrival removed").build();
        }
        return Response.status(404).build();
    }
}
