/*
 *   ShowCalendarItemDetails.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 17, 2003
 * 	 CVS-ID: $Id: ShowCalendarItemDetails.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
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

            calendForm.setCountry(event.getCountry());
            calendForm.setCountryKey( (String) ("cn:" + event.getCountry()));
            calendForm.setLocation(event.getLocation());

            calendForm.setStartDate(CalendarUtil.formatDate(event.getStartDate(), event.getStartTBD()));
            calendForm.setEndDate(CalendarUtil.formatDate(event.getEndDate(), event.getEndTBD()));

            UserInfo author = DgUtil.getUserInfo(eventItem.getUserId());
            calendForm.setEditable(false);

           /** @todo This code seems to be incorrect. Please review. Mikheil */
            if (user != null) {
                if ( (author != null) &&
                    (author.getEmail().equals(user.getEmail()) ||
                     DgUtil.isModuleInstanceAdministrator(request))) {
                    calendForm.setEditable(true);
                }
            }

            //reset status
            calendForm.setSelectedStatus(null);
        }

        return mapping.findForward("forward");
    }

}