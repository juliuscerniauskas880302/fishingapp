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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CatchServiceImplTest {
    private static final String ID_1 = "1";
    private static final String ID_2 = "2";
    private static final String VARIETY_1 = "variety1";
    private static final String VARIETY_2 = "variety2";
    private static final Double WEIGHT_1 = 1D;
    private static final Double WEIGHT_2 = 2D;
    private static final String NAMED_QUERY_FIND_ALL = "catch.findAll";

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CatchServiceImpl catchService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldGetCatchById() {
        // given
        Catch aCatch = new Catch();
        aCatch.setId(ID_1);
        aCatch.setVariety(VARIETY_1);
        aCatch.setWeight(WEIGHT_1);

        // when
        when(entityManager.find(eq(Catch.class), anyString())).thenReturn(aCatch);
        Catch result = catchService.findById(ID_1);

        // then
        assertNotNull(result);
        assertEquals(VARIETY_1, result.getVariety(), "Variety should be equal to " + VARIETY_1);
        assertEquals(ID_1, result.getId(), "ID should be equal to " + ID_1);
        assertEquals(WEIGHT_1, result.getWeight(), "Weight should be equal to " + WEIGHT_1);
    }

    @Test
    void shouldDeleteCatchById() {
        //when
        doNothing().when(entityManager).remove(any());
        when(entityManager.find(eq(Catch.class), anyString())).thenReturn(null);

        catchService.deleteById(ID_1);
        Catch result = catchService.findById(ID_1);
        // then
        assertNull(result, "Result should be null");
    }

    @Test
    void shouldReturnCatchList() {
        // given
        Catch catch1 = new Catch();
        catch1.setId(ID_1);
        catch1.setWeight(WEIGHT_1);
        catch1.setVariety(VARIETY_1);

        Catch catch2 = new Catch();
        catch2.setId(ID_2);
        catch2.setWeight(WEIGHT_2);
        catch2.setVariety(VARIETY_2);

        // when
        TypedQuery query = mock(TypedQuery.class);
        when(entityManager.createNamedQuery(NAMED_QUERY_FIND_ALL, Catch.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(catch1, catch2));

        List<Catch> result = catchService.findAll();

        // then
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Result size should be 2");
    }

    @Test
    void shouldCreateNewCatch() {
        //given
        Catch catch1 = new Catch();
        catch1.setId(ID_1);
        catch1.setWeight(WEIGHT_1);
        catch1.setVariety(VARIETY_1);

        // when
        doNothing().when(entityManager).persist(any(Catch.class));
        Response response = catchService.save(catch1);

        // then
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus(), "Response value should be " + Response.Status.CREATED.getStatusCode());
    }

    @Test
    void shouldUpdateCatchById() {
        // TODO implement method
    }

}