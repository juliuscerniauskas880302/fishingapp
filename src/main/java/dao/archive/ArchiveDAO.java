package dao.archive;

import dao.AbstractDAO;
import domain.Archive;

import java.util.List;

public interface ArchiveDAO extends AbstractDAO<Archive, String> {
    List<Archive> findAllArchiveToDelete(String interval, int difference);
    void archive(String serialized);
}
