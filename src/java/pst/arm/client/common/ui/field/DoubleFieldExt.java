package pst.arm.client.common.ui.field;


import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import pst.arm.client.common.lang.TypesConstants;

class Num {

    private Num() {
    }

    public Num(int precision, int scale) {
        this();
        this.precision = precision;
        this.scale = scale;
    }
    public int precision;
    public int scale;
}

public class DoubleFieldExt extends NumberField {

    private boolean requiredField = false;
    private TypesConstants typesConstants = GWT.create(TypesConstants.class);
    private Num num = null;

    public DoubleFieldExt() {
        this(false, new Num(17, 4) );
    }
    
    public DoubleFieldExt( int precision, int scale ) {
        this(false, new Num(precision, scale) );
    }

    public DoubleFieldExt(boolean requiredField, Num num) { 
        super();
        MessageBox.info("","DoubleFieldExt begin",null);
        setAllowNegative(false);
        setPropertyEditorType(Double.class);
        setAutoValidate(true);
        setInEditor(true);
        this.requiredField = requiredField;
        this.num = num;
        if (this.requiredField) {
            addListener(Events.OnBlur, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    validateRequiredField();
                }
            });
            addListener(Events.OnChange, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    validateRequiredField();
                }
            });
            addListener(Events.KeyUp, new KeyListener() {

                @Override
                public void componentKeyUp(ComponentEvent e) {
                    if (e.getKeyCode() == KeyCodes.KEY_TAB) {
                        validateRequiredField();
                    }
                }
            });
            MessageBox.info("","DoubleFieldExt end",null);
        }

        addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                validateValue();
            }
        });  
        
        addListener(Events.OnChange, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                validateValue();
            }
        });
        
        addListener(Events.KeyUp, new KeyListener() {

            @Override
            public void componentKeyUp(ComponentEvent e) {
                if (e.getKeyCode() == KeyCodes.KEY_TAB) {
                    validateValue();
                }
            }
        });
    }

    private Num findPrecitionScale(String number) {
        int precision = 0;
        int scale = 0;
        boolean flag = true;
        for (int i = 0; i < number.length(); ++i) {
            if (!number.substring(i, i + 1).equals(".")) {
                if (flag) {
                    ++precision;
                } else {
                    ++scale;
                }
            } else {
                flag = false;
            }
        }
        return new Num(precision, scale);
    }

    public void validateValue() {
        if (!getRawValue().isEmpty()) {
            boolean flag = false;
            try {
                Double a = Double.parseDouble(getRawValue().replace(',', '.'));
                Num n = findPrecitionScale(a.toString());
                if((n.precision > num.precision) || (n.scale > num.scale)){
                   forceInvalid(typesConstants.msgIncorrectValue() + " " + getRawValue());
                   return;
                } else {
                    clearInvalid();                    
                }
            } catch (NumberFormatException e) {
                flag = true;
            }
            if (flag) {
                forceInvalid(typesConstants.msgIncorrectValue() + " " + getRawValue());
            } else {
                clearInvalid();
            }
        } else {
            clearInvalid();
        }
    }

    public void validateRequiredField() {
        if (this.getRawValue().isEmpty()) {
            forceInvalid(typesConstants.msgRequiredField());
        } else {
            clearInvalid();
        }
    }

    @Override
    public NumberFieldMessages getMessages() {
        NumberFieldMessages msg = new NumberFieldMessages();
        if (this.getValue().doubleValue() < 0.00) {
            msg.setNegativeText(typesConstants.msgPositiveField());
            return msg;
        }
        return msg;
    }
}
