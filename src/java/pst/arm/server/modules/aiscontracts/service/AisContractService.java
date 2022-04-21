package pst.arm.server.modules.aiscontracts.service;

import java.util.List;
import java.util.Map;
import pst.arm.client.modules.aiscontracts.domain.AisContract;
import pst.arm.client.modules.aiscontracts.domain.AisOrganization;
import pst.arm.client.modules.aiscontracts.domain.SyncOperationType;
import pst.arm.client.modules.aiscontracts.domain.search.AisContractSearchCondition;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public interface AisContractService {

    public List<AisContract> getContract(AisContractSearchCondition condition, Boolean isPaging);

    public List<AisContract> getAllContract();

    public int getContractCount(AisContractSearchCondition condition);

    public AisContract saveContract(AisContract domain, Boolean isNew);

    public AisContract getContract(Long id);

    public Boolean deleteContract(List<Integer> ids);

    public AisContract createWithStages(AisContract domain);

    public AisOrganization saveOrganisation(AisOrganization org);

    public Map<SyncOperationType, Integer> saveOrganisationsWithLookup(List<AisOrganization> organisations, StringBuilder log);

    public List<Map<SyncOperationType, Integer>> saveContractsAndStagesWithLookup(List<AisContract> contracts, StringBuilder log);

}
