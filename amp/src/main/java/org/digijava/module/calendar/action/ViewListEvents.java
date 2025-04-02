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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.dbentity.CalendarSettings;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.CalendarUtil;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.dbentity.ItemStatus;

/**
 * Action displayes events in List View
 */

public class ViewListEvents
    extends CalendarPaginationAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CalendarItemForm calendForm = (CalendarItemForm) form;

        List dbEventsList = null;
        List eventsList = new ArrayList();
        java.util.Calendar fromDate;
        java.util.Calendar toDate;
        int length = 0;

        User user = RequestUtils.getUser(request);

        calendForm.setAdminViewAll(false);
        calendForm.setView(CalendarSettings.LIST_VIEW);

        // get Setting
        CalendarSettings setting = DbUtil.getCalendarSettings(request);

        // set number of items in page
    Long nOfItems = setting.getNumberOfItemsPerPage();
    if (nOfItems != null && nOfItems.longValue() != 0) {
      length = nOfItems.intValue();
    }
    else { //set default value
      length = 5;
    }

        // Pagination
        doStartPagination(calendForm, length);

        if (calendForm.getStatus() == null ||
            calendForm.getStatus().equals("all")) {
            //Get all current events
            dbEventsList = DbUtil.getCalendarEvents(null, ItemStatus.PUBLISHED,
                request, getOffset(), length + 1, getDirection());
            calendForm.setInfoText("All events");
        }
        else {
            //Get user events
            if (calendForm.getStatus().equals("mpe")) {
                dbEventsList = DbUtil.getCalendarEvents(user.getId(),
                    ItemStatus.PENDING, request, getOffset(), length + 1,
                    getDirection());
                calendForm.setInfoText("My pending events");
            }
            else if (calendForm.getStatus().equals("mall")) {
                dbEventsList = DbUtil.getCalendarEvents(user.getId(), null,
                    request,
                    getOffset(), length + 1, getDirection());
                calendForm.setInfoText("My events");
            }
            else {
                //Get all current events
                dbEventsList = DbUtil.getCalendarEvents(null,
                    ItemStatus.PUBLISHED,
                    request, getOffset(), length + 1, getDirection());
                calendForm.setInfoText("All events");
            }

        }

        doEndPagination( ( (dbEventsList != null) ? dbEventsList.size() : 0));

        if (dbEventsList != null && dbEventsList.size() > 0) {
            int n;
            if ( (length + 1) == dbEventsList.size())
                n = dbEventsList.size() - 1;
            else
                n = dbEventsList.size();

            java.util.Calendar date = null;
            CalendarItemForm.Events eventsList2 = null;
            for (int i = 0; i < n; i++) {
              Calendar item = (Calendar) dbEventsList.get(i);
              CalendarItem calendarItem = item.getFirstCalendarItem();

              if (date == null ||
                  (item.getStartDate() != null &&
                   date.getTime().compareTo(item.getStartDate()) != 0)) {

                if (eventsList2 != null)
                  eventsList.add(eventsList2);

                eventsList2 = new CalendarItemForm.Events();
                eventsList2.setEvents(new ArrayList());

                eventsList2.setDay(CalendarUtil.formatDate(item.getStartDate(), item.getStartTBD()));
              }

              CalendarItemForm.EventInfo ei = new CalendarItemForm.EventInfo();
              if (calendarItem != null) {
                if (calendarItem.getTitle() != null) {
                  ei.setTitle(calendarItem.getTitle());
                }
                if (calendarItem.getDescription() != null) {
                  ei.setDescription(calendarItem.getDescription());
                }
                if (item.getStartDate() != null) {
                  ei.setStartDate(CalendarUtil.formatDate(item.getStartDate(), item.getStartTBD()));
                }
                if (item.getEndDate() != null) {
                  ei.setEndDate(CalendarUtil.formatDate(item.getEndDate(), item.getEndTBD()));
                }

                if (item.getSourceName() != null) {
                  ei.setSourceName(item.getSourceName());
                }
                if (item.getSourceUrl() != null) {
                  ei.setSourceUrl(item.getSourceUrl());
                }

                ei.setId(item.getId());

                eventsList2.getEvents().add(ei);

                date = new GregorianCalendar();
                date.setTime(item.getStartDate());
                date.set(java.util.Calendar.MINUTE, 0);
                date.set(java.util.Calendar.HOUR_OF_DAY, 0);
                date.set(java.util.Calendar.SECOND, 0);
              }
            }

            if (eventsList2 != null)
                eventsList.add(eventsList2);

            calendForm.setEventsList(eventsList);
        }
        else
            calendForm.setEventsList(null);

        return mapping.findForward("forward");
    }
}
