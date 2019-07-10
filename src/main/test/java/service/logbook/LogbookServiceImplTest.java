package service.logbook;

import domain.Logbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.util.Arrays;
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

class LogbookServiceImplTest {
    private static final String ID_1 = "ID1";
    private static final String ID_2 = "ID2";
    private static final String NATIVE_QUERY_FIND_ALL = "SELECT * FROM LOGBOOK";

    private static final String NATIVE_QUERY_FIND_BY_PORT = "SELECT DISTINCT (LOGBOOK.*)" +
            " FROM LOGBOOK " +
            " LEFT JOIN LOGBOOK_CATCH LC ON LOGBOOK.ID = LC.LOGBOOK_ID" +
            " LEFT JOIN CATCH C ON LC.CATCHES_ID = C.ID" +
            " WHERE C.VARIETY LIKE :searchParam group by LOGBOOK.ID";

    private Logbook logbook1;
    private Logbook logbook2;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private LogbookServiceImpl logbookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        logbook1 = new Logbook();
        logbook1.setId(ID_1);

        logbook2 = new Logbook();
        logbook2.setId(ID_2);
    }

    @Test
    void shouldGetLogBookById() {
        // when
        when(entityManager.find(eq(Logbook.class), anyString())).thenReturn(logbook1);
        Logbook result = logbookService.findById(ID_1);

        // then
        assertNotNull(result, "Result should not be empty");
        assertEquals(ID_1, result.getId(), "ID should be equal to " + ID_1);
    }

    @Test
    void shouldReturnLogbookList() {
        // when
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createNativeQuery(NATIVE_QUERY_FIND_ALL, Logbook.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(logbook1, logbook2));

        List<Logbook> logbooks = logbookService.findAll();

        // then
        assertNotNull(logbooks, "List should not be empty");
        assertEquals(2, logbooks.size(), "List size should be 2");
    }

    @Test
    void shouldCrateLogbook() {
        // when
        doNothing().when(entityManager).persist(any(Logbook.class));
        Response response = logbookService.save(logbook1);

        // then
        assertEquals(Response.Status.CREATED.getStatusCode(),
                response.getStatus(),
                "Response status should be " + Response.Status.CREATED.getStatusCode());
    }

    @Test
    void shouldUpdateLogbookById() {
        // when
        when(entityManager.find(eq(Logbook.class), anyString())).thenReturn(logbook1);

        logbookService.update(logbook2, ID_1);

        // then
        verify(entityManager, times(1)).merge(eq(logbook1));
    }

    @Test
    void shouldDeleteByLogbookId() {
        // when
        when(entityManager.find(eq(Logbook.class), anyString())).thenReturn(logbook1);

        logbookService.deleteById(ID_1);

        // then
        verify(entityManager, times(1)).remove(eq(logbook1));
    }

    @Test
    void shouldFindLogbookByPort() {
        // TODO implement method
    }

    @Test
    void shouldFindLogbookBySpecies() {
        // given
        TypedQuery<Logbook> queryByMock = (TypedQuery<Logbook>) Mockito.mock(TypedQuery.class);

        // when
        when(entityManager.createNativeQuery(NATIVE_QUERY_FIND_BY_PORT, Logbook.class)).thenReturn(queryByMock);
        when(queryByMock.setParameter("searchParam", "%searchParam")).thenReturn(queryByMock);
        when(queryByMock.getResultList()).thenReturn(Arrays.asList(logbook1, logbook2));

        List<Logbook> result = logbookService.findBySpecies("searchParam");

        // then
        assertNotNull(result, "List should not be empty");
        assertEquals(2, result.size(), "List size should be 2");
    }

    @Test
    void shouldFindLogbookByArrivalDate() {
        // TODO implement method
    }

    @Test
    void shouldFindLogbookByDepartureDate() {
        // TODO implement method
    }

}