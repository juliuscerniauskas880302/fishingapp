package service.logbook;

import domain.Logbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import service.config.ConfigService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogbookServiceImplTest {
    private static final String ID_1 = "ID1";
    private static final String ID_2 = "ID2";

    private static final Date DATE_1 = new Date();
    private static final Date DATE_2 = new Date();

    private static final String SEARCH_PARAM = "SEARCH_PARAM";

    private Logbook logbook1;
    private Logbook logbook2;
    TypedQuery<Logbook> queryByMock;

    @Mock
    private EntityManager entityManager;

    @Mock
    private ConfigService configService;

    @InjectMocks
    private LogbookServiceImpl logbookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        queryByMock = (TypedQuery<Logbook>) Mockito.mock(TypedQuery.class);

        logbook1 = new Logbook();
        logbook1.setId(ID_1);

        logbook2 = new Logbook();
        logbook2.setId(ID_2);
    }

    @Test
    public void shouldGetLogBookById() {
        // when
        when(entityManager.find(eq(Logbook.class), anyString())).thenReturn(logbook1);
        Logbook result = logbookService.findById(ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Logbook.class), anyString());
        assertNotNull(result, "Result should not be empty");
        assertEquals(ID_1, result.getId(), "ID should be equal to " + ID_1);
    }

    @Test
    public void shouldReturnLogbookList() {
        // when
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createNativeQuery(anyString(), eq(Logbook.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(logbook1, logbook2));

        List<Logbook> logbooks = logbookService.findAll();

        // then
        verify(entityManager, times(1)).createNativeQuery(anyString(), eq(Logbook.class));
        assertNotNull(logbooks, "List should not be empty");
        assertEquals(2, logbooks.size(), "List size should be 2");
    }

    @Test
    public void shouldCrateLogbook() {
        // when
        doNothing().when(entityManager).persist(any(Logbook.class));
        Response response = logbookService.save(logbook1);

        // then
        verify(entityManager, times(1)).persist(any(Logbook.class));
        assertEquals(Response.Status.OK.getStatusCode(),
                response.getStatus(),
                "Response status should be " + Response.Status.OK.getStatusCode());
    }

    @Test
    public void shouldUpdateLogbookById() {
        // when
        when(entityManager.find(eq(Logbook.class), anyString())).thenReturn(logbook1);

        logbookService.update(logbook2, ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Logbook.class), anyString());
        verify(entityManager, times(1)).merge(any(Logbook.class));
    }

    @Test
    public void shouldDeleteByLogbookId() {
        // when
        when(entityManager.find(eq(Logbook.class), anyString())).thenReturn(logbook1);

        logbookService.deleteById(ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Logbook.class), anyString());
        verify(entityManager, times(1)).remove(eq(logbook1));
    }

    @Test
    public void shouldFindLogbookByPort() {
        // given
        TypedQuery<Logbook> queryByMock = (TypedQuery<Logbook>) Mockito.mock(TypedQuery.class);

        // when
        when(entityManager.createNativeQuery(anyString(), eq(Logbook.class))).thenReturn(queryByMock);
        when(queryByMock.setParameter(anyInt(), anyString())).thenReturn(queryByMock);
        when(queryByMock.getResultList()).thenReturn(Arrays.asList(logbook1));

        List<Logbook> result = logbookService.findByPort(SEARCH_PARAM);

        // then
        verify(entityManager, times(1)).createNativeQuery(anyString(), eq(Logbook.class));
        assertNotNull(result, "List should not be empty");
        assertEquals(1, result.size(), "List size should be 1");
    }

    @Test
    public void shouldFindLogbookBySpecies() {
        // when
        when(entityManager.createNativeQuery(anyString(), eq(Logbook.class))).thenReturn(queryByMock);
        when(queryByMock.setParameter(anyInt(), anyString())).thenReturn(queryByMock);
        when(queryByMock.getResultList()).thenReturn(Arrays.asList(logbook1, logbook2));

        List<Logbook> result = logbookService.findBySpecies(SEARCH_PARAM);

        // then
        verify(entityManager, times(1)).createNativeQuery(anyString(), eq(Logbook.class));
        assertNotNull(result, "List should not be empty");
        assertEquals(2, result.size(), "List size should be 2");
    }

    @Test
    public void shouldFindLogbookByArrivalDate() {
        // when
        when(entityManager.createNativeQuery(anyString(), eq(Logbook.class))).thenReturn(queryByMock);
        when(queryByMock.setParameter(anyInt(), anyString())).thenReturn(queryByMock);
        when(queryByMock.getResultList()).thenReturn(Arrays.asList(logbook1, logbook2));

        List<Logbook> result = logbookService.findByArrivalDate(DATE_1.toString(), DATE_2.toString());

        // then
        verify(entityManager, times(1)).createNativeQuery(anyString(), eq(Logbook.class));
        assertNotNull(result, "List should not be empty");
        assertEquals(2, result.size(), "List size should be 2");
    }

    @Test
    public void shouldFindLogbookByDepartureDate() {
        // when
        when(entityManager.createNativeQuery(anyString(), eq(Logbook.class))).thenReturn(queryByMock);
        when(queryByMock.setParameter(anyInt(), anyString())).thenReturn(queryByMock);
        when(queryByMock.getResultList()).thenReturn(Arrays.asList(logbook1, logbook2));

        List<Logbook> result = logbookService.findByDepartureDate(DATE_1.toString(), DATE_2.toString());

        // then
        verify(entityManager, times(1)).createNativeQuery(anyString(), eq(Logbook.class));
        assertNotNull(result, "List should not be empty");
        assertEquals(2, result.size(), "List size should be 2");
    }

}