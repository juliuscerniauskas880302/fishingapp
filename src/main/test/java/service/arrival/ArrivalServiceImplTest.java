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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArrivalServiceImplTest {
    private static final String ID_1 = "ID1";
    private static final String PORT_1 = "PORT1";
    private static final Date DATE_1 = new Date(1999, 1, 1);

    private static final String ID_2 = "ID2";
    private static final String PORT_2 = "PORT2";
    private static final Date DATE_2 = new Date(2000, 2, 2);

    private static final String NAMED_QUERY_FIND_ALL = "arrival.findAll";

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ArrivalServiceImpl arrivalService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldGetArrivalById() {
        // given
        Arrival arrival = new Arrival();
        arrival.setId(ID_1);
        arrival.setPort(PORT_1);
        arrival.setDate(DATE_1);

        // when
        when(entityManager.find(eq(Arrival.class), anyString())).thenReturn(arrival);
        Arrival result = arrivalService.findById(ID_1);

        // then
        assertNotNull(result, "Result should not be null");
        assertEquals(PORT_1, result.getPort(), "Port should be equal to " + PORT_1);
        assertEquals(ID_1, result.getId(), "ID should be equal to " + ID_1);
        assertEquals(DATE_1, result.getDate(), "Date should be equal to " + DATE_1);
    }

    @Test
    void shouldReturnArrivalList() {
        // given
        Arrival arrival1 = new Arrival();
        arrival1.setId(ID_1);
        arrival1.setPort(PORT_1);
        arrival1.setDate(DATE_1);

        Arrival arrival2 = new Arrival();
        arrival2.setId(ID_2);
        arrival2.setPort(PORT_2);
        arrival2.setDate(DATE_2);

        // when
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(NAMED_QUERY_FIND_ALL, Arrival.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(arrival1, arrival2));

        List<Arrival> result = arrivalService.findAll();

        // then
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Result size should be 2");
    }

    @Test
    void shouldCrateNewArrival() {
        //given
        Arrival arrival = new Arrival();
        arrival.setId(ID_1);
        arrival.setPort(PORT_1);
        arrival.setDate(DATE_1);

        // when
        doNothing().when(entityManager).persist(any(Arrival.class));
        Response response = arrivalService.save(arrival);

        // then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus(), "Response status should be " + Response.Status.CREATED.getStatusCode());
    }

    @Test
    void shouldUpdateArrivalById() {
        // TODO implement method
    }

    @Test
    void shouldDeleteByArrivalId() {
        //when
        doNothing().when(entityManager).remove(anyString());
        when(entityManager.find(eq(Arrival.class), anyString())).thenReturn(null);

        arrivalService.deleteById(ID_1);
        Arrival result = arrivalService.findById(ID_1);

        // then
        assertNull(result, "Result should be null");
    }
}