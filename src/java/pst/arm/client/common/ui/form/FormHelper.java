package pst.arm.client.common.ui.form;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.form.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.List;


/**
 * Class helper wraps common method for work with any type of form fields.
 * Contains method for auto create and validate fields, to get right values with
 * null check, to set values.
 * <p/>
 * @author Alexandr Kozhin
 * @since 0.0.1
 */
public class FormHelper {
    
    public static final String NOT_EMPTY_SIGN = "<sup><font color='red'>*</font></sup>";
    public static int FORM_LABEL_WIDTH_DEFAULT = 200;
    public static int FORM_LABEL_PAD_DEFAULT = 15;
    public static final String DATETIME_FORMAT_DEFAULT = "dd.MM.yyyy";

    /**
     * Set field value with check on null. If value is null, field clears.
     * <p/>
     * @param <T> Form field type
     * @param field Form field
     * @param value Value to set
     */
    public static <T> void setFieldValue(Field<T> field, T value) {
        if (value != null) {
            if (field instanceof LabelField && value instanceof String) {
                String newVal = ((String) value).replaceAll("\n", "<br />").replaceAll("\r", "");
//                field.setValue((T) newVal);
                ((LabelField) field).setText(newVal);
            } else {
                field.setValue(value);
            }
        } else {
            field.clear();
        }
    }
    
    public static <T> void setFieldRawValue(Field<T> field, String value) {
        if (value != null) {
            field.setRawValue(value);
        } else {
            field.clear();
        }
    }
    
    public static String getFieldRawValue(Field<?> field) {
        if (field.getRawValue() != null && !field.getRawValue().trim().isEmpty()) {
            return field.getRawValue().trim();
        }
        return null;
    }

