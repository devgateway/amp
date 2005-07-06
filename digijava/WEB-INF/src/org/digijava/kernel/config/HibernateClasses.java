/*
 *   HibernateClasses.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 3, 2003
 * 	 CVS-ID: $Id: HibernateClasses.java,v 1.1 2005-07-06 10:34:15 rahul Exp $
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

package org.digijava.kernel.config;

import java.util.Vector;
import java.util.Iterator;

/**
 * Data class, used by Digester to parse digi.xml.
 * See org.digijava.kernel.startup.ConfigLoaderListener,
 * org.digijava.kernel.persistence.HibernateClassLoader
 * for more details
 * @author Lasha Dolidze
 * @version 1.0
 */
public class HibernateClasses {

    private Vector hibernateClass;
    private String required;

    public HibernateClasses() {
        this.hibernateClass = new Vector();
    }

    public void addHibernateClass(HibernateClass hibernateClass) {
        this.hibernateClass.addElement(hibernateClass);
    }

    public Iterator iterator() {
        return hibernateClass.iterator();
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();

        Iterator iter = hibernateClass.iterator();
        String newLine = System.getProperty("line.separator");
        while (iter.hasNext()) {
            HibernateClass item = (HibernateClass) iter.next();
            buf.append(item.toString()).append(newLine);
        }
        buf.append(" REQUIRED - " + required);

        return buf.toString();
    }
}
