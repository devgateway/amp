/*
 *   BBCodeException.java
 *   @Author Maka Kharalashvili maka@digijava.org
 *   Created: Oct 20, 2003
 *   CVS-ID: $Id$
 *
 *   This file is part of DiGi project (www.digijava.org).
 *   DiGi is a multi-site portal system written in Java/J2EE.
 *
 *   Copyright 2001-2004 Development Gateway Foundation, Inc.
 *
 *   Licensed under the GNU General Public License version 3; see the repository
 *   LICENSE file for the full terms.
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


