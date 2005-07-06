/*
 *   ConfirmCalendarItems.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 17, 2003
 * 	 CVS-ID: $Id: ConfirmCalendarItems.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
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
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.user.UserInfo;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.dbentity.CalendarSettings;
import org.digijava.module.calendar.form.CalendarItemForm;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.dbentity.ItemStatus;

/**
 * Action displayes confirmation page for selected event items, status of which is being changed
 */

public class ConfirmCalendarItems
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CalendarItemForm calendForm = (CalendarItemForm) form;

        List events = new ArrayList();

        //translation
        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);

        String moduleName = moduleInstance.getModuleName();
        String siteName = moduleInstance.getSite().getName();
        String instanceName = moduleInstance.getInstanceName();

        RequestUtils.setTranslationAttribute(request,"moduleName",moduleName);
        RequestUtils.setTranslationAttribute(request,"siteName",siteName);

        calendForm.setModuleName(moduleName);
        calendForm.setSiteName(siteName);
        calendForm.setInstanceName(instanceName);
        //end translation

        // get default settings
        CalendarSettings setting = DbUtil.getCalendarSettings(request);

        if (calendForm.getSelectedStatus() != null) {
            if (calendForm.getSelectedStatus().equalsIgnoreCase(ItemStatus.
                PUBLISHED)) {
                calendForm.setSendMessage(setting.isSendApproveMessage());
                calendForm.setDefaultMessage(setting.getApproveMessage());
                calendForm.setStatusTitle("Publish");
            }
            else
            if (calendForm.getSelectedStatus().equalsIgnoreCase(ItemStatus.
                REJECTED)) {
                calendForm.setSendMessage(setting.isSendRejectMessage());
                calendForm.setDefaultMessage(setting.getRejectMessage());
                calendForm.setStatusTitle("Reject");
            }
            else
            if (calendForm.getSelectedStatus().equalsIgnoreCase(ItemStatus.
                REVOKE)) {
                calendForm.setSendMessage(setting.isSendRevokeMessage());
                calendForm.setDefaultMessage(setting.getRevokeMessage());
                calendForm.setStatusTitle("Re-publish");
            }
        }
        else {
         calendForm.setSelectedStatus(ItemStatus.REJECTED);
         calendForm.setSendMessage(setting.isSendRejectMessage());
         calendForm.setDefaultMessage(setting.getRejectMessage());
         calendForm.setStatusTitle("Reject");
                }

       if (calendForm.getActiveCalendarItem() != null) {

           calendForm.setSendMessage(setting.isSendRejectMessage());
           calendForm.setDefaultMessage(setting.getRejectMessage());
           calendForm.setStatusTitle("Reject");

           Calendar event = DbUtil.getCalendarItem(calendForm.
               getActiveCalendarItem());
           CalendarItem calendarItem = event.getFirstCalendarItem();

           CalendarItemForm.EventInfo ei = new CalendarItemForm.EventInfo();

           ei.setTitle(calendarItem.getTitle());
           ei.setStartDate(DgUtil.formatDate(event.getStartDate()));

           UserInfo author = DgUtil.getUserInfo(calendarItem.getUserId());

           ei.setAuthorUserId(calendarItem.getUserId());
           ei.setAuthorFirstNames(author.getFirstNames());
           ei.setAuthorLastName(author.getLastName());
           ei.setSelected(true);
           if (event.getStatus() != null) {
               ei.setStatus(event.getStatus().getName());
           }

           ei.setSelected(true);
           ei.setId(event.getId());

           events.add(ei);

           calendForm.setEventsList(events);
       }

       calendForm.setSelected(false);
       if (calendForm.getEventsList() != null ){
           Iterator iter = calendForm.getEventsList().iterator();
           while(iter.hasNext()) {
               CalendarItemForm.EventInfo item = (CalendarItemForm.EventInfo) iter.next();

               if (item.isSelected()) {
                   calendForm.setSelected(true);
                   break;
               }
           }
       }

       if (calendForm.isSelected()) {
           calendForm.setSubmitted(false);
           return mapping.findForward("forward");
       } else {
           calendForm.setSubmitted(true);
           return new ActionForward(calendForm.getReturnUrl(), true);
       }
    }

}