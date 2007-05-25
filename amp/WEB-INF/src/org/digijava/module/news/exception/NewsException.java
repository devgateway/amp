/*
 *   NewsException.java
 *   @Author Maka Kharalashvili maka@digijava.org
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

package org.digijava.module.news.exception;

import org.digijava.kernel.exception.DgException;

public class NewsException
    extends DgException {
    public NewsException() {
    }

    public NewsException(String message) {
        super(message);
    }

    public NewsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NewsException(Throwable cause) {
        super(cause);
    }

}
