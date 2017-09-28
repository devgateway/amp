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
 * Configuration class form marging site-config.xml files
 * @author Mikheil Kapanadze
 * @version 1.0
 */
public abstract class ConfigurationItem {

    public abstract void setName(String name);

    public abstract String getName();

    public abstract void validate() throws Exception;

    public abstract void merge(ConfigurationItem configurationItem);

    protected void mergeHashMap(HashMap firstMap, HashMap secondMap) {
        if (firstMap.size() == 0) {
            firstMap.putAll(secondMap);
        }
        else {
            Iterator iter = secondMap.values().iterator();
            while (iter.hasNext()) {
                ConfigurationItem secondItem = (ConfigurationItem) iter.next();
                ConfigurationItem firstItem = (ConfigurationItem) firstMap.get(
                    secondItem.getName());

                if (firstItem != null) {
                    firstItem.merge(secondItem);
                }
                else {
                    firstMap.put(secondItem.getName(), secondItem);
                }
            }
        }
    }

}
