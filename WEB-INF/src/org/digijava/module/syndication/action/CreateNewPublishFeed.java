/*
 *   CreateNewPublishFeed.java
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


package org.digijava.module.syndication.action;

import java.util.Date;
import java.util.HashSet;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.syndication.dbentity.PublicationFeed;
import org.digijava.module.syndication.form.PublishedFeedForm;
import org.digijava.module.syndication.util.DbUtil;

/**
 * Action displayes list of all news
 */

public class CreateNewPublishFeed
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        PublishedFeedForm feedForm = (PublishedFeedForm) form;

        HashSet items = null;
        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);

        if( feedForm != null ) {

                PublicationFeed feed = new PublicationFeed();

                feed.setContentType(moduleInstance.getModuleName());
                feed.setInstanceId(moduleInstance.getInstanceName());
                if (!feedForm.getSelectedCountry().equalsIgnoreCase("none")){
                    Country country = new Country();
                    country.setIso(feedForm.getSelectedCountry());
                    feed.setCountry(country);
                }
                else
                    feed.setCountry(null);
                feed.setCreationDate(new Date());
                feed.setFeedDescription(feedForm.getFeedDescription());
                feed.setFeedTitle(feedForm.getFeedTitle());
                Locale language = new Locale();
                language.setCode(feedForm.getSelectedLanguage());
                feed.setLanguage(language);
                feed.setSiteId(moduleInstance.getSite().getSiteId());
                String url = feedForm.getFeedUrl();
                feed.setFeedUrl(url);
                feed.setStatus(false);

                DbUtil.updateFeed(feed);

                url += "?id=" + String.valueOf(feed.getId());
                feed.setFeedUrl(url);
                DbUtil.updateFeed(feed);
        }


        return mapping.findForward("forward");
    }

}
