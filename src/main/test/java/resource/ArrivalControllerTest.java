package resource;

import domain.Arrival;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.arrival.ArrivalService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class ArrivalControllerTest {
    private static final String ID = "id";
    private static final String PORT = "port1";
    private static final Date DATE  = new Date(1999,2,2);
    private static final String ID_2 = "id2";
    private static final String PORT_2 = "port2";
    private static final Date DATE_2 = new Date(1999,2,2);

    @Mock
    private ArrivalService arrivalService;

    @InjectMocks
    private ArrivalController arrivalController;

    private Arrival arrival;
    private Arrival arrival2;

    @BeforeEach
    public void init()throws Exception {
        MockitoAnnotations.initMocks(this);

        arrival = new Arrival();
        arrival.setDate(DATE);
        arrival.setPort(PORT);
        arrival.setId(ID);

        arrival2 = new Arrival();
        arrival2.setDate(DATE_2);
        arrival2.setPort(PORT_2);
        arrival2.setId(ID_2);
    }

    @Test
    void shouldGetArrivalById() {
        when(arrivalService.findById(anyString())).thenReturn(arrival);

        Arrival result = arrivalController.getById(ID);

        assertNotNull(result);
        assertEquals(ID, result.getId());
        assertEquals(DATE, result.getDate());
        assertEquals(PORT, result.getPort());
    }

    @Test
    void shouldReturnAllArrivals() {
        when(arrivalService.findAll()).thenReturn(Arrays.asList(arrival, arrival2));

        List<Arrival> arrivals = arrivalController.findAll();

        assertEquals(2, arrivals.size());
        assertEquals(ID, arrivals.get(0).getId());
        assertEquals(ID_2, arrivals.get(1).getId());
    }

    @Test
    void shouldCreateArrival() {

    }

    @Test
    void shouldUpdateArrivalById() {
    }

    @Test
    void shouldDeleteArrivalById() {
        doNothing().when(arrivalService).deleteById(anyString());
        when(arrivalService.findById(anyString())).thenReturn(null);

        arrivalController.deleteById(ID);
        Arrival arrival = arrivalController.getById(ID);

        assertNull(arrival);
    }
}