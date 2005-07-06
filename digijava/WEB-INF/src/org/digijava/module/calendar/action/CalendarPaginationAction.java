/*
 *   CalendarPaginationAction.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: May 04, 2004
     * 	 CVS-ID: $Id: CalendarPaginationAction.java,v 1.1 2005-07-06 10:34:29 rahul Exp $
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

package org.digijava.module.calendar.action;

import org.apache.log4j.Logger;
import org.digijava.module.common.action.PaginationAction;
import org.digijava.module.calendar.form.CalendarPaginationForm;



/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class CalendarPaginationAction
    extends PaginationAction {

  private static Logger logger = Logger.getLogger(CalendarPaginationAction.class);

  public String navYear;
  public CalendarPaginationForm formYear;
  public int countYear;
  public int maxCountYear;
  public int offsetYear;
  int directionYear;

  /**
   *
   */
  public void doStartPaginationByYear(CalendarPaginationForm form, int maxCount) {

    this.formYear = form;
    this.countYear = maxCount;
    this.maxCountYear = maxCount;
    this.offsetYear = 0;

    navYear = form.getNavYear();

    logger.debug("Navigation Year: " + navYear);

    if (navYear == null) {
      navYear = "0";
    }
    if ( (navYear != null && Integer.parseInt(navYear) >= 0)) {
      directionYear = DIRECTION_FORWARD;
    }
    else {
      directionYear = DIRECTION_BACKWARD;
    }

    if (navYear != null) {
      int count = Integer.parseInt(navYear);
      if (count < 0)
        count++;
      this.offsetYear = (Math.abs(count)) * maxCountYear;

      logger.debug("Offset Year: " + this.offsetYear);
    }
  }

  //-----------------------------------//
  public void doEndPaginationByYear(int size) {

    // set prev and next value
    formYear.setNextYear(Integer.toString( (Integer.parseInt(navYear) + 1)));
    formYear.setPrevYear(Integer.toString( (Integer.parseInt(navYear) - 1)));
    // -------

    if (size < this.countYear + 1) {
      if (directionYear == DIRECTION_FORWARD)
        formYear.setNextYear(null);
      else
        formYear.setPrevYear(null);
    }
  }


  public int getOffsetYear() {
    return offsetYear;
  }
  public int getDirectionYear() {
    return directionYear;
  }

  public int getCountYear() {
    return countYear;
  }

  public int getNavYear() {
    return Integer.parseInt(navYear);
  }

}
