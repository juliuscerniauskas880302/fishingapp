package service.arrival;

import dto.arrival.ArrivalGetDTO;
import dto.arrival.ArrivalPostDTO;
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

public class ArrivalServiceImplTest {
    private static final String ID_1 = "ID1";
    private static final String PORT_1 = "PORT1";
    private static final Date DATE_1 = new Date();

    private static final String ID_2 = "ID2";
    private static final String PORT_2 = "PORT2";
    private static final Date DATE_2 = new Date();

    private ArrivalGetDTO arrivalGetDTO1;
    private ArrivalGetDTO arrivalGetDTO2;
    private ArrivalPostDTO arrivalPostDTO;

    @Mock
    private ArrivalServiceImpl arrivalService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        arrivalGetDTO1 = new ArrivalGetDTO();
        arrivalGetDTO1.setId(ID_1);
        arrivalGetDTO1.setPort(PORT_1);
        arrivalGetDTO1.setDate(DATE_1);

        arrivalGetDTO2 = new ArrivalGetDTO();
        arrivalGetDTO2.setId(ID_2);
        arrivalGetDTO2.setPort(PORT_2);
        arrivalGetDTO2.setDate(DATE_2);

        arrivalPostDTO = new ArrivalPostDTO();
        arrivalPostDTO.setPort(PORT_2);
        arrivalPostDTO.setDate(DATE_2);
    }

    @Test
    public void shouldGetArrivalById() throws ResourceNotFoundException {
        // when
        when(arrivalService.findById(anyString())).thenReturn(arrivalGetDTO1);
        ArrivalGetDTO result = arrivalService.findById(ID_1);

        // then
        verify(arrivalService, times(1)).findById(anyString());
        assertNotNull("Result should not be null", result);
        assertEquals("Port should be equal to " + PORT_1, PORT_1, result.getPort());
        assertEquals("ID should be equal to " + ID_1, ID_1, result.getId());
        assertEquals("Date should be equal to " + DATE_1, DATE_1, result.getDate());
    }

    @Test
    public void shouldReturnArrivalList() {
        // when
        when(arrivalService.findAll()).thenReturn(Arrays.asList(arrivalGetDTO1, arrivalGetDTO2));

        List<ArrivalGetDTO> result = arrivalService.findAll();

        // then
        verify(arrivalService, times(1)).findAll();
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should be 2", 2, result.size());
    }

    @Test
    public void shouldCrateNewArrival() throws Exception {
        // when
        doNothing().when(arrivalService).save(any(ArrivalPostDTO.class));
        arrivalService.save(arrivalPostDTO);

        // then
        verify(arrivalService, times(1)).save(any(ArrivalPostDTO.class));
    }

    @Test
    public void shouldUpdateArrivalById() throws ResourceNotFoundException {
        // when
        doNothing().when(arrivalService).update(any(ArrivalPostDTO.class), anyString());

        arrivalService.update(arrivalPostDTO, ID_1);

        // then
        verify(arrivalService, times(1)).update(any(ArrivalPostDTO.class), anyString());
    }

    @Test
    public void shouldDeleteByArrivalId() {
        // when
        doNothing().when(arrivalService).deleteById(anyString());

        arrivalService.deleteById(ID_1);

        // then
        verify(arrivalService, times(1)).deleteById(anyString());
    }
}