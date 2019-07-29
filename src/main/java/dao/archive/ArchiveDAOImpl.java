package dao.archive;

import domain.Archive;
import exception.ResourceLockedException;
import exception.ResourceNotFoundException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Stateless
public class ArchiveDAOImpl implements ArchiveDAO {
    private static final String FIND_ALL_ARCHIVES_TO_BE_DELETED = "SELECT * FROM ARCHIVE WHERE DATEARCHIVED <= DATEADD(?1, ?2, GetDate())";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Archive findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(entityManager.find(Archive.class, id))
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Archive with id: " + id));
    }

    @Override
    public List<Archive> findAll() {
        return Optional.ofNullable(entityManager.createNamedQuery("archive.findAll", Archive.class)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public void save(Archive entity) {
        entityManager.persist(entity);
        entityManager.flush();
    }

    @Override
    public void update(Archive entity) throws ResourceNotFoundException, ResourceLockedException {
        entityManager.merge(entity);
        entityManager.flush();
    }

    @Override
    public void deleteById(String id) {
        Optional<Archive> archiveOptional = Optional.ofNullable(entityManager.find(Archive.class, id));
        if (archiveOptional.isPresent()) {
            entityManager.remove(archiveOptional.get());
        } else throw new ResourceNotFoundException("Cannot find Archive with id: " + id);
    }

    @Override
    public List<Archive> findAllArchiveToDelete(String interval, String number) {
        return Optional.ofNullable(entityManager.createNativeQuery(FIND_ALL_ARCHIVES_TO_BE_DELETED, Archive.class)
                .setParameter(1, interval)
                .setParameter(2, number)
                .getResultList()).orElse(Collections.emptyList());
    }

    @Override
    public void archive(String serialized) {
        Archive archive = new Archive(serialized);
        entityManager.persist(archive);
        entityManager.flush();
    }
}