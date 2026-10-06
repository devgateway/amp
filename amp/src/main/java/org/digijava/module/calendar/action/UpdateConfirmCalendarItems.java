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
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.UserUtils;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.common.util.ModuleEmailManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
