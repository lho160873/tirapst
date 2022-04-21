package pst.arm.client.modules.aiscontracts.sync;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import pst.arm.client.modules.BasePageNew;
import pst.arm.client.modules.aiscontracts.domain.AisContractType;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class ContractsSyncPage extends BasePageNew {

    private AisContractType contractType;

    public ContractsSyncPage() {
    }

    public ContractsSyncPage(AisContractType aisContractType) {
        this.contractType = aisContractType;
    }

    @Override
    protected LayoutContainer getContentPanel() {
        return new ContractsSyncPanel(contractType);
    }
}
