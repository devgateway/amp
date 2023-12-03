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
import org.digijava.module.calendar.dbentity.CalendarSettings;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.DbUtil;

/**
 * Action determines default view for Calendar and redirects to default view
 */

public class ViewEvents
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CalendarItemForm calendForm = (CalendarItemForm) form;
        calendForm.setStatus(null);

        //---determine View --
        String currentView = new String("");

        if (calendForm.getView() == null) {

            CalendarSettings calendSettings = DbUtil.getCalendarSettings(
                request);
            if (calendSettings.getDefaultView() != null) {
                currentView = calendSettings.getDefaultView();
            }
            else {
                currentView = CalendarSettings.LIST_VIEW;
            }
        }
        else {
            currentView = calendForm.getView();
        }

        String forward = new String();

        // forward in mothview action
        if (currentView.equals(CalendarSettings.MONTH_VIEW)) {
            forward = "forwardMonth";
        }
        // forward in yearview action
        else if (currentView.equals(CalendarSettings.YEAR_VIEW)) {
            forward = "forwardYear";
        }
        // forward in listview action
        else if (currentView.equals(CalendarSettings.LIST_VIEW)) {
            forward = "forwardList";
        }

        // calculate url with current module name
        /*            String url;
                url = mapping.findForward(forward).getPath();
                 if (currentView.equals(CalendarSettings.LIST_VIEW))
                     url += "?status=all";
                return new ActionForward(url, true); */

        return mapping.findForward(forward);
    }
}
