/*
 *   UserPrincipal.java
 *   @Author Mikheil Kapanadze mikheil@powerdot.org
 * 	 Created: Sep 6, 2004
 * 	 CVS-ID: $Id: UserPrincipal.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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
package org.digijava.kernel.security.principal;

import java.security.Principal;
import org.digijava.kernel.user.User;
import java.io.Serializable;

public class UserPrincipal
    implements Principal, Serializable  {

    private static final long serialVersionUID = 1;

    private long userId;
    private String userName;
    private int hash;
    private boolean globalAdmin;

    public UserPrincipal(long userId) {
        this.userId = userId;
        this.hash = (new Long(userId)).hashCode();
        this.userName = null;
        this.globalAdmin = false;
    }

    public UserPrincipal(long userId, String firstNames, String lastName) {
        this(userId);
        this.userName = firstNames + " " + lastName;
    }

    public UserPrincipal(long userId, String firstNames, String lastName,
                         boolean globalAdmin) {
        this(userId, firstNames, lastName);
        this.globalAdmin = globalAdmin;
    }

    public UserPrincipal(User user) {
        this(user.getId().longValue(), user.getFirstNames(), user.getLastName(),
             user.isGlobalAdmin());
    }

    public boolean equals(Object another) {
        if (another instanceof UserPrincipal) {
            UserPrincipal anotherPrincipal = (UserPrincipal) another;
            return userId == anotherPrincipal.userId;
        }
        else {
            return false;
        }
    }

    public String toString() {
        if (userName == null) {
            return "<principal type=\"user\" id=\"" + userId + "\" />";
        }
        else {
            return "<principal type=\"user\" id=\"" + userId + "\">" + userName +
                "</principal>";
        }
    }

    public String getName() {
        if (userName == null) {
            return "User #" + userId;
        }
        else {
            return userName;
        }
    }

    public int hashCode() {
        return hash;
    }

    public long getUserId() {
        return userId;
    }

    public boolean isGlobalAdmin() {
        return globalAdmin;
    }
}