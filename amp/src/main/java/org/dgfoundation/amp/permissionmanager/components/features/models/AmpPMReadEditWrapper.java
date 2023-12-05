/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.components.features.models;

import java.io.Serializable;

/**
 * @author dan
 *
 */
public class AmpPMReadEditWrapper implements Serializable, Comparable {

    private Long id;
    private String name;
    private Boolean readFlag = Boolean.FALSE;
    private Boolean editFlag = Boolean.FALSE;
    
    
    
    public AmpPMReadEditWrapper(String name, Boolean readFlag, Boolean editFlag) {
        super();
        this.name = name;
        this.readFlag = readFlag;
        this.editFlag = editFlag;
    }

    public AmpPMReadEditWrapper(String name) {
        super();
        this.name = name;
    }

    public AmpPMReadEditWrapper(Long id, String name, Boolean readFlag, Boolean editFlag) {
        super();
        this.id = id;
        this.name = name;
        this.readFlag = readFlag;
        this.editFlag = editFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getReadFlag() {
        return readFlag;
    }

    public void setReadFlag(Boolean readFlag) {
        this.readFlag = readFlag;
    }

    public Boolean getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(Boolean editFlag) {
        this.editFlag = editFlag;
    }

    /**
     * 
     */
    public AmpPMReadEditWrapper() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        if(!(o instanceof AmpPMReadEditWrapper)) return -1;
        AmpPMReadEditWrapper obj = (AmpPMReadEditWrapper)o;
        return this.getId().compareTo(obj.getId());
    }
    
}
