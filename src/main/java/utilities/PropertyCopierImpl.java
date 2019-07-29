package utilities;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.enterprise.inject.Model;
import java.lang.reflect.InvocationTargetException;

@Model
public class PropertyCopierImpl implements PropertyCopier {
    private static final Logger LOG = LogManager.getLogger(PropertyCopierImpl.class);

    @Override
    public Object copy(Object dest, Object source) {
        try {
            BeanUtils.copyProperties(dest, source);
        } catch (IllegalAccessException e) {
            LOG.error("Error copying properties {} ", e);
        } catch (InvocationTargetException e) {
            LOG.error("Error copying properties {} ", e);
        }
        return dest;
    }
}
