package ejb.impl;

import domain.Departure;
import ejb.DepartureEJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
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
    public Departure findById(Long id) {
        return em.find(Departure.class, id);
    }

    @Override
    public Response create(Departure departure) {
        em.persist(departure);
        return Response.ok("Departure created").build();
    }

    @Override
    public Response update(Long id, Departure body) {
        Departure departure = em.find(Departure.class, id);
        if (departure != null) {
            departure.setDate(body.getDate());
            departure.setPort(body.getPort());
            em.merge(departure);
            return Response.ok("Departure updated").build();
        }
        return Response.status(404).build();
    }

    @Override
    public Response remove(Long id) {
        Departure departure = em.find(Departure.class, id);
        if (departure != null) {
            em.remove(departure);
            return Response.ok("Departure deleted").build();
        }
        return Response.status(404).build();
    }
}
