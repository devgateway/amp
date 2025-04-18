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
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.CalendarUtil;
import org.digijava.module.calendar.util.DbUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Action renders form, whith which existing event item identified by activeCalendarItem identity (if not null) can be updated
 */

public class ShowEditCalendarItem
      extends Action {

    public ActionForward execute(ActionMapping mapping,
                 ActionForm form,
                 javax.servlet.http.HttpServletRequest request,
                 javax.servlet.http.HttpServletResponse
                 response) throws java.lang.Exception {

    CalendarItemForm calendForm = (CalendarItemForm) form;
    User user = RequestUtils.getUser(request);

    Calendar event = null;

    calendForm.loadCalendar();

    //get countries list
    Collection countries = TrnUtil.getCountries(RequestUtils.
          getNavigationLanguage(request).getCode());
    ArrayList sortedCountries = new ArrayList(countries);
    Collections.sort(sortedCountries, TrnUtil.countryNameComparator);
    TrnCountry none = new TrnCountry(Calendar.noneCountryIso,
                     Calendar.noneCountryName);
    sortedCountries.add(0, none);

    calendForm.setCountryResidence(sortedCountries);

    if (calendForm.getActiveCalendarItem() != null) {

        event = DbUtil.getCalendarItem(calendForm.getActiveCalendarItem());
        CalendarItem calendarItem = event.getFirstCalendarItem();

        calendForm.setTitle(calendarItem.getTitle());
        calendForm.setDescription(calendarItem.getDescription());

        calendForm.setCountry(event.getCountry());
        calendForm.setLocation(event.getLocation());
        calendForm.setSourceName(event.getSourceName());
        calendForm.setSourceUrl(event.getSourceUrl());

        // populating start date & end date
        CalendarUtil.populateDates(event, calendForm);

        calendForm.setEnableHTML(event.isEnableHTML());
        calendForm.setEnableSmiles(event.isEnableSmiles());
        calendForm.setCountry(event.getCountry());
        calendForm.setLocation(event.getLocation());

    }
    return mapping.findForward("forward");
    }
}
