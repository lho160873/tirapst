package pst.arm.client.common.domain;

/**
 * Any domain should implements this interface to use base component for editing 
 * itself in {@code DomainEditWindowNew}
 * 
 * @author Alexandr Kozhin
 * @since 0.0.1
 * @see pst.arm.client.common.ui.controls.editdomain.DomainEditWindowNew
 */
public interface EditableDomain<T> {
    /**
     * Method for create empty domain object. It uses in save domain process and 
     * check on unsaved changes.
     * 
     * @return Empty domain object
     */
    public T        newInstance();
    
    /**
     * Method to copy properties from existing domain object
     * 
     * @param domain Link to domain object
     */
    public void     copy(T domain);
    
    /**
     * Check on additional domain loading
     * 
     * @return True if domain contains all data, else False
     */
    public Boolean  isDomainFull();
    
    /**
     * Get unique domain identifier
     * 
     * @return Domain object identifier
     */
    public Long   getDomainId(); 
    
    /**
     * Compare domain with existing by data
     * 
     * @param domain Domain object to compare
     * @return True if all properties are equal, else False
     */
    public Boolean  isDomainEquals(T domain);
}
