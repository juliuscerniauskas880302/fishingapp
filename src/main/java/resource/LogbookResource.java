package resource;

import domain.Arrival;
import domain.Catch;
import domain.Departure;
import domain.EndFishing;
import domain.Logbook;
import ejb.LogbookEJB;
import enums.CommunicationType;
import strategy.DatabaseSaveStrategy;
import strategy.FileSaveStrategy;

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

@Path("/logs")
public class LogbookResource {
    @Inject
    private LogbookEJB logbookEBJ;

    @GET
    @Path("/test")
    @Produces({MediaType.APPLICATION_JSON})
    public Logbook test() {
        Arrival arrival = new Arrival("Port 1", LocalDate.of(1999, 3, 15));
        Catch aCatch = new Catch("Salmon", 52.0D);
        Departure departure = new Departure("Port 2", LocalDate.of(2016, 3, 17));
        List<Catch> catches = new ArrayList<>();
        catches.add(aCatch);
        EndFishing endFishing = new EndFishing(LocalDate.of(2222, 11, 11));
        CommunicationType communicationType = CommunicationType.OFFLINE;
        Logbook logbook = new Logbook(arrival, catches, departure, endFishing, communicationType.toString());
        return logbook;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Logbook> findAll() {
        return logbookEBJ.findAll();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Logbook findById(@PathParam("id") final Long id) {
        return logbookEBJ.findById(id);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam("id") final Long id) {
        return logbookEBJ.remove(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public Response updateById(@PathParam("id") final Long id, Logbook logbook) {
        return logbookEBJ.update(id, logbook);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid Logbook logbook) {
        return logbookEBJ.create(logbook);
    }

}
