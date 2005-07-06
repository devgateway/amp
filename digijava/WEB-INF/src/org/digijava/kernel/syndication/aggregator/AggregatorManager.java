/*
 *   AggregatorManager.java
 * 	 @Author Lasha Dolidze lasha@digijava.org
 * 	 Created:
 * 	 CVS-ID: $Id: AggregatorManager.java,v 1.1 2005-07-06 10:34:25 rahul Exp $
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
package org.digijava.kernel.syndication.aggregator;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.module.syndication.dbentity.CollectorFeedItem;
import org.digijava.module.syndication.util.DbUtil;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class AggregatorManager {

    private static Logger logger = Logger.getLogger(AggregatorManager.class);

    public static synchronized void initialize() throws DgException {
        try {

            logger.info("Initialize Aggregator");

            // get default quartz scheduler
            Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
            sched.start();

            // get active feeds from database (where status = 1)
            List list = DbUtil.getActiveFeeds();
            if (list == null || list.size() == 0) {
                logger.info("No active feeds in aggregator");
            }
            else {

                logger.info("Active feeds: " + list.size());
                Iterator iter = list.iterator();
                while (iter.hasNext()) {
                    CollectorFeedItem item = (CollectorFeedItem) iter.next();

                    addJob(item, sched);
                }
            }
        }
        catch (Exception ex) {
            throw new DgException("can't initialize aggregator", ex);
        }
    }


    /**
     *
     * @param item
     * @return
     */
    public static synchronized boolean addJob(CollectorFeedItem item) {
        Scheduler sched = null;
        boolean retvalue = true;
        try {

            sched = StdSchedulerFactory.getDefaultScheduler();
            retvalue = addJob(item,sched);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }

        return retvalue;
    }

    /**
     *
     * @param item
     * @param sched
     * @return
     * @throws java.lang.Exception
     */
    public static synchronized boolean addJob(CollectorFeedItem item,
                                              Scheduler sched) {

        JobDetail jobDetail = null;
        CronTrigger trigger = null;
        try {
            if (item.getSchedule().intValue() != 0) {
                logger.warn("unknow schedule type " + item.getSchedule() +
                            " feed " + item.getFeed().getFeedUrl() +
                            ", feed skipped");
                return false;
            }

            jobDetail = new JobDetail(String.valueOf(item.getId()),
                                      "Aggregator",
                                      RssFetch.class);
            trigger = new CronTrigger(String.valueOf(item.getId()),
                                      "Aggregator");

            jobDetail.getJobDataMap().put("feedId", item.getFeed().getId());
            jobDetail.getJobDataMap().put("itemId", item.getId());
            jobDetail.getJobDataMap().put("feedUrl", item.getFeed().getFeedUrl());
            jobDetail.getJobDataMap().put("moduleName",item.getContentType());
            jobDetail.getJobDataMap().put("moduleInstance",item.getInstanceId());
            jobDetail.getJobDataMap().put("siteId",item.getSiteId());
            Calendar cal = new GregorianCalendar();
            cal.setTime(item.getScheduleTime());
            logger.info("Scheduled Daily " + cal.get(Calendar.HOUR_OF_DAY) +
                        ":" + cal.get(Calendar.MINUTE) + " Feed " +
                        item.getFeed().getFeedUrl());

            trigger.setCronExpression("0 " +
                                      cal.get(Calendar.MINUTE) +
                                      " " +
                                      cal.get(Calendar.HOUR_OF_DAY) +
                                      " ? * *");
            sched.scheduleJob(jobDetail, trigger);
        }
        catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }

        return true;
    }

    /**
     *
     * @param id
     * @return
     */
    public static synchronized boolean haveJob(Long id) {
        Scheduler sched = null;
        boolean retvalue = false;
        try {

            sched = StdSchedulerFactory.getDefaultScheduler();
            JobDetail detail = sched.getJobDetail(String.valueOf(id),"Aggregator");
            if( detail != null ) {
                return true;
            }
        }

        catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }

        return retvalue;
    }

    /**
     *
     * @param id
     * @return
     * @throws java.lang.Exception
     */
    public static synchronized boolean removeJob(Long id) {
        Scheduler sched = null;
        boolean retvalue = true;
        try {

            sched = StdSchedulerFactory.getDefaultScheduler();
            retvalue = sched.deleteJob(String.valueOf(id), "Aggregator");
        }

        catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }

        return retvalue;
    }

    /**
     *
     * @param item
     * @return
     * @throws java.lang.Exception
     */
    public static synchronized boolean updateJob(CollectorFeedItem item) {
        Scheduler sched = null;
        boolean retvalue = true;
        try {

            sched = StdSchedulerFactory.getDefaultScheduler();
            sched.deleteJob(String.valueOf(item.getId()), "Aggregator");
            retvalue = addJob(item, sched);
        }

        catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }

        return retvalue;
    }


    /**
     *
     * @param item
     * @return
     * @throws java.lang.Exception
     */
    public static synchronized boolean executeJob(Long id) {
        Scheduler sched = null;
        boolean retvalue = true;
        try {

            sched = StdSchedulerFactory.getDefaultScheduler();
            sched.triggerJob(String.valueOf(id), "Aggregator");
        }

        catch (Exception ex) {
            logger.error(ex.getMessage());
            return false;
        }

        return retvalue;
    }

}
