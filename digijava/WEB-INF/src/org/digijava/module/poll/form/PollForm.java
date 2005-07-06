/*
*   PollForm.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: PollForm.java,v 1.1 2005-07-06 10:34:32 rahul Exp $
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

package org.digijava.module.poll.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.poll.entity.Poll;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PollForm
    extends ActionForm {

  /**
   * Poll object reference
   */
  private Poll poll;

  /**
   * selected answer
   */
  private long selectedOption;

  /**
   * voted
   */
  private boolean voted;

// ------------------------------------- Public methods

  /**
   * Get poll object
   *
   * @return poll {@link Poll}
   */
  public Poll getPoll() {
    return poll;
  }

  /**
   * Set poll object
   *
   * @param poll object
   */
  public void setPoll(Poll poll) {
    this.poll = poll;
  }

  /**
   * Get selected answer
   *
   * @return selected answare
   */
  public long getSelectedOption() {
    return selectedOption;
  }

  /**
   * Set selected answer
   *
   * @param selectedOption selected answare
   */
  public void setSelectedOption(long selectedOption) {
    this.selectedOption = selectedOption;
  }

  /**
   * Check already is voted
   *
   * @return return voted or not
   */
  public boolean isVoted() {
    return voted;
  }

  /**
   * Set voter
   *
   * @param voted
   */
  public void setVoted(boolean voted) {
    this.voted = voted;
  }

}