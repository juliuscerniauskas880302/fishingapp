package ejb;

import domain.Catch;

import javax.ws.rs.core.Response;
import java.util.List;

public interface CatchEJB {
    List<Catch> findAll();

    Catch findById(Long id);

    Response create(Catch aCatch);

    Response update(Long id, Catch body);

    Response remove(Long id);
}
