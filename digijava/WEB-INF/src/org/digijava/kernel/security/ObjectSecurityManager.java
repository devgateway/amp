/*
 *   ResourcePermission.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 6, 2004
 *   CVS-ID: $Id: ObjectSecurityManager.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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

import org.digijava.kernel.security.permission.ObjectPermission;

public final class ObjectSecurityManager extends AbstractObjectSecurityManager {

    public ObjectPermission createPermission(Class clazz, Object key, int action) {
        return new ObjectPermission(clazz.getName(), key, action);
    }
}