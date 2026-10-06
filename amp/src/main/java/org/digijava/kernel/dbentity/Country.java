/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.dbentity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.function.Function;

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
    
    public final static Function<Country, String> DISTRIBUTE_BY_MSGKEY = c -> c.messageLangKey == null ? "" : c.messageLangKey;
}
