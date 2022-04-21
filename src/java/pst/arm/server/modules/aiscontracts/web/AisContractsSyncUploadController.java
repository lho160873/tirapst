package pst.arm.server.modules.aiscontracts.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pst.arm.client.modules.aiscontracts.domain.AisContract;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupply;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyAdditional;
import pst.arm.client.modules.aiscontracts.domain.AisContractType;
import pst.arm.client.modules.aiscontracts.domain.AisOrganization;
import pst.arm.client.modules.aiscontracts.domain.AisSyncResult;
import pst.arm.client.modules.aiscontracts.domain.SyncOperationType;
import pst.arm.server.modules.aiscontracts.service.AisContractService;
import pst.arm.server.modules.aiscontracts.service.AisSyncService;
import pst.arm.server.modules.aiscontracts.util.AisContractParser;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
@Controller
public class AisContractsSyncUploadController {

    @Autowired
    AisContractService service;
    @Autowired
    AisSyncService syncService;

    @RequestMapping(value = "/secure/aiscontractssyncupload.htm", method = RequestMethod.POST)
    public void handleFormUpload(
            @RequestParam("fileXml") MultipartFile fileXml,
            @RequestParam("contractType") String contractType,
            HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = null;
        try {
            StringBuilder result = new StringBuilder();
            AisContractType type = AisContractType.valueOf(contractType);
            if (type.equals(AisContractType.Service)) {
                //Organisations
                result.append(handleOrganisations(fileXml));
                //Contracts
                result.append(handleContracts(fileXml));
            } else if (type.equals(AisContractType.Supply)) {
                result.append(handleContractSupply(fileXml));
            } else if (type.equals(AisContractType.SupplyAdditional)) {
                result.append(handleContractSupplyAdditional(fileXml));
            }
            out = response.getWriter();
            out.write(result.toString());
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private String handleOrganisations(MultipartFile fileXml) throws IOException {

        List<AisOrganization> organisations = AisContractParser.getOrganisationsFromXml(new BOMInputStream(fileXml.getInputStream(), false));
        if (organisations != null && organisations.size() > 0) {
            StringBuilder log = new StringBuilder();
            Map<SyncOperationType, Integer> results = service.saveOrganisationsWithLookup(organisations, log);

            StringBuilder resultLog = new StringBuilder();
            resultLog.append("Всего организаций : ").append(organisations.size()).append("\n")
                    .append("Создано(+) : ").append(results.get(SyncOperationType.create)).append("\n")
                    .append("Обновлено(*) : ").append(results.get(SyncOperationType.update)).append("\n")
                    .append("Пропущено-без изменений(=) : ").append(results.get(SyncOperationType.equals)).append("\n")
                    .append("\n\n Лог операций:\n\n").append(log);
            return resultLog.toString();
        }
        return "Организаций не найдено.\n";
    }

    private String handleContracts(MultipartFile fileXml) throws IOException {
        List<AisContract> contractsToSave = new ArrayList<AisContract>();
        int deleted = 0;
        List<AisContract> contracts = AisContractParser.getContractsFromXml(new BOMInputStream(fileXml.getInputStream(), false));
        if (contracts != null && contracts.size() > 0) {
            for (AisContract contract : contracts) {
                if (contract.isPerformed() && !contract.isDeleted()) {
                    contractsToSave.add(contract);
                } else {
                    deleted++;
                }
            }

            StringBuilder log = new StringBuilder();
            List<Map<SyncOperationType, Integer>> results = service.saveContractsAndStagesWithLookup(contractsToSave, log);

            StringBuilder resultLog = new StringBuilder();
            resultLog.append("\n\nВсего договоров : ").append(contracts.size()).append("\n")
                    .append("Создано(+) : ").append(results.get(0).get(SyncOperationType.create)).append("\n")
                    .append("Обновлено(*) : ").append(results.get(0).get(SyncOperationType.update)).append("\n")
                    .append("Пропущено-без изменений(=) : ").append(results.get(0).get(SyncOperationType.equals)).append("\n\n")
                    .append("Пропущено(не проведен/удален) : ").append(deleted).append("\n\n")
                    .append("Всего этапов : ").append(getStageCount(contracts)).append("\n")
                    .append("Создано : ").append(results.get(1).get(SyncOperationType.create)).append("\n")
                    .append("Обновлено : ").append(results.get(1).get(SyncOperationType.update)).append("\n")
                    .append("Пропущено(без изменений) : ").append(results.get(1).get(SyncOperationType.equals)).append("\n")
                    .append("\n\n Лог операций:\n\n").append(log);

            return resultLog.toString();
        }
        return "Договоров не найдено.";
    }

    private Object getStageCount(List<AisContract> contracts) {
        int count = 0;
        for (AisContract contract : contracts) {
            count += contract.getStagesCount();
        }
        return count;
    }

    private String handleContractSupply(MultipartFile fileXml) throws IOException {
        List<AisContractSupply> contracts = AisContractParser.getContractsSupplyFromXml(new BOMInputStream(fileXml.getInputStream(), false));
        if (contracts != null && contracts.size() > 0) {
            AisSyncResult aisSyncResult = syncService.saveContractSupplyAndStagesWithLookup(contracts);
            return aisSyncResult.toString();
        }
        return "Договоров поставки не найдено";
    }

    private String handleContractSupplyAdditional(MultipartFile fileXml) throws IOException {
        List<AisContractSupplyAdditional> contracts = AisContractParser.getContractsSupplyAdditionalFromXml(new BOMInputStream(fileXml.getInputStream(), false));
        if (contracts != null && contracts.size() > 0) {
            AisSyncResult aisSyncResult = syncService.saveContractSupplyAdditionalAndStagesWithLookup(contracts);
            return aisSyncResult.toString();
        }
        return "Дополнительных соглашений для договоров поставки не найдено";
    }
}
