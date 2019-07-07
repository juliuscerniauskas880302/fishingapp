package resource;

import common.ResponseMessage;
import domain.Departure;
import service.DepartureService;

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

@Path("/departures")
@Produces({MediaType.APPLICATION_JSON})
public class DepartureController {
    @Inject
    private DepartureService departureService;

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") final String id) {
        Optional<Departure> departure = departureService.findById(id);
        if (departure.isPresent()) {
            return Response.status(Response.Status.OK).entity(departure.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessage("Not found")).build();
    }

    @GET
    public Response findAll() {
        Optional<List<Departure>> departures = departureService.findAll();
        if (departures.isPresent()) {
            return Response.status(Response.Status.OK).entity(departures.get()).build();
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ResponseMessage("Error getting list")).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid Departure source) {
        return departureService.save(source);
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateById(@PathParam("id") final String id, @Valid Departure source) {
        return departureService.update(source, id);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") final String id) {
        return departureService.deleteById(id);
    }
}
