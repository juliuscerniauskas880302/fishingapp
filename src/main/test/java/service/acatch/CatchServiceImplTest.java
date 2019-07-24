package service.acatch;

import domain.Catch;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.exception.ResourceNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Arrays;
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

public class CatchServiceImplTest {
    private static final String ID_1 = "1";
    private static final String ID_2 = "2";
    private static final String VARIETY_1 = "variety1";
    private static final String VARIETY_2 = "variety2";
    private static final Double WEIGHT_1 = 1D;
    private static final Double WEIGHT_2 = 2D;

    private Catch catch1;
    private Catch catch2;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CatchServiceImpl catchService;

    @Before
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        catch1 = new Catch();
        catch1.setId(ID_1);
        catch1.setWeight(WEIGHT_1);
        catch1.setVariety(VARIETY_1);

        catch2 = new Catch();
        catch2.setId(ID_2);
        catch2.setWeight(WEIGHT_2);
        catch2.setVariety(VARIETY_2);
    }

    @Test
    public void shouldGetCatchById() throws ResourceNotFoundException {
        // when
        when(entityManager.find(eq(Catch.class), anyString())).thenReturn(catch1);
        Catch result = catchService.findById(ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Catch.class), anyString());
        assertNotNull(result);
        assertEquals("Variety should be equal to " + VARIETY_1, VARIETY_1, result.getVariety());
        assertEquals("ID should be equal to " + ID_1, ID_1, result.getId());
        assertEquals("Weight should be equal to " + WEIGHT_1, WEIGHT_1, result.getWeight());
    }

    @Test
    public void shouldDeleteCatchById() {
        // when
        when(entityManager.find(eq(Catch.class), anyString())).thenReturn(catch1);

        catchService.deleteById(ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Catch.class), anyString());
        verify(entityManager, times(1)).remove(eq(catch1));
    }

    @Test
    public void shouldReturnCatchList() {
        // when
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(anyString(), eq(Catch.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(catch1, catch2));

        List<Catch> result = catchService.findAll();

        // then
        verify(entityManager, times(1)).createNamedQuery(anyString(), eq(Catch.class));
        assertNotNull("Result should not be null", result);
        assertEquals("Result size should be 2", 2, result.size());
    }

    @Test
    public void shouldCreateNewCatch() throws Exception {
        // when
        doNothing().when(entityManager).persist(any(Catch.class));
        catchService.save(catch1);

        // then
        verify(entityManager, times(1)).persist(any(Catch.class));
    }

    @Test
    void shouldUpdateCatchById() throws ResourceNotFoundException {
        // when
        when(entityManager.find(eq(Catch.class), anyString())).thenReturn(catch1);

        catchService.update(catch2, ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Catch.class), anyString());
        verify(entityManager, times(1)).merge(eq(catch1));
    }
}