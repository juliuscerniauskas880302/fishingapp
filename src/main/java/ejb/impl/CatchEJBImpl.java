package ejb.impl;

import domain.Catch;
import ejb.CatchEJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Stateless
public class CatchEJBImpl implements CatchEJB {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<Catch> findAll() {
        Query q = em.createQuery("SELECT c FROM Catch c");
        return q.getResultList();
    }

    @Override
    public Catch findById(Long id) {
        Optional<Catch> optional = Optional.ofNullable(em.find(Catch.class, id));
        return optional.orElseGet(null);
    }

    @Override
    public Response create(Catch aCatch) {
        em.persist(aCatch);
        return Response.ok("Catch created").build();
    }

    @Override
    public Response update(Long id, Catch body) {
        Optional<Catch> optional = Optional.ofNullable(em.find(Catch.class, id));
        if (optional.isPresent()) {
            Catch aCatch = optional.get();
            aCatch.setSpecies(body.getSpecies());
            aCatch.setWeight(body.getWeight());
            em.merge(aCatch);
            return Response.ok("Catch updated").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response remove(Long id) {
        Optional<Catch> optional = Optional.ofNullable(em.find(Catch.class, id));
        if (optional.isPresent()) {
            Catch aCatch = optional.get();
            em.remove(aCatch);
            return Response.ok("Catch deleted").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
