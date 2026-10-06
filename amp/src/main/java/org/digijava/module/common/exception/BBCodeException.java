/*
 *   BBCodeException.java
 *   @Author Maka Kharalashvili maka@digijava.org
 *   Created: Oct 20, 2003
 *   CVS-ID: $Id$
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

package org.digijava.module.common.exception;
import org.digijava.kernel.exception.DgException;


public class BBCodeException
    extends DgException {

    public BBCodeException() {
    }

    public BBCodeException(String message) {
        super(message);
    }
    public BBCodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BBCodeException(Throwable cause) {
        super(cause);
    }

}


