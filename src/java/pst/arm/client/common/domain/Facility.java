package pst.arm.client.common.domain;

import java.io.Serializable;

/**
 *
 * @author Alexandr Kozhin
 */
public class Facility implements Serializable {

    private Long facilityId;
    private String type;
    private String module;
    private String name;
    private String description;

    public Facility() {
    }

    public Facility(Long facilityId) {
        this.facilityId = facilityId;
    }

    public Facility(Long facilityId, String type, String module, String name, String description) {
        this.facilityId = facilityId;
        this.type = type;
        this.module = module;
        this.name = name;
        this.description = description;
    }

    /**
     * @return the facilityId
     */
    public Long getFacilityId() {
        return facilityId;
    }

    /**
     * @param facilityId the facilityId to set
     */
    public void setFacilityId(Long facilityId) {
        this.facilityId = facilityId;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(String module) {
        this.module = module;
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Facility) {

            return ((Facility) obj).getFacilityId() == null
                    ? this.getFacilityId() == null
                    : ((Facility) obj).getFacilityId().equals(this.getFacilityId());
        }
        return false;
    }
}
