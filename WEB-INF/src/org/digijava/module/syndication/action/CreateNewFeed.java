/*
 *   CreateNewFeed.java
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
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.syndication.aggregator.AggregatorManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.syndication.dbentity.CollectorFeed;
import org.digijava.module.syndication.dbentity.CollectorFeedItem;
import org.digijava.module.syndication.form.CollectorFeedForm;
import org.digijava.module.syndication.util.DbUtil;

/**
 * Action displayes list of all news
 */

public class CreateNewFeed
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CollectorFeedForm feedForm = (CollectorFeedForm) form;
        Set items = null;
        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);

        if( feedForm != null ) {
            CollectorFeed feed = DbUtil.getFeed(feedForm.getFeedUrl());

            if( feed == null ) {
                feed = new CollectorFeed();
                items = new HashSet();
                feed.setCreationDate(feedForm.getDateAdded());
                feed.setFeedDescription(feedForm.getFeedDescription());
                feed.setFeedTitle(feedForm.getFeedTitle());

                feed.setFeedUrl(feedForm.getFeedUrl());
                feed.setItems(items);
                feed.setSourceName(feedForm.getSourceName());
                feed.setSourceUrl(feedForm.getSourceUrl());

            } else {
                items = feed.getItems();
            }
                CollectorFeedItem newFeedItem = new CollectorFeedItem();
                newFeedItem.setContentType(feedForm.getContentType());
                newFeedItem.setInstanceId(moduleInstance.getInstanceName());
                newFeedItem.setSiteId(moduleInstance.getSite().getSiteId());
                newFeedItem.setCreationDate(new Date());
                newFeedItem.setFeed(feed);
                newFeedItem.setSchedule(feedForm.getSelectedSchedule());

                GregorianCalendar scheduleTime = new GregorianCalendar();
                scheduleTime.set(java.util.Calendar.MINUTE, 0);
                scheduleTime.set(java.util.Calendar.HOUR_OF_DAY, feedForm.getSelectedTime().intValue());
                newFeedItem.setScheduleTime(scheduleTime.getTime());

                if( feedForm.isStatus() )
                    newFeedItem.setStatus(new Integer(1));
                else
                    newFeedItem.setStatus(new Integer(0));

                items.add(newFeedItem);

                DbUtil.updateFeed(feed);

                if( feedForm.isStatus()) {
                    if( !AggregatorManager.haveJob(newFeedItem.getId()) ) {
                        AggregatorManager.addJob(newFeedItem);
                    } else {
                        AggregatorManager.updateJob(newFeedItem);
                    }
                }

        }


        return mapping.findForward("forward");
    }

}
