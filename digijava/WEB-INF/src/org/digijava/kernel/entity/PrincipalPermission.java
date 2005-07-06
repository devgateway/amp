/*
 *   PrincipalPermission.java
 *   @Author Mikheil Kapanadze mikheil@powerdot.org
 * 	 Created: Sep 6, 2004
     * 	 CVS-ID: $Id: PrincipalPermission.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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
package org.digijava.kernel.entity;

import java.util.Set;

public class PrincipalPermission{

    public static final int USER_PRINCIPAL = 0;
    public static final int GROUP_PRINCIPAL = 1;

    private int principalType;
    private long targetId;
    private String permissionClass;
    private Long principalPermissionId;
    private Set parameters;

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