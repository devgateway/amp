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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.text.DgDateFormatSymbols;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.dbentity.CalendarSettings;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.common.util.ModuleUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Action displayes events in Month View
 */

public class ViewMonthEvents
    extends CalendarPaginationAction {

    public static final int numOfCharsInTitleLocal = 10;

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CalendarItemForm calendForm = (CalendarItemForm) form;

        List dbEventsList = null;
        List eventsList = new ArrayList();
        int offset = 0;
        int length = 1000;

        Locale currentLocale = RequestUtils.getNavigationLanguage(request);
        java.util.Locale javaLocale = new java.util.Locale(currentLocale.getCode());

        DgDateFormatSymbols dgDateFormatSymbols = new DgDateFormatSymbols(javaLocale);
        String[] shortWeekDays = dgDateFormatSymbols.getShortWeekdays();
        ArrayList weekDays = new ArrayList();

        for(int i = 1; i<shortWeekDays.length; i++) {
            weekDays.add(shortWeekDays[i].substring(0, 3));
        }

        calendForm.setWeekDays(weekDays);

        calendForm.loadCalendar();
        calendForm.setAdminViewAll(false);
        calendForm.setView(CalendarSettings.MONTH_VIEW);

        User user = RequestUtils.getUser(request);

        GregorianCalendar currCal = new GregorianCalendar();
        GregorianCalendar fromDate = new GregorianCalendar();
        GregorianCalendar toDate = new GregorianCalendar();

        if ( (calendForm.getJumpToMonth() != null) &&
            (calendForm.getJumpToYear() != null)) {
            int nav = 12 *
                (Integer.parseInt(calendForm.getJumpToYear()) -
                 currCal.get(java.util.Calendar.YEAR)) +
                (Integer.parseInt(calendForm.getJumpToMonth()) -
                 currCal.get(java.util.Calendar.MONTH) - 1);
            calendForm.setNav(Integer.toString(nav));
        }

        doStartPagination(calendForm, 1);
        currCal.add(java.util.Calendar.MONTH, getNavigation());
        fromDate.add(java.util.Calendar.MONTH, getNavigation());
        toDate.add(java.util.Calendar.MONTH, getNavigation());
        doEndPagination(CalendarPaginationAction.INFINIT);

        //----------------
        calendForm.setJumpToMonth(Integer.toString(currCal.get(java.util.
            Calendar.MONTH) + 1));
        calendForm.setJumpToYear(Integer.toString(currCal.get(java.util.
            Calendar.YEAR)));

        //set info text
        calendForm.setInfoText(new SimpleDateFormat("MMMMM").format(
            currCal.getTime()) + " " +
                               new SimpleDateFormat("yyyy").format(
                                   currCal.getTime()));

        //----
        fromDate.set(java.util.Calendar.DAY_OF_MONTH,
                     currCal.getActualMinimum(java.util.Calendar.
                                              DAY_OF_MONTH));
        fromDate.add(java.util.Calendar.DAY_OF_MONTH,
                     - (fromDate.get(java.util.Calendar.DAY_OF_WEEK) -
                        1));
        //----
        toDate.set(java.util.Calendar.DAY_OF_MONTH,
                   currCal.
                   getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
        toDate.add(java.util.Calendar.DAY_OF_MONTH,
                   7 - toDate.get(java.util.Calendar.DAY_OF_WEEK));

        if (calendForm.getStatus() == null ||
            calendForm.getStatus().equals("all")) {
            //Get all current events
            dbEventsList = DbUtil.getCalendarEvents(ItemStatus.
                PUBLISHED, request, offset, length + 1,
                fromDate.getTime(), toDate.getTime());
            calendForm.setInfoText("All events");
        }
        else {
            //Get user events
            if (calendForm.getStatus().equals("mpe")) {
                dbEventsList = DbUtil.getCalendarEvents(user.getId(),
                    ItemStatus.PENDING, offset, length,
                    fromDate.getTime(), toDate.getTime());
                calendForm.setInfoText("My pending events");
            }
            else if (calendForm.getStatus().equals("mall")) {
                dbEventsList = DbUtil.getCalendarEvents(user.getId(), null,
                    offset, length, fromDate.getTime(), toDate.getTime());
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

        if (dbEventsList != null) {

            GregorianCalendar date = new GregorianCalendar();
            date = fromDate;
            date.set(java.util.Calendar.MINUTE,
                     date.getActualMaximum(java.util.Calendar.MINUTE) / 2);
            date.set(java.util.Calendar.HOUR_OF_DAY,
                     date.getActualMaximum(java.util.Calendar.HOUR_OF_DAY) / 2);
            date.set(java.util.Calendar.SECOND,
                     date.getActualMaximum(java.util.Calendar.SECOND) / 2);
            boolean monthDay = false;

            if (date.get(java.util.Calendar.DAY_OF_MONTH) ==
                date.getActualMinimum(java.util.Calendar.DAY_OF_MONTH)) {
                monthDay = true;
            }

            while (date.getTime().compareTo(toDate.getTime()) <= 0) {

                List weekEvents = new ArrayList();

                for (int i = 0;
                     (i < 7) &&
                     (date.getTime().compareTo(toDate.getTime()) <= 0);
                     i++) {
                    CalendarItemForm.Events ev = new CalendarItemForm.
                        Events();

                    ev.setDate(date.getTime());
                    ev.setMonthDay(monthDay);

                    if (date.get(java.util.Calendar.DAY_OF_MONTH) ==
                        date.getActualMinimum(java.util.Calendar.
                                              DAY_OF_MONTH)) {
                        ev.setDay(new SimpleDateFormat("MMMMM").format(
                            date.getTime()).substring(0, 3) +
                                  new SimpleDateFormat("d").format(date.
                            getTime()));
                    }
                    else {
                        ev.setDay(new SimpleDateFormat("d").format(date.
                            getTime()));
                    }

                    ev.setToday(false);
                    GregorianCalendar today = new GregorianCalendar();
                    if ( (date.get(java.util.Calendar.DAY_OF_MONTH) ==
                          today.get(java.util.Calendar.DAY_OF_MONTH)) &&
                        (date.get(java.util.Calendar.MONTH) ==
                         today.get(java.util.Calendar.MONTH)) &&
                        (date.get(java.util.Calendar.YEAR) ==
                         today.get(java.util.Calendar.YEAR))) {

                        ev.setToday(true);
                        ev.setMonthDay(false);
                    }

                    List dayEvents = new ArrayList();
                    Iterator iterator = dbEventsList.iterator();
                    while (iterator.hasNext()) {
                        CalendarItemForm.EventInfo ei = new
                            CalendarItemForm.EventInfo();

                        Calendar item = (Calendar) iterator.next();
                        CalendarItem calendarItem = item.
                            getFirstCalendarItem();

                        GregorianCalendar startDate = new
                            GregorianCalendar();
                        GregorianCalendar endDate = new
                            GregorianCalendar();

                        startDate.setTime(item.getStartDate());
                        // reseting hour & minute fields
                        startDate.set(java.util.Calendar.HOUR_OF_DAY,
                                      startDate.
                                      getActualMinimum(java.util.Calendar.
                            HOUR_OF_DAY));
                        startDate.set(java.util.Calendar.MINUTE,
                                      startDate.
                                      getActualMinimum(java.util.Calendar.
                            MINUTE));

                        endDate.setTime(item.getEndDate());
                        // reseting hour & minute fields
                        endDate.set(java.util.Calendar.HOUR_OF_DAY,
                                    endDate.
                                    getActualMaximum(java.util.Calendar.
                            HOUR_OF_DAY));
                        endDate.set(java.util.Calendar.MINUTE,
                                    endDate.
                                    getActualMaximum(java.util.Calendar.MINUTE));

                        if ( (startDate.getTime().compareTo(ev.getDate()) <=
                              0) &&
                            (endDate.getTime().compareTo(ev.getDate()) >=
                             0)) {

                            if (calendarItem != null) {
                                if (calendarItem.getTitle() != null) {
                                    ei.setTitle(ModuleUtil.truncateWords(
                                        calendarItem.getTitle(),
                                        numOfCharsInTitleLocal));
                                }
                                if (calendarItem != null) {
                                    if (calendarItem.getDescription() != null) {
                                        ei.setDescription(calendarItem.
                                            getDescription());
                                    }
                                    if (item.getSourceName() != null) {
                                        ei.setSourceName(item.getSourceName());
                                    }
                                    if (item.getSourceUrl() != null) {
                                        ei.setSourceUrl(item.getSourceUrl());
                                    }

                                    ei.setId(item.getId());

                                    dayEvents.add(ei);
                                }
                            }
                        }
                    }
                    if (dayEvents.size() == 0) {
                        ev.setEvents(null);
                    }
                    else {
                        if (dayEvents.size() > 3) {
                            ev.setMore(true);
                            ev.setEvents(dayEvents.subList(0, 3));
                        }
                        else {
                            ev.setEvents(dayEvents);
                            ev.setMore(false);
                        }
                    }
                    weekEvents.add(ev);

                    if (date.get(java.util.Calendar.DAY_OF_MONTH) ==
                        date.getActualMaximum(java.util.Calendar.
                                              DAY_OF_MONTH)) {
                        monthDay = !monthDay;
                    }
                    date.add(java.util.Calendar.DAY_OF_MONTH, 1);
                }
                eventsList.add(weekEvents);
            }
            calendForm.setEventsList(eventsList);
        }
        else {
            calendForm.setEventsList(null);
        }

        return mapping.findForward("forward");
    }
}
