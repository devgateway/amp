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

import org.apache.log4j.Logger;
import org.digijava.kernel.config.SingleServerJob;
import org.digijava.kernel.config.SingleServerJobs;
import org.digijava.kernel.util.DigiConfigManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

public abstract class SingleHostJob
      implements Job {

    private static Logger logger = Logger.getLogger(SingleHostJob.class);
    private static List hostNames;

    static {
    //host names
    hostNames = new ArrayList();
    try {
        Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
        NetworkInterface ni = (NetworkInterface) interfaces.nextElement();
        Enumeration ipAddresses = ni.getInetAddresses();
        while (ipAddresses.hasMoreElements()) {
            InetAddress addr = (InetAddress) ipAddresses.nextElement();
            if (!addr.isAnyLocalAddress() && !addr.isLoopbackAddress()) {
            hostNames.add(addr.getHostName());
            }

        }

        }
    }
    catch (SocketException ex) {
        logger.error("Unable to resolve names for the host", ex);
    }
    }

    public void execute(JobExecutionContext context) throws
      JobExecutionException {

    //get single server jobs
    SingleServerJobs singleServerJobs = DigiConfigManager.getConfig().
          getSingleServerJobs();

    SingleServerJob singleServerJob = (SingleServerJob) singleServerJobs.
          getSingleServerJobs().get(this.getClass().getName());

    if (singleServerJob != null) {
        if (singleServerJob.getJobHosts() != null &&
        singleServerJob.getJobHosts().size() != 0) {
        Map hostNamesMap = new HashMap();
        Iterator iter = hostNames.iterator();
        while (iter.hasNext()) {
            String item = (String) iter.next();
            hostNamesMap.put(item, item);
        }
        Map jobHostNamesMap = new HashMap();
        iter = singleServerJob.getJobHosts().iterator();
        while (iter.hasNext()) {
            String item = (String) iter.next();
            jobHostNamesMap.put(item, item);
        }

        iter = hostNames.iterator();
        while (iter.hasNext()) {
            String item = (String) iter.next();
            if (jobHostNamesMap.get(item) != null) {
            executeSingleHostJob(context);
            break;
            }
        }
        }
        else {
        logger.warn("Job's host list empty");
        }
    }
    else {
        logger.warn("Job is NULL");
    }
    }

    public abstract boolean executeSingleHostJob(JobExecutionContext context) throws
      JobExecutionException;
}
