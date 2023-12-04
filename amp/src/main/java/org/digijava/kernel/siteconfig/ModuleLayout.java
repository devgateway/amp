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

    public void addModule(AmpModule module) {
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
