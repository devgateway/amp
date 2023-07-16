/**
 * This file is part of DiGi project (www.digijava.org).
 * DiGi is a multi-site portal system written in Java/J2EE.
 *
 * Copyright (C) 2002-2007 Development Gateway Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.digijava.kernel.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.kernel.cache.AbstractCache;
import org.digijava.kernel.util.DigiCacheManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.quartz.*;

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
    JobKey jobKey = jobDetail.getKey();
    String jobName = jobKey.getName();

    AbstractCache jobCache = DigiCacheManager.getInstance().getCache(CACHE_REGION);
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
