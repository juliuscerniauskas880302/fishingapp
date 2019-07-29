package service.archive;

import dao.archive.ArchiveDAO;
import domain.Archive;
import exception.ResourceLockedException;
import exception.ResourceNotFoundException;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.List;

@Model
public class ArchiveServiceImpl implements ArchiveService {

    @Inject
    private ArchiveDAO archiveDAO;

    @Override
    public Archive findById(String id) throws ResourceNotFoundException {
        return archiveDAO.findById(id);
    }

    @Override
    public List<Archive> findAll() {
        return archiveDAO.findAll();
    }

    @Override
    public List<Archive> findAllArchiveToDelete(String interval, String number) {
        return archiveDAO.findAllArchiveToDelete(interval, number);
    }

    @Override
    public void save(Archive archive) {
        archiveDAO.save(archive);
    }

    @Override
    public void archive(String serialized) {
        archiveDAO.archive(serialized);
    }

    @Override
    public void update(Archive archive, String id) throws ResourceNotFoundException, ResourceLockedException {
        archiveDAO.update(archive);
    }

    @Override
    public void deleteById(String id) {
        archiveDAO.deleteById(id);
    }
}
