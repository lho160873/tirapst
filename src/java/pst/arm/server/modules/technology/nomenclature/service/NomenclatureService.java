package pst.arm.server.modules.technology.nomenclature.service;

import java.util.List;
import java.util.Map;
import pst.arm.client.modules.technology.nomenclature.model.Nomenclature;
import pst.arm.client.modules.technology.nomenclature.model.NomenclatureSearchCondition;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public interface NomenclatureService {
    Map<Integer, List<Nomenclature>> getNomenclatures(NomenclatureSearchCondition condition);
    boolean isNomenclatureNotMatched(String attributeValue);
    void matchNomenclatures(Nomenclature nomenclatureSearch, Nomenclature nomenclaturePmasc);
    List<Nomenclature> getMatchedNomenclatures(NomenclatureSearchCondition condition);
    void removeNomenclatureAssociation(Nomenclature nomenclature);
}
