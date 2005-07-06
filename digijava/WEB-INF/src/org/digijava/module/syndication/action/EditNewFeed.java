/*
 *   EditNewFeed.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: EditNewFeed.java,v 1.1 2005-07-06 10:34:23 rahul Exp $
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

import java.util.GregorianCalendar;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.syndication.aggregator.AggregatorManager;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.syndication.dbentity.CollectorFeedItem;
import org.digijava.module.syndication.form.CollectorFeedForm;
import org.digijava.module.syndication.util.DbUtil;

/**
 * Action displayes list of all news
 */

public class EditNewFeed
    extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 javax.servlet.http.HttpServletRequest request,
                                 javax.servlet.http.HttpServletResponse
                                 response) throws java.lang.Exception {

        CollectorFeedForm feedForm = (CollectorFeedForm) form;

        ModuleInstance moduleInstance = RequestUtils.getRealModuleInstance(request);

        if( feedForm != null ) {
            CollectorFeedItem feed = DbUtil.getCollectorFeedItem(feedForm.getItemId());
            if( feed != null ) {

                feed.setSchedule(feedForm.getSelectedSchedule());

                GregorianCalendar scheduleTime = new GregorianCalendar();
                scheduleTime.set(java.util.Calendar.MINUTE, 0);
                scheduleTime.set(java.util.Calendar.HOUR_OF_DAY, feedForm.getSelectedTime().intValue());
                feed.setScheduleTime(scheduleTime.getTime());

                if( feedForm.isStatus()) {
                    feed.setStatus(new Integer(1));
                } else {
                    feed.setStatus(new Integer(0));
                }

                DbUtil.updateFeedCollector(feed);

                if( feedForm.isStatus()) {
                    if( !AggregatorManager.haveJob(feed.getId()) ) {
                        AggregatorManager.addJob(feed);
                    } else {
                        AggregatorManager.updateJob(feed);
                    }
                } else {
                    AggregatorManager.removeJob(feed.getId());
                }
            }
        }


        return mapping.findForward("forward");
    }

}
