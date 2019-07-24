package service.arrival;

import domain.Arrival;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.exception.ResourceNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
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

    private Arrival arrival1;
    private Arrival arrival2;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ArrivalServiceImpl arrivalService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        arrival1 = new Arrival();
        arrival1.setId(ID_1);
        arrival1.setPort(PORT_1);
        arrival1.setDate(DATE_1);

        arrival2 = new Arrival();
        arrival2.setId(ID_2);
        arrival2.setPort(PORT_2);
        arrival2.setDate(DATE_2);
    }

    @Test
    public void shouldGetArrivalById() throws ResourceNotFoundException {
        // when
        when(entityManager.find(eq(Arrival.class), anyString())).thenReturn(arrival1);
        Arrival result = arrivalService.findById(ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Arrival.class), anyString());
        assertNotNull( "Result should not be null",result);
        assertEquals(PORT_1, result.getPort(), "Port should be equal to " + PORT_1);
        assertEquals(ID_1, result.getId(), "ID should be equal to " + ID_1);
        assertEquals("Date should be equal to " + DATE_1, DATE_1, result.getDate());
    }

    @Test
    public void shouldReturnArrivalList() {
        // when
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(anyString(), eq(Arrival.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(arrival1, arrival2));

        List<Arrival> result = arrivalService.findAll();

        // then
        verify(entityManager, times(1)).createNamedQuery(anyString(), eq(Arrival.class));
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should be 2",2, result.size());
    }

    @Test
    public void shouldCrateNewArrival() throws Exception {
        // when
        doNothing().when(entityManager).persist(any(Arrival.class));
        arrivalService.save(arrival1);

        // then
        verify(entityManager, times(1)).persist(any(Arrival.class));
    }

    @Test
    public void shouldUpdateArrivalById() throws ResourceNotFoundException {
        // when
        when(entityManager.find(eq(Arrival.class), anyString())).thenReturn(arrival1);

        arrivalService.update(arrival2, ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Arrival.class), anyString());
        verify(entityManager, times(1)).merge(eq(arrival1));
    }

    @Test
    public void shouldDeleteByArrivalId() {
        // when
        when(entityManager.find(eq(Arrival.class), anyString())).thenReturn(arrival1);

        arrivalService.deleteById(ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Arrival.class), anyString());
        verify(entityManager, times(1)).remove(eq(arrival1));
    }
}