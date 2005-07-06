/*
 *   Page.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 17, 2003
 * 	 CVS-ID: $Id: Page.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

package org.digijava.kernel.siteconfig;

public class Page
    extends ConfigurationItem {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return "<page name=\"" + name + "\">" + value + "</page>";
    }

    public void merge(ConfigurationItem configurationItem) {
        Page secondPage = (Page) configurationItem;

        if (secondPage.getValue() != null) {
            value = secondPage.getValue();
        }
    }

    public void validate() throws Exception {};

}