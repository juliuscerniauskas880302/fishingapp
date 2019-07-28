package service.logbook;

import dto.logbook.LogbookGetDTO;
import dto.logbook.LogbookPostDTO;
import service.exception.ResourceLockedException;
import service.exception.ResourceNotFoundException;

import java.util.List;

public interface LogbookService {

    List<LogbookGetDTO> findByPort(String port);

    List<LogbookGetDTO> findBySpecies(String species);

    List<LogbookGetDTO> findByArrivalDateIn(String date1, String date2);

    List<LogbookGetDTO> findByDepartureDateIn(String date1, String date2);

    void saveAll(List<LogbookPostDTO> logbooks) throws Exception;

    LogbookGetDTO findById(String id) throws ResourceNotFoundException;

    List<LogbookGetDTO> findAll();

    void save(LogbookPostDTO dto);

    void update(LogbookPostDTO dto, String id) throws ResourceNotFoundException, ResourceLockedException;

    void deleteById(String id);
}
