package pst.arm.server.modules.aiscontracts.dao;

import java.util.List;
import pst.arm.client.modules.aiscontracts.domain.AisContract;
import pst.arm.client.modules.aiscontracts.domain.AisContractRefTable;
import pst.arm.client.modules.aiscontracts.domain.AisContractStage;
import pst.arm.client.modules.aiscontracts.domain.AisOrganization;
import pst.arm.client.modules.aiscontracts.domain.search.AisContractSearchCondition;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface AisContractDAO {

    public List<AisContract> select(AisContractSearchCondition condition, Boolean isPaging);

    public List<AisContract> selectAll();

    public AisContract insert(AisContract domain);

    public boolean update(AisContract domain);

    public boolean delete(long id);

    public boolean saveWithDeleteContructNorm(AisContract domain);

    public AisContract selectRow(long id);

    public int count(AisContractSearchCondition condition);

    public AisContract create(AisContract domain);

    public AisContractStage createStage(AisContractStage stage);

    public Object getRefRecordId(AisContractRefTable ref, String value);

    public AisOrganization saveOrganisation(AisOrganization org);

    public Integer getContractIdByNum(String field);

    public AisOrganization getOrganisationByIdAis(String get);

    public AisOrganization updateOrganisation(AisOrganization org);

    public AisContract updateAisContract(AisContract domain);

    public AisContract getAisContractByIdAis(String field);

    public AisContractStage getAisContractStageByContractAndLine(Integer contractId, String field);

    public void updateStage(AisContractStage stage);

}
