package dao;

import service.exception.ResourceLockedException;
import service.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.util.List;

public interface AbstractDAO<K, M extends Serializable> {

    K findById(M id) throws ResourceNotFoundException;

    List<K> findAll();

    void save(K entity);

    void update(K entity) throws ResourceNotFoundException, ResourceLockedException;

    void deleteById(M id);
}
