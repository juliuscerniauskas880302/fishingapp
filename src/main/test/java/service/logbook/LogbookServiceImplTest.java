package service.logbook;

import domain.Logbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.util.Arrays;
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

class LogbookServiceImplTest {
    private static final String ID_1 = "ID_1";

    private static final String NATIVE_QUERY_FIND_ALL = "SELECT * FROM LOGBOOK";

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private LogbookServiceImpl logbookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldGetLogBookById() {
        // given
        Logbook logbook = new Logbook();
        logbook.setId(ID_1);

        // when
        when(entityManager.find(eq(Logbook.class), anyString())).thenReturn(logbook);
        Logbook result = logbookService.findById(ID_1);

        // then
        assertNotNull(result, "Result should not be empty");
        assertEquals(ID_1, result.getId(), "ID should be equal to " + ID_1);
    }

    @Test
    void shouldReturnLogbookList() {
        // given
        Logbook logbook1 = new Logbook();
        Logbook logbook2 = new Logbook();

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
        //given
        Logbook logbook = new Logbook();

        // when
        doNothing().when(entityManager).persist(any(Logbook.class));
        Response response = logbookService.save(logbook);

        // then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus(), "Response status should be " + Response.Status.CREATED.getStatusCode());
    }

    @Test
    void shouldUpdateLogbookById() {
        // TODO implement method
    }

    @Test
    void shouldDeleteByLogbookId() {
        //when
        doNothing().when(entityManager).remove(anyString());
        when(entityManager.find(eq(Logbook.class), anyString())).thenReturn(null);

        logbookService.deleteById(ID_1);
        Logbook result = logbookService.findById(ID_1);

        // then
        assertNull(result, "Result should be null");
    }

    @Test
    void shouldFindLogbookByPort() {
        // TODO implement method
    }

    @Test
    void shouldFindLogbookBySpecies() {
        // TODO implement method
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