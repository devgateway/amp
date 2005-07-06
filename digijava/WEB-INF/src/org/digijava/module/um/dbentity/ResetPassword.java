/*
 *   ResetPassword.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created:
 *   CVS-ID: $Id: ResetPassword.java,v 1.1 2005-07-06 10:34:30 rahul Exp $
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

package org.digijava.module.um.dbentity;

import java.util.Date;

public class ResetPassword {

    private long userId;
    private String code;
    private java.util.Date resetDate;

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