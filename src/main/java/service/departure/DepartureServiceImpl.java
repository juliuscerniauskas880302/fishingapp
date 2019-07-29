package service.departure;

import dao.departure.DepartureDAO;
import domain.Departure;
import dto.departure.DepartureGetDTO;
import dto.departure.DeparturePostDTO;
import exception.ResourceLockedException;
import exception.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilities.PropertyCopierImpl;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Model
public class DepartureServiceImpl implements DepartureService {
    private static final Logger LOG = LogManager.getLogger(DepartureServiceImpl.class);

    @Inject
    private DepartureDAO departureDao;
    @Inject
    private PropertyCopierImpl propertyCopier;

    @Override
    public DepartureGetDTO findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(departureDao.findById(id)).map(departure -> {
            DepartureGetDTO dto = new DepartureGetDTO();
            propertyCopier.copy(dto, departure);
            return dto;
        }).orElseThrow(() -> new ResourceNotFoundException("Cannot find Departure with id: " + id));
    }

    @Override
    public List<DepartureGetDTO> findAll() {
        return departureDao.findAll().stream().map(departure -> {
                    DepartureGetDTO dto = new DepartureGetDTO();
                    propertyCopier.copy(dto, departure);
                    return dto;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public void save(DeparturePostDTO dto) {
        Departure departure = new Departure();
        propertyCopier.copy(departure, dto);
        try {
            departureDao.save(departure);
            LOG.info("Departure {} has been created.", departure.getId());
        } catch (Exception ex) {
            LOG.info("Unable to persist Departure {}. {}.", departure.getId(), ex.getMessage());
        }
    }

    @Override
    public void update(DeparturePostDTO dto, String id) throws ResourceNotFoundException, ResourceLockedException {
        Optional.ofNullable(departureDao.findById(id))
                .map(departure -> {
                    propertyCopier.copy(departure, dto);
                    try {
                        departureDao.update(departure);
                        LOG.info("Departure {} has been updated.", id);
                    } catch (OptimisticLockException ex) {
                        throw new ResourceLockedException("Departure resource with id: " + id + " is locked");
                    }
                    return departure;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Departure with id: " + id));
    }

    @Override
    public void deleteById(String id) {
        departureDao.deleteById(id);
    }
}