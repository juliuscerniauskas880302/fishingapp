package ejb;

import domain.Logbook;

import javax.ws.rs.core.Response;
import java.util.List;

public interface LogbookEJB {
    List<Logbook> findAll();

    Logbook findById(Long id);

    Response create(Logbook logbook, String place);

    Response update(Long id, Logbook logbook);

    Response remove(Long id);
}
