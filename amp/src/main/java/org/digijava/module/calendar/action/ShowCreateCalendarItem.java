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
import org.digijava.module.calendar.form.CalendarItemForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;

/**
 * Action renders form, whith which new event is created
 */

public class ShowCreateCalendarItem
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CalendarItemForm calendForm = (CalendarItemForm) form;
        User user = RequestUtils.getUser(request);

        calendForm.loadCalendar();
        //get countries list
        Collection countries = TrnUtil.getCountries(RequestUtils.
            getNavigationLanguage(request).getCode());
        ArrayList sortedCountries = new ArrayList(countries);
        Collections.sort(sortedCountries, TrnUtil.countryNameComparator);
    TrnCountry none = new TrnCountry(Calendar.noneCountryIso,Calendar.noneCountryName);
    sortedCountries.add(0,none);

        calendForm.setCountryResidence(sortedCountries);

        String month;
        String day;
        String year;
        String hour;
        String min;

        java.util.Calendar currentDate = new GregorianCalendar();

        month = Integer.toString(currentDate.get(java.util.Calendar.MONTH) + 1);
        day = Integer.toString(currentDate.get(java.util.Calendar.DAY_OF_MONTH));
        year = Integer.toString(currentDate.get(java.util.Calendar.YEAR));
        hour = Integer.toString(currentDate.get(java.util.Calendar.HOUR_OF_DAY));
        min = Integer.toString(currentDate.get(java.util.Calendar.MINUTE));

        calendForm.setSourceUrl(request.getScheme() + "://");

        calendForm.setStartMonth(month);
        calendForm.setStartDay(day);
        calendForm.setStartYear(year);
        calendForm.setStartHour(hour);
        calendForm.setStartMin(min);

        calendForm.setAllStartDayEvent(true);
        calendForm.setAllEndDayEvent(true);

        calendForm.setEndMonth(month);
        calendForm.setEndDay(day);
        calendForm.setEndYear(year);
        calendForm.setEndHour(hour);
        calendForm.setEndMin(min);

        return mapping.findForward("forward");

    }

}
