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
