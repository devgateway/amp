/*
 *   ModulePageGroup.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Apr 15, 2004
 * 	 CVS-ID: $Id: ModulePageGroup.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

import java.util.HashMap;
import java.util.Iterator;

public class ModulePageGroup {

    private HashMap pages;
    private String moduleName;

    public ModulePageGroup() {
        pages = new HashMap();
    }

    public String toString() {
        String newLine = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer();
        buf.append("<module name=\"" + moduleName +
            "\">").append(newLine);

        Iterator iter = pages.values().iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            buf.append(item).append(newLine);
        }

        buf.append("</module");

        return buf.toString();
    }

    public HashMap getPages() {
        return pages;
    }

    public void setPages(HashMap pages) {
        this.pages = pages;
    }

    public void addPage(GroupedPage groupedPage) {
        pages.put(groupedPage.getName(), groupedPage);
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}