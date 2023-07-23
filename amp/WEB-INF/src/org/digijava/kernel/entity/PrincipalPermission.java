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

package org.digijava.kernel.entity;

import java.util.Set;
import javax.persistence.*;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DG_PRINCIPAL_PERMISSION")
public class PrincipalPermission{

    public static final int USER_PRINCIPAL = 0;
    public static final int GROUP_PRINCIPAL = 1;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DG_PRINCIPAL_PERMISSION_SEQ")
    @SequenceGenerator(name = "DG_PRINCIPAL_PERMISSION_SEQ", sequenceName = "DG_PRINCIPAL_PERMISSION_SEQ", allocationSize = 1)
    @Column(name = "PRINCIPAL_PERMISSION_ID")
    private Long principalPermissionId;

    @Column(name = "PRINCIPAL_TYPE")
    private int principalType;

    @Column(name = "TARGET_ID")
    private Long targetId;

    @Column(name = "PERMISSION_CLASS", length = 255)
    private String permissionClass;

    @OneToMany(mappedBy = "principalPermission", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("param_index ASC")
    private Set<PrincipalPermissionParameter> parameters = new HashSet<>();




    public String getPermissionClass() {
        return permissionClass;
    }

    public void setPermissionClass(String permissionClass) {
        this.permissionClass = permissionClass;
    }

    public Long getPrincipalPermissionId() {
        return principalPermissionId;
    }

    public void setPrincipalPermissionId(Long principalPermissionId) {
        this.principalPermissionId = principalPermissionId;
    }

    public int getPrincipalType() {
        return principalType;
    }

    public void setPrincipalType(int principalType) {
        this.principalType = principalType;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public Set getParameters() {
        return parameters;
    }

    public void setParameters(Set parameters) {
        this.parameters = parameters;
    }

}
