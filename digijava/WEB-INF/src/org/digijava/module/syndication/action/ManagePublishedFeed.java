/*
 *   ManagePublishedFeed.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: ManagePublishedFeed.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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
import org.digijava.module.syndication.dbentity.PublicationFeed;
import org.digijava.module.syndication.form.PublishedFeedForm;
import org.digijava.module.syndication.util.DbUtil;

/**
 * Action displayes list of all news
 */

public class ManagePublishedFeed
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {


        PublishedFeedForm feedForm = (PublishedFeedForm) form;

        if( feedForm.getItemId() != null ) {
                // delete action
                if( feedForm.getAction() == 3 ) {
                    DbUtil.deleteFeed(feedForm.getItemId());
                }
        } else {

            switch( feedForm.getItemOperation() ) {
                case 1:
                    deleteFeeds(feedForm);
                break;
                case 2:
                    publishFeeds(feedForm);
                break;
            }
        }

        feedForm.setSelectedItems(null);
        feedForm.setItemOperation(0);

        return mapping.findForward("forward");
    }

    /**
     *
     * @param feedForm
     * @throws java.lang.Exception
     */
    private void deleteFeeds(PublishedFeedForm feedForm) throws java.lang.Exception {
        if( feedForm.getSelectedItems() != null ) {
            for( int i = 0; i < feedForm.getSelectedItems().length; i++ ) {
                DbUtil.deleteFeed(new Long(feedForm.getSelectedItems()[i]));
            }
        }
    }

    /**
     *
     * @param feedForm
     * @throws java.lang.Exception
     */
    private void publishFeeds(PublishedFeedForm feedForm) throws java.lang.Exception {
        PublicationFeed feed = null;
        Long id = null;
        if( feedForm.getSelectedItems() != null ) {
            for( int i = 0; i < feedForm.getSelectedItems().length; i++ ) {
                id = new Long(feedForm.getSelectedItems()[i]);
                feed = DbUtil.getPublishedFeed(id);
                if( feed != null ) {
                    feed.setStatus(true);
                    DbUtil.updateFeed(feed);
                }
            }
        }
    }

}
