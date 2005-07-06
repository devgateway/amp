/*
 *   EditorException.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Dec 17, 2003
 * 	 CVS-ID: $Id: EditorException.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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
package org.digijava.module.editor.exception;

import org.digijava.kernel.exception.DgException;

public class EditorException
    extends DgException {

    public EditorException() {
    }

    public EditorException(String message) {
        super(message);
    }

    public EditorException(String message, Throwable cause) {
        super(message, cause);
    }

    public EditorException(Throwable cause) {
        super(cause);
    }
}
