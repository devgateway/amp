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

package org.digijava.kernel.security;

import org.apache.log4j.Logger;
import org.digijava.kernel.util.I18NHelper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;
import java.util.Vector;

public class ResourcePermissionCollection
    extends PermissionCollection
    implements Serializable {

    private static final long serialVersionUID = 1;

    private static Logger logger = I18NHelper.getKernelLogger(ResourcePermissionCollection.class);

    private Vector perms;

    public ResourcePermissionCollection() {
        perms = new Vector();
    }

    public boolean implies(Permission p) {
        if (! (p instanceof ResourcePermission)) {
            return false;
        }

        Enumeration enumElements = perms.elements();
        while (enumElements.hasMoreElements()) {
            ResourcePermission nextPerm = (ResourcePermission) enumElements.nextElement();
            if (nextPerm.implies( (ResourcePermission) p)) {
                //here we know that the name checking went succesfully and we check the actions

                return true;
            }
        }
        return false;
    }

    public void add(Permission p) {
        logger.debug("Adding permission (" + p +
                     ") to ResourcePermissionCollection");
        perms.add(p);
    }

    public Enumeration elements() {
        return perms.elements();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws
        ClassNotFoundException, IOException {
        ois.defaultReadObject();
    }

}
