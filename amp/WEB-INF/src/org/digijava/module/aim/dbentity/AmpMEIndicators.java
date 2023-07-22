package org.digijava.module.aim.dbentity;

import javax.persistence.*;

@Entity
@Table(name = "AMP_ME_INDICATORS")
@Deprecated
public class AmpMEIndicators {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AMP_ME_INDICATORS_seq")
    @SequenceGenerator(name = "AMP_ME_INDICATORS_seq", sequenceName = "AMP_ME_INDICATORS_seq", allocationSize = 1)
    @Column(name = "amp_me_indicator_id")
    private Long ampMEIndId;

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "default_ind")
    private Boolean defaultInd;

    @Column(name = "ascending_ind")
    private Boolean ascendingInd;
    
    
    /**
     * @return Returns the ampMEIndId.
     */
    public Long getAmpMEIndId() {
        return ampMEIndId;
    }
    /**
     * @param ampMEIndId The ampMEIndId to set.
     */
    public void setAmpMEIndId(Long ampMEIndId) {
        this.ampMEIndId = ampMEIndId;
    }
    /**
     * @return Returns the code.
     */
    public String getCode() {
        return code;
    }
    /**
     * @param code The code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return Returns the defaultInd.
     */
    public boolean isDefaultInd() {
        return defaultInd;
    }
    /**
     * @param defaultInd The defaultInd to set.
     */
    public void setDefaultInd(boolean defaultInd) {
        this.defaultInd = defaultInd;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * @return Returns the ascendingInd.
     */
    public boolean isAscendingInd() {
        return ascendingInd;
    }
    /**
     * @param ascendingInd The ascendingInd to set.
     */
    public void setAscendingInd(boolean ascendingInd) {
        this.ascendingInd = ascendingInd;
    }
}
