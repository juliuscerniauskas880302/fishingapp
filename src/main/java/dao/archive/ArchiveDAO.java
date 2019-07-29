package dao.archive;

import dao.AbstractDAO;
import domain.Archive;

import java.util.List;

public interface ArchiveDAO extends AbstractDAO<Archive, String> {
    List<Archive> findAllArchiveToDelete(String interval, String number);
    void archive(String serialized);
}
