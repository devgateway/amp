/*
 *   HibernateClass.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: HibernateClass.java,v 1.1 2005-07-06 10:34:15 rahul Exp $
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

package org.digijava.kernel.config;

/**
 * Data class, used by Digester to parse digi.xml.
 * See org.digijava.kernel.startup.ConfigLoaderListener,
 * org.digijava.kernel.persistence.HibernateClassLoader
 * for more details
 * @author Lasha Dolidze
 * @version 1.0
 */
public class HibernateClass {

    private String content;
    private String required;
    private String filter;
    private boolean precache;
    private boolean load;
    private String region;

    public HibernateClass() {
        precache = false;
        load = true;
        filter = null;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public boolean isPrecache() {
        return precache;
    }

    public void setPrecache(boolean precache) {
        this.precache = precache;
    }

    public String toString() {
        return "hibernate-class: content=\"" + content + "\" required=\"" +
            required + "\"" + " precache=\"" + precache + "\"" +
            "filter=\"" + filter + "\" region=\"" + region + "\"";
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}