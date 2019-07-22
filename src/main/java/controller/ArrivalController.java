package controller;

import common.ApplicationVariables;
import domain.Arrival;
import service.arrival.ArrivalService;
import service.exception.ResourceNotFoundException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path(ApplicationVariables.ARRIVAL_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class ArrivalController {

    @Inject
    private ArrivalService arrivalService;

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") final String id) {
        try {
            return Response.status(Response.Status.CREATED).entity(arrivalService.findById(id)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    public List<Arrival> findAll() {
        return arrivalService.findAll();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid Arrival source) {
        return arrivalService.save(source);
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateById(@PathParam("id") final String id, @Valid Arrival source) {
        arrivalService.update(source, id);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deleteById(@PathParam("id") final String id) {
        arrivalService.deleteById(id);
    }

}
