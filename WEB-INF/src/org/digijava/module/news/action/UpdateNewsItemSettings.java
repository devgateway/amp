/*
 *   UpdateNewsItemSettings.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id$
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

package org.digijava.module.news.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.news.form.NewsAdminForm;
import org.digijava.module.news.util.DbUtil;

/**
 *  Action updates news items settings into database
 */

public class UpdateNewsItemSettings
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        NewsAdminForm adminForm = (NewsAdminForm) form;

        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

        if( moduleInstance != null ) {
            adminForm.getSetting().setNumberOfCharsInTitle(adminForm.getNumOfCharsInTitle());
            adminForm.getSetting().setNumberOfItemsPerPage(adminForm.getNumOfItemsPerPage());
	    adminForm.getSetting().setShortVersionDelimiter(adminForm.getShortVersionDelimiter());

            DbUtil.updateSetting(adminForm.getSetting());

            moduleInstance.setNumberOfItemsInTeaser(adminForm.getSelectedNumOfItemsInTeaser());
            org.digijava.module.admin.util.DbUtil.updateSiteInstance(moduleInstance);
        }

        return mapping.findForward("forward");
    }

}