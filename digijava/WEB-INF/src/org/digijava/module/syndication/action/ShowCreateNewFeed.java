/*
 *   ShowCreateNewFeed.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: ShowCreateNewFeed.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.syndication.form.CollectorFeedForm;

/**
 * Action displayes list of all news
 */

public class ShowCreateNewFeed
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CollectorFeedForm feedForm = (CollectorFeedForm) form;

        if( feedForm != null ) {
            feedForm.loadSchedule();
            feedForm.setProcessingMode(new Long(1));
            feedForm.setFeedUrl( request.getScheme() + "://");
            feedForm.setSourceUrl(request.getScheme() + "://");
        }

        return mapping.findForward("forward");
    }

}
