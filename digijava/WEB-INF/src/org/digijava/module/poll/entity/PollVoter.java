/*
*   PollVoter.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: PollVoter.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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

package org.digijava.module.poll.entity;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PollVoter {

  /**
   * Poll voter unique id
   */
  private long pollVoterId;

  /**
   * Poll unique id
   */
  private long pollId;

  /**
   * Voter ip address
   */
  private String ipAddress;

  /**
   * Poll object reference
   */
  private Poll poll;


// ------------------------------------- Public methods

  /**
   * Get voter id
   *
   * @return voter id
   */
  public long getPollVoterId() {
    return pollVoterId;
  }

  /**
   * Set voter id
   *
   * @param pollVoterId voter id
   */
  public void setPollVoterId(long pollVoterId) {
    this.pollVoterId = pollVoterId;
  }

  /**
   * get poll id
   *
   * @return poll id
   */
  public long getPollId() {
    return pollId;
  }

  /**
   * Set poll id
   *
   * @param pollId poll id
   */
  public void setPollId(long pollId) {
    this.pollId = pollId;
  }

  /**
   * Get voter ip address
   *
   * @return voter ip address
   */
  public String getIpAddress() {
    return ipAddress;
  }

  /**
   * Set voter ip address
   *
   * @param ipAddress voter ip address
   */
  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }


  /**
   * Get Poll object reference
   *
   * @return {@link Poll} object
   */
  public Poll getPoll() {
    return poll;
  }

  /**
   * Set Poll object reference
   *
   * @param poll {@link Poll} object
   */
 public void setPoll(Poll poll) {
    this.poll = poll;
  }
}