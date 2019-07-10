package service.departure;

import domain.Departure;
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

class DepartureServiceImplTest {
    private static final String ID_1 = "ID1";
    private static final String PORT_1 = "PORT1";
    private static final Date DATE_1 = new Date(1999, 1, 1);

    private static final String ID_2 = "ID2";
    private static final String PORT_2 = "PORT2";
    private static final Date DATE_2 = new Date(2000, 2, 2);

    private static final String NAMED_QUERY_FIND_ALL = "departure.findAll";

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private DepartureServiceImpl departureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldGetDepartureById() {
        // given
        Departure departure = new Departure();
        departure.setId(ID_1);
        departure.setPort(PORT_1);
        departure.setDate(DATE_1);

        // when
        when(entityManager.find(eq(Departure.class), anyString())).thenReturn(departure);
        Departure result = departureService.findById(ID_1);

        // then
        assertNotNull(result, "Result should not be null");
        assertEquals(PORT_1, result.getPort(), "Port should be equal to " + PORT_1);
        assertEquals(ID_1, result.getId(), "ID should be equal to " + ID_1);
        assertEquals(DATE_1, result.getDate(), "Date should be equal to " + DATE_1);
    }

    @Test
    void shouldReturnDepartureList() {
        // given
        Departure departure1 = new Departure();
        departure1.setId(ID_1);
        departure1.setPort(PORT_1);
        departure1.setDate(DATE_1);

        Departure departure2 = new Departure();
        departure2.setId(ID_2);
        departure2.setPort(PORT_2);
        departure2.setDate(DATE_2);

        // when
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(NAMED_QUERY_FIND_ALL, Departure.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(departure1, departure2));

        List<Departure> result = departureService.findAll();

        // then
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Result size should be 2");
    }

    @Test
    void shouldCreateNewDeparture() {
        //given
        Departure departure = new Departure();
        departure.setId(ID_1);
        departure.setPort(PORT_1);
        departure.setDate(DATE_1);

        // when
        doNothing().when(entityManager).persist(any(Departure.class));
        Response response = departureService.save(departure);

        // then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus(), "Response status should be " + Response.Status.CREATED.getStatusCode());
    }

    @Test
    void update() {
        // TODO implement method
    }

    @Test
    void shouldDeleteByDepartureId() {
        // given
        Departure departure = new Departure();
        departure.setId(ID_1);

        // when
        when(entityManager.find(eq(Departure.class), anyString())).thenReturn(departure);

        departureService.deleteById(ID_1);

        // then
        verify(entityManager, times(1)).remove(eq(departure));
    }
}