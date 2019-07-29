package service.departure;

import dto.departure.DepartureGetDTO;
import dto.departure.DeparturePostDTO;
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

public class DepartureServiceImplTest {
    private static final String ID_1 = "ID1";
    private static final String PORT_1 = "PORT1";
    private static final Date DATE_1 = new Date();

    private static final String ID_2 = "ID2";
    private static final String PORT_2 = "PORT2";
    private static final Date DATE_2 = new Date();

    private DepartureGetDTO departureGetDTO1;
    private DepartureGetDTO departureGetDTO2;
    private DeparturePostDTO departurePostDTO;

    @Mock
    private DepartureServiceImpl departureService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        departureGetDTO1 = new DepartureGetDTO();
        departureGetDTO1.setId(ID_1);
        departureGetDTO1.setPort(PORT_1);
        departureGetDTO1.setDate(DATE_1);

        departureGetDTO2 = new DepartureGetDTO();
        departureGetDTO2.setId(ID_2);
        departureGetDTO2.setPort(PORT_2);
        departureGetDTO2.setDate(DATE_2);

        departurePostDTO = new DeparturePostDTO();
        departurePostDTO.setPort(PORT_2);
        departurePostDTO.setDate(DATE_2);
    }

    @Test
    public void shouldGetDepartureById() throws ResourceNotFoundException {
        // when
        when(departureService.findById(anyString())).thenReturn(departureGetDTO1);
        DepartureGetDTO result = departureService.findById(ID_1);

        // then
        verify(departureService, times(1)).findById(anyString());
        assertNotNull("Result should not be null", result);
        assertEquals("Port should be equal to " + PORT_1, PORT_1, result.getPort());
        assertEquals("ID should be equal to " + ID_1, ID_1, result.getId());
        assertEquals("Date should be equal to " + DATE_1, DATE_1, result.getDate());
    }

    @Test
    public void shouldReturnDepartureList() {
        // when
        when(departureService.findAll()).thenReturn(Arrays.asList(departureGetDTO1, departureGetDTO2));

        List<DepartureGetDTO> result = departureService.findAll();

        // then
        verify(departureService, times(1)).findAll();
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should be 2", 2, result.size());
    }

    @Test
    public void shouldCreateNewDeparture() throws Exception {
        // when
        doNothing().when(departureService).save(any(DeparturePostDTO.class));
        departureService.save(departurePostDTO);

        // then
        verify(departureService, times(1)).save(any(DeparturePostDTO.class));
    }

    @Test
    public void shouldUpdateDepartureById() throws ResourceNotFoundException {
        // when
        doNothing().when(departureService).update(any(DeparturePostDTO.class), anyString());

        departureService.update(departurePostDTO, ID_1);

        // then
        verify(departureService, times(1)).update(any(DeparturePostDTO.class), anyString());
    }

    @Test
    public void shouldDeleteByDepartureId() {
        // when
        doNothing().when(departureService).deleteById(anyString());

        departureService.deleteById(ID_1);

        // then
        verify(departureService, times(1)).deleteById(anyString());
    }
}