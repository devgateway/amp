/*
 *   SDMException.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id$
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
package org.digijava.module.sdm.exception;

import org.digijava.kernel.exception.DgException;

public class SDMException
    extends DgException {
    public SDMException() {
    }

    public SDMException(String message) {
        super(message);
    }

    public SDMException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDMException(Throwable cause) {
        super(cause);
    }

}
