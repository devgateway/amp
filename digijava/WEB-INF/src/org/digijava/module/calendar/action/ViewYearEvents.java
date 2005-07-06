/*
 *   ViewYearEvents.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 25, 2003
 * 	 CVS-ID: $Id: ViewYearEvents.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
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

        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);
        String siteId = moduleInstance.getSite().getSiteId();
        String instanceName = moduleInstance.getInstanceName();


        List dbEventsList = null;
        List eventsList = new ArrayList();

        int offset = 0;
        int length = 0;

        calendForm.loadCalendar();
        calendForm.setAdminViewAll(false);
        calendForm.setView(CalendarSettings.YEAR_VIEW);

        User user = RequestUtils.getUser(request);

        // set number of items in page
        length = moduleInstance.getNumberOfItemsInTeaser().intValue();

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
            if (request.getParameter("go") != null && calendForm.getJumpToDate() != null) {
                //go action here
                String jumpToDate = calendForm.getJumpToDate();
                String day = new String();
                String month = new String();
                String year = new String();

                StringTokenizer tokenizer = new StringTokenizer(jumpToDate, "-");
                if (tokenizer.hasMoreTokens()) {
                    year = tokenizer.nextToken().trim();

                        /*currCal.set(java.util.Calendar.YEAR, Integer.parseInt(t));
                         fromDate.set(java.util.Calendar.YEAR, Integer.parseInt(t));
                         toDate.set(java.util.Calendar.YEAR, Integer.parseInt(t));*/
                }
                if (tokenizer.hasMoreTokens()) {
                    month = tokenizer.nextToken().trim();

                    currCal.set(java.util.Calendar.MONTH,
                                Integer.parseInt(month)-1);
                    fromDate.set(java.util.Calendar.MONTH,
                                 Integer.parseInt(month)-1);
                    toDate.set(java.util.Calendar.MONTH,
                               Integer.parseInt(month)-1);
                }
                if (tokenizer.hasMoreTokens()) {
                    day = tokenizer.nextToken().trim();

                    currCal.set(java.util.Calendar.DAY_OF_MONTH,
                                Integer.parseInt(day));
                    fromDate.set(java.util.Calendar.DAY_OF_MONTH,
                                 Integer.parseInt(day));
                    toDate.set(java.util.Calendar.DAY_OF_MONTH,
                               Integer.parseInt(day));
                }

                int nav = (Integer.parseInt(year) -
                           currCal.get(java.util.Calendar.YEAR));
                calendForm.setNav(Integer.toString(nav));

                fromDate.set(java.util.Calendar.HOUR,0);
                fromDate.set(java.util.Calendar.MINUTE,0);
                fromDate.set(java.util.Calendar.SECOND,0);
                fromDate.set(java.util.Calendar.MILLISECOND,0);
                //
                toDate.set(java.util.Calendar.HOUR,toDate.getMaximum(java.util.Calendar.HOUR));
                toDate.set(java.util.Calendar.MINUTE,toDate.getMaximum(java.util.Calendar.MINUTE));
                toDate.set(java.util.Calendar.SECOND,toDate.getMaximum(java.util.Calendar.SECOND));
                toDate.set(java.util.Calendar.MILLISECOND,toDate.getMaximum(java.util.Calendar.MILLISECOND));
            }

            doStartPaginationByYear(calendForm, 1);
             currCal.add(java.util.Calendar.YEAR, Integer.parseInt(navYear));
             fromDate.add(java.util.Calendar.YEAR, Integer.parseInt(navYear));
             toDate.add(java.util.Calendar.YEAR, Integer.parseInt(navYear));
            doEndPaginationByYear(CalendarPaginationAction.INFINIT);
        }
        ///
        String jumpToDate = new String();
        if (currCal.get(java.util.Calendar.MONTH) < 10) {
            jumpToDate = currCal.get(java.util.Calendar.YEAR) +
                "-0" + (currCal.get(java.util.Calendar.MONTH) + 1) +
                "-" + currCal.get(java.util.Calendar.DAY_OF_MONTH);

        }
        else {
            jumpToDate = currCal.get(java.util.Calendar.YEAR) +
                "-" + (currCal.get(java.util.Calendar.MONTH) + 1) +
                "-" + currCal.get(java.util.Calendar.DAY_OF_MONTH);

        }
        calendForm.setJumpToDate(jumpToDate);
        ////
        calendForm.setCurrentYear(Integer.toString(currCal.get(java.util.
            Calendar.YEAR)));
        //set info text
        calendForm.setInfoText(" for " +
                               new SimpleDateFormat("MMMMM").
                               format(currCal.getTime()) +
                               " " +
                               new SimpleDateFormat("yyyy").format(currCal.
            getTime()));

        //set today Text----
        calendForm.setTodayDate(currCal.getTime());

        if (calendForm.getStatus() == null ||
            calendForm.getStatus().equals("all")) {
            //Get all current events
            dbEventsList = DbUtil.getCalendarEvents(siteId,instanceName,
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
                dbEventsList = DbUtil.getCalendarEvents(siteId,instanceName,ItemStatus.
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
                    if (calItem!=null) {
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
                                                          getStartDate(),itemNext.getStartTBD()));
                      }
                      if (itemNext.getEndDate() != null) {
                        ei.setEndDate(CalendarUtil.formatDate(itemNext.
                                getEndDate(),itemNext.getEndTBD()));
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

                if (item.getCalendarId().equals(new Long(currCal.get(java.util.
                    Calendar.MONTH) + 1))) {
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