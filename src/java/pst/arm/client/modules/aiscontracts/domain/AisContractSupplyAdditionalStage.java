package pst.arm.client.modules.aiscontracts.domain;

public class AisContractSupplyAdditionalStage extends AisContractSupplyStage{

    public AisContractSupplyAdditionalStage() {
    }

    public AisContractSupplyAdditionalStage(AisContractSupplyStage stage) {
        fields = stage.fields;
    }

    @Override
    public String getTableName() {
        return "CONTRACT_DP_STAGE_AIS";
    }

    @Override
    public String getPK() {
        return "CONTRACT_DP_STAGE_AIS_ID";
    }

    @Override
    public AisContractSupplyAdditionalStage newInstance() {
        return new AisContractSupplyAdditionalStage();
    }
}
