/*
 *   EditNewPublishFeed.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: EditNewPublishFeed.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

public class EditNewPublishFeed
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
            PublicationFeed feed = DbUtil.getPublishedFeed(feedForm.getItemId());
            if( feed != null ) {

                feed.setFeedDescription(feedForm.getFeedDescription());
                feed.setFeedTitle(feedForm.getFeedTitle());
                feed.setId(feedForm.getItemId());

                if (!feedForm.getSelectedCountry().equalsIgnoreCase("none")){
                    Country country = new Country();
                    country.setIso(feedForm.getSelectedCountry());
                    feed.setCountry(country);
                }
                else
                    feed.setCountry(null);

                Locale language = new Locale();
                language.setCode(feedForm.getSelectedLanguage());
                feed.setLanguage(language);

                DbUtil.updateFeed(feed);
            }
        }


        return mapping.findForward("forward");
    }

}
