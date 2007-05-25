/*
 *   SyndicationException.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Aug 26, 2003
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

package org.digijava.module.syndication.exception;

import org.digijava.kernel.exception.DgException;

public class SyndicationException
    extends DgException {
    public SyndicationException() {
    }

    public SyndicationException(String message) {
        super(message);
    }

    public SyndicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SyndicationException(Throwable cause) {
        super(cause);
    }

}
