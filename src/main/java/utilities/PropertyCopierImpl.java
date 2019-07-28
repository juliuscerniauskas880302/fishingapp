package utilities;

import org.apache.commons.beanutils.BeanUtils;

import javax.enterprise.inject.Model;
import java.lang.reflect.InvocationTargetException;

@Model
public class PropertyCopierImpl implements PropertyCopier {

    @Override
    public Object copy(Object dest, Object source) {
        try {
            BeanUtils.copyProperties(dest, source);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return dest;
    }
}
