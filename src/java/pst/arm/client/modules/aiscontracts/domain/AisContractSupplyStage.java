package pst.arm.client.modules.aiscontracts.domain;

/**
 * Created by akozhin on 16.12.14.
 */
public class AisContractSupplyStage extends AisSyncObject<AisContractSupplyStageField> implements Instanciable<AisContractSupplyStage> {
    public static final String TABLE = "CONTRACT_P_STAGE_AIS";
    public static final String ID = "CONTRACT_P_STAGE_AIS_ID";

    protected Integer contractId;

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public String asString() {
        if (getFields() != null) {
            String str = getFields().get(AisContractSupplyStageField.LINK_);
            if (getId() != null) {
                str = "\t#" + getId() + " " + str + " - " + getFields().get(AisContractSupplyStageField.LINE_AIS);
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
    public AisContractSupplyStageField[] getAvailableFields() {
        return AisContractSupplyStageField.values();
    }

    public Integer getContractId() {
        return contractId;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    @Override
    public AisContractSupplyStage newInstance() {
        return new AisContractSupplyStage();
    }
}
