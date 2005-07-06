/*
 *   SingleServerJobs.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 14, 2004
 * 	 CVS-ID: $Id: SingleServerJobs.java,v 1.1 2005-07-06 10:34:15 rahul Exp $
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

package org.digijava.kernel.config;

import java.util.HashMap;
import java.util.Map;

public class SingleServerJobs {

    private Map singleServerJobs;

    public SingleServerJobs() {
	singleServerJobs = new HashMap();
    }

    public Map getSingleServerJobs() {
	return singleServerJobs;
    }

    public void addSingleServerJob(SingleServerJob singleServerJob) {
	singleServerJobs.put(singleServerJob.getClassName(), singleServerJob);
    }

}