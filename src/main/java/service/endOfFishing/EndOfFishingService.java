package service.endOfFishing;

import dto.endOfFishing.EndOfFishingGetDTO;
import dto.endOfFishing.EndOfFishingPostDTO;
import exception.ResourceLockedException;
import exception.ResourceNotFoundException;

import java.util.List;

public interface EndOfFishingService {

    EndOfFishingGetDTO findById(String id) throws ResourceNotFoundException;

    List<EndOfFishingGetDTO> findAll();

    void save(EndOfFishingPostDTO dto);

    void update(EndOfFishingPostDTO dto, String id) throws ResourceNotFoundException, ResourceLockedException;

    void deleteById(String id);
}
