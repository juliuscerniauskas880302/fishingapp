package controller;

import common.ApplicationVariables;
import domain.Departure;
import service.departure.DepartureService;
import service.exception.ResourceNotFoundException;

import javax.inject.Inject;
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

@Path(ApplicationVariables.DEPARTURE_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class DepartureController {

    @Inject
    private DepartureService departureService;

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") final String id) {
        try {
            return Response.status(Response.Status.CREATED).entity(departureService.findById(id)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    public List<Departure> findAll() {
        return departureService.findAll();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid Departure source) {
        return departureService.save(source);
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateById(@PathParam("id") final String id, @Valid Departure source) {
        departureService.update(source, id);
    }

    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") final String id) {
        departureService.deleteById(id);
    }

}
