/*
 *   PrincipalPermissionParameter.java
 *   @Author Mikheil Kapanadze mikheil@powerdot.org
 * 	 Created: Sep 13, 2004
     * 	 CVS-ID: $Id: PrincipalPermissionParameter.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class PrincipalPermissionParameter
    implements Serializable {

    private String parameterClass;
    private String parameterValue;

    private PrincipalPermission principalPermission;
    private int index;

    public boolean equals(Object other) {
        if (other instanceof PrincipalPermissionParameter) {
            PrincipalPermissionParameter castOther = (
                PrincipalPermissionParameter)
                other;
            return new EqualsBuilder()
                .append(this.getPrincipalPermission() == null ? null :
                        this.getPrincipalPermission().getPrincipalPermissionId(),
                        castOther.getPrincipalPermission() == null ? null :
                        castOther.getPrincipalPermission().
                        getPrincipalPermissionId())
                .append(this.getIndex(), castOther.getIndex())
                .isEquals();
        }
        else {
            return false;
        }
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(this.getPrincipalPermission() == null ? null :
                    this.getPrincipalPermission().getPrincipalPermissionId())
            .append(this.getIndex())
            .toHashCode();
    }

    public String getParameterClass() {
        return parameterClass;
    }

    public void setParameterClass(String parameterClass) {
        this.parameterClass = parameterClass;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public PrincipalPermission getPrincipalPermission() {
        return principalPermission;
    }

    public void setPrincipalPermission(PrincipalPermission principalPermission) {
        this.principalPermission = principalPermission;
    }

}