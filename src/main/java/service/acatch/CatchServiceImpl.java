package service.acatch;

import dao.aCatch.CatchDAO;
import domain.Catch;
import dto.aCatch.CatchGetDTO;
import dto.aCatch.CatchPostDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import exception.ResourceLockedException;
import exception.ResourceNotFoundException;
import utilities.PropertyCopierImpl;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Model
public class CatchServiceImpl implements CatchService {
    private static final Logger LOG = LogManager.getLogger(CatchServiceImpl.class);

    @Inject
    private CatchDAO catchDao;
    @Inject
    private PropertyCopierImpl propertyCopier;

    @Override
    public CatchGetDTO findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(catchDao.findById(id)).map(aCatch -> {
            CatchGetDTO dto = new CatchGetDTO();
            propertyCopier.copy(dto, aCatch);
            return dto;
        }).orElseThrow(() -> new ResourceNotFoundException("Cannot find Catch with id: " + id));
    }

    @Override
    public List<CatchGetDTO> findAll() {
        return catchDao.findAll().stream().map(aCatch -> {
                    CatchGetDTO dto = new CatchGetDTO();
                    propertyCopier.copy(dto, aCatch);
                    return dto;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public void save(CatchPostDTO dto) {
        Catch aCatch = new Catch();
        propertyCopier.copy(aCatch, dto);
        try {
            catchDao.save(aCatch);
            LOG.info("Arrival {} has been created.", aCatch.getId());
        } catch (Exception ex) {
            LOG.info("Unable to persist Arrival {}. {}.", aCatch.getId(), ex.getMessage());
        }
    }

    @Override
    public void update(CatchPostDTO dto, String id) throws ResourceNotFoundException, ResourceLockedException {
        Optional.ofNullable(catchDao.findById(id))
                .map(aCatch -> {
                    propertyCopier.copy(aCatch, dto);
                    try {
                        catchDao.update(aCatch);
                        LOG.info("Catch {} has been updated.", id);
                    } catch (OptimisticLockException ex) {
                        throw new ResourceLockedException("Catch resource with id: " + id + " is locked");
                    }
                    return aCatch;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Catch with id: " + id));
    }

    @Override
    public void deleteById(String id) {
        catchDao.deleteById(id);
    }
}
