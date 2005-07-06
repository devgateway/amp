/*
 *   ViewEvents.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 17, 2003
 * 	 CVS-ID: $Id: ViewEvents.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
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