package resource;

import common.ResponseMessage;
import domain.Arrival;
import domain.Catch;
import domain.Departure;
import domain.EndOfFishing;
import domain.Logbook;
import enums.CommunicationType;
import service.LogbookService;

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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/logs")
@Produces({MediaType.APPLICATION_JSON})
public class LogbookController {
    @Inject
    private LogbookService logbookService;

    @GET
    @Path("/test")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getTest() {
        String strDatewithTime = "2015-08-04T10:11:30";
        Arrival arrival = new Arrival("Arrival port", LocalDate.now());
        Departure departure = new Departure("Departure port", LocalDate.now());
        EndOfFishing endOfFishing = new EndOfFishing(LocalDate.now());
        List<Catch> catches = new ArrayList<>();
        catches.add(new Catch("Salmon", 55.5D));
        Logbook logbook = new Logbook(arrival,departure,endOfFishing,catches, CommunicationType.NETWORK.toString());
        return Response.ok().entity(logbook).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") final String id) {
        Optional<Logbook> logbook = logbookService.findById(id);
        if (logbook.isPresent()) {
            return Response.status(Response.Status.OK).entity(logbook.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).entity(new ResponseMessage("Not found")).build();
    }

    @GET
    public Response findAll() {
        Optional<List<Logbook>> catches = logbookService.findAll();
        if (catches.isPresent()) {
            return Response.status(Response.Status.OK).entity(catches.get()).build();
        }
        return Response.status(Response.Status.EXPECTATION_FAILED).entity(new ResponseMessage("Error getting list")).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid Logbook source) {
        return logbookService.save(source);
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateById(@PathParam("id") final String id, @Valid Logbook source) {
        return logbookService.update(source, id);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") final String id) {
        return logbookService.deleteById(id);
    }

}
