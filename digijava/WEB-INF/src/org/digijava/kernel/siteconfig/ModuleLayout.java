/*
 *   ModuleLayout.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 17, 2003
 * 	 CVS-ID: $Id: ModuleLayout.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

/**
 * Data class, used by Digester to parse site-config.xml.
 * See org.digijava.kernel.util.ConfigurationManager for more details
 * @author Mikheil Kapanadze
 * @version 1.0
 */
public class ModuleLayout
    extends ConfigurationItem {
    private HashMap module;

    public ModuleLayout() {
        this.module = new HashMap();
    }

    public String getName() {
        return null;
    }

    public void setName(String name) {}

    public void addModule(Module module) {
        this.module.put(module.getName(), module);
    }

    public HashMap getModule() {
        return module;
    }

    public String toString() {
        String newLine = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer();
        buf.append("<module-layout>").append(newLine);

        Iterator iter = module.values().iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            buf.append(item).append(newLine);
        }
        buf.append("</module-layout>");

        return buf.toString();
    }

    public void merge(ConfigurationItem configurationItem) {
        ModuleLayout secondModuleLayout = (ModuleLayout) configurationItem;

        mergeHashMap(module, secondModuleLayout.getModule());
    }

    public void validate() throws Exception {};

}