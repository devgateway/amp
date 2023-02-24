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
import org.digijava.kernel.config.*;

/**
 * Data class, used by Digester to parse site-config.xml.
 * See org.digijava.kernel.util.ConfigurationManager for more details
 * @author Mikheil Kapanadze
 * @version 1.0
 */
public class Module
    extends ConfigurationItem {
    private String teaserFile;
    private String name;
    private HashMap instance;
    private HashMap page;
    private HashMap teaser;

    public Module() {
        this.instance = new HashMap();
        this.page = new HashMap();
        this.teaserFile = null;
        this.teaser = new HashMap();
    }

    public String getTeaserFile() {
        return teaserFile;
    }

    public void setTeaserFile(String teaserFile) {
        this.teaserFile = teaserFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addInstance(Instance instance) {
        this.instance.put(instance.getName(), instance);
    }

    public HashMap getInstance() {
        return instance;
    }

    public String toString() {

        String newLine = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer();
        buf.append("<module name=\"" + name + "\" teaser-file=\"" + teaserFile +
                   "\">").append(newLine);

        Iterator iter = instance.values().iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            buf.append(item).append(newLine);
        }

        iter = page.values().iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            buf.append(item).append(newLine);
        }

        iter = teaser.values().iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            buf.append(item).append(newLine);
        }
        buf.append("</module>");
        return buf.toString();

    }

    public HashMap getPage() {
        return page;
    }

    public void addPage(Page page) {
        this.page.put(page.getName(), page);
    }

    public void addTeaser(Teaser teaser) {
        this.teaser.put(teaser.getName(), teaser);
    }

    public void merge(ConfigurationItem configurationItem) {
        Module secondModule = (Module) configurationItem;

        if (secondModule.getTeaserFile() != null) {
            teaserFile = secondModule.getTeaserFile();
        }

        mergeHashMap(instance, secondModule.getInstance());
        mergeHashMap(page, secondModule.getPage());
        mergeHashMap(teaser, secondModule.getTeaser());
    }

    public void reverseMerge(ConfigurationItem configurationItem) {
        Module secondModule = (Module) configurationItem;

        if (teaserFile != null) {
            secondModule.teaserFile = teaserFile;
        }

        mergeHashMap(secondModule.instance, instance);
        mergeHashMap(secondModule.page, page);
        mergeHashMap(secondModule.teaser, teaser);
    }

    public void validate() throws Exception {};

    public HashMap getTeaser() {
        return teaser;
    }
}
