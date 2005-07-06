/*
 *   CreateCalendarItem.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 17, 2003
 * 	 CVS-ID: $Id: CreateCalendarItem.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
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
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.kernel.util.SiteConfigUtils;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.dbentity.CalendarSettings;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.CalendarPopulator;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.common.util.ModuleEmailManager;

/**
 * Action creates new event item and saves it into database
 */

public class CreateCalendarItem
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CalendarItemForm calendForm = (CalendarItemForm) form;

        User user = RequestUtils.getUser(request);
        String status = ItemStatus.PUBLISHED;

        // check if user loged
        if (user != null) {

            // set preview false
            calendForm.setPreview(false);

            // get default settings
            CalendarSettings setting = DbUtil.getCalendarSettings(request);

            // if moderated event status set Pending
            if (setting.isModerated() &&
                (!DgUtil.isModuleInstanceAdministrator(request)))
                status = ItemStatus.PENDING;

                // create event item
            Calendar calendar = DbUtil.createCalendarEvent(user.getId(),
                status,
                calendForm.getSelectedLanguage(),
                calendForm.getTitle(),
                calendForm.getDescription(),
                request);

            calendar.setCountry(calendForm.getCountry());
            calendar.setSourceName(calendForm.getSourceName());
            calendar.setSourceUrl(calendForm.getSourceUrl());
            calendar.setLocation(calendForm.getLocation());

            
            CalendarPopulator populator = new CalendarPopulator(calendForm);
            
            // set start Date
            calendar.setStartDate(populator.getStart().getTime());
            calendar.setStartTBD(populator.getStartTBD());
            // ------------

            //set end Date
            calendar.setEndDate(populator.getEnd().getTime());
            calendar.setEndTBD(populator.getEndTBD());
            // ----------------

            calendar.setEnableHTML(calendForm.isEnableHTML());
            calendar.setEnableSmiles(calendForm.isEnableSmiles());

            // update into database
            DbUtil.updateCalendarEvent(calendar);

            if (setting.isModerated() &&
                (!DgUtil.isModuleInstanceAdministrator(request))) {

                ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(
                    request);

                String link = request.getScheme() + "://" +
                    request.getServerName() + ":" +
                    new Long(request.getServerPort()).toString() +
                    SiteConfigUtils.buildDgURL(request, false) +
                    SiteConfigUtils.getCurrentModuleURL(request) +
                    "/showCalendarItems.do?status=pe";

                CalendarItem calendarItem = calendar.getFirstCalendarItem();
                ModuleEmailManager.sendAdminEmail(moduleInstance,
                                                  calendarItem.getTitle(),
                                                  calendar.getSourceName(),
                                                  calendar.getSourceUrl(),
                                                  calendarItem.getDescription(),
                                                  link);

            }

        }
        else {
            ActionErrors errors = new ActionErrors();
            errors.add(null,
                       new ActionError("error.calendar.userEmpty"));
            saveErrors(request, errors);

        }

        return mapping.findForward("forward");
    }

}