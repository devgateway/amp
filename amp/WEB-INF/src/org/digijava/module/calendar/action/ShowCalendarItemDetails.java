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
import org.digijava.kernel.user.User;
import org.digijava.kernel.user.UserInfo;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.CalendarUtil;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.util.BBCodeParser;
import org.digijava.kernel.request.Site;
import javax.security.auth.Subject;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.ModuleUtils;

/**
 * Action displayes details of event item identified by activeCalendarItem identity(if not null)
 */

public class ShowCalendarItemDetails
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

        Calendar event = null;
        if (calendForm.getActiveCalendarItem() != null) {

            calendForm.setStatus(null);
            //ser return Url
            String currentUrl = DgUtil.getFullURL(request);
            String sitetUrl = DgUtil.getSiteUrl(RequestUtils.getSiteDomain(request),
                                                request);
            String returnUrl = currentUrl.substring(sitetUrl.length(),
                currentUrl.length());
            int index = returnUrl.indexOf("/default");
            if (index >= 0) {
                returnUrl = returnUrl.substring(0, index) +
                    returnUrl.substring(index + ( (String) ("/default")).length(),
                                        returnUrl.length());
            }
            calendForm.setReturnUrl(returnUrl);
            //

            event = DbUtil.getCalendarItem(calendForm.getActiveCalendarItem());
            CalendarItem eventItem = event.getFirstCalendarItem();

            calendForm.setTitle(eventItem.getTitle());

            calendForm.setDescription(BBCodeParser.parse(eventItem.
                getDescription(),
                event.isEnableSmiles(),
                event.isEnableHTML(),
                request));

            if( event.getSourceName() == null ) {
                calendForm.setSourceName(event.getSourceUrl());
            }else {
                calendForm.setSourceName(event.getSourceName());
            }
            calendForm.setSourceUrl(event.getSourceUrl());

        if (event.getCountry() != null) {
          calendForm.setCountry(event.getCountry());
          calendForm.setCountryName(event.getCountry());
          calendForm.setCountryKey((String)("cn:"+event.getCountry()));
        } else {
          calendForm.setCountry(Calendar.noneCountryIso);
          calendForm.setCountryName(Calendar.noneCountryName);
          calendForm.setCountryKey((String)("cn:"+Calendar.noneCountryIso));
        }


            calendForm.setLocation(event.getLocation());

            calendForm.setStartDate(CalendarUtil.formatDate(event.getStartDate(), event.getStartTBD()));
            calendForm.setEndDate(CalendarUtil.formatDate(event.getEndDate(), event.getEndTBD()));

            UserInfo author = DgUtil.getUserInfo(eventItem.getUserId());
            calendForm.setEditable(false);

           /** @todo This code seems to be incorrect. Please review. Mikheil */
            if (user != null) {
                if ( (author != null) &&
                    (author.getEmail().equals(user.getEmail()) ||
                     ModuleUtils.isContentAdministrator(subject, currentSite, moduleInstance))) {
                    calendForm.setEditable(true);
                }
            }

            //reset status
            calendForm.setSelectedStatus(null);
        }

        return mapping.findForward("forward");
    }

}
