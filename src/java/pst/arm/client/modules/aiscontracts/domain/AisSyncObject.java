package pst.arm.client.modules.aiscontracts.domain;

import pst.arm.server.modules.aiscontracts.util.DataUtil;
import pst.arm.server.modules.aiscontracts.util.FieldsUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by akozhin on 20.12.14.
 */
public abstract class AisSyncObject<T extends Field> {
    Logger log = Logger.getLogger("AisSyncObject");
    protected Integer id;
    protected Map<T, String> fields = new HashMap<T, String>();

    public Map<T, String> getFields() {
        return fields;
    }

    public Map<T, String> getFieldValues() {
        return getFields();
    }

    public void addFieldValue(T field, String value) {
        getFields().put(field, value);
    }


    public String getField(T key) {
        if (getFields() != null) {
            return getFields().get(key);
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public abstract String getTableName();

    public abstract String asString();

    public abstract String getPK();

    public abstract T[] getAvailableFields();

    @Override
    public boolean equals(Object obj) {
        for (Map.Entry<T, String> entrySet : getFields().entrySet()) {
            T key = entrySet.getKey();
            if (key.getName().isEmpty()) {
                continue;
            }
            Object thisValue = getValue(key);
            Object compareValue = ((AisSyncObject) obj).getValue(key);
            if (!thisValue.equals(compareValue)) {
                log.info(key + " " + thisValue + " <> " + compareValue);
                return false;
            }
        }
        return true;
    }

    protected Object getValue(T field) {
        if (field != null) {
            return DataUtil.getCastedValue(field.getType(), fields.get(field));
        }
        return null;
    }
}
