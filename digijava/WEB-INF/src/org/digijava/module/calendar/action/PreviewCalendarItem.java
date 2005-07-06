/*
 *   PreviewCalendarItem.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 17, 2003
 * 	 CVS-ID: $Id: PreviewCalendarItem.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
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
import java.util.Iterator;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.user.User;
import org.digijava.kernel.user.UserInfo;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.CalendarPopulator;
import org.digijava.module.calendar.util.CalendarUtil;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.util.BBCodeParser;

/**
 * Action Previews the event before publcation
 */

public class PreviewCalendarItem
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CalendarItemForm calendForm = (CalendarItemForm) form;
        User user = RequestUtils.getUser(request);

        // get user navigation langugae
        Locale navigationLanguge = user.getUserLangPreferences().
            getNavigationLanguage();

        String param = request.getParameter("CreateOrEdit");

        calendForm.setPreview(true);
        CalendarItemForm.EventInfo previewItem = new CalendarItemForm.EventInfo();

        
        CalendarPopulator populator = new CalendarPopulator(calendForm);
        
        //start Date
        previewItem.setStartDate(CalendarUtil.formatDate(populator.getStart(), populator.getStartTBD()));
        previewItem.setStart(populator.getStart().getTime());
        
        // -----------

        //set end Date
        previewItem.setEndDate(CalendarUtil.formatDate(populator.getEnd(), populator.getEndTBD()));
        previewItem.setEnd(populator.getEnd().getTime());
        // ---------

        //set Title
        previewItem.setTitle(calendForm.getTitle());

        //set Description
        previewItem.setDescription(BBCodeParser.parse(calendForm.getDescription(),
            calendForm.isEnableSmiles(),
            calendForm.isEnableHTML(),
            request));
        if (param.equals("editCalendarItem")) {

            if (calendForm.getActiveCalendarItem() != null) {

                Calendar event = DbUtil.getCalendarItem(calendForm.
                    getActiveCalendarItem());

                UserInfo author = DgUtil.getUserInfo(event.getFirstCalendarItem().
                    getUserId());

                previewItem.setAuthorUserId(event.getFirstCalendarItem().
                                            getUserId());
                previewItem.setAuthorFirstNames(author.getFirstNames());
                previewItem.setAuthorLastName(author.getLastName());
            }

        }
        else {
            if (user != null) {
                previewItem.setAuthorUserId(user.getId());
                previewItem.setAuthorFirstNames(user.getFirstNames());
                previewItem.setAuthorLastName(user.getLastName());
            }

        }
        previewItem.setLocation(calendForm.getLocation());
        if( calendForm.getSourceName() == null ) {
            previewItem.setSourceName(calendForm.getSourceUrl());
        }else {
            previewItem.setSourceName(calendForm.getSourceName());
        }
        previewItem.setSourceUrl(calendForm.getSourceUrl());

        Collection countries = TrnUtil.getCountries(RequestUtils.
            getNavigationLanguage(request).getCode());
        ArrayList sortedCountries = new ArrayList(countries);
        Collections.sort(sortedCountries, TrnUtil.countryNameComparator);

        Iterator iter = countries.iterator();
        while (iter.hasNext()) {
            TrnCountry item = (TrnCountry) iter.next();
            if (item.getIso().equals(calendForm.getCountry())) {
                previewItem.setCountry(item.getIso());
                previewItem.setCountryKey( (String) ("cn:" + item.getIso()));
                break;
            }
        }
        calendForm.setCountryResidence(sortedCountries);

        calendForm.setPreviewItem(previewItem);

        ActionErrors errors = calendForm.validate(mapping, request);
        if (errors != null && errors.size() != 0) {
            saveErrors(request, errors);
            calendForm.setPreview(false);
            calendForm.loadCalendar();
        }

        if (param.equals("editCalendarItem")) {
            return mapping.findForward("forwardEdit");
        }
        else {
            return mapping.findForward("forwardCreate");
        }
    }
}