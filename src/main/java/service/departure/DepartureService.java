package service.departure;

import dto.departure.DepartureGetDTO;
import dto.departure.DeparturePostDTO;
import exception.ResourceLockedException;
import exception.ResourceNotFoundException;

import java.util.List;

public interface DepartureService {

    DepartureGetDTO findById(String id) throws ResourceNotFoundException;

    List<DepartureGetDTO> findAll();

    void save(DeparturePostDTO dto);

    void update(DeparturePostDTO dto, String id) throws ResourceNotFoundException, ResourceLockedException;

    void deleteById(String id);
}
