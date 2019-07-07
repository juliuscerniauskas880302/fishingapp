package service.impl;

import common.ResponseMessage;
import domain.Departure;
import service.DepartureService;

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
public class DepartureServiceImpl implements DepartureService {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Optional<Departure> findById(String id) {
        return Optional.ofNullable(manager.find(Departure.class, id));
    }

    @Override
    public Optional<List<Departure>> findAll() {
        TypedQuery<Departure> query = manager.createNamedQuery("departure.findAll", Departure.class);
        return Optional.ofNullable(query.getResultList());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response save(Departure source) {
        manager.persist(source);
        if (manager.contains(source)) {
            return Response.status(Response.Status.OK).entity(source).build();
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ResponseMessage("Could not add")).build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response update(Departure source, String id) {
        Optional<Departure> optional = Optional.ofNullable(manager.find(Departure.class, id));
        if (optional.isPresent()) {
            Departure departure = optional.get();
            departure.setDate(source.getDate());
            departure.setPort(source.getPort());
            manager.merge(departure);
            return Response.status(Response.Status.OK).entity(
                    new ResponseMessage("Departure with id: " + id + " updated."))
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity(
                new ResponseMessage("Departure with id: " + id + " does not exists."))
                .build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response deleteById(String id) {
        Optional<Departure> optional = Optional.ofNullable(manager.find(Departure.class, id));
        if (optional.isPresent()) {
            manager.remove(optional.get());
            if (manager.contains(optional.get())) {
                return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                        new ResponseMessage("Could delete Departure with id: " + id))
                        .build();
            } else {
                return Response.status(Response.Status.OK).entity(
                        new ResponseMessage("Departure with id: " + id + " deleted"))
                        .build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity(
                new ResponseMessage("Could not find Departure with id: " + id))
                .build();
    }

}