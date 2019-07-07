package resource;

import common.ResponseMessage;
import domain.Catch;
import service.CatchService;

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
import java.util.Optional;

@Path("/catches")
@Produces({MediaType.APPLICATION_JSON})
public class CatchController {

    @Inject
    private CatchService catchService;

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") final String id) {
        Optional<Catch> aCatch = catchService.findById(id);
        if (aCatch.isPresent()) {
            return Response.status(Response.Status.OK).entity(aCatch.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessage("Not found")).build();
    }

    @GET
    public Response findAll() {
        Optional<List<Catch>> catches = catchService.findAll();
        if (catches.isPresent()) {
            return Response.status(Response.Status.OK).entity(catches.get()).build();
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ResponseMessage("Error getting list")).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid Catch source) {
        return catchService.save(source);
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateById(@PathParam("id") final String id, @Valid Catch source) {
        return catchService.update(source, id);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") final String id) {
        return catchService.deleteById(id);
    }

}
