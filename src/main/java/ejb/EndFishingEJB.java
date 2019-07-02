package ejb;

import domain.EndFishing;

import java.util.List;

public interface EndFishingEJB {
    List<EndFishing> findAll();

    void create(EndFishing endFishing);

    void update(Long id, EndFishing endFishing);

    void remove(Long id);
}
