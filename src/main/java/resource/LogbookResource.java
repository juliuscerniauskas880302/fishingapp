package resource;

import domain.Arrival;
import domain.Catch;
import domain.Departure;
import domain.EndFishing;
import domain.Logbook;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/logs")
public class LogbookResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public JsonArray findAll() {
        Arrival arrival = new Arrival("Vilnius Port", new Date());
        List<Catch> catches = new ArrayList<>();
        catches.add(new Catch("Salmon", 50.0D));
        catches.add(new Catch("Salmon", 50.0D));
        catches.add(new Catch("Salmon", 50.0D));
        catches.add(new Catch("Salmon", 50.0D));
        catches.add(new Catch("Salmon", 50.0D));
        catches.add(new Catch("Salmon", 50.0D));
        catches.add(new Catch("Salmon", 50.0D));
        Departure departure = new Departure("Klaipeda Port", new Date());
        EndFishing endFishing = new EndFishing(new Date());
        Logbook logbook = new Logbook(arrival, catches, departure, endFishing);
        Logbook logbook2 = new Logbook(arrival, catches, departure, endFishing);
        Logbook logbook3 = new Logbook(arrival, catches, departure, endFishing);
        return Json.createArrayBuilder().add(logbook.toJson())
                .add(logbook2.toJson())
                .add(logbook3.toJson())
                .build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public JsonObject findById(@PathParam("id") final Long id) {
        Logbook logbook = new Logbook();
        logbook.setId(id);
        return logbook.toJson();
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Logbook add(@Valid Logbook logbook) {
        return logbook;
    }
}
