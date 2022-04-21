package pst.arm.client.modules.technology.nomenclature.model;

import java.io.Serializable;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 * @since
 */
public class NomenclatureSearchCondition implements Serializable{
    private String name;
    private Integer level;
    public NomenclatureSearchCondition() {
    }

    public NomenclatureSearchCondition(String name) {
        this.name = name;
    }

    public NomenclatureSearchCondition(String name, Integer level) {
        this.name = name;
        this.level = level;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }
    
}
