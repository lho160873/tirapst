package pst.arm.client.modules.leveltask;

/**
 *
 * @author LKHorosheva
 * @since 0.0.1
 */
public enum EModule {

    EMClientCard("clientcard","Карточка клиента"),    
    EMClientCardEdit("clientcardedit","Карточка клиента"),
    EMSubContractorCard("subcontractorcard","Карточка субподрядчика"),
    EMInquiriesapp("inquiriesapp","Обращение"),
    EMInquiriesreq("inquiriesreq","Запрос");
    
    
    protected String moduleName;
    protected String moduleNameRus;

    private EModule() {
        moduleName = "";
    }

    private EModule(String module,String moduleRus) {
        moduleName = module;
        moduleNameRus = moduleRus;
    }

    public String getModuleName() {
        return moduleName;
    }
    public String getModuleNameRus() {
        return moduleNameRus;
    }
    
    static public String getModuleNameRus( String module ) {
        String name = "";

        if (module.equals( EMClientCard.getModuleName())) {
            name = EMClientCard.getModuleNameRus();
        }
        if (module.equals( EMClientCardEdit.getModuleName())) {
            name = EMClientCardEdit.getModuleNameRus();
        }    
        if (module.equals(EMSubContractorCard.getModuleName())) {
            name = EMSubContractorCard.getModuleNameRus();
        }
        if (module.equals(EModule.EMInquiriesreq.getModuleName())) {
            name = EMInquiriesreq.getModuleNameRus();
        }
        if (module.equals(EModule.EMInquiriesapp.getModuleName())) {
            name = EMInquiriesapp.getModuleNameRus();
        }
        return name;

    }
}
