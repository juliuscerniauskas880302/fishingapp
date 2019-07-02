package ejb;

import domain.Departure;

import java.util.List;

public interface DepartureEJB {
    List<Departure> findAll();

    void create(Departure departure);

    void update(Long id, Departure departure);

    void remove(Long id);
}
