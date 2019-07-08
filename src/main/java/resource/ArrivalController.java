package resource;

import domain.Arrival;
import service.arrival.ArrivalService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/arrivals")
@Produces({MediaType.APPLICATION_JSON})
public class ArrivalController {
    @Inject
    private ArrivalService arrivalService;

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Arrival getById(@PathParam("id") final String id) {
        return arrivalService.findById(id);
    }

    @GET
    public List<Arrival> findAll() {
        return arrivalService.findAll();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(@Valid Arrival source) {
        return arrivalService.save(source);
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void updateById(@PathParam("id") final String id, @Valid Arrival source) {
        arrivalService.update(source, id);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deleteById(@PathParam("id") final String id) {
        arrivalService.deleteById(id);
    }

}