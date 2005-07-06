/*
 *   ConfirmNewPublishFeed.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: ConfirmNewPublishFeed.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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
package org.digijava.module.syndication.action;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.translator.util.TrnLocale;
import org.digijava.kernel.translator.util.TrnUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.syndication.form.PublishedFeedForm;
import org.digijava.module.syndication.util.DbUtil;

/**
 * Action displayes confirmation page for selected news items, status of which is being changed
 */

public class ConfirmNewPublishFeed
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        PublishedFeedForm feedForm = (PublishedFeedForm) form;

        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);

        if( feedForm != null ) {
            List list = DbUtil.getPublishedFeeds(moduleInstance.getSite().
                                                 getSiteId(),
                                                 moduleInstance.getInstanceName(),
                                                 moduleInstance.getModuleName(),
                                                 feedForm.getSelectedCountry(),
                                                 feedForm.getSelectedLanguage());


            if (list.size() != 0 && feedForm.getProcessingMode().intValue() == 1) {
                ActionErrors errors = new ActionErrors();
                errors.add(null, new ActionError("display.syndication.feedAlreadyPublished"));
                saveErrors(request, errors);
                return mapping.findForward("return");
            }

            // get module instance
            feedForm.setContentType(moduleInstance.getModuleName());
            feedForm.setDateAdded(new Date());

            // set language name
            Iterator iter = TrnUtil.getLanguages(RequestUtils.getNavigationLanguage(request).getCode()).iterator();
            while (iter.hasNext()) {
                TrnLocale item = (TrnLocale) iter.next();
                if (item.getCode().equalsIgnoreCase(feedForm.
                    getSelectedLanguage())) {
                    feedForm.setSelectedLanguageName(item.getName());
                    break;
                }
            }

            // set country name
            iter = TrnUtil.getCountries(RequestUtils.getNavigationLanguage(request).getCode()).iterator();
            while (iter.hasNext()) {
                TrnCountry item = (TrnCountry)iter.next();
                if( item.getIso().equalsIgnoreCase(feedForm.getSelectedCountry()) ) {
                    feedForm.setSelectedCountryName(item.getName());
                    break;
                }
            }

            String moduleUrl = RequestUtils.getFullModuleUrl(request);
            moduleUrl += "publish.do";
            feedForm.setFeedUrl(moduleUrl);

        }

        return mapping.findForward("forward");
    }
}
