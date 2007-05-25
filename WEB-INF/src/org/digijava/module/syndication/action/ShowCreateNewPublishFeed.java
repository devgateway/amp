/*
 *   ShowCreateNewPublishFeed.java
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

import java.util.List;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.module.syndication.form.PublishedFeedForm;
import org.digijava.module.syndication.util.SynUtil;

/**
 * Action displayes list of all news
 */

public class ShowCreateNewPublishFeed
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        PublishedFeedForm feedForm = (PublishedFeedForm) form;

        if( feedForm != null ) {
            feedForm.setProcessingMode(new Long(1));

            // set country list
            List sortedCountries = SynUtil.getCountries(request);
            TrnCountry none = new TrnCountry("none", "None");
            sortedCountries.add(0, none);
            feedForm.setCountry(sortedCountries);

            // set languages
            List sortedLanguages = SynUtil.getLanguages(request);
            feedForm.setLanguage(sortedLanguages);

        }

        return mapping.findForward("forward");
    }

}
