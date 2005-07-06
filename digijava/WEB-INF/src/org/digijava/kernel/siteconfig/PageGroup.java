/*
 *   PageGroup.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Apr 15, 2004
 * 	 CVS-ID: $Id: PageGroup.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

public class PageGroup {
    private HashMap modulePageGroups;
    private String masterLayout;
    private String tileName;

    public PageGroup() {
        this.modulePageGroups = new HashMap();
    }

    public String toString() {
        String newLine = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer();
        buf.append("<page-group masterLayout=\"" + masterLayout +
                   "\" tileName=\"" + tileName + "\">").append(newLine);

        Iterator iter = modulePageGroups.values().iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            buf.append(item).append(newLine);
        }

        buf.append("</page-group");

        return buf.toString();
    }

    public HashMap getModulePageGroups() {
        return modulePageGroups;
    }

    public void setModulePageGroups(HashMap modulePageGroups) {
        this.modulePageGroups = modulePageGroups;
    }

    public void addModulePageGroup(ModulePageGroup modulePageGroup) {
        modulePageGroups.put(modulePageGroup.getModuleName(), modulePageGroup);
    }

    public String getMasterLayout() {
        return masterLayout;
    }

    public void setMasterLayout(String masterLayout) {
        this.masterLayout = masterLayout;
    }

    public String getTileName() {
        return tileName;
    }

    public void setTileName(String tileName) {
        this.tileName = tileName;
    }
}