/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.security.principal;

import java.io.Serializable;
import java.security.Principal;

import org.digijava.kernel.user.User;

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
