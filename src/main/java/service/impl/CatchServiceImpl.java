package service.impl;

import common.ResponseMessage;
import domain.Catch;
import service.CatchService;

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
public class CatchServiceImpl implements CatchService {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Optional<Catch> findById(String id) {
        return Optional.ofNullable(manager.find(Catch.class, id));
    }

    @Override
    public Optional<List<Catch>> findAll() {
        TypedQuery<Catch> query = manager.createNamedQuery("catch.findAll", Catch.class);
        return Optional.ofNullable(query.getResultList());
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response save(Catch source) {
        manager.persist(source);
        if (manager.contains(source)) {
            return Response.status(Response.Status.OK).entity(source).build();
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ResponseMessage("Could not add")).build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response update(Catch source, String id) {
        Optional<Catch> optional = Optional.ofNullable(manager.find(Catch.class, id));
        if (optional.isPresent()) {
            Catch aCatch = optional.get();
            aCatch.setVariety(source.getVariety());
            aCatch.setWeight(source.getWeight());
            manager.merge(aCatch);
            return Response.status(Response.Status.OK).entity(
                    new ResponseMessage("Catch with id: " + id + " updated."))
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity(
                new ResponseMessage("Catch with id: " + id + " does not exists."))
                .build();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Response deleteById(String id) {
        Optional<Catch> optional = Optional.ofNullable(manager.find(Catch.class, id));
        if (optional.isPresent()) {
            manager.remove(optional.get());
            if (manager.contains(optional.get())) {
                return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                        new ResponseMessage("Could delete Catch with id: " + id))
                        .build();
            } else {
                return Response.status(Response.Status.OK).entity(
                        new ResponseMessage("Catch with id: " + id + " deleted"))
                        .build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity(
                new ResponseMessage("Could not find Catch with id: " + id))
                .build();
    }
}
