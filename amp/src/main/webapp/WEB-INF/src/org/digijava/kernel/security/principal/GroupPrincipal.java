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

package org.digijava.kernel.security.principal;

import java.io.Serializable;
import java.security.Principal;

import org.digijava.kernel.user.Group;

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
