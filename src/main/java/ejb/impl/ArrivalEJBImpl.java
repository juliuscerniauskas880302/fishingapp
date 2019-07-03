package ejb.impl;

import domain.Arrival;
import ejb.ArrivalEJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

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
        Optional<Arrival> optional = Optional.ofNullable(em.find(Arrival.class, id));
        return optional.orElseGet(null);
    }

    @Override
    public Response create(Arrival arrival) {
        em.persist(arrival);
        return Response.ok("Arrival created").build();
    }

    @Override
    public Response update(Long id, Arrival body) {
        Optional<Arrival> optional = Optional.ofNullable(em.find(Arrival.class, id));
        if (optional.isPresent()) {
            Arrival arrival = optional.get();
            arrival.setPort(body.getPort());
            arrival.setDate(body.getDate());
            em.merge(arrival);
            return Response.ok("Arrival updated").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response remove(Long id) {
        Optional<Arrival> optional = Optional.ofNullable(em.find(Arrival.class, id));
        if (optional.isPresent()) {
            Arrival arrival = optional.get();
            em.remove(arrival);
            return Response.ok("Arrival removed").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
