package pst.arm.client.common.utils;

import com.extjs.gxt.ui.client.data.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wesStyle on 11/07/14.
 */
public class BeanModelListReader implements DataReader<List<ModelData>> {
    private boolean factoryForEachBean;

    /**
     * Return if a BeanModelFactory is created for each bean or not.
     *
     * @return true if a BeanModelFactory is created for each bean or not
     */
    public boolean isFactoryForEachBean() {
        return factoryForEachBean;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<ModelData> read(Object loadConfig, Object data) {
        if (data instanceof List) {
            List<Object> beans = (List) data;
            if (beans.size() > 0) {
                if (factoryForEachBean) {
                    List models = new ArrayList(beans.size());
                    for (Object o : beans) {
                        BeanModelFactory factory = BeanModelLookup.get().getFactory(o.getClass());
                        assert factory != null : "No BeanModelFactory found for " + o.getClass();
                        models.add(factory.createModel(o));
                    }
                    return newLoadResult(loadConfig, models);
                } else {
                    BeanModelFactory factory = BeanModelLookup.get().getFactory(beans.get(0).getClass());
                    assert factory != null : "No BeanModelFactory found for " + beans.get(0).getClass();
                    return newLoadResult(loadConfig, (List) factory.createModel(beans));
                }
            }
            return newLoadResult(loadConfig, (List) beans);

        } else if (data instanceof ListLoadResult) {
            ListLoadResult result = (ListLoadResult) data;
            List beans = result.getData();
            if (beans.size() > 0) {
                List converted;
                if (factoryForEachBean) {
                    converted = new ArrayList(beans.size());
                    for (Object o : beans) {
                        BeanModelFactory factory = BeanModelLookup.get().getFactory(o.getClass());
                        assert factory != null : "No BeanModelFactory found for " + o.getClass();
                        converted.add(factory.createModel(o));
                    }
                } else {
                    BeanModelFactory factory = BeanModelLookup.get().getFactory(beans.get(0).getClass());
                    assert factory != null : "No BeanModelFactory found for " + beans.get(0).getClass();
                    converted = factory.createModel(beans);
                }
                beans.clear();
                beans.addAll(converted);
            }
            return (List) data;
        }
        assert false : "Error converting data";

        return null;
    }

    /**
     * Set to true to create a BeanModelFactory for each bean in the list
     * (defaults to false).
     *
     * @param factoryForEachBean true to enable
     */
    public void setFactoryForEachBean(boolean factoryForEachBean) {
        this.factoryForEachBean = factoryForEachBean;
    }

    /**
     * Template method that provides a new load result.
     *
     * @param models the models
     * @return the load result
     */
    protected List<ModelData> newLoadResult(Object loadConfig, List<ModelData> models) {
        return models;
    }

}
