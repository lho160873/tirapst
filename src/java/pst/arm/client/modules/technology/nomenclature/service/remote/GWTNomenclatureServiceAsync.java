package pst.arm.client.modules.technology.nomenclature.service.remote;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import java.util.Map;
import pst.arm.client.modules.technology.nomenclature.model.Nomenclature;
import pst.arm.client.modules.technology.nomenclature.model.NomenclatureSearchCondition;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public interface GWTNomenclatureServiceAsync {
    public void getNomenclatures(NomenclatureSearchCondition condition, AsyncCallback<Map<Integer, List<Nomenclature>>> callback);
    public void matchNomenclatures(Nomenclature nomenclatureSearch, Nomenclature nomenclaturePmasc, AsyncCallback<Void> callback);
    public void getMatchedNomenclatures(NomenclatureSearchCondition condition, AsyncCallback<List<Nomenclature>> callback);
    public void removeNomenclatureAssociation(Nomenclature nomenclature, AsyncCallback<Void> callback);
}
