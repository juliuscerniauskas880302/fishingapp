package controller;

import common.ApplicationVariables;
import domain.Logbook;
import service.exception.ResourceLockedException;
import service.exception.ResourceNotFoundException;
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
import java.util.List;

@Path(ApplicationVariables.LOGBOOK_PATH)
@Produces({MediaType.APPLICATION_JSON})
public class LogbookController {

    @Inject
    private LogbookService logbookService;

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") final String id) {
        try {
            return Response.status(Response.Status.CREATED).entity(logbookService.findById(id)).build();
        } catch (ResourceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    public List<Logbook> findAll() {
        return logbookService.findAll();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid Logbook source) {
        try {
            logbookService.save(source);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/logs")
    public void saveAll(@Valid List<Logbook> logbooks) {
        try {
            logbookService.saveAll(logbooks);
        } catch (Exception ex) {

        }
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateById(@PathParam("id") final String id, @Valid Logbook source) {
        try {
            logbookService.update(source, id);
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
    public List<Logbook> getLogbookByArrivalDateIn(
            @PathParam("date1") final String date1, @PathParam("date2") final String date2) {
        return logbookService.findByArrivalDateIn(date1, date2);
    }

    @GET
    @Path("/search/arrival/{date1}/{date2}")
    public List<Logbook> getLogbookByDepartureDateIn(
            @PathParam("date1") final String date1, @PathParam("date2") final String date2) {
        return logbookService.findByDepartureDateIn(date1, date2);
    }
}
