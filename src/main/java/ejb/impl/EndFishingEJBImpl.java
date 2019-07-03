package ejb.impl;

import domain.EndFishing;
import ejb.EndFishingEJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Stateless
public class EndFishingEJBImpl implements EndFishingEJB {
    @PersistenceContext
    EntityManager em;

    @Override
    public List<EndFishing> findAll() {
        Query q = em.createQuery("SELECT e FROM EndFishing e");
        return q.getResultList();
    }

    @Override
    public EndFishing findById(Long id) {
        Optional<EndFishing> optional = Optional.ofNullable(em.find(EndFishing.class, id));
        return optional.orElseGet(null);
    }

    @Override
    public Response create(EndFishing endFishing) {
        em.persist(endFishing);
        return Response.ok("EndOfFishing created").build();
    }

    @Override
    public Response update(Long id, EndFishing body) {
        Optional<EndFishing> optional = Optional.ofNullable(em.find(EndFishing.class, id));
        if (optional.isPresent()) {
            EndFishing endFishing = optional.get();
            endFishing.setDate(body.getDate());
            em.merge(endFishing);
            return Response.ok("EndOfFishing updated").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response remove(Long id) {
        Optional<EndFishing> optional = Optional.ofNullable(em.find(EndFishing.class, id));
        if (optional.isPresent()) {
            EndFishing endFishing = optional.get();
            em.remove(endFishing);
            return Response.ok("EndOfFishing removed").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
