/*
 *   ShowCreateCalendarItem.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 17, 2003
 * 	 CVS-ID: $Id: ShowCreateCalendarItem.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
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
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.form.CalendarItemForm;

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