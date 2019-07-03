package ejb.impl;

import domain.Departure;
import ejb.DepartureEJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

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
        Optional<Departure> optional = Optional.ofNullable(em.find(Departure.class, id));
        return optional.orElseGet(null);
    }

    @Override
    public Response create(Departure departure) {
        em.persist(departure);
        return Response.ok("Departure created").build();
    }

    @Override
    public Response update(Long id, Departure body) {
        Optional<Departure> optional = Optional.ofNullable(em.find(Departure.class, id));
        if (optional.isPresent()) {
            Departure departure = optional.get();
            departure.setDate(body.getDate());
            departure.setPort(body.getPort());
            em.merge(departure);
            return Response.ok("Departure updated").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response remove(Long id) {
        Optional<Departure> optional = Optional.ofNullable(em.find(Departure.class, id));
        if (optional.isPresent()) {
            Departure departure = optional.get();
            em.remove(departure);
            return Response.ok("Departure deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
