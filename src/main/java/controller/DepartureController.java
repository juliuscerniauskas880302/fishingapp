package controller;

import common.ApplicationVariables;
import dto.departure.DepartureGetDTO;
import dto.departure.DeparturePostDTO;
import service.departure.DepartureService;
import service.exception.ResourceLockedException;
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
    public List<DepartureGetDTO> findAll() {
        return departureService.findAll();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid DeparturePostDTO source) {
        try {
            departureService.save(source);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateById(@PathParam("id") final String id, @Valid DeparturePostDTO source) {
        try {
            departureService.update(source, id);
            return Response.status(Response.Status.OK).build();
        } catch (ResourceLockedException ex) {
            return Response.status(Response.Status.CONFLICT).entity(ex.toString()).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.toString()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") final String id) {
        departureService.deleteById(id);
    }
}
