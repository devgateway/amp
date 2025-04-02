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

package org.digijava.module.admin.form;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

//ActionForm for user Search

public class SearchUserForm
    extends ActionForm {

    private String searchUserInfo;
    private List userList;
    private String targetAction;
    private boolean hideBanned;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        searchUserInfo = null;
        userList = null;
        targetAction = null;
        hideBanned = false;
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {
        return null; // Validation was moved to SearchUser.java
    }

    public String[] getSelectedUsers() {
        return null;
    }

    public String getSearchUserInfo() {
        return searchUserInfo;
    }

    public void setSearchUserInfo(String searchUserInfo) {
        this.searchUserInfo = searchUserInfo;
    }

    public List getUserList() {
        return userList;
    }

    public void setUserList(List userList) {
        this.userList = userList;
    }

    public String getTargetAction() {
        return targetAction;
    }

    public boolean isHideBanned() {
        return hideBanned;
    }

    public void setTargetAction(String targetAction) {
        this.targetAction = targetAction;
    }

    public void setHideBanned(boolean hideBanned) {
        this.hideBanned = hideBanned;
    }

}
