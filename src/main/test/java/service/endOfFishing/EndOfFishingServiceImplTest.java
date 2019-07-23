package service.endOfFishing;

import domain.EndOfFishing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.exception.ResourceNotFoundException;

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

public class EndOfFishingServiceImplTest {
    private static final String ID_1 = "ID1";
    private static final Date DATE_1 = new Date();

    private static final String ID_2 = "ID2";
    private static final Date DATE_2 = new Date();

    private EndOfFishing endOfFishing1;
    private EndOfFishing endOfFishing2;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EndOfFishingServiceImpl endOfFishingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        endOfFishing1 = new EndOfFishing();
        endOfFishing1.setId(ID_1);
        endOfFishing1.setDate(DATE_1);

        endOfFishing2 = new EndOfFishing();
        endOfFishing2.setId(ID_2);
        endOfFishing2.setDate(DATE_2);
    }

    @Test
    public void shouldGetByEndOfFishingId() throws ResourceNotFoundException {
        // when
        when(entityManager.find(eq(EndOfFishing.class), anyString())).thenReturn(endOfFishing1);
        EndOfFishing result = endOfFishingService.findById(ID_1);

        // then
        verify(entityManager, times(1)).find(eq(EndOfFishing.class), anyString());
        assertNotNull(result, "Result should not be null");
        assertEquals(ID_1, result.getId(), "ID should be equal to " + ID_1);
        assertEquals(DATE_1, result.getDate(), "Date should be equal to " + DATE_1);
    }

    @Test
    public void shouldReturnEndOfFishingList() {
        // when
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(anyString(), eq(EndOfFishing.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(endOfFishing1, endOfFishing2));

        List<EndOfFishing> result = endOfFishingService.findAll();

        // then
        verify(entityManager, times(1)).createNamedQuery(anyString(), eq(EndOfFishing.class));
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Result size should be 2");
    }

    @Test
    public void shouldCreateNewEndOfFishing() throws Exception {
        // when
        doNothing().when(entityManager).persist(any(EndOfFishing.class));
        endOfFishingService.save(endOfFishing1);

        // then
        verify(entityManager, times(1)).persist(any(EndOfFishing.class));
    }

    @Test
    public void shouldUpdateEndOfFishingById() throws ResourceNotFoundException {
        // when
        when(entityManager.find(eq(EndOfFishing.class), anyString())).thenReturn(endOfFishing1);

        endOfFishingService.update(endOfFishing2, ID_1);

        // then
        verify(entityManager, times(1)).find(eq(EndOfFishing.class), anyString());
        verify(entityManager, times(1)).merge(eq(endOfFishing1));
    }

    @Test
    public void shouldDeleteByEndOfFishingId() {
        // when
        when(entityManager.find(eq(EndOfFishing.class), anyString())).thenReturn(endOfFishing1);

        endOfFishingService.deleteById(ID_1);

        // then
        verify(entityManager, times(1)).find(eq(EndOfFishing.class), anyString());
        verify(entityManager, times(1)).remove(eq(endOfFishing1));
    }
}