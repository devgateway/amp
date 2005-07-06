/*
 *   AbstractSingletonJob.java
 * 	 @Author George Kvizhinadze gio@powerdot.org
 * 	 Created: Jun 22, 2004
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


package org.digijava.kernel.job.sample;

import org.apache.log4j.Logger;
import org.digijava.kernel.job.AbstractSingletonJob;
import org.quartz.JobExecutionException;
import org.quartz.JobExecutionContext;
import org.digijava.kernel.job.JobCachedObject;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestJob
    extends AbstractSingletonJob {

  private static Logger logger = Logger.getLogger(TestJob.class);

  public boolean executeSingletonJob(JobExecutionContext context) throws
      JobExecutionException {
    boolean success = true;

    Date date = context.getFireTime();
    logger.info("Quartz call : " + context.getJobDetail().getName()
                       + " : " + date.toString());

    try {
      Thread.sleep(1000);
    }
    catch (InterruptedException ex) {
    }

    logger.debug("Wake up");

    return success;
  }
}