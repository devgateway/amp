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

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "DG_PRINCIPAL_PERMISSION_PARAM")
public class PrincipalPermissionParameter
    implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "PRINCIPAL_PERMISSION_ID")
    private PrincipalPermission principalPermission;

    @Id
    @Column(name = "PARAM_INDEX")
    private int index;

    @Column(name = "PARAM_CLASS")
    private String parameterClass;

    @Column(name = "PARAM_VALUE", columnDefinition = "text")
    private String parameterValue;


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
