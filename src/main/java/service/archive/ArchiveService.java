package service.archive;

import domain.Archive;
import exception.ResourceLockedException;
import exception.ResourceNotFoundException;

import java.util.List;

public interface ArchiveService {

    Archive findById(String id) throws ResourceNotFoundException;

    List<Archive> findAll();

    List<Archive> findAllArchiveToDelete(String interval, String number);

    void save(Archive archive);

    void archive(String serialized);

    void update(Archive archive, String id) throws ResourceNotFoundException, ResourceLockedException;

    void deleteById(String id);
}
