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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.dbentity.CalendarSettings;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.CalendarUtil;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.dbentity.ItemStatus;
import java.util.Date;
import java.text.ParseException;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionMessage;

/**
 * Action displayes events in Year View
 */

public class ViewYearEvents
    extends CalendarPaginationAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {
        CalendarItemForm calendForm = (CalendarItemForm) form;

        Date jumpToDt = null;
        String jumpTo = calendForm.getJumpToDate();
        if (jumpTo != null && jumpTo.trim().length() != 0) {
            try {
                jumpToDt = (new SimpleDateFormat("yyyy-MM-dd")).parse(jumpTo);
            }
            catch (ParseException ex) {
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE,
                           new ActionMessage("errors.date",
                                           calendForm.getJumpToDate()));
                saveErrors(request, errors);
                return mapping.getInputForward();
            }
        }
        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(
            request);
        String siteId = moduleInstance.getSite().getSiteId();
        String instanceName = moduleInstance.getInstanceName();

        List dbEventsList = null;
        List eventsList = new ArrayList();

        calendForm.loadCalendar();
        calendForm.setAdminViewAll(false);
        calendForm.setView(CalendarSettings.YEAR_VIEW);

        User user = RequestUtils.getUser(request);

        // set number of items in page

        //-------
        GregorianCalendar currCal = new GregorianCalendar();
        GregorianCalendar fromDate = new GregorianCalendar();
        GregorianCalendar toDate = new GregorianCalendar();

        if (request.getParameter("td") == null) {
            if (calendForm.getActiveMonth() != null) {
                currCal.set(java.util.Calendar.MONTH,
                            calendForm.getActiveMonth().intValue() - 1);

                fromDate.set(java.util.Calendar.MONTH,
                             calendForm.getActiveMonth().intValue() - 1);

                toDate.set(java.util.Calendar.MONTH,
                           calendForm.getActiveMonth().intValue() - 1);

            }
            //----
            fromDate.set(java.util.Calendar.DAY_OF_MONTH,
                         currCal.getActualMinimum(java.util.Calendar.
                                                  DAY_OF_MONTH));
            //----
            toDate.set(java.util.Calendar.DAY_OF_MONTH,
                       currCal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
            //----
            if (request.getParameter("go") != null && jumpToDt != null) {
                //go action here

                GregorianCalendar jumpCal = new GregorianCalendar();
                jumpCal.setTime(jumpToDt);

                int month = jumpCal.get(java.util.Calendar.MONTH);
                currCal.set(java.util.Calendar.MONTH, month);
                fromDate.set(java.util.Calendar.MONTH, month);
                toDate.set(java.util.Calendar.MONTH, month);

                int day = jumpCal.get(java.util.Calendar.DAY_OF_MONTH);
                currCal.set(java.util.Calendar.DAY_OF_MONTH, day);
                fromDate.set(java.util.Calendar.DAY_OF_MONTH, day);
                toDate.set(java.util.Calendar.DAY_OF_MONTH, day);

                int nav = (jumpCal.get(java.util.Calendar.YEAR) -
                           currCal.get(java.util.Calendar.YEAR));

                calendForm.setNavYear(String.valueOf(nav));
                calendForm.setNav(String.valueOf(nav));
/*
                currCal = (GregorianCalendar)jumpCal.clone();
                fromDate = (GregorianCalendar)jumpCal.clone();
                toDate = (GregorianCalendar)jumpCal.clone();
*/
                fromDate.set(java.util.Calendar.HOUR, 0);
                fromDate.set(java.util.Calendar.MINUTE, 0);
                fromDate.set(java.util.Calendar.SECOND, 0);
                fromDate.set(java.util.Calendar.MILLISECOND, 0);
                //
                toDate.set(java.util.Calendar.HOUR,
                           toDate.getMaximum(java.util.Calendar.HOUR));
                toDate.set(java.util.Calendar.MINUTE,
                           toDate.getMaximum(java.util.Calendar.MINUTE));
                toDate.set(java.util.Calendar.SECOND,
                           toDate.getMaximum(java.util.Calendar.SECOND));
                toDate.set(java.util.Calendar.MILLISECOND,
                           toDate.getMaximum(java.util.Calendar.MILLISECOND));
            }
            doStartPaginationByYear(calendForm, 1);

            currCal.add(java.util.Calendar.YEAR, Integer.parseInt(navYear));
            fromDate.add(java.util.Calendar.YEAR, Integer.parseInt(navYear));
            toDate.add(java.util.Calendar.YEAR, Integer.parseInt(navYear));

            doEndPaginationByYear(CalendarPaginationAction.INFINIT);
        }
        calendForm.setJumpToDate(new SimpleDateFormat("yyyy-MM-dd").format(
            currCal.getTime()));
        ////
        calendForm.setCurrentYear(Integer.toString(currCal.get(java.util.
            Calendar.YEAR)));
        //set info text
        calendForm.setInfoText(" for " +
                               new SimpleDateFormat("MMMMM yyyy").
                               format(currCal.getTime()));

        //set today Text----
        calendForm.setTodayDate(currCal.getTime());

        if (calendForm.getStatus() == null ||
            calendForm.getStatus().equals("all")) {
            //Get all current events
            dbEventsList = DbUtil.getCalendarEvents(siteId, instanceName,
                ItemStatus.
                PUBLISHED,
                fromDate.getTime(),
                toDate.getTime());

            if (calendForm.getInfoText() != null) {
                calendForm.setInfoText( (String) ("All events") +
                                       calendForm.getInfoText());
            }
            else {
                calendForm.setInfoText("All events");
            }
        }
        else {
            //Get user events
            if (calendForm.getStatus() == null ||
                calendForm.getStatus().equals("mpe")) {
                dbEventsList = DbUtil.getCalendarEvents(user.getId(),
                    ItemStatus.PENDING,
                    fromDate.getTime(), toDate.getTime());

                if (calendForm.getInfoText() != null) {
                    calendForm.setInfoText( (String) ("My pending events") +
                                           calendForm.getInfoText());
                }
                else {
                    calendForm.setInfoText("My pending events");
                }
            }
            else if (calendForm.getStatus().equals("mall")) {
                dbEventsList = DbUtil.getCalendarEvents(user.getId(), null,
                    fromDate.getTime(), toDate.getTime());

                if (calendForm.getInfoText() != null) {
                    calendForm.setInfoText( (String) ("My events") +
                                           calendForm.getInfoText());
                }
                else {
                    calendForm.setInfoText("My events");
                }
            }
            else {
                //Get all current events
                dbEventsList = DbUtil.getCalendarEvents(siteId, instanceName,
                    ItemStatus.
                    PUBLISHED,
                    fromDate.getTime(), toDate.getTime());
                if (calendForm.getInfoText() != null) {
                    calendForm.setInfoText( (String) ("All events") +
                                           calendForm.getInfoText());
                }
                else {
                    calendForm.setInfoText("All events");
                }
            }
        }

        // Pagination
        CalendarSettings setting = DbUtil.getCalendarSettings(request);
        // set number of items per page
        int numberOfItemsPerPage;

        Long nOfItems = setting.getNumberOfItemsPerPage();
        if (nOfItems != null && nOfItems.longValue() != 0) {
            numberOfItemsPerPage = nOfItems.intValue();
        }
        else { //set default value
            numberOfItemsPerPage = 5;
        }
        doStartPagination(calendForm, numberOfItemsPerPage);

        if (dbEventsList != null) {
            if (dbEventsList.size() <= numberOfItemsPerPage) {
                calendForm.setNext(null);
                calendForm.setPrev(null);
            }
            else {
                dbEventsList = dbEventsList.subList(getOffset(),
                    dbEventsList.size());

                if (dbEventsList.size() >= (numberOfItemsPerPage + 1)) {
                    dbEventsList = dbEventsList.subList(0,
                        (numberOfItemsPerPage + 1));
                }
                endPagination(dbEventsList.size());
            }
        }
        //end pegination

        if (dbEventsList != null) {

            int n;
            if ( (numberOfItemsPerPage + 1) == dbEventsList.size())
                n = dbEventsList.size() - 1;
            else {
                n = dbEventsList.size();
            }

            for (int i = 0; i < n; i++) {
                Calendar item = (Calendar) dbEventsList.get(i);
                CalendarItem calendarItem = item.getFirstCalendarItem();

                GregorianCalendar date1 = new GregorianCalendar();

                date1.setTime(item.getStartDate());
                date1.set(java.util.Calendar.HOUR_OF_DAY, 0);
                date1.set(java.util.Calendar.MINUTE, 0);
                date1.set(java.util.Calendar.SECOND, 0);

                CalendarItemForm.Events ev = new CalendarItemForm.Events();
                boolean present = false;

                ev.setDate(item.getStartDate());
                ev.setDay(DgUtil.formatDate(item.getStartDate()));

                List dayEvents = new ArrayList();
                for (int k = i; k < dbEventsList.size(); k++) {
                    CalendarItemForm.EventInfo ei = new CalendarItemForm.
                        EventInfo();

                    Calendar itemNext = (Calendar) dbEventsList.get(k);
                    CalendarItem calItem = itemNext.getFirstCalendarItem();

                    GregorianCalendar date2 = new GregorianCalendar();

                    date2.setTime(itemNext.getStartDate());
                    date2.set(java.util.Calendar.HOUR_OF_DAY, 0);
                    date2.set(java.util.Calendar.MINUTE, 0);
                    date2.set(java.util.Calendar.SECOND, 0);

                    if (date2.getTime().compareTo(date1.getTime()) != 0) {
                        i = k;
                        break;
                    }
                    if (calItem != null) {
                        if (calItem.getTitle() != null) {
                            ei.setTitle(calItem.getTitle());
                        }
                        if (calItem.getDescription() != null) {
                            ei.setDescription(calItem.getDescription());
                        }

                        if (itemNext.getSourceName() != null) {
                            ei.setSourceName(itemNext.getSourceName());
                        }
                        if (itemNext.getSourceUrl() != null) {
                            ei.setSourceUrl(itemNext.getSourceUrl());
                        }

                        if (itemNext.getStartDate() != null) {
                            ei.setStartDate(CalendarUtil.formatDate(itemNext.
                                getStartDate(), itemNext.getStartTBD()));
                        }
                        if (itemNext.getEndDate() != null) {
                            ei.setEndDate(CalendarUtil.formatDate(itemNext.
                                getEndDate(), itemNext.getEndTBD()));
                        }

                        ei.setId(itemNext.getId());

                        dayEvents.add(ei);

                        i = k;
                    }
                }

                if (dayEvents.size() != 0) {
                    ev.setEvents(dayEvents);
                    present = true;
                }
                if (present) {
                    eventsList.add(ev);
                }
            }
        }
        if (eventsList.size() != 0) {
            calendForm.setEventsList(eventsList);
        }
        else {
            calendForm.setEventsList(null);
        }

        List monthsList = new ArrayList();

        Iterator iterator = calendForm.getMonths().iterator();
        while (iterator.hasNext()) {
            org.digijava.module.um.util.Calendar item = new org.digijava.
                module.um.util.Calendar();

            ArrayList rowMonths = new ArrayList();
            for (int j = 0; j < 3 && iterator.hasNext(); j++) {
                item = (org.digijava.module.um.util.Calendar) iterator.next();
                if (item.getCalendarId().equals(new Long(currCal.get(java.util.Calendar.MONTH) + 1))) {
                    item.setToday(true);
                }

                rowMonths.add(item);
            }
            monthsList.add(rowMonths);
        }
        calendForm.setMonthsList(monthsList);

        return mapping.findForward("forward");
    }

}
