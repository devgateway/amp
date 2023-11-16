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
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.calendar.dbentity.CalendarSettings;
import org.digijava.module.calendar.form.CalendarAdminForm;
import org.digijava.module.calendar.util.DbUtil;
import org.digijava.module.common.dbentity.ItemStatus;

import java.util.ArrayList;

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
