package pst.arm.client.modules.technology.nomenclature.service.remote;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import java.util.Map;
import pst.arm.client.modules.technology.nomenclature.model.Nomenclature;
import pst.arm.client.modules.technology.nomenclature.model.NomenclatureSearchCondition;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
@RemoteServiceRelativePath("service/nomenclatureService")
public interface GWTNomenclatureService extends RemoteService {
    Map<Integer, List<Nomenclature>> getNomenclatures(NomenclatureSearchCondition condition);
    void matchNomenclatures(Nomenclature nomenclatureSearch, Nomenclature nomenclaturePmasc);
    List<Nomenclature> getMatchedNomenclatures(NomenclatureSearchCondition condition);
    void removeNomenclatureAssociation(Nomenclature nomenclature);
}
