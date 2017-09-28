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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.Collection;
import org.digijava.kernel.entity.Locale;
import java.util.ArrayList;
import java.util.List;
import org.digijava.kernel.util.SiteConfigUtils;
import java.util.HashMap;
import java.util.Collections;
import java.util.HashSet;
import org.digijava.kernel.request.SiteDomain;
import java.util.Iterator;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.ResourcePermission;

public class GroupMembersForm
    extends ActionForm {

    private Long groupId;
    private Collection users;
    private Long userId;
    private String usersToAdd;
    private String groupName;

    public Long getGroupId() {
        return groupId;
    }

    public void reset() {
        usersToAdd = null;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Collection getUsers() {
        return users;
    }

    public void setUsers(Collection users) {
        this.users = users;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsersToAdd() {
        return usersToAdd;
    }

    public void setUsersToAdd(String usersToAdd) {
        this.usersToAdd = usersToAdd;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

}
