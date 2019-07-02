package ejb;

import domain.EndFishing;

import javax.ws.rs.core.Response;
import java.util.List;

public interface EndFishingEJB {
    List<EndFishing> findAll();

    EndFishing findById(Long id);

    Response create(EndFishing endFishing);

    Response update(Long id, EndFishing endFishing);

    Response remove(Long id);
}
