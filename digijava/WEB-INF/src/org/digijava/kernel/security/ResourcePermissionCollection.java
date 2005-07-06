/*
 *   ResourcePermissionCollection.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 14, 2003
 *   CVS-ID: $Id: ResourcePermissionCollection.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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
package org.digijava.kernel.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.digijava.kernel.util.I18NHelper;

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
