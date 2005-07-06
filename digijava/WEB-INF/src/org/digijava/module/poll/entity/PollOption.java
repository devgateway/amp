/*
*   PollOption.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: PollOption.java,v 1.1 2005-07-06 10:34:31 rahul Exp $
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

public class PollOption {

  /**
   * Poll option unique id
   */
  private long pollOptionId;

  /**
   * Poll unique id
   */
  private long pollId;

  /**
   * Poll answer word
   */
  private String name;

  /**
   * Number of votes
   */
  private long votes;

  /**
   * Poll object reference
   */
  private Poll poll;


// ------------------------------------- Public methods

  /**
   * Get Poll option unique id
   *
   * @return option id
   */
  public long getPollOptionId() {
    return pollOptionId;
  }

  /**
   * Set Poll option unique id
   *
   * @param pollOptionId option id
   */
  public void setPollOptionId(long pollOptionId) {
    this.pollOptionId = pollOptionId;
  }

  /**
   * Get Poll unique id
   *
   * @return poll id
   */
  public long getPollId() {
    return pollId;
  }

  /**
   * Set Poll unique id
   *
   * @param pollId poll id
   */
  public void setPollId(long pollId) {
    this.pollId = pollId;
  }

  /**
   * Get answer word
   *
   * @return answer
   */
  public String getName() {
    return name;
  }

  /**
   * Set answer word
   *
   * @param name answer
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get number of votes
   *
   * @return vote number
   */
  public long getVotes() {
    return votes;
  }

  /**
   * Set number of votes
   *
   * @param votes vote number
   */
  public void setVotes(long votes) {
    this.votes = votes;
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