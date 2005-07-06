/*
 *   EditCalendarItem.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 17, 2003
 * 	 CVS-ID: $Id: EditCalendarItem.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
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

package org.digijava.module.calendar.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.CalendarPopulator;
import org.digijava.module.calendar.util.DbUtil;

/**
 * Action edits an existing event item identified by activeCalendarItem identity(if not null), updates it into database and redirects back to the action from which it was invoked
 */

public class EditCalendarItem
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CalendarItemForm calendForm = (CalendarItemForm) form;
        Calendar event = null;

        if (calendForm.getActiveCalendarItem() != null) {

            event = DbUtil.getCalendarItem(calendForm.getActiveCalendarItem());
            CalendarItem calendarItem = event.getFirstCalendarItem();

            calendarItem.setCalendar(event);
            calendarItem.setTitle(calendForm.getTitle());
            calendarItem.setDescription(calendForm.getDescription());
            calendarItem.setLanguage(calendForm.getSelectedLanguage());

            event.setCountry(calendForm.getCountry());
            event.setLocation(calendForm.getLocation());

            event.setSourceName(calendForm.getSourceName());
            event.setSourceUrl(calendForm.getSourceUrl());

            event.setEnableHTML(calendForm.isEnableHTML());
            event.setEnableSmiles(calendForm.isEnableSmiles());

            //start Date
            CalendarPopulator populator = new CalendarPopulator(calendForm);
            event.setStartDate(populator.getStart().getTime());
            event.setStartTBD(populator.getStartTBD());
            
            // end Date
            event.setEndDate(populator.getEnd().getTime());
            event.setEndTBD(populator.getEndTBD());

            //update here
            DbUtil.updateCalendarEvent(event);
        }

        if (calendForm.getReturnUrl() != null) {
            return new ActionForward(calendForm.getReturnUrl(), true);
        }
        else {
            return new ActionForward("/calendar", true);
        }
    }

}