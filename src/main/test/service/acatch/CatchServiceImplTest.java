package service.acatch;

import dto.aCatch.CatchGetDTO;
import dto.aCatch.CatchPostDTO;
import exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CatchServiceImplTest {
    private static final String ID_1 = "1";
    private static final String ID_2 = "2";
    private static final String VARIETY_1 = "variety1";
    private static final String VARIETY_2 = "variety2";
    private static final Double WEIGHT_1 = 1D;
    private static final Double WEIGHT_2 = 2D;

    private CatchGetDTO catchGetDTO1;
    private CatchGetDTO catchGetDTO2;
    private CatchPostDTO catchPostDTO;

    @Mock
    private CatchServiceImpl catchService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        catchGetDTO1 = new CatchGetDTO();
        catchGetDTO1.setId(ID_1);
        catchGetDTO1.setWeight(WEIGHT_1);
        catchGetDTO1.setVariety(VARIETY_1);

        catchGetDTO2 = new CatchGetDTO();
        catchGetDTO2.setId(ID_2);
        catchGetDTO2.setWeight(WEIGHT_2);
        catchGetDTO2.setVariety(VARIETY_2);

        catchPostDTO = new CatchPostDTO();
        catchPostDTO.setWeight(WEIGHT_2);
        catchPostDTO.setVariety(VARIETY_2);
    }

    @Test
    public void shouldGetCatchById() throws ResourceNotFoundException {
        // when
        when(catchService.findById(anyString())).thenReturn(catchGetDTO1);
        CatchGetDTO result = catchService.findById(ID_1);

        // then
        //verify(catchService, times(1)).findById(anyString());
        assertNotNull(result);
        assertEquals("Variety should be equal to " + VARIETY_1, VARIETY_1, result.getVariety());
        assertEquals("ID should be equal to " + ID_1, ID_1, result.getId());
        assertEquals("Weight should be equal to " + WEIGHT_1, WEIGHT_1, result.getWeight());
    }

    @Test
    public void shouldDeleteCatchById() {
        // when
        doNothing().when(catchService).deleteById(anyString());

        catchService.deleteById(ID_1);

        // then
        verify(catchService, times(1)).deleteById(anyString());
    }

    @Test
    public void shouldReturnCatchList() {
        // when
        when(catchService.findAll()).thenReturn(Arrays.asList(catchGetDTO1, catchGetDTO2));

        List<CatchGetDTO> result = catchService.findAll();

        // then
        verify(catchService, times(1)).findAll();
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should be 2", 2, result.size());
    }

    @Test
    public void shouldCreateNewCatch() throws Exception {
        // when
        doNothing().when(catchService).save(any(CatchPostDTO.class));
        catchService.save(catchPostDTO);

        // then
        verify(catchService, times(1)).save(any(CatchPostDTO.class));
    }

    @Test
    public void shouldUpdateCatchById() throws ResourceNotFoundException {
        // when
        doNothing().when(catchService).update(any(CatchPostDTO.class), anyString());

        catchService.update(catchPostDTO, ID_1);

        // then
        verify(catchService, times(1)).update(any(CatchPostDTO.class), anyString());
    }
}