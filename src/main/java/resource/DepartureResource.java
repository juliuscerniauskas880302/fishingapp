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
import javax.ws.rs.core.Response;
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

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Departure findById(@PathParam("id") final Long id) {
        return departureEJB.findById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Departure departure) {
        return departureEJB.create(departure);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") final Long id, Departure departure) {
        return departureEJB.update(id, departure);
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") final Long id) {
        return departureEJB.remove(id);
    }
}
