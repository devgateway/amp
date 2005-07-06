/*
 *   SingleServerJob.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Oct 14, 2004
 * 	 CVS-ID: $Id: SingleServerJob.java,v 1.1 2005-07-06 10:34:15 rahul Exp $
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

import java.util.ArrayList;
import java.util.List;

public class SingleServerJob {

    private List jobHosts;
    private String className;

    public SingleServerJob() {
	jobHosts = new ArrayList();
    }

    public List getJobHosts() {
	return jobHosts;
    }

    public void addJobHost(String jobHost) {
	jobHosts.add(jobHost);
    }

    public String getClassName() {
	return className;
    }

    public void setClassName(String className) {
	this.className = className;
    }

}