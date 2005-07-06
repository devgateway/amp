/*
 *   SingleHostJob.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 14, 2004
 * 	 CVS-ID: $Id: SingleHostJob.java,v 1.1 2005-07-06 10:34:32 rahul Exp $
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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.digijava.kernel.config.SingleServerJob;
import org.digijava.kernel.config.SingleServerJobs;
import org.digijava.kernel.util.DigiConfigManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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