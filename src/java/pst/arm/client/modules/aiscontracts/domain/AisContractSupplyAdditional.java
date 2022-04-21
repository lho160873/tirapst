package pst.arm.client.modules.aiscontracts.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akozhin on 23.12.14.
 */
public class AisContractSupplyAdditional extends AisSyncObject<AisContractSupplyAdditionalField> implements Instanciable<AisContractSupplyAdditional>{
    public static final String TABLE = "CONTRACT_DP_AIS";
    public static final String ID = "CONTRACTDP_AIS_ID";
    private List<AisContractSupplyAdditionalStage> stages = new ArrayList<AisContractSupplyAdditionalStage>();
    public List<AisContractSupplyAdditionalStage> getStages() {
        return stages;
    }
   public void setStages(List<AisContractSupplyAdditionalStage> stages) {
        this.stages = stages;
    }
    public boolean hasStages() {
        return getStages()!=null && !getStages().isEmpty();
    }
    public String asString() {
        if( getFields() != null ) {
            String str = getFields().get(AisContractSupplyAdditionalField.LINK_);
            if( getId() != null ){
                str = "#"+getId() + " " + str;
            }
            return str;
        }
        return "";
    }
    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public String getPK() {
        return ID;
    }

    @Override
    public AisContractSupplyAdditionalField[] getAvailableFields() {
        return AisContractSupplyAdditionalField.values();
    }

    @Override
    public AisContractSupplyAdditional newInstance() {
        return new AisContractSupplyAdditional();
    }
}
