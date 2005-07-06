/*
 *   Group.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Jul 2, 2003
 * 	 CVS-ID: $Id: Group.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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
package org.digijava.kernel.user;

import org.digijava.kernel.Entity;
import java.security.Principal;
import org.digijava.kernel.request.Site;
import java.util.Set;
import java.util.HashMap;
import java.io.Serializable;
import org.digijava.kernel.security.SitePermission;
import org.digijava.kernel.security.ResourcePermission;

public class Group
    extends Entity
    implements Principal, Serializable {

    public static final String ADMINISTRATORS = "ADM";
    public static final String MEMBERS = "MEM";
    public static final String TRANSLATORS = "TRN";
    public static final String EDITORS = "EDT";

    public static final String ADMINISTRATORS_NAME = "Administrators";
    public static final String MEMBERS_NAME = "Members";
    public static final String TRANSLATORS_NAME = "Translators";
    public static final String EDITORS_NAME = "Editors";

    public static final HashMap defaultGroups;

    static {
        defaultGroups = new HashMap();
        defaultGroups.put(ADMINISTRATORS, ADMINISTRATORS_NAME);
        defaultGroups.put(MEMBERS, MEMBERS_NAME);
        defaultGroups.put(TRANSLATORS, TRANSLATORS_NAME);
        defaultGroups.put(EDITORS, EDITORS_NAME);
    }

    private Site site;
    private Set permissions;
    private String key;
    private Set users;
    private Long parentId;
    private boolean inheritSecurity;

    public Group() {
    }

    public Group(Site site, String groupName, String key) {
        super(groupName);
        this.site = site;
        this.key = key;
    }

    public String toString() {
        return "[" + this.getClass().getName() + "]" + this.name;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Set getPermissions() {
        return permissions;
    }

    public void setPermissions(Set permissions) {
        this.permissions = permissions;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isDefaultGroup() {
        return defaultGroups.get(key) != null;
    }

    public boolean isAdminGroup() {
        if( key == null ) return false;
        return key.equals(ADMINISTRATORS);
    }

    public boolean isMemberGroup() {
        if( key == null ) return false;
        return key.equals(MEMBERS);
    }

    public boolean isTranslatorGroup() {
        if( key == null ) return false;
        return key.equals(TRANSLATORS);
    }

    public boolean isEditorGroup() {
        if( key == null ) return false;
        return key.equals(EDITORS);
    }

    /**
     * Returns required action(s) for default group and null for non-default
         * one. For example, ADMIN for administrators group, READ for members group,
     * etc
     * @return Returns required action(s)
     */
    public String getRequiredActions() {
        if (isAdminGroup()) {
            return ResourcePermission.ADMIN;
        }
        else
        if (isMemberGroup()) {
            return SitePermission.READ;
        }
        else
        if (isTranslatorGroup()) {
            return SitePermission.TRANSLATE;
        }
        else
        if (isEditorGroup()) {
            return SitePermission.WRITE;
        }
        else
            return null;

    }

    public Set getUsers() {
        return users;
    }

    public void setUsers(Set users) {
        this.users = users;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public boolean isInheritSecurity() {
        return inheritSecurity;
    }

    public void setInheritSecurity(boolean inheritSecurity) {
        this.inheritSecurity = inheritSecurity;
    }

   /**
    * Returs group name
    * @deprecated  You should use getName() instead
    * @return group name
    */

    public String getGroupName() {
        return name;
    }


}