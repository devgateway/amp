/*
 *   UserLoginInfo.java
 * 	 @Author Mikheil Kapanadze mikheil@powerdot.org
 * 	 Created: Sep 24, 2004
 *	 CVS-ID: $Id: UserLoginInfo.java,v 1.1 2005-07-06 10:34:08 rahul Exp $
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

import java.util.Date;

public class UserLoginInfo {
    private Long id;
    private Date lastVisit;
    private Date secondToLastVisit;
    private long numberOfSessions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    public long getNumberOfSessions() {
        return numberOfSessions;
    }

    public void setNumberOfSessions(long numberOfSessions) {
        this.numberOfSessions = numberOfSessions;
    }

    public Date getSecondToLastVisit() {
        return secondToLastVisit;
    }

    public void setSecondToLastVisit(Date secondToLastVisit) {
        this.secondToLastVisit = secondToLastVisit;
    }

}