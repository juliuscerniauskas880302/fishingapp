package ejb.impl;

import domain.Logbook;
import ejb.LogbookEJB;
import enums.CommunicationType;
import strategy.DatabaseSaveStrategy;
import strategy.FileSaveStrategy;
import strategy.SaveStrategy;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Stateless
public class LogbookEBJDBImpl implements LogbookEJB {
    @PersistenceContext
    private EntityManager em;

    private SaveStrategy saveStrategy;

    @Override
    public List<Logbook> findAll() {
        Query q = em.createQuery("SELECT l from Logbook l");
        return q.getResultList();
    }

    @Override
    public Logbook findById(Long id) {
        Optional<Logbook> optional = Optional.ofNullable(em.find(Logbook.class, id));
        return optional.orElseGet(null);
    }

    @Override
    public Response create(Logbook logbook) {
        if (logbook.getCommunicationType() == CommunicationType.OFFLINE) {
            saveStrategy = new FileSaveStrategy();
        } else {
            saveStrategy = new DatabaseSaveStrategy(em);
        }
        return saveStrategy.create(logbook);
    }

    @Override
    public Response update(Long id, Logbook body) {
        Optional<Logbook> optional = Optional.ofNullable(em.find(Logbook.class, id));
        if (optional.isPresent()) {
            Logbook logbook = optional.get();
            logbook.setArrival(body.getArrival());
            logbook.setCatches(body.getCatches());
            logbook.setDeparture(body.getDeparture());
            logbook.setEndFishing(body.getEndFishing());
            em.merge(logbook);
            return Response.ok("Logbook updated").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @Override
    public Response remove(Long id) {
        Optional<Logbook> optional = Optional.ofNullable(em.find(Logbook.class, id));
        if (optional.isPresent()) {
            Logbook logbook = optional.get();
            em.remove(logbook);
            return Response.ok("Logbook removed").build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
