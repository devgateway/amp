/*
 *   GroupPrincipal.java
 *   @Author Mikheil Kapanadze mikheil@powerdot.org
 * 	 Created: Sep 6, 2004
 * 	 CVS-ID: $Id: GroupPrincipal.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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
package org.digijava.kernel.security.principal;

import java.security.Principal;
import org.digijava.kernel.user.Group;
import java.io.Serializable;

public class GroupPrincipal
    implements Principal, Serializable {

    private static final long serialVersionUID = 1;

    private long groupId;
    private String groupName;
    private int hash;

    public GroupPrincipal(long groupId) {
        this.groupId = groupId;
        this.hash = (new Long(groupId)).hashCode();
        this.groupName = null;
    }

    public GroupPrincipal(long userId, String siteName, String groupName) {
        this(userId);
        this.groupName = siteName + " " + groupName;
    }

    public GroupPrincipal(Group group) {
        this(group.getId().longValue(), group.getSite().getName(),
             group.getName());

    }

    public boolean equals(Object another) {
        if (another instanceof GroupPrincipal) {
            GroupPrincipal anotherPrincipal = (GroupPrincipal) another;
            return groupId == anotherPrincipal.groupId;
        }
        else {
            return false;
        }
    }

    public String toString() {
        if (groupName == null) {
            return "<principal type=\"group\" id=\"" + groupId + "\" />";
        }
        else {
            return "<principal type=\"group\" id=\"" + groupId + "\">" +
                groupName +
                "</principal>";
        }
    }

    public String getName() {
        if (groupName == null) {
            return "Group #" + groupId;
        }
        else {
            return groupName;
        }
    }

    public int hashCode() {
        return hash;
    }

    public long getGroupId() {
        return groupId;
    }

}