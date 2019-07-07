package resource;

import common.ResponseMessage;
import domain.EndOfFishing;
import service.EndOfFishingService;

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

@Path("/end")
@Produces({MediaType.APPLICATION_JSON})
public class EndOfFishingController {
    @Inject
    private EndOfFishingService endOfFishingService;

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") final String id) {
        Optional<EndOfFishing> departure = endOfFishingService.findById(id);
        if (departure.isPresent()) {
            return Response.status(Response.Status.OK).entity(departure.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessage("Not found")).build();
    }

    @GET
    public Response findAll() {
        Optional<List<EndOfFishing>> departures = endOfFishingService.findAll();
        if (departures.isPresent()) {
            return Response.status(Response.Status.OK).entity(departures.get()).build();
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ResponseMessage("Error getting list")).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid EndOfFishing source) {
        return endOfFishingService.save(source);
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateById(@PathParam("id") final String id, @Valid EndOfFishing source) {
        return endOfFishingService.update(source, id);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") final String id) {
        return endOfFishingService.deleteById(id);
    }
}
