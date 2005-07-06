/*
*   Calendar.java
*   @Author Maka Kharalashvili maka@digijava.org
*   Created:
*   CVS-ID: $Id: Calendar.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

package org.digijava.module.um.util;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Calendar {

    private Long calendarId;
    private String calendarText;
    private boolean today;

    public Calendar(Long calendarId, String calendarText) {
        this.calendarId = calendarId;
        this.calendarText = calendarText;
    }

    public Calendar() {
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }

    public String getCalendarText() {
        return calendarText;
    }

    public void setCalendarText(String calendarText) {
        this.calendarText = calendarText;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }
}