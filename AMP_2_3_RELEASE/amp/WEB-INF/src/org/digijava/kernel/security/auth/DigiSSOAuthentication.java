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

package org.digijava.kernel.security.auth;

import org.digijava.kernel.security.HttpLoginManager;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;

public class DigiSSOAuthentication implements Authentication {

    private String sessionId;
    HttpLoginManager.LoginInfo loginInfo;

    public DigiSSOAuthentication(HttpLoginManager.LoginInfo loginInfo, String sessionId) {
        this.sessionId = sessionId;
        this.loginInfo = loginInfo;
    }

    public GrantedAuthority[] getAuthorities() {
        return null;
    }

    public Object getCredentials() {
        return null;
    }

    public Object getDetails() {
        return null;
    }

    public Object getPrincipal() {
        return loginInfo.getActualUserId();
    }

    public boolean isAuthenticated() {
        return loginInfo.isLoggedIn();
    }

    public void setAuthenticated(boolean _boolean) throws
        IllegalArgumentException {
        throw new UnsupportedOperationException("setAuthenticated() can't be called");
    }

    public String getName() {
        return "";
    }

    public String getSessionId() {
        return sessionId;
    }
}
