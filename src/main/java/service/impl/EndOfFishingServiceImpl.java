package service.impl;

import common.ResponseMessage;
import domain.EndOfFishing;
import service.EndOfFishingService;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Stateful
public class EndOfFishingServiceImpl implements EndOfFishingService {
    @PersistenceContext
    private EntityManager manager;

    @Override
    public Optional<EndOfFishing> findById(String id) {
        return Optional.ofNullable(manager.find(EndOfFishing.class, id));
    }

    @Override
    public Optional<List<EndOfFishing>> findAll() {
        TypedQuery<EndOfFishing> query = manager.createNamedQuery("endOfFishing.findAll", EndOfFishing.class);
        return Optional.ofNullable(query.getResultList());
    }

    @Override
    public Response save(EndOfFishing source) {
        manager.persist(source);
        if (manager.contains(source)) {
            return Response.status(Response.Status.OK).entity(source).build();
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ResponseMessage("Could not add")).build();
    }

    @Override
    public Response update(EndOfFishing source, String id) {
        Optional<EndOfFishing> optional = Optional.ofNullable(manager.find(EndOfFishing.class, id));
        if (optional.isPresent()) {
            EndOfFishing endOfFishing = optional.get();
            endOfFishing.setDate(source.getDate());
            manager.merge(endOfFishing);
            return Response.status(Response.Status.OK).entity(
                    new ResponseMessage("EndOfFishing with id: " + id + " updated."))
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity(
                new ResponseMessage("EndOfFishing with id: " + id + " does not exists."))
                .build();
    }

    @Override
    public Response deleteById(String id) {
        Optional<EndOfFishing> optional = Optional.ofNullable(manager.find(EndOfFishing.class, id));
        if (optional.isPresent()) {
            manager.remove(optional.get());
            if (manager.contains(optional.get())) {
                return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                        new ResponseMessage("Could delete EndOfFishing with id: " + id))
                        .build();
            } else {
                return Response.status(Response.Status.OK).entity(
                        new ResponseMessage("EndOfFishing with id: " + id + " deleted"))
                        .build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).entity(
                new ResponseMessage("Could not find EndOfFishing with id: " + id))
                .build();
    }
}
