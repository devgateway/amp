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
