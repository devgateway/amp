/*
 *   TranslationPermissionsForm.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 15, 2004
 * 	 CVS-ID: $Id: TranslationPermissionsForm.java,v 1.1 2005-07-06 10:34:20 rahul Exp $
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

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class TranslationPermissionsForm
      extends ActionForm {

    public static class SiteInfo {
	private Long id;
	private String name;

	public SiteInfo() {
	}

	public SiteInfo(Long id, String name) {
	    this.id = id;
	    this.name = name;
	}

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

    public static class PermissionInfo {
	private long id;
	private Long siteId;
	private String localeId;

	public PermissionInfo() {

	}

	public PermissionInfo(long id, Long siteId, String localeId) {
	    this.id = id;
	    this.siteId = siteId;
	    this.localeId = localeId;
	}

	public long getId() {
	    return id;
	}

	public void setId(long id) {
	    this.id = id;
	}

	public void setLocaleId(String localeId) {
	    this.localeId = localeId;
	}

	public String getLocaleId() {
	    return localeId;
	}

	public Long getSiteId() {
	    return siteId;
	}

	public void setSiteId(Long siteId) {
	    this.siteId = siteId;
	}
    }

    private Long groupId;
    private String groupName;

    private Long userId;
    private String firstNames;
    private String lastName;

    private String siteName;

    private ArrayList permissions;
    private Collection sites;
    private Collection languages;

    private boolean userMode;

    public PermissionInfo getPermission(int index) {
	PermissionInfo info = null;
	int actualSize = permissions.size();
	if (index >= actualSize) {
	    // Expand the list
	    for (int i = 0; i <= index - actualSize; i++) {
		permissions.add(new PermissionInfo());
	    }
	}

	return (PermissionInfo) permissions.get(index);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
	permissions = new ArrayList();
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

    public Collection getLanguages() {
	return languages;
    }

    public void setLanguages(Collection languages) {
	this.languages = languages;
    }

    public ArrayList getPermissions() {
	return permissions;
    }

    public void setPermissions(ArrayList permissions) {
	this.permissions = permissions;
    }

    public Collection getSites() {
	return sites;
    }

    public void setSites(Collection sites) {
	this.sites = sites;
    }

    public String getSiteName() {
	return siteName;
    }

    public void setSiteName(String siteName) {
	this.siteName = siteName;
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

    public Long getUserId() {
	return userId;
    }

    public void setUserId(Long userId) {
	this.userId = userId;
    }

    public boolean isUserMode() {
	return userMode;
    }

    public void setUserMode(boolean userMode) {
	this.userMode = userMode;
    }

}