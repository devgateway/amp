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

        if (calendForm.getCountry().equals(Calendar.noneCountryIso)){
          event.setCountry(null);
        } else {
          event.setCountry(calendForm.getCountry());
        }
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
