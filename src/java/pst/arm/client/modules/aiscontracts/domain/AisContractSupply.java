package pst.arm.client.modules.aiscontracts.domain;

import java.util.ArrayList;
import java.util.List;

public class AisContractSupply extends AisSyncObject<AisContractSupplyField> implements Instanciable<AisContractSupply>{
    public static final String TABLE = "CONTRACT_P_AIS";
    public static final String ID = "CONTRACTP_AIS_ID";
    private List<AisContractSupplyStage> stages = new ArrayList<AisContractSupplyStage>();

    public List<AisContractSupplyStage> getStages() {
        return stages;
    }

    public void setStages(List<AisContractSupplyStage> stages) {
        this.stages = stages;
    }

    public String asString() {
        if( getFields() != null ) {
            String str = getFields().get(AisContractSupplyField.LINK_);
            if( getId() != null ){
                str = "#"+getId() + " " + str;
            }
            return str;
        }
        return "";
    }

    @Override
    public String getPK() {
        return ID;
    }

    @Override
    public AisContractSupplyField[] getAvailableFields() {
        return AisContractSupplyField.values();
    }

    public boolean hasStages() {
        return getStages()!=null && !getStages().isEmpty();
    }

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public AisContractSupply newInstance() {
        return new AisContractSupply();
    }
}
