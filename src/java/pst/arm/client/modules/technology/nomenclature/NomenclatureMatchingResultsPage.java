package pst.arm.client.modules.technology.nomenclature;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.technology.nomenclature.ui.NomenclatureMatchingResults;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class NomenclatureMatchingResultsPage extends BasePageNew {

    @Override
    protected LayoutContainer getContentPanel() {
        return new NomenclatureMatchingResults();
    }
}
