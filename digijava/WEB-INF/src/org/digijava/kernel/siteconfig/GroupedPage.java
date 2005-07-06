/*
 *   GroupedPage.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Apr 15, 2004
 * 	 CVS-ID: $Id: GroupedPage.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

public class GroupedPage {
    private String name;

    public String toString() {
        String newLine = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer();
        buf.append("<page>").append(name).append("</page>").append(newLine);

        return buf.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}