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

package org.digijava.module.calendar.action;

import org.apache.log4j.Logger;
import org.digijava.module.calendar.form.CalendarPaginationForm;
import org.digijava.module.common.action.PaginationAction;



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
