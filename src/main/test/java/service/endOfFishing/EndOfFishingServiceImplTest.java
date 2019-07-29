package service.endOfFishing;

import dto.endOfFishing.EndOfFishingGetDTO;
import dto.endOfFishing.EndOfFishingPostDTO;
import exception.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EndOfFishingServiceImplTest {
    private static final String ID_1 = "ID1";
    private static final Date DATE_1 = new Date();

    private static final String ID_2 = "ID2";
    private static final Date DATE_2 = new Date();

    private EndOfFishingGetDTO endOfFishingGetDTO1;
    private EndOfFishingGetDTO endOfFishingGetDTO2;
    private EndOfFishingPostDTO endOfFishingPostDTO;

    @Mock
    private EndOfFishingServiceImpl endOfFishingService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        endOfFishingGetDTO1 = new EndOfFishingGetDTO();
        endOfFishingGetDTO1.setId(ID_1);
        endOfFishingGetDTO1.setDate(DATE_1);

        endOfFishingGetDTO2 = new EndOfFishingGetDTO();
        endOfFishingGetDTO2.setId(ID_2);
        endOfFishingGetDTO2.setDate(DATE_2);

        endOfFishingPostDTO = new EndOfFishingPostDTO();
        endOfFishingPostDTO.setDate(DATE_2);
    }

    @Test
    public void shouldGetByEndOfFishingId() throws ResourceNotFoundException {
        // when
        when(endOfFishingService.findById(anyString())).thenReturn(endOfFishingGetDTO1);
        EndOfFishingGetDTO result = endOfFishingService.findById(ID_1);

        // then
        verify(endOfFishingService, times(1)).findById(anyString());
        assertNotNull("Result should not be null", result);
        assertEquals("ID should be equal to " + ID_1, ID_1, result.getId());
        assertEquals("Date should be equal to " + DATE_1, DATE_1, result.getDate());
    }

    @Test
    public void shouldReturnEndOfFishingList() {
        // when
        when(endOfFishingService.findAll()).thenReturn(Arrays.asList(endOfFishingGetDTO1, endOfFishingGetDTO2));

        List<EndOfFishingGetDTO> result = endOfFishingService.findAll();

        // then
        verify(endOfFishingService, times(1)).findAll();
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should be 2", 2, result.size());
    }

    @Test
    public void shouldCreateNewEndOfFishing() throws Exception {
        // when
        doNothing().when(endOfFishingService).save(any(EndOfFishingPostDTO.class));
        endOfFishingService.save(endOfFishingPostDTO);

        // then
        verify(endOfFishingService, times(1)).save(any(EndOfFishingPostDTO.class));
    }

    @Test
    public void shouldUpdateEndOfFishingById() throws ResourceNotFoundException {
        // when
        doNothing().when(endOfFishingService).update(any(EndOfFishingPostDTO.class), anyString());

        endOfFishingService.update(endOfFishingPostDTO, ID_1);

        // then
        verify(endOfFishingService, times(1)).update(any(EndOfFishingPostDTO.class), anyString());
    }

    @Test
    public void shouldDeleteByEndOfFishingId() {
        // when
        doNothing().when(endOfFishingService).deleteById(anyString());

        endOfFishingService.deleteById(ID_1);

        // then
        verify(endOfFishingService, times(1)).deleteById(anyString());
    }
}