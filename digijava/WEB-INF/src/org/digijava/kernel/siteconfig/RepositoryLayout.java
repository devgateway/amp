/*
 *   RepositoryLayout.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Apr 14, 2004
 * 	 CVS-ID: $Id: RepositoryLayout.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

public class RepositoryLayout {
    private HashMap secondaryPages;
    private Module module;

    public RepositoryLayout() {
        secondaryPages = new HashMap();
    }

    public String toString() {
        String newLine = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer();
        buf.append("<config>").append(newLine);

        buf.append("<secondary-pages>").append(newLine);
        Iterator iter = secondaryPages.values().iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            buf.append(item).append(newLine);
        }
        buf.append("</secondary-pages>").append(newLine);
        if (module != null) {
            buf.append(module).append(newLine);
        }
        buf.append("</config");

        return buf.toString();
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public HashMap getSecondaryPages() {
        return secondaryPages;
    }

    public void setSecondaryPages(HashMap secondaryPages) {
        this.secondaryPages = secondaryPages;
    }

    public void addSecondaryPage(SecondaryPage secondaryPage) {
        secondaryPages.put(secondaryPage.getName(), secondaryPage);
    }
}