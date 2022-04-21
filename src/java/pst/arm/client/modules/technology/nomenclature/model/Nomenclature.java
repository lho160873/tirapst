package pst.arm.client.modules.technology.nomenclature.model;

import java.io.Serializable;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class Nomenclature implements Serializable {

    public enum NomenclatureSource {

        Search, Pmasc
    }

    private NomenclatureSource source;
    private Integer nomenclatureId;
    private Integer signDb;
    private String id;
    private String name;

    public Nomenclature() {
    }

    public Nomenclature(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return the source
     */
    public NomenclatureSource getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(NomenclatureSource source) {
        this.source = source;
    }

    /**
     * @return the nomenclatureId
     */
    public Integer getNomenclatureId() {
        return nomenclatureId;
    }

    /**
     * @param nomenclatureId the nomenclatureId to set
     */
    public void setNomenclatureId(Integer nomenclatureId) {
        this.nomenclatureId = nomenclatureId;
    }

    /**
     * @return the signDb
     */
    public Integer getSignDb() {
        return signDb;
    }

    /**
     * @param signDb the signDb to set
     */
    public void setSignDb(Integer signDb) {
        this.signDb = signDb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
