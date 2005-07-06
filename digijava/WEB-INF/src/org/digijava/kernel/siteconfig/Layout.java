/*
 *   Layout.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 17, 2003
 * 	 CVS-ID: $Id: Layout.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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
public class Layout
    extends ConfigurationItem
    implements Cloneable {
    private HashMap put;
    private HashMap putItem;
    private String name;
    private String file;
    private String extendsLayout;
    private boolean fileBlank;

    public Layout() {
        this.put = new HashMap();
        this.putItem = new HashMap();
        this.name = null;
        this.file = null;
        this.fileBlank = true;
    }

    public void addPut(Put put) {
        this.put.put(put.getName(), put);
    }

    public void addPutItem(PutItem putItem) {
        this.putItem.put(putItem.getName(), putItem);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        String newLine = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer();
        buf.append("<layout name=\"" + name + "\" extends=\"" + extendsLayout +
                   "\" file=\"" + file + "\">").append(newLine);

        Iterator iter = put.values().iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            buf.append(item).append(newLine);
        }

        iter = putItem.values().iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            buf.append(item).append(newLine);
        }
        buf.append("</layout>");

        return buf.toString();
    }

    public HashMap getPut() {
        return put;
    }

    public HashMap getPutItem() {
        return putItem;
    }

    public String getExtendsLayout() {
        return extendsLayout;
    }

    public void setExtendsLayout(String extendsLayout) {
        this.extendsLayout = extendsLayout;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void merge(ConfigurationItem configurationItem) {
        Layout secondLayout = (Layout) configurationItem;

        // If both layouts had "file" attribute empty, then merged layout
        // will also have empty flag
        fileBlank = fileBlank & secondLayout.fileBlank;

        if (secondLayout.getFile() != null) {
            file = secondLayout.getFile();
        }

        extendsLayout = secondLayout.getExtendsLayout();

        mergeHashMap(put, secondLayout.getPut());
        mergeHashMap(putItem, secondLayout.getPutItem());
    }

    public void setPut(HashMap put) {
        this.put = put;
    }

    public void setPutItem(HashMap putItem) {
        this.putItem = putItem;
    }

    public void validate() throws Exception {};

    public boolean isFileBlank() {
        return fileBlank;
    }

    public void setFileBlank(boolean fileBlank) {
        this.fileBlank = fileBlank;
    }

}