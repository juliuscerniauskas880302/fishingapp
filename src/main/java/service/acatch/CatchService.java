package service.acatch;

import dto.aCatch.CatchGetDTO;
import dto.aCatch.CatchPostDTO;
import service.exception.ResourceLockedException;
import service.exception.ResourceNotFoundException;

import java.util.List;

public interface CatchService {

    CatchGetDTO findById(String id) throws ResourceNotFoundException;

    List<CatchGetDTO> findAll();

    void save(CatchPostDTO dto);

    void update(CatchPostDTO dto, String id) throws ResourceNotFoundException, ResourceLockedException;

    void deleteById(String id);
}
