package service;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

public interface Dao<T, ID extends Serializable> {
    T findById(ID id);

    List<T> findAll();

    Response save(T o);

    void update(T o, ID id);

    void deleteById(ID id);
}
