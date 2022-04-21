package pst.arm.server.modules.technology.nomenclature.service.gwt;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.technology.nomenclature.model.Nomenclature;
import pst.arm.client.modules.technology.nomenclature.model.NomenclatureSearchCondition;
import pst.arm.client.modules.technology.nomenclature.service.remote.GWTNomenclatureService;
import pst.arm.server.common.web.GWTController;
import pst.arm.server.modules.technology.nomenclature.service.NomenclatureService;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
@Service("GWTNomenclatureService")
public class GWTNomenclatureServiceImpl extends GWTController implements GWTNomenclatureService{
    @Autowired
    NomenclatureService service;
    
    @Override
    public Map<Integer, List<Nomenclature>> getNomenclatures(NomenclatureSearchCondition condition) {
        return service.getNomenclatures(condition);
    }

    @Override
    public void matchNomenclatures(Nomenclature nomenclatureSearch, Nomenclature nomenclaturePmasc) {
        service.matchNomenclatures(nomenclatureSearch, nomenclaturePmasc);
    }

    @Override
    public List<Nomenclature> getMatchedNomenclatures(NomenclatureSearchCondition condition) {
        return service.getMatchedNomenclatures(condition);
    }

    @Override
    public void removeNomenclatureAssociation(Nomenclature nomenclature) {
        service.removeNomenclatureAssociation(nomenclature);
    }
}
