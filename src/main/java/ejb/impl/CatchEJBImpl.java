package ejb.impl;

import domain.Catch;
import ejb.CatchEJB;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

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
    public void create(Catch aCatch) {
        em.persist(aCatch);
    }

    @Override
    public void update(Long id, Catch body) {
        Catch aCatch = em.find(Catch.class, id);
        if(aCatch != null) {
            aCatch.setSpecies(body.getSpecies());
            aCatch.setWeight(body.getWeight());
            em.merge(aCatch);
        }
    }

    @Override
    public void remove(Long id) {
        Catch aCatch = em.find(Catch.class, id);
        em.remove(aCatch);
    }
}
