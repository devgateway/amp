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

import org.apache.struts.action.*;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.ModuleUtils;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.dbentity.CalendarSettings;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.CalendarPopulator;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.dbentity.ItemStatus;
import org.digijava.module.common.util.ModuleEmailManager;

import javax.security.auth.Subject;

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
    Site currentSite = RequestUtils.getSite(request);
    ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);
    Subject subject = RequestUtils.getSubject(request);

    String status = ItemStatus.PUBLISHED;

    // check if user loged
    if (user != null) {

        // set preview false
        calendForm.setPreview(false);

        // get default settings
        CalendarSettings setting = DbUtil.getCalendarSettings(request);

        // if moderated event status set Pending
        if (setting.isModerated() &&
        (!ModuleUtils.isContentAdministrator(subject, currentSite, moduleInstance)))
        status = ItemStatus.PENDING;

        // create event item
        Calendar calendar = DbUtil.createCalendarEvent(user.getId(),
          status,
          calendForm.getSelectedLanguage(),
          calendForm.getTitle(),
          calendForm.getDescription(),
          request);

        if (calendForm.getCountry().equals(Calendar.noneCountryIso)) {
        calendar.setCountry(null);
        }
        else {
        calendar.setCountry(calendForm.getCountry());
        }
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
        (!ModuleUtils.isContentAdministrator(subject, currentSite, moduleInstance))) {

        String url = RequestUtils.getFullModuleUrl(request);

        String link = url + "showCalendarItems.do?status=pe";

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
        ActionMessages errors = new ActionMessages();
        errors.add(null,
               new ActionMessage("error.calendar.userEmpty"));
        saveErrors(request, errors);

    }

    return mapping.findForward("forward");
    }

}
