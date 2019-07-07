package resource;

import common.ResponseMessage;
import domain.Arrival;
import service.ArrivalService;

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

@Path("/arrivals")
@Produces({MediaType.APPLICATION_JSON})
public class ArrivalController {
    @Inject
    private ArrivalService arrivalService;

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") final String id) {
        Optional<Arrival> arrival = arrivalService.findById(id);
        if (arrival.isPresent()) {
            return Response.status(Response.Status.OK).entity(arrival.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessage("Not found")).build();
    }

    @GET
    public Response findAll() {
        Optional<List<Arrival>> arrivalList = arrivalService.findAll();
        if (arrivalList.isPresent()) {
            return Response.status(Response.Status.OK).entity(arrivalList.get()).build();
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ResponseMessage("Error getting list")).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid Arrival source) {
        return arrivalService.save(source);
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateById(@PathParam("id") final String id, @Valid Arrival source) {
        return arrivalService.update(source, id);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") final String id) {
        return arrivalService.deleteById(id);
    }

}
