package service.arrival;

import dao.arrival.ArrivalDAO;
import domain.Arrival;
import dto.arrival.ArrivalGetDTO;
import dto.arrival.ArrivalPostDTO;
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
public class ArrivalServiceImpl implements ArrivalService {
    private static final Logger LOG = LogManager.getLogger(ArrivalServiceImpl.class);

    @Inject
    private ArrivalDAO arrivalDao;
    @Inject
    private PropertyCopierImpl propertyCopier;

    @Override
    public ArrivalGetDTO findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(arrivalDao.findById(id)).map(arrival -> {
            ArrivalGetDTO dto = new ArrivalGetDTO();
            propertyCopier.copy(dto, arrival);
            return dto;
        }).orElseThrow(() -> new ResourceNotFoundException("Cannot find Arrival with id: " + id));
    }

    @Override
    public List<ArrivalGetDTO> findAll() {
        return arrivalDao.findAll().stream().map(arrival -> {
                    ArrivalGetDTO dto = new ArrivalGetDTO();
                    propertyCopier.copy(dto, arrival);
                    return dto;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public void save(ArrivalPostDTO dto) {
        Arrival arrival = new Arrival();
        propertyCopier.copy(arrival, dto);
        try {
            arrivalDao.save(arrival);
            LOG.info("Arrival {} has been created.", arrival.getId());
        } catch (Exception ex) {
            LOG.info("Unable to persist Arrival {}. {}.", arrival.getId(), ex.getMessage());
        }
    }

    @Override
    public void update(ArrivalPostDTO dto, String id) throws ResourceNotFoundException, ResourceLockedException {
        Optional.ofNullable(arrivalDao.findById(id))
                .map(arrival -> {
                    propertyCopier.copy(arrival, dto);
                    try {
                        arrivalDao.update(arrival);
                        LOG.info("Arrival {} has been updated.", id);
                    } catch (OptimisticLockException ex) {
                        throw new ResourceLockedException("Arrival resource with id: " + id + " is locked");
                    }
                    return arrival;
                })
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Arrival with id: " + id));
    }

    @Override
    public void deleteById(String id) {
        arrivalDao.deleteById(id);
    }
}
