package service.arrival;

import domain.Arrival;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @BeforeEach
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
    public void shouldGetArrivalById() {
        // when
        when(entityManager.find(eq(Arrival.class), anyString())).thenReturn(arrival1);
        Arrival result = arrivalService.findById(ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Arrival.class), anyString());
        assertNotNull(result, "Result should not be null");
        assertEquals(PORT_1, result.getPort(), "Port should be equal to " + PORT_1);
        assertEquals(ID_1, result.getId(), "ID should be equal to " + ID_1);
        assertEquals(DATE_1, result.getDate(), "Date should be equal to " + DATE_1);
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
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Result size should be 2");
    }

    @Test
    public void shouldCrateNewArrival() {
        // when
        doNothing().when(entityManager).persist(any(Arrival.class));
        Response response = arrivalService.save(arrival1);

        // then
        assertEquals(Response.Status.CREATED.getStatusCode(),
                response.getStatus(),
                "Response status should be " + Response.Status.CREATED.getStatusCode());
    }

    @Test
    public void shouldUpdateArrivalById() {
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