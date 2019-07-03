package ejb.impl;

import domain.Logbook;
import ejb.LogbookEJB;
import enums.CommunicationType;
import strategy.DatabaseSaveStrategy;
import strategy.FileSaveStrategy;
import strategy.SaveContext;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
public class LogbookEBJImpl implements LogbookEJB {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private SaveContext saveContext;

    @Override
    public List<Logbook> findAll() {
        Query q = em.createQuery("SELECT l from Logbook l");
        return q.getResultList();
    }

    @Override
    public Logbook findById(Long id) {
        return em.find(Logbook.class, id);
    }

    @Override
    public Response create(Logbook logbook) {
        if (logbook.getCommunicationType() == CommunicationType.OFFLINE) {
            saveContext.setSaveStrategy(new FileSaveStrategy());
        } else {
            saveContext.setSaveStrategy(new DatabaseSaveStrategy());
        }
        return saveContext.save(logbook);
    }

    @Override
    public Response update(Long id, Logbook body) {
        Logbook logbook = em.find(Logbook.class, id);
        if (logbook == null) {
            return Response.status(404).build();
        }
        logbook.setArrival(body.getArrival());
        logbook.setCatches(body.getCatches());
        logbook.setDeparture(body.getDeparture());
        logbook.setEndFishing(body.getEndFishing());
        em.merge(logbook);
        return Response.ok("Logbook updated").build();
    }

    @Override
    public Response remove(Long id) {
        Logbook logbook = em.find(Logbook.class, id);
        if (logbook == null) {
            return Response.status(404).build();
        }
        em.remove(logbook);
        return Response.ok("Logbook removed").build();
    }
}
