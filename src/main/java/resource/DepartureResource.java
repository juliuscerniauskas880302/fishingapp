package resource;

import domain.Departure;
import ejb.DepartureEJB;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/departures")
public class DepartureResource {
    @Inject
    private DepartureEJB departureEJB;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Departure> findAll() {
        return departureEJB.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(Departure departure) {
        departureEJB.create(departure);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("id") final Long id, Departure departure) {
        departureEJB.update(id, departure);
    }

    @DELETE
    @Path("/{id}")
    public void remove(@PathParam("id") final Long id) {
        departureEJB.remove(id);
    }
}
