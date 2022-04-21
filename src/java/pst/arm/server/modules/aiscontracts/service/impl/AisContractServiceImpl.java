package pst.arm.server.modules.aiscontracts.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import static org.apache.commons.lang.StringUtils.isBlank;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.aiscontracts.domain.AisContract;
import pst.arm.client.modules.aiscontracts.domain.AisContractField;
import pst.arm.client.modules.aiscontracts.domain.AisContractStage;
import pst.arm.client.modules.aiscontracts.domain.AisOrganization;
import pst.arm.client.modules.aiscontracts.domain.SyncOperationType;
import pst.arm.client.modules.aiscontracts.domain.search.AisContractSearchCondition;
import pst.arm.server.modules.aiscontracts.dao.AisContractDAO;
import pst.arm.server.modules.aiscontracts.service.AisContractService;
import static pst.arm.server.utils.ServerUtil.isNotEmpty;
import static pst.arm.server.utils.ServerUtil.isNotNull;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
@Service
public class AisContractServiceImpl implements AisContractService {

    private AisContractDAO dao;
    private MessageSource messageSource;
    private static Logger log = Logger.getLogger("AisContractServiceImpl");

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setDao(AisContractDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<AisContract> getAllContract() {
        List<AisContract> list = dao.selectAll();
        return list;
    }

    @Override
    public List<AisContract> getContract(AisContractSearchCondition condition, Boolean isPaging) {
        List<AisContract> listClientCard = dao.select(condition, isPaging);

        return listClientCard;
    }

    @Override
    public int getContractCount(AisContractSearchCondition condition) {
        return dao.count(condition);
    }

    @Override
    public AisContract saveContract(AisContract domain, Boolean isNew) {
        if (isNew) {
            return dao.insert(domain);
        } else {
            dao.update(domain);
            return domain;
        }
    }

    @Override
    public Boolean deleteContract(List<Integer> ids) {
        //throw new UnsupportedOperationException("Not supported yet.");
        for (Integer id : ids) {
            dao.delete(id);
            // dictionaryContentService.deleteRow(id.longValue());
        }
        return true;
    }

    @Override
    public AisContract getContract(Long id) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return dao.selectRow(id);
    }

    @Override
    public AisContract createWithStages(AisContract contract) {
        //Данные по умолчанию
        contract.addFieldValue(AisContractField.contractType, "2");
        AisContract createdContract = dao.getAisContractByIdAis(contract.getField(AisContractField.id));
        prepareAisContract(contract);
        if (createdContract == null) {
            createdContract = dao.create(contract);
            log.info("[+] " + contract.asString(false));
        } else {
            if (!contract.equals(createdContract)) {
                contract.setContractId(createdContract.getContractId());
                createdContract = dao.updateAisContract(contract);
                log.info("[>] " + contract.asString(false));
            } else {
                log.info("[=] " + contract.asString(false));
            }
        }
        if (isNotEmpty(contract.getStages())) {
            for (AisContractStage stage : contract.getStages()) {
                stage.setContractId(createdContract.getContractId());
                prepareStage(stage);
                AisContractStage createdStage = dao.getAisContractStageByContractAndLine(createdContract.getContractId(), stage.getField(AisContractStage.Field.lineNumber));
                if (createdStage == null) {
                    dao.createStage(stage);
                    log.info(" [+] " + stage.asString());
                } else {
                    if (!stage.equals(createdStage)) {
                        stage.setStageId(createdStage.getStageId());
                        dao.updateStage(stage);
                        log.info(" [>]" + stage.asString());
                    } else {
                        log.info(" [=]" + stage.asString());
                    }
                }
            }
        }
        return createdContract;
    }

    /**
     * Подготовка к сохранению - поиск значений в связанных таблицах
     *
     * @param domain
     * @return
     */
    private AisContract prepareAisContract(AisContract domain) {
        for (Map.Entry<AisContractField, String> field : domain.getFieldValues().entrySet()) {
            if (isNotNull(field.getKey().getRef()) && !isBlank(field.getValue())) {
                Object id = dao.getRefRecordId(field.getKey().getRef(), field.getValue());
                if (isNotNull(id)) {
                    domain.addFieldValue(field.getKey(), id.toString());
                } else {
                    domain.addFieldValue(field.getKey(), null);
                }
            }
        }
        //Заполняем родителя для DopDogovor
        if (StringUtils.isNotBlank(domain.getField(AisContractField.parentContractNum))) {
            Integer id = dao.getContractIdByNum(domain.getField(AisContractField.parentContractNum));
            domain.getFieldValues().remove(AisContractField.parentContractNum);
            if (id != null) {
                domain.addFieldValue(AisContractField.parentContractNum, id.toString());
            }
        }
        return domain;
    }

