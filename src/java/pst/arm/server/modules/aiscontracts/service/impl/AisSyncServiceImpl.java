package pst.arm.server.modules.aiscontracts.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupply;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyAdditional;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyAdditionalField;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyAdditionalStage;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyField;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyStage;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyStageField;
import pst.arm.client.modules.aiscontracts.domain.AisSyncObject;
import pst.arm.client.modules.aiscontracts.domain.AisSyncResult;
import pst.arm.client.modules.aiscontracts.domain.Field;
import pst.arm.client.modules.aiscontracts.domain.Instanciable;
import pst.arm.client.modules.aiscontracts.domain.SyncOperationType;
import pst.arm.server.modules.aiscontracts.dao.AisSyncDAO;
import pst.arm.server.modules.aiscontracts.service.AisSyncService;

@Service
public class AisSyncServiceImpl implements AisSyncService {

    @Autowired
    private AisSyncDAO dao;

    @Override
    public AisSyncResult saveContractSupplyAndStagesWithLookup(List<AisContractSupply> contracts) {
        AisSyncResult aisSyncResult = new AisSyncResult();
        aisSyncResult.addSyncType(AisContractSupply.class);
        aisSyncResult.addSyncType(AisContractSupplyStage.class);

        for (AisContractSupply contract : contracts) {
            AisContractSupply contractSupply = createOrUpdate(contract, aisSyncResult, AisContractSupplyField.LINK_);
            if (contract.hasStages() && contractSupply != null) {
                for (AisContractSupplyStage stage : contract.getStages()) {
                    stage.setContractId(contractSupply.getId());
                    createOrUpdate(stage, aisSyncResult, AisContractSupplyStageField.LINK_, AisContractSupplyStageField.LINE_AIS);
                }
            }
        }
        return aisSyncResult;
    }

    @Override
    public AisSyncResult saveContractSupplyAdditionalAndStagesWithLookup(List<AisContractSupplyAdditional> contracts) {
        AisSyncResult aisSyncResult = new AisSyncResult();
        aisSyncResult.addSyncType(AisContractSupplyAdditional.class);
        aisSyncResult.addSyncType(AisContractSupplyAdditionalStage.class);

        for (AisContractSupplyAdditional contract : contracts) {
            AisContractSupplyAdditional contractSupply = createOrUpdate(contract, aisSyncResult, AisContractSupplyAdditionalField.LINK_);
            if (contract.hasStages() && contractSupply != null) {
                for (AisContractSupplyAdditionalStage stage : contract.getStages()) {
                    stage.setContractId(contractSupply.getId());
                    createOrUpdate(stage, aisSyncResult, AisContractSupplyStageField.LINE_AIS, AisContractSupplyStageField.LINK_);
                }
            }
        }
        return aisSyncResult;
    }

    private <T extends AisSyncObject & Instanciable> T createOrUpdate(T object, AisSyncResult result, Field... fields) {
        T createdObject = dao.get(object, fields);
        SyncOperationType operation;
        if (createdObject == null) {
            createdObject = dao.create(object);
            operation = SyncOperationType.create;
        } else {
            if (!object.equals(createdObject)) {
                object.setId(createdObject.getId());
                createdObject = dao.update(object);
                operation = SyncOperationType.update;
            } else {
                operation = SyncOperationType.equals;
            }
        }
        if (createdObject != null) {
            result.addResult(createdObject.getClass(), operation, createdObject.asString());
        }
        return createdObject;
    }
}
