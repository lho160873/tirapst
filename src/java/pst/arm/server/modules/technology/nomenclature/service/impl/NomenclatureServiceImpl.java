package pst.arm.server.modules.technology.nomenclature.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pst.arm.client.modules.technology.nomenclature.model.Nomenclature;
import pst.arm.client.modules.technology.nomenclature.model.NomenclatureSearchCondition;
import pst.arm.server.modules.technology.nomenclature.dao.NomenclatureDAO;
import pst.arm.server.modules.technology.nomenclature.service.NomenclatureService;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
@Service
public class NomenclatureServiceImpl implements NomenclatureService{
    @Autowired
    NomenclatureDAO dao;

    @Override
    public Map<Integer, List<Nomenclature>> getNomenclatures(NomenclatureSearchCondition condition) {
        return dao.getNomenclatures(condition);
    }

    @Override
    public boolean isNomenclatureNotMatched(String idSearch) {
        return dao.isNomenclatureNotMatched(idSearch);
    }

    @Override
    public void matchNomenclatures(Nomenclature nomenclatureSearch, Nomenclature nomenclaturePmasc) {
        dao.matchNomenclatures(nomenclatureSearch, nomenclaturePmasc);
    }

    @Override
    public List<Nomenclature> getMatchedNomenclatures(NomenclatureSearchCondition condition) {
        return dao.getMatchedNomenclatures(condition);
    }

    @Override
    public void removeNomenclatureAssociation(Nomenclature nomenclature) {
        dao.removeNomenclatureAssociation(nomenclature);
    }
}
