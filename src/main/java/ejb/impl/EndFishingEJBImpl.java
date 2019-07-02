package ejb.impl;

import domain.EndFishing;
import ejb.EndFishingEJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

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
        return em.find(EndFishing.class, id);
    }

    @Override
    public Response create(EndFishing endFishing) {
        em.persist(endFishing);
        return Response.ok("EndOfFishing created").build();
    }

    @Override
    public Response update(Long id, EndFishing body) {
        EndFishing endFishing = em.find(EndFishing.class, id);
        if (endFishing != null) {
            endFishing.setDate(body.getDate());
            em.merge(endFishing);
            return Response.ok("EndOfFishing updated").build();
        }
        return Response.status(404).build();
    }

    @Override
    public Response remove(Long id) {
        EndFishing endFishing = em.find(EndFishing.class, id);
        if (endFishing != null) {
            em.remove(endFishing);
            return Response.ok("EndOfFishing removed").build();
        }
        return Response.status(404).build();
    }
}
