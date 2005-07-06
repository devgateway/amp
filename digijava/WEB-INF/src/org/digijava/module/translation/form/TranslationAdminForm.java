/*
 *   TranslationAdminForm.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 15, 2004
 * 	 CVS-ID: $Id: TranslationAdminForm.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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


package org.digijava.module.translation.form;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import java.util.Collection;
import java.util.ArrayList;

public class TranslationAdminForm
      extends ActionForm {

    public static class GroupInfo {
	private Long id;
	private String name;

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
    }

    public static class UserInfo {

	private Long id;
	private String firstNames;
	private String lastName;
	private String email;

	public Long getId() {
	    return id;
	}

	public void setId(Long id) {
	    this.id = id;
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

    private List groups;
    private Long groupId;
    private String groupName;

    private Long userId;
    private String searchUserInfo;
    private List users;

    private String siteName;

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

    public List getGroups() {
	return groups;
    }

    public void setGroups(List groups) {
	this.groups = groups;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
	groupId = null;
	groupName = null;

	siteName = null;
	userId = null;

	//searchUserInfo = null;
    }

    public String getSiteName() {
	return siteName;
    }

    public void setSiteName(String siteName) {
	this.siteName = siteName;
    }

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public String getSearchUserInfo() {
	return searchUserInfo;
    }

    public void setSearchUserInfo(String searchUserInfo) {
	this.searchUserInfo = searchUserInfo;
    }

    public List getUsers() {
	return users;
    }

    public void setUsers(List users) {
	this.users = users;
    }

}