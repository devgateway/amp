/*
 *   UpdateCalendarItemsSettings.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 17, 2003
 * 	 CVS-ID: $Id: UpdateCalendarItemsSettings.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
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
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.form.CalendarAdminForm;
import org.digijava.module.calendar.util.DbUtil;

/**
 * Action updates event items settings into database
 */

public class UpdateCalendarItemsSettings
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CalendarAdminForm adminForm = (CalendarAdminForm) form;

        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);

        if (moduleInstance != null) {
            adminForm.getSetting().setDefaultView(adminForm.
                                                  getSelectedDefaultView());
            adminForm.getSetting().setNumberOfCharsInTitle(adminForm.
                getNumOfCharsInTitle());
            adminForm.getSetting().setNumberOfItemsPerPage(adminForm.
                getNumOfItemsPerPage());

            DbUtil.updateSetting(adminForm.getSetting());

            moduleInstance.setNumberOfItemsInTeaser(adminForm.
                getSelectedNumOfItemsInTeaser());
            org.digijava.module.admin.util.DbUtil.updateSiteInstance(
                moduleInstance);
        }
        return mapping.findForward("forward");
    }

}