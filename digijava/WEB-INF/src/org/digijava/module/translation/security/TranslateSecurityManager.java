/*
 *   TranslateSecurityManager.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 10, 2004
 * 	 CVS-ID: $Id: TranslateSecurityManager.java,v 1.1 2005-07-06 10:34:25 rahul Exp $
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

package org.digijava.module.translation.security;

import org.digijava.kernel.security.AbstractObjectSecurityManager;
import org.digijava.kernel.security.permission.ObjectPermission;

public class TranslateSecurityManager
      extends AbstractObjectSecurityManager {

    public ObjectPermission createPermission(Class clazz, Object key,
					     int action) {
	return new TranslatePermission( (TranslateObject) key, action);
    }

    public static TranslateSecurityManager getInstance() {
	return new TranslateSecurityManager();
    }
}