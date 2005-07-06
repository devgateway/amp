/*
 *   SiteLayout.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 17, 2003
 * 	 CVS-ID: $Id: SiteLayout.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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
import java.util.Collection;
import java.util.ArrayList;

/**
 * Data class, used by Digester to parse site-config.xml.
 * See org.digijava.kernel.util.ConfigurationManager for more details
 * @author Mikheil Kapanadze
 * @version 1.0
 */
public class SiteLayout
    extends ConfigurationItem {
    private HashMap layout;
    private Collection pageGroups;

    public String getName() {
        return null;
    }

    public void setName(String name) {}

    public SiteLayout() {
        this.layout = new HashMap();
        this.pageGroups = new ArrayList();
    }

    public void addLayout(Layout layout) {
        this.layout.put(layout.getName(), layout);
    }

    public HashMap getLayout() {
        return layout;
    }

    public String toString() {
        String newLine = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer();
        buf.append("<site-layout>").append(newLine);

        Iterator iter = layout.values().iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            buf.append(item).append(newLine);
        }

        iter = pageGroups.iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            buf.append(item).append(newLine);
        }
        buf.append("</site-layout>");

        return buf.toString();
    }

    public void merge(ConfigurationItem configurationItem) {
        SiteLayout secondSiteLayout = (SiteLayout) configurationItem;

        mergeHashMap(layout, secondSiteLayout.getLayout());
        pageGroups = new ArrayList(secondSiteLayout.pageGroups);
    }

    public void setLayout(HashMap layout) {
        this.layout = layout;
    }

    public void validate() throws Exception {};

    public Collection getPageGroups() {
        return pageGroups;
    }

    public void setPageGroups(Collection pageGroups) {
        this.pageGroups = pageGroups;
    }

    public void addPageGroup(PageGroup pageGroup) {
        pageGroups.add(pageGroup);
    }

}