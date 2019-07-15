package controller;

import domain.Arrival;
import domain.Catch;
import domain.Departure;
import domain.EndOfFishing;
import domain.Logbook;
import enums.CommunicationType;
import service.logbook.LogbookService;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/logs")
@Produces({MediaType.APPLICATION_JSON})
public class LogbookController {
    @Inject
    private LogbookService logbookService;

    @GET
    @Path("/test")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getTest() {
        Arrival arrival = new Arrival("Arrival port", new Date());
        Departure departure = new Departure("Departure port", new Date());
        EndOfFishing endOfFishing = new EndOfFishing(new Date());
        List<Catch> catches = new ArrayList<>();
        catches.add(new Catch("Salmon", 55.5D));
        Logbook logbook = new Logbook(arrival, departure, endOfFishing, catches, CommunicationType.NETWORK.toString());
        return Response.ok().entity(logbook).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Logbook getById(@PathParam("id") final String id) {
        return logbookService.findById(id);
    }

    @GET
    public List<Logbook> findAll() {
        return logbookService.findAll();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid Logbook source) {
        return logbookService.save(source);
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateById(@PathParam("id") final String id, @Valid Logbook source) {
        logbookService.update(source, id);
    }

    @DELETE
    @Path("/{id}")
    public void deleteById(@PathParam("id") final String id) {
        logbookService.deleteById(id);
    }

    @GET
    @Path("/search/port/{port}")
    public List<Logbook> getLogbookByPort(@PathParam("port") final String port) {
        return logbookService.findByPort(port);
    }

    @GET
    @Path("/search/species/{species}")
    public List<Logbook> getLogbookBySpecies(@PathParam("species") final String species) {
        return logbookService.findBySpecies(species);
    }

    @GET
    @Path("/search/departure/{date1}/{date2}")
    public List<Logbook> getLogbookByArrivalDate(
            @PathParam("date1") final String date1,
            @PathParam("date2") final String date2
    ) {
        return logbookService.findByArrivalDate(date1, date2);
    }

    @GET
    @Path("/search/arrival/{date1}/{date2}")
    public List<Logbook> getLogbookByDepartureDate(
            @PathParam("date1") final String date1,
            @PathParam("date2") final String date2
    ) {
        return logbookService.findByDepartureDate(date1, date2);
    }

}