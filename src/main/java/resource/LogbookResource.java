package resource;

import domain.Arrival;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;

@Path("/arrivals")
public class ArrivalResource {

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Arrival getAll() {
        return new Arrival(1L,"Vilnius port", LocalDate.now());
    }
}
