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

package org.digijava.module.um.dbentity;

import java.util.Date;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DG_RESET_PASSWORD")
public class ResetPassword {
    @Id
    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "RESET_DATE", length = 400)
    private Date resetDate;

    @Column(name = "CODE", length = 100)
    private String code;

    public ResetPassword() {}

    public ResetPassword(long userId, String code) {
        this.userId = userId;
        this.code = code;
        this.resetDate = new Date();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public java.util.Date getResetDate() {
        return resetDate;
    }

    public void setResetDate(java.util.Date resetDate) {
        this.resetDate = resetDate;
    }

}
