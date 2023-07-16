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

package org.digijava.kernel.job.sample;

import org.apache.log4j.Logger;
import org.digijava.kernel.job.AbstractSingletonJob;
import org.quartz.JobExecutionException;
import org.quartz.JobExecutionContext;
import org.digijava.kernel.job.JobCachedObject;
import org.quartz.JobKey;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestJob
    extends AbstractSingletonJob {

  private static Logger logger = Logger.getLogger(TestJob.class);

  public boolean executeSingletonJob(JobExecutionContext context) throws
      JobExecutionException {
    boolean success = true;

    Date date = context.getFireTime();
    JobKey jobKey =  context.getJobDetail().getKey();
    logger.info("Quartz call : " + jobKey.getName()
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
