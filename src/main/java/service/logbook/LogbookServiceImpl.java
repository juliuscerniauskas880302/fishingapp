package service.logbook;

import dao.logbook.LogbookDAO;
import domain.Logbook;
import dto.logbook.LogbookGetDTO;
import dto.logbook.LogbookPostDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import exception.ResourceLockedException;
import exception.ResourceNotFoundException;
import utilities.PropertyCopierImpl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
@SuppressWarnings("unchecked")
public class LogbookServiceImpl implements LogbookService {
    private static final Logger LOG = LogManager.getLogger(LogbookServiceImpl.class);

    @Inject
    private LogbookDAO logbookDAO;
    @Inject
    private PropertyCopierImpl propertyCopier;

    @Override
    public LogbookGetDTO findById(String id) throws ResourceNotFoundException {
        return Optional.ofNullable(logbookDAO.findById(id)).map(logbook -> {
            LogbookGetDTO dto = new LogbookGetDTO();
            propertyCopier.copy(dto, logbook);
            return dto;
        }).orElseThrow(() -> new ResourceNotFoundException("Cannot find Logbook with id: " + id));
    }

    @Override
    public List<LogbookGetDTO> findAll() {
        return logbookDAO.findAll().stream().map(logbook -> {
                    LogbookGetDTO dto = new LogbookGetDTO();
                    propertyCopier.copy(dto, logbook);
                    return dto;
                }
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void save(LogbookPostDTO dto) {
        Logbook logbook = new Logbook();
        propertyCopier.copy(logbook, dto);
        logbookDAO.save(logbook);
    }

    @Override
    @Transactional
    public void update(LogbookPostDTO dto, String id) {
        Optional.ofNullable(logbookDAO.findById(id))
                .map(endOfFishing -> {
                    propertyCopier.copy(logbookDAO, dto);
                    try {
                        logbookDAO.update(endOfFishing);
                        LOG.info("Logbook {} has been updated.", id);
                    } catch (OptimisticLockException ex) {
                        throw new ResourceLockedException("Logbook resource with id: " + id + " is locked");
                    }
                    return endOfFishing;
                })
                .orElseThrow(() ->
                        {
                            LOG.warn("Cannot find Logbook {}.", id);
                            return new ResourceNotFoundException("Cannot find Logbook with id: " + id);
                        }
                );
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        logbookDAO.deleteById(id);
    }

    @Override
    public List<LogbookGetDTO> findByPort(String port) {
        return logbookDAO.findByPort(port).stream().map(logbook -> {
            LogbookGetDTO dto = new LogbookGetDTO();
            propertyCopier.copy(dto, logbook);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<LogbookGetDTO> findBySpecies(String species) {
        return logbookDAO.findBySpecies(species).stream().map(logbook -> {
            LogbookGetDTO dto = new LogbookGetDTO();
            propertyCopier.copy(dto, logbook);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<LogbookGetDTO> findByArrivalDateIn(String date1, String date2) {
        return logbookDAO.findByArrivalDateIn(date1, date2).stream().map(logbook -> {
            LogbookGetDTO dto = new LogbookGetDTO();
            propertyCopier.copy(dto, logbook);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<LogbookGetDTO> findByDepartureDateIn(String date1, String date2) {
        return logbookDAO.findByDepartureDateIn(date1, date2).stream().map(logbook -> {
            LogbookGetDTO dto = new LogbookGetDTO();
            propertyCopier.copy(dto, logbook);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveAll(List<LogbookPostDTO> logbooks) throws Exception {
        logbooks.forEach(source -> {
            try {
                save(source);
            } catch (Exception e) {
            }
        });
    }
}