package ejb.impl;

import domain.EndFishing;
import ejb.EndFishingEJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public void create(EndFishing endFishing) {
        em.persist(endFishing);
    }

    @Override
    public void update(Long id, EndFishing endFishing) {

    }

    @Override
    public void remove(Long id) {
        EndFishing endFishing = em.find(EndFishing.class, id);
        em.remove(endFishing);
    }
}
