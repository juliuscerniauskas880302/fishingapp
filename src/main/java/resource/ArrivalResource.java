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
import javax.ws.rs.core.Response;
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

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Arrival findById(@PathParam("id") final Long id) {
        return arrivalEJB.findById(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Arrival arrival) {
        return arrivalEJB.create(arrival);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") final Long id, Arrival arrival) {
        return arrivalEJB.update(id, arrival);
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") final Long id) {
        return arrivalEJB.remove(id);
    }

}
