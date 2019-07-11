package service.config;

import domain.config.Configuration;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConfigServiceImplTest {
    private static final String ID_1 = "ID1";
    private static final String KEY_1 = "KEY1";
    private static final String VALUE_1 = "VALUE1";
    private static final String DESCRIPTION_1 = "DESCRIPTION1";

    private static final String ID_2 = "ID2";
    private static final String KEY_2 = "KEY2";
    private static final String VALUE_2 = "VALUE2";
    private static final String DESCRIPTION_2 = "DESCRIPTION2";

    private static final String DEFAULT_VALUE = "DEFAULT";

    private TypedQuery<Configuration> queryByMock;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ConfigServiceImpl configService;

    private Configuration config1, config2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        queryByMock = (TypedQuery<Configuration>) Mockito.mock(TypedQuery.class);

        config1 = new Configuration();
        config1.setId(ID_1);
        config1.setValue(VALUE_1);
        config1.setKey(KEY_1);
        config1.setDescription(DESCRIPTION_1);

        config2 = new Configuration();
        config2.setId(ID_2);
        config2.setValue(VALUE_2);
        config2.setKey(KEY_2);
        config2.setDescription(DESCRIPTION_2);
    }

    @Test
    public void shouldCreateNewConfig() {
        // when
        doNothing().when(entityManager).persist(any(Configuration.class));
        Response response = configService.add(config1);

        // then
        verify(entityManager, times(1)).persist(any(Configuration.class));
        assertEquals(Response.Status.CREATED.getStatusCode(),
                response.getStatus(),
                "Response status should be " + Response.Status.CREATED.getStatusCode());
    }

    @Test
    public void shouldDeleteConfigByKey() {
        // when
        when(entityManager.createNativeQuery(anyString(), eq(Configuration.class))).thenReturn(queryByMock);
        when(queryByMock.setParameter(anyInt(), anyString())).thenReturn(queryByMock);
        when(queryByMock.getResultList()).thenReturn(Arrays.asList(config1));

        configService.delete(KEY_1);

        // then
        verify(entityManager, times(1)).createNativeQuery(anyString(), eq(Configuration.class));
        verify(entityManager, times(1)).remove(eq(config1));
    }

    @Test
    public void shouldUpdateByKey() {
        // when
        when(entityManager.createNativeQuery(anyString(), eq(Configuration.class))).thenReturn(queryByMock);
        when(queryByMock.setParameter(anyInt(), anyString())).thenReturn(queryByMock);
        when(queryByMock.getResultList()).thenReturn(Arrays.asList(config1));

        configService.update(KEY_1, VALUE_1, DESCRIPTION_1);

        // then
        verify(entityManager, times(1)).createNativeQuery(anyString(), eq(Configuration.class));
        verify(entityManager, times(1)).merge(config1);
    }

    @Test
    public void shouldGetValueByKey() {
        // when
        when(entityManager.createNativeQuery(anyString(), eq(Configuration.class))).thenReturn(queryByMock);
        when(queryByMock.setParameter(anyInt(), anyString())).thenReturn(queryByMock);
        when(queryByMock.getSingleResult()).thenReturn(config1);

        String value = configService.getValueByKey(KEY_1, DEFAULT_VALUE);

        // then
        verify(entityManager, times(1)).createNativeQuery(anyString(), eq(Configuration.class));
        assertEquals(config1.getValue(), value, "Configuration value should be " + config1.getValue());
    }

    @Test
    public void shouldGetAllConfigs() {
        // when
        when(entityManager.createNativeQuery(anyString(), eq(Configuration.class))).thenReturn(queryByMock);
        when(queryByMock.getResultList()).thenReturn(Arrays.asList(config1, config2));

        List<Configuration> result = configService.getAll();

        // then
        verify(entityManager, times(1)).createNativeQuery(anyString(), eq(Configuration.class));
        assertEquals(2, result.size(), "Result list size should be 2");
    }
}