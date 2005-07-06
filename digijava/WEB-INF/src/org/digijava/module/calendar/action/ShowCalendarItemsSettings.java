/*
 *   ShowCalendarItemsSettings.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 17, 2003
 * 	 CVS-ID: $Id: ShowCalendarItemsSettings.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.dbentity.CalendarSettings;
import org.digijava.module.calendar.form.CalendarAdminForm;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.dbentity.ItemStatus;

/**
 * Action displayes Calendar Settings page
 */

public class ShowCalendarItemsSettings
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CalendarAdminForm adminForm = (CalendarAdminForm) form;

        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);
        CalendarSettings setting = null;

        //translation
        String moduleName = moduleInstance.getModuleName();
        String siteName = moduleInstance.getSite().getName();
        String instanceName = moduleInstance.getInstanceName();

        RequestUtils.setTranslationAttribute(request,"moduleName",moduleName);
        RequestUtils.setTranslationAttribute(request,"siteName",siteName);

        adminForm.setApprove(ItemStatus.PUBLISHED);
        adminForm.setReject(ItemStatus.REJECTED);
        adminForm.setRevoke(ItemStatus.REVOKE);
        adminForm.setArchive(ItemStatus.ARCHIVED);

        adminForm.setModuleName(moduleName);
        adminForm.setSiteName(siteName);
        adminForm.setInstanceName(instanceName);
        //end translation

        // get Setting
        setting = DbUtil.getCalendarSettings(request);

        // populate number of event item ( default 7 items )
        //
        ArrayList list = new ArrayList();
        for (int i = 0; i < ModuleInstance.NUMBER_OF_ITEMS_IN_TEASER; i++) {
            list.add(new CalendarAdminForm.Item(Integer.toString(i + 1)));
        }
        // -----------------------------------------

        adminForm.setSelectedNumOfItemsInTeaser(moduleInstance.
                                                getNumberOfItemsInTeaser());
        adminForm.setSelectedDefaultView(setting.getDefaultView());
        adminForm.setNumberOfTeasers(list);
        adminForm.setNumOfCharsInTitle(setting.getNumberOfCharsInTitle());
        adminForm.setNumOfItemsPerPage(setting.getNumberOfItemsPerPage());
        adminForm.setSetting(setting);

        return mapping.findForward("forward");

    }

}