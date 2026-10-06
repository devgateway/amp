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
