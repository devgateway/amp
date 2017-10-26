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

package org.digijava.kernel.user;

import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.digijava.kernel.entity.Entity;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.security.ResourcePermission;
import org.digijava.kernel.security.SitePermission;

import com.google.common.collect.ImmutableSet;

public class Group
    extends Entity
    implements Principal, Serializable {

    public static final String ADMINISTRATORS = "ADM";
    public static final String SUPER_ADMINISTRATORS = "SADM";
    public static final String MEMBERS = "MEM";
    public static final String TRANSLATORS = "TRN";
    public static final String EDITORS = "EDT";
    public static final String PLEDGERS = "PLE";
    public static final String NATIONAL_COORDINATORS = "NCO";

    public static final String ADMINISTRATORS_NAME = "Administrators";
    public static final String SUPER_ADMINISTRATORS_NAME = "Super Administrators";
    public static final String MEMBERS_NAME = "Members";
    public static final String TRANSLATORS_NAME = "Translators";
    public static final String EDITORS_NAME = "Editors";
    public static final String PLEDGERS_NAME = "Pledgers";
    public static final String NATIONAL_COORDINATORS_NAME = "National Coordinators";

    public static final HashMap defaultGroups;

    static {
        defaultGroups = new HashMap();
        defaultGroups.put(ADMINISTRATORS, ADMINISTRATORS_NAME);
        defaultGroups.put(SUPER_ADMINISTRATORS, SUPER_ADMINISTRATORS_NAME);
        defaultGroups.put(MEMBERS, MEMBERS_NAME);
        defaultGroups.put(TRANSLATORS, TRANSLATORS_NAME);
        defaultGroups.put(EDITORS, EDITORS_NAME);
        defaultGroups.put(PLEDGERS, PLEDGERS_NAME);
        defaultGroups.put(NATIONAL_COORDINATORS, NATIONAL_COORDINATORS_NAME);
    }

    private Site site;
    private String key;
    private Set users;
    private Long parentId;
    private boolean inheritSecurity;
    
    public static final Set<String> ADMIN_GROUPS = ImmutableSet.of(Group.ADMINISTRATORS, Group.SUPER_ADMINISTRATORS);

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

    /**
     * Get Permissions
     * @return Set permissions
     * @deprecated this method is subject of remove
     */
    public Set getPermissions() {
        return new HashSet();
    }

    /**
     * Set permissions
     * @param permissions Set
     * @deprecated this method will throw error
     */
    public void setPermissions(Set permissions) {
        throw new IllegalStateException("group.setPermissions() method is not supported any more");
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

    public boolean isSuperAdminGroup() {
        if (key == null) {
            return false;
        }

        return key.equals(SUPER_ADMINISTRATORS);
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

    public Boolean isNationalCoordinatorGroup(){
        if( key == null ) return false;
        return key.equals(NATIONAL_COORDINATORS);       
    }
    
    /**
     * Returns required action(s) for default group and null for non-default
         * one. For example, ADMIN for administrators group, READ for members group,
     * etc
     * @return Returns required action(s)
     */
    public String getRequiredActions() {
        if (isSuperAdminGroup()) {
            return ResourcePermission.SUPER_ADMIN;
        } else if (isAdminGroup()) {
            return ResourcePermission.ADMIN;
        } else if (isMemberGroup()) {
            return SitePermission.READ;
        } else if (isTranslatorGroup()) {
            return SitePermission.TRANSLATE;
        } else if (isEditorGroup()) {
            return SitePermission.WRITE;
        }

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
