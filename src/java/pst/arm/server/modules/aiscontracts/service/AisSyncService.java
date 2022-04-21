package pst.arm.server.modules.aiscontracts.service;

import java.util.List;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupply;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyAdditional;
import pst.arm.client.modules.aiscontracts.domain.AisSyncResult;

public interface AisSyncService {

    public AisSyncResult saveContractSupplyAndStagesWithLookup(List<AisContractSupply> contracts);

    public AisSyncResult saveContractSupplyAdditionalAndStagesWithLookup(List<AisContractSupplyAdditional> contractList);
}
