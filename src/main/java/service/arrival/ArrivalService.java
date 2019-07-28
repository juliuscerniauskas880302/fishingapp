package service.arrival;

import dto.arrival.ArrivalGetDTO;
import dto.arrival.ArrivalPostDTO;
import service.exception.ResourceLockedException;
import service.exception.ResourceNotFoundException;

import java.util.List;

public interface ArrivalService {

    ArrivalGetDTO findById(String id) throws ResourceNotFoundException;

    List<ArrivalGetDTO> findAll();

    void save(ArrivalPostDTO dto);

    void update(ArrivalPostDTO dto, String id) throws ResourceNotFoundException, ResourceLockedException;

    void deleteById(String id);
}
