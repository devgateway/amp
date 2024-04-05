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
public class Instance extends ConfigurationItem {
  private String name;
  private String teaserFile;
  private HashMap page;
  private HashMap teaser;


  public Instance() {
    this.name = null;
    this.teaserFile = null;
    this.page = new HashMap();
    this.teaser = new HashMap();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getTeaserFile() {
    return teaserFile;
  }

  public void setTeaserFile(String teaserFile) {
    this.teaserFile = teaserFile;
  }

  public String toString() {

    String newLine = System.getProperty("line.separator");
    StringBuffer buf = new StringBuffer();
    buf.append("<instance name=\"" + name + "\" teaser-file=\"" + teaserFile + "\">").append(newLine);

    Iterator iter = page.values().iterator();
    while (iter.hasNext()) {
      Object item = iter.next();
      buf.append(item).append(newLine);
    }

    buf.append("</instance>");
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
    Instance secondInstance = (Instance)configurationItem;

    if (secondInstance.getTeaserFile() != null) {
      teaserFile = secondInstance.getTeaserFile();
    }

    mergeHashMap(page, secondInstance.getPage());
    mergeHashMap(teaser, secondInstance.getTeaser());
  }

  public void validate() throws Exception {
      Iterator iter = page.values().iterator();
      while (iter.hasNext()) {
          Page page = (Page)iter.next();
          page.validate();
      }
  }
    public HashMap getTeaser() {
        return teaser;
    }

}
