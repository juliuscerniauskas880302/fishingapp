package service.acatch;

import domain.Catch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CatchServiceTest {

    private final static String ID_1 = "1";
    private final static String ID_2 = "2";
    private final static String VARIETY_1 = "variety1";
    private final static String VARIETY_2 = "variety2";
    private final static Double WEIGHT_1 = 1D;
    private final static Double WEIGHT_2 = 2D;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CatchServiceImpl catchService;

    @BeforeEach
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        Catch catch1 = new Catch();
        catch1.setWeight(WEIGHT_1);
        catch1.setVariety(VARIETY_1);
        catch1.setId(ID_1);
        Catch catch2 = new Catch();
        catch2.setWeight(WEIGHT_2);
        catch2.setVariety(VARIETY_2);
        catch2.setId(ID_2);
        when(entityManager.find(eq(Catch.class), anyString())).thenReturn(catch1);
    }

    @Test
    public final void shouldGetArrivalById() {
        // given
        Catch aCatch = new Catch();
        aCatch.setId(ID_1);
        aCatch.setVariety(VARIETY_1);
        aCatch.setWeight(WEIGHT_1);

        // when
        Catch result = catchService.findById(ID_1);

        // then
        assertNotNull(result);
        assertEquals(VARIETY_1, result.getVariety());
        assertEquals(ID_1, result.getId());
        assertEquals(WEIGHT_1, result.getWeight());
    }
}