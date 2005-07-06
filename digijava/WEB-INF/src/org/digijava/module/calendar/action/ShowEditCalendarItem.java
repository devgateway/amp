/*
 *   ShowEditCalendarItem.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 17, 2003
 * 	 CVS-ID: $Id: ShowEditCalendarItem.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.CalendarUtil;
import org.digijava.module.calendar.util.DbUtil;

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
        // get user navigation languge
        Locale navigationLanguge = user.getUserLangPreferences().
            getNavigationLanguage();

        //get countries list
        Collection countries = TrnUtil.getCountries(RequestUtils.
            getNavigationLanguage(request).getCode());
        ArrayList sortedCountries = new ArrayList(countries);
        Collections.sort(sortedCountries, TrnUtil.countryNameComparator);
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
            CalendarUtil.populateDates(event,calendForm);
            

            calendForm.setEnableHTML(event.isEnableHTML());
            calendForm.setEnableSmiles(event.isEnableSmiles());
            calendForm.setCountry(event.getCountry());
            calendForm.setLocation(event.getLocation());

        }
        return mapping.findForward("forward");
    }
}