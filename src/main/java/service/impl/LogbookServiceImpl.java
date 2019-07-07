package service.impl;

import common.ResponseMessage;
import domain.Logbook;
import enums.CommunicationType;
import service.LogbookService;
import strategy.DatabaseSavingStrategy;
import strategy.FileSavingStrategy;
import strategy.SavingStrategy;

import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Stateful
public class LogbookServiceImpl implements LogbookService {
    private static final String FILE_PATH = "C:\\datafiles\\input\\logbook.log";

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Optional<Logbook> findById(String id) {
        return Optional.ofNullable(manager.find(Logbook.class, id));
    }

    @Override
    public Optional<List<Logbook>> findAll() {
        TypedQuery<Logbook> query = manager.createNamedQuery("logbook.findAll", Logbook.class);
        return Optional.ofNullable(query.getResultList());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response save(Logbook source) {
        SavingStrategy savingStrategy;
        if (source.getCommunicationType() == CommunicationType.OFFLINE) {
            savingStrategy = new FileSavingStrategy(FILE_PATH);
        } else {
            savingStrategy = new DatabaseSavingStrategy(manager);
        }
        return savingStrategy.save(source);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response update(Logbook source, String id) {
        Optional<Logbook> optional = Optional.ofNullable(manager.find(Logbook.class, id));
        if (optional.isPresent()) {
            Logbook logbook = optional.get();
            logbook.setDeparture(source.getDeparture());
            logbook.setCatches(source.getCatches());
            logbook.setArrival(source.getArrival());
            logbook.setEndOfFishing(source.getEndOfFishing());
            logbook.setCommunicationType(source.getCommunicationType());
            manager.merge(logbook);
            return Response.status(Response.Status.OK).entity(
                    new ResponseMessage("Logbook with id: " + id + " updated."))
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity(
                new ResponseMessage("Logbook with id: " + id + " does not exists."))
                .build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response deleteById(String id) {
        Optional<Logbook> optional = Optional.ofNullable(manager.find(Logbook.class, id));
        if (optional.isPresent()) {
            manager.remove(optional.get());
            if (manager.contains(optional.get())) {
                return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                        new ResponseMessage("Could delete Logbook with id: " + id))
                        .build();
            } else {
                return Response.status(Response.Status.OK).entity(
                        new ResponseMessage("Logbook with id: " + id + " deleted"))
                        .build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity(
                new ResponseMessage("Could not find Logbook with id: " + id))
                .build();
    }
}
