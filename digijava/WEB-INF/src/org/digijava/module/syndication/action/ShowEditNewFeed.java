/*
 *   ShowEditNewFeed.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: ShowEditNewFeed.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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
import org.digijava.module.syndication.dbentity.CollectorFeedItem;
import org.digijava.module.syndication.form.CollectorFeedForm;
import org.digijava.module.syndication.util.DbUtil;

/**
 * Action displayes list of all news
 */

public class ShowEditNewFeed
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {


        CollectorFeedForm feedForm = (CollectorFeedForm) form;

        if( feedForm != null ) {
            CollectorFeedItem feed = DbUtil.getCollectorFeedItem(feedForm.getItemId());
            if( feed != null ) {
                feedForm.loadSchedule();
                feedForm.setProcessingMode(new Long(2));
                feedForm.setFeedTitle(feed.getFeed().getFeedTitle());
                feedForm.setFeedDescription(feed.getFeed().getFeedDescription());
                feedForm.setFeedUrl(feed.getFeed().getFeedUrl());
                feedForm.setSourceName(feed.getFeed().getSourceName());
                feedForm.setSourceUrl(feed.getFeed().getSourceUrl());
                feedForm.setSelectedSchedule(feed.getSchedule());
                if( feed.getStatus().intValue() == 0)
                    feedForm.setStatus(false);
                else
                    feedForm.setStatus(true);
            }

        }



        return mapping.findForward("forward");
    }

}
