/*
 *   UserInfo.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Dec 18, 2003
 *	 CVS-ID: $Id: UserInfo.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

package org.digijava.kernel.user;

import java.io.Serializable;

public class UserInfo
    implements Serializable {

    private String firstNames;
    private String lastName;
    private String email;

    public UserInfo() {}

    public UserInfo(String firstNames, String lastName, String email) {
        this.firstNames = firstNames;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstNames() {
        return firstNames;
    }

    public void setFirstNames(String firstNames) {
        this.firstNames = firstNames;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}