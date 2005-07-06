/*
 *   UpdateConfirmCalendarItems.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 17, 2003
 * 	 CVS-ID: $Id: UpdateConfirmCalendarItems.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.common.util.ModuleEmailManager;

/**
 * Action updates selected events statuses into database, sends appropriate notification email alerts to event's authors and redirects back to the action from which it was invoked
 */

public class UpdateConfirmCalendarItems
    extends Action {

  public ActionForward execute(ActionMapping mapping,
                               ActionForm form,
                               javax.servlet.http.HttpServletRequest request,
                               javax.servlet.http.HttpServletResponse
                               response) throws java.lang.Exception {

    CalendarItemForm calendForm = (CalendarItemForm) form;
    List events = new ArrayList();

    String status = "";

    List selectedEvents = calendForm.getEventsList();

    if (selectedEvents != null && selectedEvents.size() != 0) {

      Iterator iter = selectedEvents.iterator();
      if (iter.hasNext()) {
        while (iter.hasNext()) {
          CalendarItemForm.EventInfo item = (CalendarItemForm.
                                             EventInfo) iter.next();

          if (item.isSelected()) {

            // get event by id
            Calendar event = DbUtil.getCalendarItem(item.getId());

            if (calendForm.getSelectedStatus() != null) {
              if (calendForm.getSelectedStatus().equalsIgnoreCase(
                  ItemStatus.REVOKE))
                status = ItemStatus.PUBLISHED;
              else
                status = calendForm.getSelectedStatus();
            }
            else {
              status = ItemStatus.REJECTED;
            }

            // update event status
            DbUtil.updateStatus(event, status);

            CalendarItem calendarItem = event.getFirstCalendarItem();

            User author = UserUtils.getUser(calendarItem.
                                            getUserId());

            if (author != null && calendForm.isSendMessage()) {

              ModuleEmailManager.sendUserEmail(RequestUtils.getRealModuleInstance(
                  request), author, calendarItem.getTitle(), event.getSourceUrl(),status);
            }
          }
        }
      }
    }

    return new ActionForward(calendForm.getReturnUrl(), true);
  }

}