package service;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface Dao<T, ID extends Serializable> {
    Optional<T> findById(ID id);

    Optional<List<T>> findAll();

    Response save(T o);

    Response update(T o, ID id);

    Response deleteById(ID id);
}
