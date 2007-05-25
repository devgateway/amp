/*
*   Poll.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id$
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

import java.util.Set;


/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Poll {

  /**
   * Poll unique id
   */
  private long pollId;

  /**
   * poll question word
   */
  private String question;

  /**
   * poll teaser( module ) instance key
   */
  private String instanceKey;

  /**
   * set of poll answers
   */
  private Set pollOptions;

  /**
   * set of poll voters
   */
  private Set pollVoters;


// ------------------------------------- Public methods

  /**
   * Get Poll unique id
   *
   * @return poll id
   */
  public long getPollId() {
    return pollId;
  }

  /**
   * Get Poll unique id
   *
   * @param pollId poll id
   */
  public void setPollId(long pollId) {
    this.pollId = pollId;
  }

  /**
   * Get Poll question word
   *
   * @return poll question word
   */
  public String getQuestion() {
    return question;
  }

  /**
   * Set Poll question word
   *
   * @param question question word
   */
  public void setQuestion(String question) {
    this.question = question;
  }

  /**
   * Get Poll teaser( module ) instance key
   *
   * @return key
   */
  public String getInstanceKey() {
    return instanceKey;
  }

  /**
   * Set Poll teaser( module ) instance key
   *
   * @param instanceKey key
   */
  public void setInstanceKey(String instanceKey) {
    this.instanceKey = instanceKey;
  }

  /**
   * Get set of answers
   *
   * @return {@link PollOption}
   */
  public java.util.Set getPollOptions() {
    return pollOptions;
  }

  /**
   * Set set of answers
   *
   * @param pollOptions set of poll answers {@link PollOption}
   */
  public void setPollOptions(java.util.Set pollOptions) {
    this.pollOptions = pollOptions;
  }

  /**
   * Get poll voters
   *
   * @return set of poll voters {@link PollVoter}
   */
  public Set getPollVoters() {
    return pollVoters;
  }

  /**
   * Set poll voters
   *
   * @param pollVoters set of poll voters {@link PollVoter}
   */
  public void setPollVoters(Set pollVoters) {
    this.pollVoters = pollVoters;
  }

}