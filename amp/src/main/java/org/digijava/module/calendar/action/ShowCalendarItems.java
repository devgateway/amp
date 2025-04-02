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
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.UserInfo;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.dbentity.CalendarSettings;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.dbentity.ItemStatus;

/**
 * Action displayes events according to the selected status defined by FormBean
 * property - status, action redirects back to the action, which invoked it
 */

public class ShowCalendarItems
    extends CalendarPaginationAction {

  private static Logger logger = Logger.getLogger(ShowCalendarItems.class);

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    CalendarItemForm calendForm = (CalendarItemForm) form;

    if (!calendForm.isSubmitted()) {
        calendForm.setSelected(true);
    }
    calendForm.setSubmitted(false);
    //
    List dbEventsList = new ArrayList();
    List eventsList = new ArrayList();
    List statusList = new ArrayList();
    Collection itemStatus = null;

    ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);
    String siteId = moduleInstance.getSite().getSiteId();
    String instanceName = moduleInstance.getInstanceName();

    calendForm.setAdminViewAll(true);

    // set status item
    statusList.add(new CalendarItemForm.EventInfo("Published",
                                                  ItemStatus.PUBLISHED, false));
    statusList.add(new CalendarItemForm.EventInfo("Pending",
                                                  ItemStatus.PENDING, false));
    statusList.add(new CalendarItemForm.EventInfo("Rejected",
                                                  ItemStatus.REJECTED, false));
    statusList.add(new CalendarItemForm.EventInfo("All", "all", false));

    calendForm.setStatusList(statusList);
    // ---------------

    //set return Url
    String currentUrl = DgUtil.getFullURL(request);

    String sitetUrl = DgUtil.getSiteUrl(RequestUtils.getSiteDomain(request),
                                        request);

    String returnUrl = currentUrl.substring(sitetUrl.length(),
                                            currentUrl.length());

    logger.debug("Return URL: " + returnUrl);

    int index = returnUrl.indexOf("/default");
    if (index >= 0) {
      returnUrl = returnUrl.substring(0, index) +
          returnUrl.substring(index + ( (String) ("/default")).length(),
                              returnUrl.length());
    }
    calendForm.setReturnUrl(returnUrl);
    //-----------
    String param = calendForm.getStatus();

    logger.debug("Status: " + param);

    // Pagination
    int numberOfEventItems = 0;

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

    //Initialize published EventsList from Datebase Here
    if ( (param != null) && param.equalsIgnoreCase(ItemStatus.PENDING)) {

      // Get Pending items from database
      dbEventsList = DbUtil.getCalendarEvents(siteId, instanceName,
                                              ItemStatus.PENDING,
                                              getOffset(),
                                              numberOfItemsPerPage + 1,
                                              getDirection());
      numberOfEventItems = DbUtil.getNumOfEventItems(siteId, instanceName,
          ItemStatus.PENDING);
      //
      CalendarItemForm.EventInfo eventInfo = (CalendarItemForm.EventInfo)
          statusList.get(1);
      eventInfo.setSelected(true);

      itemStatus = new ArrayList();
      itemStatus.add(new CalendarItemForm.EventStatusInfo(ItemStatus.
          PUBLISHED, "Publish"));
      itemStatus.add(new CalendarItemForm.EventStatusInfo(ItemStatus.
          REJECTED, "Reject"));

      calendForm.setCalendarStatus(itemStatus);
    }
    else
    if ( (param != null) && param.equalsIgnoreCase(ItemStatus.PUBLISHED)) {

      // Get Published items from database
      dbEventsList = DbUtil.getCalendarEvents(siteId, instanceName,
                                              ItemStatus.PUBLISHED,
                                              getOffset(),
                                              numberOfItemsPerPage + 1,
                                              getDirection());

      numberOfEventItems = DbUtil.getNumOfEventItems(siteId, instanceName,
          ItemStatus.PUBLISHED);
      //
      CalendarItemForm.EventInfo eventInfo = (CalendarItemForm.EventInfo)
          statusList.get(0);
      eventInfo.setSelected(true);

      itemStatus = new ArrayList();
      itemStatus.add(new CalendarItemForm.EventStatusInfo(ItemStatus.
          REJECTED, "Reject"));

      calendForm.setCalendarStatus(itemStatus);
    }
    else
    if ( (param != null) && param.equalsIgnoreCase(ItemStatus.REJECTED)) {

      // Get Rejected items from database
      dbEventsList = DbUtil.getCalendarEvents(siteId, instanceName,
                                              ItemStatus.REJECTED,
                                              getOffset(),
                                              numberOfItemsPerPage + 1,
                                              getDirection());
      numberOfEventItems = DbUtil.getNumOfEventItems(siteId, instanceName,
          ItemStatus.REJECTED);
      //
      CalendarItemForm.EventInfo eventInfo = (CalendarItemForm.EventInfo)
          statusList.get(2);
      eventInfo.setSelected(true);

      itemStatus = new ArrayList();
      itemStatus.add(new CalendarItemForm.EventStatusInfo(ItemStatus.
          REVOKE, "Revoke"));

      calendForm.setCalendarStatus(itemStatus);

    }
    else { //show All
      logger.debug("Showing All items");

      ModuleInstance instance = RequestUtils.getModuleInstance(request);

      // Get all items from database
      dbEventsList = DbUtil.getCalendarEvents(siteId, instanceName,
                                              null,
                                              getOffset(),
                                              numberOfItemsPerPage + 1,
                                              getDirection());

      numberOfEventItems = DbUtil.getNumOfEventItems(siteId, instanceName, null);
      //
      //-- Let's see what the hell is coming from the DB --Irakli
      if (logger.isDebugEnabled()) {
        java.util.Iterator iter = dbEventsList.iterator();
        org.digijava.module.calendar.dbentity.Calendar calendar = null;
        while (iter.hasNext()) {
          calendar = (org.digijava.module.calendar.dbentity.Calendar)
              iter.next();
          logger.debug("next item: "
                       + calendar.getStartDate());
        }
      }

      CalendarItemForm.EventInfo eventInfo = (CalendarItemForm.EventInfo)
          statusList.get(3);
      eventInfo.setSelected(true);

      itemStatus = new ArrayList();
      itemStatus.add(new CalendarItemForm.EventStatusInfo(ItemStatus.
          PUBLISHED, "Publish"));
      itemStatus.add(new CalendarItemForm.EventStatusInfo(ItemStatus.
          REJECTED, "Reject"));

      calendForm.setCalendarStatus(itemStatus);
    }
    calendForm.setWholeSize(numberOfEventItems);
    doEndPagination(calendForm,
                    ( (dbEventsList != null) ? dbEventsList.size() : 0));
    //
    if (dbEventsList != null && dbEventsList.size() > 0) {
      int n;
      if ( (numberOfItemsPerPage + 1) == dbEventsList.size())
        n = dbEventsList.size() - 1;
      else
        n = dbEventsList.size();

      for (int i = 0; i < n; i++) {
        Calendar event = (Calendar) dbEventsList.get(i);
        CalendarItem calendarItem = event.getFirstCalendarItem();

        CalendarItemForm.EventInfo ei = new CalendarItemForm.EventInfo();

        if (calendarItem != null) {
          if (calendarItem.getTitle() != null) {
            if (calendarItem.getTitle().length() > 8)
              ei.setTitle(calendarItem.getTitle().substring(0, 8) +
                          "...");
            else
              ei.setTitle(calendarItem.getTitle());
          }

          UserInfo author = DgUtil.getUserInfo(calendarItem.getUserId());

          ei.setId(event.getId());

          ei.setAuthorFirstNames(author.getFirstNames());
          ei.setAuthorLastName(author.getLastName());
          ei.setStartDate(DgUtil.formatDate(event.getStartDate()));
          if (event.getStatus() != null) {
            ei.setStatus(event.getStatus().getName());
          }

          eventsList.add(ei);
        }
      }
    }
    if (eventsList.size() != 0)
      calendForm.setEventsList(eventsList);
    else
      calendForm.setEventsList(null);

    return mapping.findForward("forward");
  }

}
