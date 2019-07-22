package service;

import service.exception.ResourceNotFoundException;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, K extends Serializable> {

    T findById(K id) throws ResourceNotFoundException;

    List<T> findAll();

    Response save(T o);

    void update(T o, K id);

    void deleteById(K id);

}
