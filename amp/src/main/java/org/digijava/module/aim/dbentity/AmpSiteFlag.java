/*
 * AmpSiteFlag.java
 * Created: 15 July 2006
 */

package org.digijava.module.aim.dbentity;

public class AmpSiteFlag {
    
    private Long countryId;
    
    private byte[] flag;

    private String contentType;
    
    private boolean defaultFlag;

    /**
     * @return Returns the countryId.
     */
    public Long getCountryId() {
        return countryId;
    }

    /**
     * @param countryId The countryId to set.
     */
    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    /**
     * @return Returns the defaultFlag.
     */
    public boolean isDefaultFlag() {
        return defaultFlag;
    }

    /**
     * @param defaultFlag The defaultFlag to set.
     */
    public void setDefaultFlag(boolean defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    /**
     * @return Returns the flag.
     */
    public byte[] getFlag() {
        return flag;
    }

    /**
     * @param flag The flag to set.
     */
    public void setFlag(byte[] flag) {
        this.flag = flag;
    }

    /**
     * @return Returns the contentType.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType The contentType to set.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
}
