package resource;

import domain.Arrival;
import ejb.ArrivalEJB;

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

@Path("/arrivals")
public class ArrivalResource {
    @Inject
    private ArrivalEJB arrivalEJB;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Arrival> findAll() {
        return arrivalEJB.findAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void create(Arrival arrival) {
        arrivalEJB.create(arrival);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("id") final Long id, Arrival arrival) {
        arrivalEJB.update(id, arrival);
    }

    @DELETE
    @Path("/{id}")
    public void remove(@PathParam("id") final Long id) {
        arrivalEJB.remove(id);
    }

}
