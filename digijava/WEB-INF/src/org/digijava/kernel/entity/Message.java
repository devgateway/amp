/*
 *   Message.java
 * 	 @Author Shamanth Murthy
 *   Created: Aug 30, 2003
 *   CVS-ID: $Id: Message.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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
package org.digijava.kernel.entity;
import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
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
	private String siteId;
    private int hashCode;
    private boolean hasHashCode;

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
	 * @param timestamp
	 */
	public void setSiteId(String timestamp) {
		siteId = timestamp;
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

}
