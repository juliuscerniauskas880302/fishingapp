package ejb;

import domain.Departure;

import javax.ws.rs.core.Response;
import java.util.List;

public interface DepartureEJB {
    List<Departure> findAll();

    Departure findById(Long id);

    Response create(Departure departure);

    Response update(Long id, Departure departure);

    Response remove(Long id);
}
