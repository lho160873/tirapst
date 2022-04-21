package pst.arm.server.modules.aiscontracts.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.input.BOMInputStream;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import pst.arm.client.modules.aiscontracts.domain.AisContract;
import pst.arm.client.modules.aiscontracts.domain.AisContractField;
import pst.arm.client.modules.aiscontracts.domain.AisContractStage;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupply;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyAdditional;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyAdditionalField;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyAdditionalStage;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyField;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyStage;
import pst.arm.client.modules.aiscontracts.domain.AisContractSupplyStageField;
import pst.arm.client.modules.aiscontracts.domain.AisOrganization;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class AisContractParser {

    private static final String WRONG_DATE = "01.01.0001 0:00:00";
    private static final String DEFAULT_DATE = "01.01.1900 0:00:00";

    private static Logger log = Logger.getLogger("AisContractParser");

    public static List<AisContract> getContractsFromXml(InputStream is) {
        return getContractsFromXml(is, Integer.MAX_VALUE);
    }

    public static List<AisContract> getContractsFromXml(InputStream is, int loops) {
        List<AisContract> contracts = new ArrayList<AisContract>();
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            Document document = reader.read(new InputStreamReader(is, "UTF-8"));
            for (String section : new String[]{"Dogovor", "DopDogovor"}) {
                List list = document.selectNodes("//" + section);
                Iterator iterator = list.iterator();
                int i = 0;
                while (iterator.hasNext()) {
                    if (i > loops) {
                        break;
                    }
                    i++;

                    Element dogovor = (Element) iterator.next();
                    AisContract contract = new AisContract();
                    for (Iterator poleIterator = dogovor.elementIterator("pole"); poleIterator.hasNext();) {
                        Element pole = (Element) poleIterator.next();
                        AisContractField field = AisContractField.fromName(pole.attributeValue("name"));
                        String value = pole.attributeValue("value");

                        value = prepareValue(field, value);
                        if (isNotBlank(value)) {
                            contract.addFieldValue(field, value);
                        }

                    }

                    for (Iterator poleIterator = dogovor.element("steps").elementIterator("step"); poleIterator.hasNext();) {
                        Element step = (Element) poleIterator.next();
                        AisContractStage stage = new AisContractStage();
                        for (Iterator stepIterator = step.elementIterator("pole"); stepIterator.hasNext();) {
                            Element pole = (Element) stepIterator.next();
                            AisContractStage.Field field = AisContractStage.Field.fromName(pole.attributeValue("name"));
                            String value = pole.attributeValue("value");

                            value = prepareValue(field, value);
                            if (field != null && isNotBlank(value)) {
                                stage.addFieldValue(field, value);
                            }
                        }
                        contract.addStage(stage);
                    }
                    if (isBlank(contract.getFieldValues().get(AisContractField.contractNum))) {
                        contract.getFieldValues().remove(AisContractField.contractNum);
                        contract.getFieldValues().put(AisContractField.contractNum, "Без номера");
                    }

                    if (section.equals("DopDogovor")) {
                        replaceField(contract.getFieldValues(), AisContractField.contractNum, AisContractField.contractNumDop);
                        if (isBlank(contract.getFieldValues().get(AisContractField.agreementNum))) {
                            contract.getFieldValues().remove(AisContractField.agreementNum);
                            contract.getFieldValues().put(AisContractField.agreementNum, "Без номера");
                        }
                    }

                    contracts.add(contract);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return contracts;
    }

    public static List<AisOrganization> getOrganisationsFromXml(InputStream is) {
        return getOrganisationsFromXml(is, Integer.MAX_VALUE);
    }

    public static List<AisOrganization> getOrganisationsFromXml(InputStream is, int loops) {
        List<AisOrganization> organisations = new ArrayList<AisOrganization>();
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            Document document = reader.read(new InputStreamReader(is, "UTF-8"));
            List list = document.selectNodes("//firma");
            Iterator iterator = list.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                if (i > loops) {
                    break;
                }
                i++;
                Element dogovor = (Element) iterator.next();
                AisOrganization org = new AisOrganization();
                for (Iterator poleIterator = dogovor.elementIterator("pole"); poleIterator.hasNext();) {
                    Element pole = (Element) poleIterator.next();
                    AisOrganization.Field field = AisOrganization.Field.fromName(pole.attributeValue("name"));
                    String value = pole.attributeValue("value");
                    if (field != null && isNotBlank(value)) {
                        if (field.isString() && value.length() > field.getMaxLength()) {
                            value = value.substring(0, field.getMaxLength());
                        }
                        org.addFieldValue(field, value);
                    }
                }
                if (org.getFieldValues() != null
                        && !org.getFieldValues().isEmpty()) {
                    org.addFieldValue(AisOrganization.Field.enabled, "1");
                    org.addFieldValue(AisOrganization.Field.signCustomer, "1");
                    org.addFieldValue(AisOrganization.Field.signExecutor, "0");
                    org.addFieldValue(AisOrganization.Field.company, "1");

                }
                organisations.add(org);
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return organisations;
    }

    private static void replaceField(Map<AisContractField, String> map, AisContractField from, AisContractField to) {
        if (map.containsKey(from)) {
            String fromVal = map.get(from);
            map.remove(from);
            map.put(to, fromVal);
        }
    }

    private static String prepareValue(AisContractField field, String value) {
        if (field != null && isNotBlank(value)) {
            switch (field.getType()) {
                case String:
                    if (value.length() > field.getMaxLength()) {
                        value = value.substring(0, field.getMaxLength());
                    }
                    break;
                case Double:
                    value = value.replaceAll("[^\\d,]", "").replaceAll(",", ".");
                    break;
                case Date:
                    if (value.equals(WRONG_DATE)) {
                        value = DEFAULT_DATE;
                    }
                    break;
            }
            return value;
        }
        return null;
    }

    private static String prepareValue(AisContractStage.Field field, String value) {
        if (field != null && isNotBlank(value)) {
            switch (field.getType()) {
                case String:
                    if (value.length() > field.getMaxLength()) {
                        value = value.substring(0, field.getMaxLength());
                    }
                    break;
                case Double:
                    value = value.replaceAll("[^\\d,]", "").replaceAll(",", ".");
                    break;
                case Date:
                    if (value.equals(WRONG_DATE)) {
                        value = DEFAULT_DATE;
                    }
                    break;
            }
            return value;
        }
        return null;
    }

    public static List<AisContractSupply> getContractsSupplyFromXml(BOMInputStream is) {
        List<AisContractSupply> contracts = new ArrayList<AisContractSupply>();
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            Document document = reader.read(new InputStreamReader(is, "UTF-8"));
            List list = document.selectNodes("/database/dogovors/record");
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Element dogovor = (Element) iterator.next();
                AisContractSupply contractSupply = new AisContractSupply();
                for (Iterator poleIterator = dogovor.elementIterator("pole"); poleIterator.hasNext();) {
                    Element pole = (Element) poleIterator.next();
                    AisContractSupplyField field = AisContractSupplyField.fromName(pole.attributeValue("name"));
                    String value = pole.attributeValue("value");
                    value = DataUtil.prepareValue(field, value);
                    if (field != null && isNotBlank(value)) {
                        contractSupply.addFieldValue(field, value);
                    }
                }
                contractSupply.setStages(getContractsSupplyStages(dogovor));
                contracts.add(contractSupply);

            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return contracts;
    }

    private static List<AisContractSupplyStage> getContractsSupplyStages(Element element) {
        List<AisContractSupplyStage> stages = new ArrayList<AisContractSupplyStage>();
        List nodes = element.selectNodes("intab[@name='ЭтапДоговора']/record");
        Iterator iterator = nodes.iterator();
        while (iterator.hasNext()) {
            Element stage = (Element) iterator.next();
            AisContractSupplyStage contractSupplyStage = new AisContractSupplyStage();
            for (Iterator poleIterator = stage.elementIterator("pole"); poleIterator.hasNext();) {
                Element pole = (Element) poleIterator.next();
                AisContractSupplyStageField field = AisContractSupplyStageField.fromName(pole.attributeValue("name"));
                String value = pole.attributeValue("value");
                value = DataUtil.prepareValue(field, value);
                if (field != null && isNotBlank(value)) {
                    contractSupplyStage.addFieldValue(field, value);
                }
            }
            stages.add(contractSupplyStage);

        }
        return stages;
    }

    public static List<AisContractSupplyAdditional> getContractsSupplyAdditionalFromXml(BOMInputStream is) {
        List<AisContractSupplyAdditional> contracts = new ArrayList<AisContractSupplyAdditional>();
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            Document document = reader.read(new InputStreamReader(is, "UTF-8"));
            List list = document.selectNodes("/database/dogovors/record");
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Element dogovor = (Element) iterator.next();
                AisContractSupplyAdditional contractSupply = new AisContractSupplyAdditional();
                for (Iterator poleIterator = dogovor.elementIterator("pole"); poleIterator.hasNext();) {
                    Element pole = (Element) poleIterator.next();
                    AisContractSupplyAdditionalField field = AisContractSupplyAdditionalField.fromName(pole.attributeValue("name"));
                    String value = pole.attributeValue("value");
                    value = DataUtil.prepareValue(field, value);
                    if (field != null && isNotBlank(value)) {
                        contractSupply.addFieldValue(field, value);
                    }
                }
                contractSupply.setStages(getContractsSupplyAdditionalStages(dogovor));
                contracts.add(contractSupply);

            }
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return contracts;
    }

    private static List<AisContractSupplyAdditionalStage> getContractsSupplyAdditionalStages(Element dogovor) {
        List<AisContractSupplyStage> contractsSupplyStages = getContractsSupplyStages(dogovor);
        List<AisContractSupplyAdditionalStage> list = new ArrayList<AisContractSupplyAdditionalStage>(contractsSupplyStages.size());
        for (AisContractSupplyStage contractsSupplyStage : contractsSupplyStages) {
            list.add(new AisContractSupplyAdditionalStage(contractsSupplyStage));
        }
        return list;
    }
}
