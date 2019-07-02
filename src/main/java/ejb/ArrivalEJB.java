package ejb;

import domain.Arrival;

import javax.ws.rs.core.Response;
import java.util.List;

public interface ArrivalEJB {
    List<Arrival> findAll();

    Arrival findById(Long id);

    Response create(Arrival arrival);

    Response update(Long id, Arrival body);

    Response remove(Long id);
}
