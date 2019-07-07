package service.impl;

import common.ResponseMessage;
import domain.Arrival;
import service.ArrivalService;

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
public class ArrivalServiceImpl implements ArrivalService {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Optional<Arrival> findById(String id) {
        return Optional.ofNullable(manager.find(Arrival.class, id));
    }

    @Override
    public Optional<List<Arrival>> findAll() {
        TypedQuery<Arrival> query = manager.createNamedQuery("arrival.findAll", Arrival.class);
        return Optional.ofNullable(query.getResultList());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response save(Arrival source) {
        manager.persist(source);
        if (manager.contains(source)) {
            return Response.status(Response.Status.OK).entity(source).build();
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ResponseMessage("Could not add")).build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response update(Arrival source, String id) {
        Optional<Arrival> optional = Optional.ofNullable(manager.find(Arrival.class, id));
        if (optional.isPresent()) {
            Arrival arrival = optional.get();
            arrival.setPort(source.getPort());
            arrival.setDate(source.getDate());
            manager.merge(arrival);
            return Response.status(Response.Status.OK).entity(
                    new ResponseMessage("Arrival with id: " + id + " updated."))
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity(
                new ResponseMessage("Arrival with id: " + id + " does not exists."))
                .build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response deleteById(String id) {
        Optional<Arrival> optional = Optional.ofNullable(manager.find(Arrival.class, id));
        if (optional.isPresent()) {
            manager.remove(optional.get());
            if (manager.contains(optional.get())) {
                return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                        new ResponseMessage("Could delete Arrival with id: " + id))
                        .build();
            } else {
                return Response.status(Response.Status.OK).entity(
                        new ResponseMessage("Arrival with id: " + id + " deleted"))
                        .build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity(
                new ResponseMessage("Could not find Arrival with id: " + id))
                .build();
    }
}