    @Override
    public AisOrganization saveOrganisation(AisOrganization org) {
        AisOrganization existedOrganization = dao.getOrganisationByIdAis(org.getFieldValues().get(AisOrganization.Field.id));
        if (existedOrganization == null) {
            AisOrganization savedOrganisation = dao.saveOrganisation(org);
            log.info("[+]" + savedOrganisation.asString());
            return savedOrganisation;
        } else {
            if (!org.equals(existedOrganization)) {
                org.setId(existedOrganization.getId());
                AisOrganization updatedOrganisation = dao.updateOrganisation(org);
                log.info("[>]" + updatedOrganisation.asString());
                return updatedOrganisation;
            } else {
                log.info("[=]" + org.asString());
                return org;
            }
        }
    }

    private void prepareStage(AisContractStage stage) {
        if (stage.getFieldValues() != null
                && !stage.getFieldValues().isEmpty()) {
            if (isBlank(stage.getFieldValues().get(AisContractStage.Field.stageNumber))) {
                stage.getFieldValues().remove(AisContractStage.Field.stageNumber);
                stage.getFieldValues().put(AisContractStage.Field.stageNumber, "Нет номера");
            }
            if (isBlank(stage.getFieldValues().get(AisContractStage.Field.stageName))) {
                stage.getFieldValues().remove(AisContractStage.Field.stageName);
                stage.getFieldValues().put(AisContractStage.Field.stageName, "Нет наименования");
            }
        }
    }

    @Override
    public Map<SyncOperationType, Integer> saveOrganisationsWithLookup(List<AisOrganization> organisations, StringBuilder log) {
        //Prepare
        Map<SyncOperationType, Integer> result = new HashMap<SyncOperationType, Integer>();
        int created = 0;
        int updated = 0;
        int skipped = 0;
        String msg;

        //Iterate
        for (AisOrganization org : organisations) {
            AisOrganization existedOrganization = dao.getOrganisationByIdAis(org.getFieldValues().get(AisOrganization.Field.id));
            if (existedOrganization == null) {
                AisOrganization savedOrganisation = dao.saveOrganisation(org);
                msg = "+ " + savedOrganisation.asString();
                created++;
            } else {
                if (!org.equals(existedOrganization)) {
                    org.setId(existedOrganization.getId());
                    AisOrganization updatedOrganisation = dao.updateOrganisation(org);
                    msg = "* " + updatedOrganisation.asString();
                    updated++;
                } else {
                    msg = "= " + org.asString();
                    skipped++;
                }
            }
            log.append(msg).append("\n");
        }
        result.put(SyncOperationType.create, created);
        result.put(SyncOperationType.update, updated);
        result.put(SyncOperationType.equals, skipped);
        return result;
    }

    @Override
    public List<Map<SyncOperationType, Integer>> saveContractsAndStagesWithLookup(List<AisContract> contracts, StringBuilder log) {
        //Prepare vars
        String msg;
        int contractCreated = 0;
        int contractUpdated = 0;
        int contractSkipped = 0;
        int stageCreated = 0;
        int stageUpdated = 0;
        int stageSkipped = 0;

        for (AisContract contract : contracts) {
            //Данные по умолчанию
            contract.addFieldValue(AisContractField.contractType, "2");
            AisContract createdContract = dao.getAisContractByIdAis(contract.getField(AisContractField.id));
            prepareAisContract(contract);
            if (createdContract == null) {
                createdContract = dao.create(contract);
                msg = "+ " + contract.asString(false);
                contractCreated++;
            } else {
                if (!contract.equals(createdContract)) {
                    contract.setContractId(createdContract.getContractId());
                    createdContract = dao.updateAisContract(contract);
                    msg = "* " + contract.asString(false);
                    contractUpdated++;
                } else {
                    msg = "= " + contract.asString(false);
                    contractSkipped++;
                }
            }
            log.append(msg).append("\n");
            if (isNotEmpty(contract.getStages())) {
                for (AisContractStage stage : contract.getStages()) {
                    stage.setContractId(createdContract.getContractId());
                    prepareStage(stage);
                    AisContractStage createdStage = dao.getAisContractStageByContractAndLine(createdContract.getContractId(), stage.getField(AisContractStage.Field.lineNumber));
                    if (createdStage == null) {
                        dao.createStage(stage);
                        msg = "\t+ " + stage.asString();
                        stageCreated++;
                    } else {
                        if (!stage.equals(createdStage)) {
                            stage.setStageId(createdStage.getStageId());
                            dao.updateStage(stage);
                            msg = "\t* " + stage.asString();
                            stageUpdated++;
                        } else {
                            msg = "\t= " + stage.asString();
                            stageSkipped++;
                        }
                    }
                    log.append(msg).append("\n");
                }
            }
            log.append("\n");
        }
        List<Map<SyncOperationType, Integer>> list = new ArrayList<Map<SyncOperationType, Integer>>(2);
        Map<SyncOperationType, Integer> result = new HashMap<SyncOperationType, Integer>();
        result.put(SyncOperationType.create, contractCreated);
        result.put(SyncOperationType.update, contractUpdated);
        result.put(SyncOperationType.equals, contractSkipped);
        list.add(result);

        result = new HashMap<SyncOperationType, Integer>();
        result.put(SyncOperationType.create, stageCreated);
        result.put(SyncOperationType.update, stageUpdated);
        result.put(SyncOperationType.equals, stageSkipped);
        list.add(result);
        return list;
    }
}
