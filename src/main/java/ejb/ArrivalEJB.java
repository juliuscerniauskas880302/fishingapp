package ejb;

import domain.Arrival;

import java.util.List;

public interface ArrivalEJB {
    List<Arrival> findAll();

    void create(Arrival arrival);

    void update(Long id, Arrival body);

    void remove(Long id);
}
