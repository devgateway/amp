/*
 *   AbstractSingletonJob.java
 * 	 @Author George Kvizhinadze gio@digijava.org
 * 	 Created: Jun 22, 2004
 * 	 CVS-ID: $Id: AbstractSingletonJob.java,v 1.1 2005-07-06 10:34:32 rahul Exp $
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

package org.digijava.kernel.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.kernel.util.DigiCacheManager;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.StatefulJob;
import net.sf.swarmcache.ObjectCache;
import org.digijava.kernel.util.DigiConfigManager;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public abstract class AbstractSingletonJob
    implements StatefulJob {

  private static Logger logger = Logger.getLogger(AbstractSingletonJob.class);
  public final static String CACHE_REGION =
      "org.digijava.kernel.job.JobsCacheRegion";

  public AbstractSingletonJob() {
  }

  private static Map jobNameVersionMap = null;

  static {
    jobNameVersionMap = new HashMap ();
  }

  private synchronized long getJobVersion(String jobName) {
    Long jobVersion = (Long) jobNameVersionMap.get(jobName);
    return (jobVersion==null)?0:jobVersion.longValue();
  }

  private synchronized void setJobVersion(String jobName, long version) {
    jobNameVersionMap.put(jobName, new Long(version));
  }


  public void execute(JobExecutionContext context) throws
      JobExecutionException {

    JobDetail jobDetail = context.getJobDetail();
    String jobName = jobDetail.getName();

    ObjectCache jobCache = DigiCacheManager.getInstance().getCache(
        CACHE_REGION);
    JobCachedObject cachedJob = (JobCachedObject) jobCache.get(jobName);
    String serverName = DigiConfigManager.getConfig().getServerType();
    if (serverName == null || serverName.trim().length() == 0) {
      logger.warn ("Server type is not specified in digi.xml. " +
                   "This may cause abnormal execution of the scheduled job");
      serverName = JobCachedObject.UNKNOWN_SERVER_NAME;
    }

    if (cachedJob == null ||
        (cachedJob.getVerson() == getJobVersion(jobName)/* &&
         cachedJob.getState() != JobCachedObject.STATE_RUNNING*/)) {

      logger.info("Processing job [" + this.getClass().getName() + "]");

      if (cachedJob != null) {
        cachedJob.setVerson(cachedJob.getVerson() + 1);
        logger.debug("Job [" + this.getClass().getName() + "] version is " + cachedJob.getVerson());
      }
      else {
        cachedJob = new JobCachedObject();
        cachedJob.setModifyTime(new Date());
        cachedJob.setJobName(jobName);
        cachedJob.setVerson(1);
        logger.info("Seting initial version to job [" + this.getClass().getName() +
                    "]");
      }

      cachedJob.setServerName(serverName);
      cachedJob.setState(JobCachedObject.STATE_RUNNING);
      jobCache.put(jobName, cachedJob);

      this.setJobVersion(jobName, cachedJob.getVerson());


        try {
          Thread.sleep(DigiConfigManager.getConfig().getJobDelaySec() * 1000);
        }
        catch (InterruptedException iex) {
          logger.warn("Job delay exception",
                      iex);
        }

      cachedJob = (JobCachedObject)jobCache.get(jobName);

      if (cachedJob.getVerson() ==
          this.getJobVersion(jobName) &&
          cachedJob.getServerName().compareTo(serverName) == 0) {

        boolean success = false;
        try {
          success = executeSingletonJob(context);
        }
        catch (SchedulerException ex) {
          logger.warn("Unable to get schedule job [" + this.getClass().getName() +
                      "]",
                      ex);
        }

        if (success) {
          cachedJob.setState(JobCachedObject.STATE_FINISHED);
        }
        else {
          cachedJob.setState(JobCachedObject.STATE_FAILED);
        }
        jobCache.put(jobName, cachedJob);
        logger.info("Job [" + this.getClass().getName() + "] completed (" +
                    cachedJob.getState() + ")");
      } else {
        setJobVersion(jobName, cachedJob.getVerson());
        logger.debug("Job is running on another server. Version: " +
                     String.valueOf(cachedJob.getVerson()));
      }
    }
    else {
      setJobVersion(jobName, cachedJob.getVerson());
      logger.debug("Job is running on another server. Version: " +
                   String.valueOf(cachedJob.getVerson()));
    }
  }

  public abstract boolean executeSingletonJob(JobExecutionContext context) throws
      JobExecutionException;
}