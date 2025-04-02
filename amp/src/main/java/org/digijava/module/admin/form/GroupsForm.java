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

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class GroupsForm
    extends ActionForm {

    private ArrayList groups;
    private Long groupId;
    private String groupName;
    private ArrayList otherGroups;

    public static class GroupInfo {
        private Long id;
        private String name;
        private boolean defaultGroup;
        private boolean empty;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isDefaultGroup() {
            return defaultGroup;
        }

        public void setDefaultGroup(boolean defaultGroup) {
            this.defaultGroup = defaultGroup;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        public boolean isEmpty() {
            return empty;
        }

    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        groupId = null;
        groupName = null;
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {
        ActionErrors errors = new ActionErrors();

        if ( (groupName == null) || (groupName.trim().length() == 0)) {
            errors.add(null,
                       new ActionMessage("error.admin.groupNameEmpty"));
        }

        return errors.isEmpty() ? null : errors;
    }

    public ArrayList getGroups() {
        return groups;
    }

    public void setGroups(ArrayList groups) {
        this.groups = groups;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList getOtherGroups() {
        return otherGroups;
    }

    public void setOtherGroups(ArrayList otherGroups) {
        this.otherGroups = otherGroups;
    }

}
