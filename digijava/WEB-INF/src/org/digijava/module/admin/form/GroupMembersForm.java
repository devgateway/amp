/*
 *   GroupMembersForm.java
 *   @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Sep 20, 2003
 * 	 CVS-ID: $Id: GroupMembersForm.java,v 1.1 2005-07-06 10:34:13 rahul Exp $
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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
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