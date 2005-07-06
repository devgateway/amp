/*
*   Country.java
*   @Author Irakli Nadareishvili inadareishvili@worldbank.org
*   Created: Jul 30, 2003
*   CVS-ID: $Id: Country.java,v 1.1 2005-07-06 10:34:18 rahul Exp $
*
*   This file is part of DiGi project (www.digijava.org).
*   DiGi is a multi-site portal system written in Java/J2EE.
*
*   Confidential and Proprietary, Subject to the Non-Disclosure
*   Agreement, Version 1.0, between the Development Gateway
*   Foundation, Inc and the Recipient -- Copyright 2001-2004 Development
*   Gateway Foundation, Inc.
*
*   Unauthorized Disclosure Prohibited.
*
*************************************************************************/
package org.digijava.kernel.dbentity;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The value object class for the Countries. Maps to the table PL_COUNTRIES
 *
 */
public class Country
    implements Serializable {

    /** identifier field */
    private String iso;

    /** persistent fields */
    private Long countryId;
    private String countryName;
    private String iso3;
    private String stat;
    private String showCtry;
    private String decCtryFlag;
    private String messageLangKey;
    private boolean available;

    /** full constructor */
    public Country(Long countryId, String iso, String countryName, String iso3,
                   String stat, String showCtry, String decCtryFlag) {
        this.countryId = countryId;
        this.iso = iso;
        this.countryName = countryName;
        this.iso3 = iso3;
        this.stat = stat;
        this.showCtry = showCtry;
        this.decCtryFlag = decCtryFlag;
    }

    /** default constructor */
    public Country() {
    }

    /** minimal constructor */
    public Country(String iso) {
        this.iso = iso;
    }

    public Long getCountryId() {
        return this.countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getIso() {
        return this.iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getIso3() {
        return this.iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getStat() {
        return this.stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getShowCtry() {
        return this.showCtry;
    }

    public void setShowCtry(String showCtry) {
        this.showCtry = showCtry;
    }

    public String getDecCtryFlag() {
        return this.decCtryFlag;
    }

    public void setDecCtryFlag(String decCtryFlag) {
        this.decCtryFlag = decCtryFlag;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("iso", getIso())
            .toString();
    }

    public boolean equals(Object other) {
        if (! (other instanceof Country))
            return false;
        Country castOther = (Country) other;
        return new EqualsBuilder()
            .append(this.getIso(), castOther.getIso())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getIso())
            .toHashCode();
    }

    public String getMessageLangKey() {
        return messageLangKey;
    }

    public void setMessageLangKey(String messageLangKey) {
        this.messageLangKey = messageLangKey;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
