package service.endOfFishing;

import dao.endOfFishing.EndOfFishingDAO;
import domain.EndOfFishing;
import dto.endOfFishing.EndOfFishingGetDTO;
import dto.endOfFishing.EndOfFishingPostDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.exception.ResourceLockedException;
import service.exception.ResourceNotFoundException;
import utilities.PropertyCopierImpl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
public class EndOfFishingServiceImpl implements EndOfFishingService {
    private static final Logger LOG = LogManager.getLogger(EndOfFishingServiceImpl.class);

    @Inject
    private EndOfFishingDAO endOfFishingDAO;
    @Inject
    private PropertyCopierImpl propertyCopier;

    @Override
    public EndOfFishingGetDTO findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(endOfFishingDAO.findById(id)).map(arrival -> {
            EndOfFishingGetDTO dto = new EndOfFishingGetDTO();
            propertyCopier.copy(dto, arrival);
            return dto;
        }).orElseThrow(() -> new ResourceNotFoundException("Cannot find EndOfFishing with id: " + id));
    }

    @Override
    public List<EndOfFishingGetDTO> findAll() {
        return endOfFishingDAO.findAll().stream().map(endOfFishing -> {
                    EndOfFishingGetDTO dto = new EndOfFishingGetDTO();
                    propertyCopier.copy(dto, endOfFishing);
                    return dto;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public void save(EndOfFishingPostDTO dto) {
        EndOfFishing endOfFishing = new EndOfFishing();
        propertyCopier.copy(endOfFishing, dto);
        try {
            endOfFishingDAO.save(endOfFishing);
            LOG.info("EndOfFishing {} has been created.", endOfFishing.getId());
        } catch (Exception ex) {
            LOG.info("Unable to persist EndOfFishing {}. {}.", endOfFishing.getId(), ex.getMessage());
        }
    }

    @Override
    public void update(EndOfFishingPostDTO dto, String id) throws ResourceNotFoundException, ResourceLockedException {
        Optional.ofNullable(endOfFishingDAO.findById(id))
                .map(endOfFishing -> {
                    propertyCopier.copy(endOfFishingDAO, dto);
                    try {
                        endOfFishingDAO.update(endOfFishing);
                        LOG.info("EndOfFishing {} has been updated.", id);
                    } catch (OptimisticLockException ex) {
                        throw new ResourceLockedException("EndOfFishing resource with id: " + id + " is locked");
                    }
                    return endOfFishing;
                })
                .orElseThrow(() ->
                        {
                            LOG.warn("Cannot find EndOfFishing {}.", id);
                            return new ResourceNotFoundException("Cannot find EndOfFishing with id: " + id);
                        }
                );
    }

    @Override
    public void deleteById(String id) {
        endOfFishingDAO.deleteById(id);
    }
}
