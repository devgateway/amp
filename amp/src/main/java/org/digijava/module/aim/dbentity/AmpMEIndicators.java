package org.digijava.module.aim.dbentity;


@Deprecated
public class AmpMEIndicators {
    private Long ampMEIndId;
    private String code;
    private String name;
    private String description;
    private boolean defaultInd;
    private boolean ascendingInd;
    
    
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
