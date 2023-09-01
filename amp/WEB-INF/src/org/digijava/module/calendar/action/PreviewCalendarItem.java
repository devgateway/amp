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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

        String param = request.getParameter("CreateOrEdit");

        calendForm.setPreview(true);
        CalendarItemForm.EventInfo previewItem = new CalendarItemForm.EventInfo();

        CalendarPopulator populator = new CalendarPopulator(calendForm);

        //start Date
        previewItem.setStartDate(CalendarUtil.formatDate(populator.getStart(),
            populator.getStartTBD()));
        previewItem.setStart(populator.getStart().getTime());

        // -----------

        //set end Date
        previewItem.setEndDate(CalendarUtil.formatDate(populator.getEnd(),
            populator.getEndTBD()));
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
        if (calendForm.getSourceName() == null) {
            previewItem.setSourceName(calendForm.getSourceUrl());
        }
        else {
            previewItem.setSourceName(calendForm.getSourceName());
        }
        previewItem.setSourceUrl(calendForm.getSourceUrl());

        if (calendForm.getCountry().equals(Calendar.noneCountryIso)) {
            previewItem.setCountry(Calendar.noneCountryIso);
            previewItem.setCountryName(Calendar.noneCountryName);
            previewItem.setCountryKey( (String) ("cn:" +
                                                 Calendar.noneCountryIso));
        }
        else {
            previewItem.setCountry(calendForm.getCountry());
            previewItem.setCountryName(calendForm.getCountry());
            previewItem.setCountryKey( (String) ("cn:" + calendForm.getCountry()));
        }
        Collection countries = TrnUtil.getCountries(RequestUtils.
            getNavigationLanguage(request).getCode());
        ArrayList sortedCountries = new ArrayList(countries);
        Collections.sort(sortedCountries, TrnUtil.countryNameComparator);
        TrnCountry none = new TrnCountry(Calendar.noneCountryIso,
                                         Calendar.noneCountryName);
        sortedCountries.add(0, none);

        calendForm.setCountryResidence(sortedCountries);

        calendForm.setPreviewItem(previewItem);

        ActionMessages errors = calendForm.validate(mapping, request);
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
