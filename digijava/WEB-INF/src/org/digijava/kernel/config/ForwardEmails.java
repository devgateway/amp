/*
 *   EmailCapture.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Nov 28, 2003
 * 	 CVS-ID: $Id: ForwardEmails.java,v 1.1 2005-07-06 10:34:15 rahul Exp $
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
package org.digijava.kernel.config;

import java.util.ArrayList;
import java.util.List;

public class ForwardEmails {
    private List emails;
    private boolean enabled;

    public ForwardEmails() {
        emails = new ArrayList();
        enabled = false;
    }

    public List getEmails() {
        return emails;
    }

    public void addEmail(String email) {
        emails.add(email);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}