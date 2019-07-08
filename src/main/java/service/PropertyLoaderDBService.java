package service;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateful
public class PropertyLoaderDBService {
    @PersistenceContext
    private EntityManager manager;

    public String getValueByKey(String key, String defaultvalue) {

        List<String> list = manager.createQuery("select c.value from Configuration c where exists (select p from Configuration p where p.keyc.key=?1)")
                .setParameter(1, key).getResultList();
        if (list.isEmpty()) {
            return defaultvalue;
        }
        return list.get(0);
    }

}
