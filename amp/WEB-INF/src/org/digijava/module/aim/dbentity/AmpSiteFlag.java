/*
 * AmpSiteFlag.java
 * Created: 15 July 2006
 */

package org.digijava.module.aim.dbentity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AMP_SITE_FLAGS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmpSiteFlag {

    @Id
    @Column(name = "country_id")
    private Long countryId;

    @Column(name = "flag")
    private byte[] flag;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "def_flag")
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
