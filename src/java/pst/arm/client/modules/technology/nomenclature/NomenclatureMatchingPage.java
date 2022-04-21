package pst.arm.client.modules.technology.nomenclature;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import pst.arm.client.modules.BasePageNew;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class NomenclatureMatchingPage extends BasePageNew {
    @Override
    protected LayoutContainer getContentPanel() {        
        return new NomenclatureMatchingPanel();
    }
}