package service.logbook;

import dto.logbook.LogbookGetDTO;
import dto.logbook.LogbookPostDTO;
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

public class LogbookServiceImplTest {
    private static final String ID_1 = "ID1";
    private static final String ID_2 = "ID2";

    private static final Date DATE_1 = new Date();
    private static final Date DATE_2 = new Date();

    private static final String SEARCH_PARAM = "SEARCH_PARAM";

    private LogbookGetDTO logbookGetDTO1;
    private LogbookGetDTO logbookGetDTO2;
    private LogbookPostDTO logbookPostDTO;

    @Mock
    private LogbookServiceImpl logbookService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        logbookGetDTO1 = new LogbookGetDTO();
        logbookGetDTO1.setId(ID_1);

        logbookGetDTO2 = new LogbookGetDTO();
        logbookGetDTO2.setId(ID_2);

        logbookPostDTO = new LogbookPostDTO();
    }

    @Test
    public void shouldGetLogbookById() throws ResourceNotFoundException {
        // when
        when(logbookService.findById(anyString())).thenReturn(logbookGetDTO1);
        LogbookGetDTO result = logbookService.findById(ID_1);

        // then
        verify(logbookService, times(1)).findById(anyString());
        assertNotNull("Result should not be empty", result);
        assertEquals("ID should be equal to " + ID_1, ID_1, result.getId());
    }

    @Test
    public void shouldReturnLogbookList() {
        // when
        when(logbookService.findAll()).thenReturn(Arrays.asList(logbookGetDTO1, logbookGetDTO2));

        List<LogbookGetDTO> logbooks = logbookService.findAll();

        // then
        verify(logbookService, times(1)).findAll();
        assertNotNull("List should not be empty", logbooks);
        assertEquals("List size should be 2", 2, logbooks.size());
    }

    @Test
    public void shouldCrateLogbook() throws Exception {
        // when
        doNothing().when(logbookService).save(any(LogbookPostDTO.class));
        logbookService.save(logbookPostDTO);

        // then
        verify(logbookService, times(1)).save(any(LogbookPostDTO.class));
    }

    @Test
    public void shouldUpdateLogbookById() {
        // when
        doNothing().when(logbookService).update(any(LogbookPostDTO.class), anyString());

        logbookService.update(logbookPostDTO, ID_1);

        // then
        verify(logbookService, times(1)).update(any(LogbookPostDTO.class), anyString());
    }

    @Test
    public void shouldDeleteByLogbookId() {
        // when
        doNothing().when(logbookService).deleteById(anyString());

        logbookService.deleteById(ID_1);

        // then
        verify(logbookService, times(1)).deleteById(anyString());
    }

    @Test
    public void shouldFindLogbookByPort() {
        // when
        when(logbookService.findByPort(anyString())).thenReturn(Arrays.asList(logbookGetDTO1, logbookGetDTO2));

        List<LogbookGetDTO> result = logbookService.findByPort(SEARCH_PARAM);

        // then
        verify(logbookService, times(1)).findByPort(anyString());
        assertNotNull("List should not be empty", result);
        assertEquals("List size should be 1", 2, result.size());
    }

    @Test
    public void shouldFindLogbookBySpecies() {
        // when
        when(logbookService.findBySpecies(anyString())).thenReturn(Arrays.asList(logbookGetDTO1, logbookGetDTO2));

        List<LogbookGetDTO> result = logbookService.findBySpecies(SEARCH_PARAM);

        // then
        verify(logbookService, times(1)).findBySpecies(anyString());
        assertNotNull("List should not be empty", result);
        assertEquals("List size should be 2", 2, result.size());
    }

    @Test
    public void shouldFindLogbookByArrivalDate() {
        // when
        when(logbookService.findByArrivalDateIn(anyString(), anyString())).thenReturn(Arrays.asList(logbookGetDTO1, logbookGetDTO2));

        List<LogbookGetDTO> result = logbookService.findByArrivalDateIn(DATE_1.toString(), DATE_2.toString());

        // then
        verify(logbookService, times(1)).findByArrivalDateIn(anyString(), anyString());
        assertNotNull("List should not be empty", result);
        assertEquals("List size should be 2", 2, result.size());
    }

    @Test
    public void shouldFindLogbookByDepartureDate() {
        // when
        when(logbookService.findByDepartureDateIn(anyString(), anyString())).thenReturn(Arrays.asList(logbookGetDTO1, logbookGetDTO2));

        List<LogbookGetDTO> result = logbookService.findByDepartureDateIn(DATE_1.toString(), DATE_2.toString());

        // then
        verify(logbookService, times(1)).findByDepartureDateIn(anyString(), anyString());
        assertNotNull("List should not be empty", result);
        assertEquals("List size should be 2", 2, result.size());
    }
}