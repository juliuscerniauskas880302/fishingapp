package ejb;

import domain.Catch;

import java.util.List;

public interface CatchEJB {
    List<Catch> findAll();

    void create(Catch aCatch);

    void update(Long id, Catch body);

    void remove(Long id);
}
