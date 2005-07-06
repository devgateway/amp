/*
 *   Instance.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 17, 2003
 * 	 CVS-ID: $Id: Instance.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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
import org.digijava.kernel.config.*;

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