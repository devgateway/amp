/*
 *   GroupsForm.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 20, 2003
 * 	 CVS-ID: $Id: GroupsForm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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
package org.digijava.module.admin.form;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
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
                       new ActionError("error.admin.groupNameEmpty"));
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