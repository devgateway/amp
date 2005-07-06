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


package org.digijava.kernel.job;

import java.io.Serializable;
import java.util.Date;

public class JobCachedObject implements Serializable {

  public static final int STATE_UNKNOWN = 0;
  public static final int STATE_RUNNING = 1;
  public static final int STATE_FINISHED = 2;
  public static final int STATE_FAILED = 3;

  public static final String UNKNOWN_SERVER_NAME = "Unknown server name";

  private Date modifyTime;
  private long verson;
  private int state;
  private String jobName;
  private String serverName;

  public JobCachedObject() {
    verson = 0;
    state = STATE_UNKNOWN;
  }

  public String getJobName() {
    return jobName;
  }
  public void setJobName(String jobName) {
    this.jobName = jobName;
  }
  public Date getModifyTime() {
    return modifyTime;
  }
  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }
  public String getServerName() {
    return serverName;
  }
  public void setServerName(String serverName) {
    this.serverName = serverName;
  }
  public int getState() {
    return state;
  }
  public void setState(int state) {
    this.state = state;
  }
  public long getVerson() {
    return verson;
  }
  public void setVerson(long verson) {
    this.verson = verson;
  }
}