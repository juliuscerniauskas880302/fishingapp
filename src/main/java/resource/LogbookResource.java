package resource;

import domain.*;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("/logs")
public class LogbookResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Logbook findAll() {
        Arrival arrival = new Arrival(1L, "Vilnius Port", new Date());
        Catch aCatch = new Catch(2L,"Salmon", 50.0D);
        Departure departure = new Departure(3L,"Klaipeda Port", new Date());
        EndFishing endFishing = new EndFishing(5L, new Date());
        return new Logbook(5L, arrival, aCatch, departure, endFishing);
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Logbook add(@Valid Logbook logbook) {
        return logbook;
    }
}