    /**
     * Validate list of fields. Set field focus if field value is invalid.
     * <p/>
     * @param fields List of fields to validate
     * <p/>
     * @return True if all fields values are valid
     */
    public static Boolean isValid(List<Field<?>> fields) {
        for (Field<?> field : fields) {
            Boolean valid = field.validate();
            if (!valid) {
                field.focus();
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
    
    public static LabelField createLabelField(String label) {
        return createLabelField(label, null);
    }

    /**
     * Create FieldLable by label and value
     * <p/>
     * @param label Field label
     * @param value Field value
     * <p/>
     * @return LabelField object with predefined properties
     */
    public static LabelField createLabelField(String label, Object value) {
        LabelField field = new LabelField() {
            private String replaceEnters(String value) {
                if (value != null) {
                    
                    return value.replaceAll("\n", "<br />").replaceAll("\r", "");
                }
                
                return null;
            }
            
            @Override
            public void setValue(Object value) {
                if (value instanceof String) {
                    super.setValue(replaceEnters((String) value));
                } else {
                    super.setValue(value);
                }
            }
            
            @Override
            public void setText(String text) {
                super.setText(replaceEnters(text));
            }
            
            @Override
            protected void onResize(int width, int height) {
                setStyleAttribute("width", "100%");
                super.onResize(width, height);
            }
        };
        field.setFieldLabel(label);
        setLabelFieldProperties(field);
        if (value != null && !value.toString().isEmpty()) {
            field.setValue(value.toString());
            setFieldValue(field, value);
        }
        
        return field;
    }

    /**
     * Create TextField with predefined properties
     * <p/>
     * @param label
     * @param allowBlank
     * @param maxLength
     * @param value
     * <p/>
     * @return
     */
    public static TextField<String> createTextField(String label, Boolean allowBlank, Integer maxLength, String value) {
        TextField<String> textField = new TextField<String>();
        textField.setValidateOnBlur(Boolean.TRUE);
        textField.setFieldLabel(label);
        if (allowBlank != null) {
            textField.setAllowBlank(allowBlank);
            if (!allowBlank) {
                setNotEmptyFieldLabel(label, textField);
            }
        }
        if (maxLength != null) {
            textField.setMaxLength(maxLength);
        }
        setFieldValue(textField, value);
        return textField;
    }

    /**
     * Method to set LabelField properties to work right with scrolling
     * <p/>
     * @param field LabelField object
     */
    public static void setLabelFieldProperties(LabelField field) {
        field.setLabelSeparator(":");
        //If in development mode - create white border and hide it for scrolling works fine
        if (!GWT.isScript()) {
            field.setBorders(Boolean.TRUE);
            field.setStyleAttribute("border-color", "white");
        }
    }

    /**
     * Get integer value from number field
     * <p/>
     * @param field Number field objectsetNotEmptyFieldLabel
     * <p/>
     * @return Integer value
     */
    public static Integer getIntValue(NumberField field) {
        return (field.getValue() == null) ? null : field.getValue().intValue();
    }
    
    public static Double getDoubleValue(NumberField field) {
        return (field.getValue() == null) ? null : field.getValue().doubleValue();
    }
    
    public static void setNotEmptyFieldLabel(String lable, Field<?> field) {
        field.setFieldLabel(lable + NOT_EMPTY_SIGN);
    }
    
    public static void setFieldLabelAllowBlank(Field<?> field, Boolean allow) {
        if (allow) {
            field.setFieldLabel(field.getFieldLabel().replace(NOT_EMPTY_SIGN, ""));
        } else {
            if (!field.getFieldLabel().contains(NOT_EMPTY_SIGN)) {
                field.setFieldLabel(field.getFieldLabel() + NOT_EMPTY_SIGN);
            }
        }
    }
    
    public static void clearState(List<Field<?>> fields) {
        for (Field<?> field : fields) {
            field.clearInvalid();
            field.clearState();
        }
    }
    
    public static boolean isObjectsNull(Object... params) {
        for (Object object : params) {
            if (object != null) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isObjectsNotNull(Object... params) {
        for (Object object : params) {
            if (object == null) {
                return false;
            }
        }
        return true;
    }
    
    public static void clearFields(List<Field<?>> fields) {
        for (Field<?> field : fields) {
            field.clear();
        }
    }
    
    public static LayoutContainer createFormPanel() {
        FormLayout layout = new FormLayout(FormPanel.LabelAlign.RIGHT);
        layout.setLabelWidth(FORM_LABEL_WIDTH_DEFAULT);
        layout.setLabelPad(FORM_LABEL_PAD_DEFAULT);
        
        LayoutContainer panel = new LayoutContainer(layout);
        panel.setStyleAttribute("paddingTop", "5");
        panel.setScrollMode(Style.Scroll.AUTOY);
        return panel;
    }
    
    public static DateField createDateField(String label, Boolean allowBlank) {
        DateField df = new DateField();
        df.setFieldLabel(label);
        df.setAllowBlank(allowBlank);
        df.getPropertyEditor().setFormat(DateTimeFormat.getFormat(DATETIME_FORMAT_DEFAULT));
        df.setValidator(new Validator() {
            @Override
            public String validate(Field<?> field, String value) {   
                if ( value.length()>6 && value.length() < 10 ) {
                    return value+" недопустимая дата - она должна быть в формате "+DATETIME_FORMAT_DEFAULT.toUpperCase();
                } 
                return null;
            }
        });
        df.setWidth(30);
        return df;
    }
    
    public static DateField createDateField(String appRegDateFrom) {
        return createDateField(appRegDateFrom, Boolean.TRUE);
    }
    
    public static TextField<String> createTextField(String label) {
        TextField<String> textField = new TextField<String>();
        textField.setValidateOnBlur(Boolean.TRUE);
        textField.setFieldLabel(label);
        return textField;
    }
    
    public static NumberField createNumberField(String label, Boolean allowBlank, Class<?> type) {
        NumberField textField = new NumberField();
        textField.setValidateOnBlur(Boolean.TRUE);
        textField.setEmptyText(null);
        textField.setFieldLabel(label);
        if (allowBlank != null) {
            textField.setAllowBlank(allowBlank);
            if (!allowBlank) {
                setNotEmptyFieldLabel(label, textField);
            }
        }
        textField.setPropertyEditorType(type);
        return textField;
    }
    
    public static NumberField createNumberField(String label, Boolean allowBlank, Class<?> type, Number maxValue) {
        NumberField textField = createNumberField(label, allowBlank, type);
        textField.setMaxValue(maxValue);
        
        return textField;
    }
    
    public static NumberField createNumberField(String label, Boolean allowBlank, Class<?> type, Boolean allowNegative, Boolean allowDecimals) {
        NumberField textField = createNumberField(label, allowBlank, type);
        textField.setAllowNegative(allowNegative);
        textField.setAllowDecimals(allowDecimals);
        return textField;
    }
    
    public static NumberField createNumberField(String label, Boolean allowBlank, Class<?> type, Boolean allowNegative, Boolean allowDecimals, Number maxValue) {
        NumberField textField = createNumberField(label, allowBlank, type);
        textField.setAllowNegative(allowNegative);
        textField.setAllowDecimals(allowDecimals);
        textField.setMaxValue(maxValue);
        return textField;
    }
    
    public static Double getDoubleValue(NumberField nfWeight, Double defaultValue) {
        Double val = getDoubleValue(nfWeight);
        return val == null ? defaultValue : val;
    }
    
    public static ContentPanel getContentPanel() {
        ContentPanel panel = new ContentPanel();
        panel.setHeaderVisible(false);
        panel.setBodyBorder(false);
        panel.setBorders(false);
        panel.setFrame(true);
//        panel.setStyleAttribute("background-color", "#DFE8F6");
        
        return panel;
    }
    
    public static FieldSet getFieldSet(String header) {
        FieldSet fs = new FieldSet();
        fs.setLayout(new FitLayout());
        fs.setHeading(header);
        fs.setScrollMode(Style.Scroll.AUTOY);
        return fs;
    }
    
    public static TabItem getTabItem(String text){
    TabItem item = new TabItem();
        item.setClosable(false);
        item.setBorders(false);
        
        item.setText(text);
        item.setLayout(new FitLayout());
        item.setStyleAttribute("background-color", "#DFE8F6");
        return item;
    }

    public static void setReadOnlyProp(Field field, Boolean isReadOnly) {
        field.setReadOnly(isReadOnly);
        if (isReadOnly) {
            field.setInputStyleAttribute("color", "gray");
            field.setInputStyleAttribute("background-image", "none");
            field.setInputStyleAttribute("background-color", "#f2f6fb");
        }
        else
        {
            field.setInputStyleAttribute("color", "black");
            field.setInputStyleAttribute("background-color", "white");
        }
    }
    
}
