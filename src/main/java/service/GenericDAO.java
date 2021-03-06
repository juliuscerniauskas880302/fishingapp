package service;

import exception.ResourceLockedException;
import exception.ResourceNotFoundException;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, K extends Serializable> {

    T findById(K id) throws ResourceNotFoundException;

    List<T> findAll();

    void save(T o);

    void update(T o, K id) throws ResourceNotFoundException, ResourceLockedException;

    void deleteById(K id);

}
