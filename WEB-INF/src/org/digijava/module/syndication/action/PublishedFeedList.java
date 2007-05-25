/*
 *   PublishedFeedList.java
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.common.action.PaginationAction;
import org.digijava.module.syndication.form.PublishedFeedForm;
import org.digijava.module.syndication.util.DbUtil;

/**
 * Action displayes list of all news
 */

public class PublishedFeedList
    extends PaginationAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        PublishedFeedForm feedForm = (PublishedFeedForm) form;
        List feedList = null;

        // get module instance
        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);

        doStartPagination(feedForm, feedForm.getItemsPerPage());

        feedList = DbUtil.getPublishedFeeds(moduleInstance.getSite().getSiteId(),
                                   moduleInstance.getInstanceName(), moduleInstance.getModuleName(),
                                   getOffset(), feedForm.getItemsPerPage() + 1);

        if (feedList != null && feedList.size() > 0) {

            int n;
            if ( (feedForm.getItemsPerPage() + 1) == feedList.size())
                n = feedList.size() - 1;
            else {
                n = feedList.size();
            }

            feedForm.setPublishedFeedList(feedList.subList(0, n));

        } else {
          feedForm.setPublishedFeedList(null);
        }


        endPagination((feedList != null) ? feedList.size() : 0);


        return mapping.findForward("forward");
    }

}
