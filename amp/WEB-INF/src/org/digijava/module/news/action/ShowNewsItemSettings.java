/*
 *   ShowNewsItemSettings.java
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

import java.util.ArrayList;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.DgUtil;
import org.digijava.module.news.dbentity.NewsSettings;
import org.digijava.module.news.form.NewsAdminForm;
import org.digijava.module.news.util.DbUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.common.dbentity.ItemStatus;


/**
 * Action updates news items settings into database
 */

public class ShowNewsItemSettings
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        NewsAdminForm adminForm = (NewsAdminForm) form;
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);
        NewsSettings setting = null;

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
        setting = DbUtil.getNewsSettings(request);

        // populate number of news item ( default 7 items )
        //
        ArrayList list = new ArrayList();
        for( int i = 0; i < ModuleInstance.NUMBER_OF_ITEMS_IN_TEASER; i++ ) {
            list.add(new NewsAdminForm.Item(Integer.toString(i + 1)));
        }
        // -----------------------------------------

        adminForm.setSelectedNumOfItemsInTeaser(moduleInstance.getNumberOfItemsInTeaser());
        adminForm.setNumberOfTeasers(list);
        adminForm.setNumOfCharsInTitle(setting.getNumberOfCharsInTitle());
        adminForm.setNumOfItemsPerPage(setting.getNumberOfItemsPerPage());
	adminForm.setShortVersionDelimiter(setting.getShortVersionDelimiter());
        adminForm.setSetting(setting);

        return mapping.findForward("forward");

    }


}