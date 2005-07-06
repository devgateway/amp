/*
*   ViewConfigException.java
*   @Author Mikheil Kapanadze mikheil@digijava.org
*   Created: Apr 08, 2004
*   CVS-ID: $Id: ViewConfigException.java,v 1.1 2005-07-06 10:34:18 rahul Exp $
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
package org.digijava.kernel.viewmanager;

import org.digijava.kernel.exception.DgException;

public class ViewConfigException extends DgException {

    public ViewConfigException() {
    }

    public ViewConfigException(String message) {
        super(message);
    }

    public ViewConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViewConfigException(Throwable cause) {
        super(cause);
    }
}