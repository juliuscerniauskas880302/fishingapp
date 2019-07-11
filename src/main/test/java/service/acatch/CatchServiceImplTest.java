package service.acatch;

import domain.Catch;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CatchServiceImplTest {
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

    @BeforeEach
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
    void shouldGetCatchById() {
        // when
        when(entityManager.find(eq(Catch.class), anyString())).thenReturn(catch1);
        Catch result = catchService.findById(ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Catch.class), anyString());
        assertNotNull(result);
        assertEquals(VARIETY_1, result.getVariety(), "Variety should be equal to " + VARIETY_1);
        assertEquals(ID_1, result.getId(), "ID should be equal to " + ID_1);
        assertEquals(WEIGHT_1, result.getWeight(), "Weight should be equal to " + WEIGHT_1);
    }

    @Test
    void shouldDeleteCatchById() {
        // when
        when(entityManager.find(eq(Catch.class), anyString())).thenReturn(catch1);

        catchService.deleteById(ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Catch.class), anyString());
        verify(entityManager, times(1)).remove(eq(catch1));
    }

    @Test
    void shouldReturnCatchList() {
        // when
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(anyString(), eq(Catch.class))).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(catch1, catch2));

        List<Catch> result = catchService.findAll();

        // then
        verify(entityManager, times(1)).createNamedQuery(anyString(), eq(Catch.class));
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Result size should be 2");
    }

    @Test
    void shouldCreateNewCatch() {
        // when
        doNothing().when(entityManager).persist(any(Catch.class));
        Response response = catchService.save(catch1);

        // then
        verify(entityManager, times(1)).persist(any(Catch.class));
        assertEquals(Response.Status.CREATED.getStatusCode(),
                response.getStatus(),
                "Response value should be " + Response.Status.CREATED.getStatusCode());
    }

    @Test
    void shouldUpdateCatchById() {
        // when
        when(entityManager.find(eq(Catch.class), anyString())).thenReturn(catch1);

        catchService.update(catch2, ID_1);

        // then
        verify(entityManager, times(1)).find(eq(Catch.class), anyString());
        verify(entityManager, times(1)).merge(eq(catch1));
    }

}