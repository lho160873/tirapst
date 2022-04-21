package pst.arm.client.common.ui.form;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;
import pst.arm.client.common.lang.CommonConstants;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public class VTypeValidator implements Validator {

  private VType type;
  
  public VTypeValidator(VType type){
    this.type = type;
  }
  @Override
  public String validate(Field<?> field, String value) {
        CommonConstants constants = GWT.create(CommonConstants.class);
    String res = null;
    if(!value.matches(type.regex)){
      res = type.name;// + constants.notValid();
    }
    return res;
  }

}