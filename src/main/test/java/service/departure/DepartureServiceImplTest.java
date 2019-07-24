package service.departure;

import domain.Departure;
import domain.Logbook;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

public class DepartureServiceImplTest {
    private static final String ID_1 = "ID1";
    private static final String PORT_1 = "PORT1";
    private static final Date DATE_1 = new Date();

    private static final String ID_2 = "ID2";
    private static final String PORT_2 = "PORT2";
    private static final Date DATE_2 = new Date();

    private Departure departure1;
    private Departure departure2;

    private TypedQuery<Logbook> queryByMock;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private DepartureServiceImpl departureService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        queryByMock = (TypedQuery<Logbook>) Mockito.mock(TypedQuery.class);

        departure1 = new Departure();
        departure1.setId(ID_1);
        departure1.setPort(PORT_1);
        departure1.setDate(DATE_1);

        departure2 = new Departure();
        departure2.setId(ID_2);
        departure2.setPort(PORT_2);
        departure2.setDate(DATE_2);
    }

    @Test
    public void shouldGetDepartureById() throws ResourceNotFoundException {
        // when
        when(entityManager.find(eq(Departure.class), anyString())).thenReturn(departure1);
        Departure result = departureService.findById(ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Departure.class), anyString());
        assertNotNull("Result should not be null", result);
        assertEquals("Port should be equal to " + PORT_1, PORT_1, result.getPort());
        assertEquals("ID should be equal to " + ID_1, ID_1, result.getId());
        assertEquals("Date should be equal to " + DATE_1, DATE_1, result.getDate());
    }

    @Test
    public void shouldReturnDepartureList() {
        // when
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(anyString(), eq(Departure.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(departure1, departure2));

        List<Departure> result = departureService.findAll();

        // then
        verify(entityManager, times(1)).createNamedQuery(anyString(), eq(Departure.class));
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should be 2",2, result.size());
    }

    @Test
    public void shouldCreateNewDeparture() throws Exception {
        // when
        doNothing().when(entityManager).persist(any(Departure.class));
        departureService.save(departure1);

        // then
        verify(entityManager, times(1)).persist(any(Departure.class));
    }

    @Test
    public void shouldUpdateDepartureById() throws ResourceNotFoundException {
        // when
        when(entityManager.find(eq(Departure.class), anyString())).thenReturn(departure1);

        departureService.update(departure2, ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Departure.class), anyString());
        verify(entityManager, times(1)).merge(eq(departure1));
    }

    @Test
    public void shouldDeleteByDepartureId() {
        // when
        when(entityManager.find(eq(Departure.class), anyString())).thenReturn(departure1);

        departureService.deleteById(ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Departure.class), anyString());
        verify(entityManager, times(1)).remove(eq(departure1));
    }
}