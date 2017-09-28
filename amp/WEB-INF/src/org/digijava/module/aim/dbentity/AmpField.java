/*
 * AmpField.java 
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;

public class AmpField implements Serializable {
    private Long ampFieldId;
    
    private String fieldName;
    
    /**
     * @return Returns the ampFieldId.
     */
    public Long getAmpFieldId() {
        return ampFieldId;
    }
    /**
     * @param ampFieldId The ampFieldId to set.
     */
    public void setAmpFieldId(Long ampFieldId) {
        this.ampFieldId = ampFieldId;
    }
    /**
     * @return Returns the fieldName.
     */
    public String getFieldName() {
        return fieldName;
    }
    /**
     * @param fieldName The fieldName to set.
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

