package pst.arm.client.modules.technology.nomenclature.events;

import pst.arm.client.modules.technology.nomenclature.model.Nomenclature;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public interface NomenclatureMatchingListener {
    public void onNomenclatureMatched(Nomenclature n);
}
