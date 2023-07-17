/*
 * AmpField.java 
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_FIELD")
public class AmpField implements Serializable {
    @Id
    @GeneratedValue(generator = "ampFieldSeq")
    @SequenceGenerator(name = "ampFieldSeq", sequenceName = "AMP_FIELD_seq", allocationSize = 1)
    @Column(name = "amp_field_id")
    private Long ampFieldId;

    @Column(name = "field_name")
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

