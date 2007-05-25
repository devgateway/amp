/*
 *   ManageFeed.java
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.syndication.aggregator.AggregatorManager;
import org.digijava.module.syndication.dbentity.CollectorFeedItem;
import org.digijava.module.syndication.form.CollectorFeedForm;
import org.digijava.module.syndication.util.DbUtil;

/**
 * Action displayes list of all news
 */

public class ManageFeed
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CollectorFeedForm feedForm = (CollectorFeedForm) form;
        if (feedForm.getItemId() != null) {
            switch (feedForm.getAction()) {
                case 0: {
                    CollectorFeedItem feed = DbUtil.getCollectorFeedItem(
                        feedForm.getItemId());
                    if (feed != null) {
                        feed.setStatus(new Integer(0));
                        DbUtil.updateFeedCollector(feed);
                        if( AggregatorManager.haveJob(feed.getId()) )
                            AggregatorManager.removeJob(feed.getId());
                    }
                }
                break;
                case 1: {
                    CollectorFeedItem feed = DbUtil.getCollectorFeedItem(
                        feedForm.getItemId());
                    if (feed != null) {
                        feed.setStatus(new Integer(1));
                        DbUtil.updateFeedCollector(feed);
                        if( !AggregatorManager.haveJob(feed.getId()) )
                            AggregatorManager.addJob(feed);
                        else
                            AggregatorManager.updateJob(feed);
                    }
                }
                break;
                case 3: {
                    DbUtil.deleteCollectorFeed(feedForm.getItemId());
                    AggregatorManager.removeJob(feedForm.getItemId());
                }
                break;
            }
        }
        else {

            switch (feedForm.getItemOperation()) {
                case 1:
                    deleteFeeds(feedForm);
                    break;
                case 2:
                    changeFeedStatus(feedForm, new Integer(1));
                    break;
                case 3:
                    changeFeedStatus(feedForm, new Integer(0));
                    break;
                case 4:
                    manualHarvest(feedForm);
                    break;
            }
        }

        return mapping.findForward("forward");
    }


    /**
     *
     * @param feedForm
     * @throws java.lang.Exception
     */
    private void manualHarvest(CollectorFeedForm feedForm) throws java.lang.
        Exception {
        if (feedForm.getSelectedItems() != null) {
            for (int i = 0; i < feedForm.getSelectedItems().length; i++) {
                Long id = new Long(feedForm.getSelectedItems()[i]);
                AggregatorManager.executeJob(id);
            }
        }
    }

    /**
     *
     * @param feedForm
     * @throws java.lang.Exception
     */
    private void deleteFeeds(CollectorFeedForm feedForm) throws java.lang.
        Exception {
        if (feedForm.getSelectedItems() != null) {
            for (int i = 0; i < feedForm.getSelectedItems().length; i++) {
                Long id = new Long(feedForm.getSelectedItems()[i]);
                DbUtil.deleteCollectorFeed(id);
                AggregatorManager.removeJob(id);
            }
        }
    }

    /**
     *
     * @param feedForm
     * @throws java.lang.Exception
     */
    private void changeFeedStatus(CollectorFeedForm feedForm, Integer status) throws
        java.lang.Exception {
        CollectorFeedItem feed = null;
        Long id = null;
        if (feedForm.getSelectedItems() != null) {
            for (int i = 0; i < feedForm.getSelectedItems().length; i++) {
                id = new Long(feedForm.getSelectedItems()[i]);
                feed = DbUtil.getCollectorFeedItem(id);
                if (feed != null) {
                    feed.setStatus(status);
                    DbUtil.updateFeedCollector(feed);
                    if( status.intValue() == 1 ) {
                        if( !AggregatorManager.haveJob(feed.getId()) )
                            AggregatorManager.addJob(feed);
                        else
                            AggregatorManager.updateJob(feed);
                    } else if(status.intValue() == 0) {
                        AggregatorManager.removeJob(feed.getId());
                    }
                }
            }
        }
    }

}
