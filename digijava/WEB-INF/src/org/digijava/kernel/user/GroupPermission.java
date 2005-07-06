/*
 *   DgGroupPermission.java
 * 	 @Author Mikheil Kapanadze mikheil@digijava.org
 * 	 Created: Aug 16, 2003
 * 	 CVS-ID: $Id: GroupPermission.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

import org.digijava.kernel.security.*;

public class GroupPermission {

    public static final int SITE_PERMISSION = 0;
    public static final int MODULE_INSTANCE_PERMISSION = 1;

    private long groupPermissionId;
    private Group group;
    private int permissionType;
    private String targetName;
    private String actions;

    public GroupPermission() {
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public long getGroupPermissionId() {
        return groupPermissionId;
    }

    public void setGroupPermissionId(long groupPermissionId) {
        this.groupPermissionId = groupPermissionId;
    }

    public int getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(int permissionType) {
        this.permissionType = permissionType;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}