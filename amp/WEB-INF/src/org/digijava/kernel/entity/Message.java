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

package org.digijava.kernel.entity;
import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.SiteCache;
import org.digijava.module.aim.util.AmpMath;
/**
 * @author shamanth.murthy
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 *
 * This class maps to message_lang table
 */
public class Message implements Serializable{

    private static final long serialVersionUID = 1;

    private String key;
    private String message;
    private String locale;
    private Timestamp created;
    private Timestamp lastAccessed;
    private String siteId;
    private String keyWords;
    private int hashCode;
    private boolean hasHashCode;
    private String originalMessage;
    private String prefix;
    private Boolean ampOffline;
    public Message(){
        hasHashCode = false;
    }
    /**
     * @hibernate.property
     *  not-null="true"
     *  column="key"
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * @hibernate.property
     *  not-null="true"
     *  column="lang_iso"
     * @return
     */
    public String getLocale() {
        return locale;
    }

    /**
     * @hibernate.property
     *  not-null="true"
     *  column="message_utf8"
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param string
     */
    public void setKey(String string) {
        key = string;
        hasHashCode = false;
    }

    /**
     * @param string
     */
    public void setLocale(String string) {
        locale = string;
        hasHashCode = false;
    }

    /**
     * @param string
     */
    public void setMessage(String string) {
        message = string;
        hasHashCode = false;
    }

    /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        /*
    public String toString() {
        return new ToStringBuilder(this)
            .append("Key", (getKey()).toString())
            .append("Locale", getLocale())
                        .append("siteId", getSiteId())
            .toString();
    }
*/
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if (!(other instanceof Message))
            return false;
        Message castOther = (Message) other;
        return compareStrings(this.key, castOther.key) &&
            compareStrings(this.siteId, castOther.siteId) &&
            compareStrings(this.locale, castOther.locale);
        //return this.key.equals()
        /*
        return new EqualsBuilder()
            .append(this.key, castOther.key)
            .append(this.siteId, castOther.siteId)
            .append(this.locale, castOther.locale)
            .isEquals();
         */
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        if (!hasHashCode) {
            hashCode = new HashCodeBuilder()
                .append(key)
                .append(locale)
                .append(siteId)
                .toHashCode();
        }

        return hashCode;
    }
    /**
     * @return
     */
    public Timestamp getCreated() {
        return created;
    }

    /**
     * @param timestamp
     */
    public void setCreated(Timestamp timestamp) {
        created = timestamp;
    }

    /**
     * @return
     */
    public String getSiteId() {
        return siteId;
    }


    /**
     * @deprecated - do not use directly as it subscribes you to a certain DB encoding. Use {@link #setSite(Site instead)}
     * @param timestamp
     */
    public void setSiteId(String siteId) {
        if ((siteId != null) && (!AmpMath.isLong(siteId)))
        {
            this.siteId = SiteCache.lookupByName(siteId).getId().toString();
            Logger.getLogger(this.getClass()).error("wrong siteId in Message, should be number!" + siteId, new RuntimeException());
        }
        this.siteId = siteId;
    }
    
    public void setSite(Site site)
    {
        setSiteId(site == null ? null : site.getId().toString());
    }

    private boolean compareStrings(String first, String second) {
        if (first != null && second != null) {
            return first.equals(second);
        }
        else
        if (first == null && second == null) {
            return true;
        }
        else {
            return false;
        }
    }
    public void setLastAccessed(Timestamp lastAccessed) {
        this.lastAccessed = lastAccessed;
    }
    public Timestamp getLastAccessed() {
        return lastAccessed;
    }
    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }
    public String getKeyWords() {
        return keyWords;
    }
    public void setOriginalMessage(String originalMessage) {
        this.originalMessage = originalMessage;
    }
    public String getOriginalMessage() {
        return originalMessage;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getAmpOffline() {
        return ampOffline;
    }

    public void setAmpOffline(Boolean ampOffline) {
        this.ampOffline = ampOffline;
    }
}
