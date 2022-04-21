package pst.arm.client.modules.aiscontracts.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import pst.arm.client.modules.aiscontracts.util.FieldsUtil;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class AisOrganization implements Serializable {

    private Integer id;
    private Map<Field, String> fieldValues;

    public AisOrganization() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    public Object getValue(Field key) {
        return FieldsUtil.getValue(key.type, fieldValues.get(key));
    }

    public String getField(Field key) {
        return fieldValues.get(key);
    }

    public enum Field {

        id("Код", "ID_AIS", 9),
        fullName("ПолноеНаименование", "FULL_NAME", 100),
        shortName("Наименование", "SHORT_NAME", 30),
        enabled("", "ENABLED", FieldType.Integer),
        signCustomer("", "SIGN_CUST", FieldType.Integer),
        signExecutor("", "SIGN_EXEC", FieldType.Integer),
        company("", "COMPANY_ID", FieldType.Integer),
        address("Адрес", "ADDRESS", 150),
        inn("ИНН", "INN", 12),
        cpp("КПП", "CPP", 9),
        site("сайт", "SAIT", 30);
        private String name, column;
        private FieldType type = FieldType.String;
        private Integer maxLength;

        Field(String name, String column) {
            this.name = name;
            this.column = column;
        }

        Field(String name, String column, Integer maxLength) {
            this.name = name;
            this.column = column;
            this.maxLength = maxLength;
        }

        Field(String name, String column, FieldType type) {
            this.name = name;
            this.column = column;
            this.type = type;
        }

        public static Field fromName(String name) {
            for (Field field : values()) {
                if (field.name.equals(name)) {
                    return field;
                }
            }
            return null;
        }

        public String getColumn() {
            return column;
        }

        public FieldType getType() {
            return type;
        }

        public Integer getMaxLength() {
            return maxLength;
        }

        private String getName() {
            return name;
        }

        public boolean isString() {
            return type.equals(FieldType.String);
        }

    }

    public Map<Field, String> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(Map<Field, String> fieldValues) {
        this.fieldValues = fieldValues;
    }

    public void addFieldValue(Field name, String value) {
        if (fieldValues == null) {
            fieldValues = new HashMap<Field, String>();
        }
        fieldValues.put(name, value);
    }

    public String asString() {
        String s = "";
        if (id != null) {
            s += "#" + id.toString() + "-";
        }
        if (fieldValues.containsKey(Field.id)) {
            s += fieldValues.get(Field.id) + " ";
        }
        if (fieldValues.get(Field.fullName) != null && !fieldValues.get(Field.fullName).isEmpty()) {
            s += fieldValues.get(Field.fullName);
        } else if (fieldValues.get(Field.shortName) != null && !fieldValues.get(Field.shortName).isEmpty()) {
            s += fieldValues.get(Field.shortName);
        }
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AisOrganization) {
            for (Map.Entry<Field, String> entrySet : fieldValues.entrySet()) {
                AisOrganization.Field key = entrySet.getKey();
                if (key.name.isEmpty()) {
                    continue;
                }
                String thisValue = entrySet.getValue();
                String compareValue = ((AisOrganization) obj).getFieldValues().get(key);
                if (key.isString() && compareValue != null && !compareValue.isEmpty() && compareValue.length() > key.getMaxLength()) {
                    compareValue = compareValue.substring(0, key.getMaxLength());
                }
                if (!thisValue.equals(compareValue)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 43 * hash + (this.fieldValues != null ? this.fieldValues.hashCode() : 0);
        return hash;
    }

}
