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

package org.digijava.kernel.config;

import java.util.Iterator;
import java.util.Vector;

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
        StringBuilder buf = new StringBuilder();

        Iterator iter = hibernateClass.iterator();
        String newLine = System.getProperty("line.separator");
        while (iter.hasNext()) {
            HibernateClass item = (HibernateClass) iter.next();
            buf.append(item.toString()).append(newLine);
        }
        buf.append(" REQUIRED - ").append(required);

        return buf.toString();
    }
}
