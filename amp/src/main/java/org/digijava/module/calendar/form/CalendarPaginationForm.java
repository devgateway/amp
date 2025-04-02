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

package org.digijava.module.calendar.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionErrors;
import org.digijava.module.common.action.PaginationForm;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class CalendarPaginationForm
    extends PaginationForm {

  // Pagination
  private String prevYear;
  private String nextYear;

  private String navYear;


  public String getNavYear() {
    return navYear;
  }

  public void setNavYear(String navYear) {
    this.navYear = navYear;
  }

  public String getNextYear() {
    return nextYear;
  }

  public void setNextYear(String nextYear) {
    this.nextYear = nextYear;
  }

  public String getPrevYear() {
    return prevYear;
  }

  public void setPrevYear(String prevYear) {
    this.prevYear = prevYear;
  }

  // ---------------

  public void reset(ActionMapping mapping, HttpServletRequest request) {

    prevYear = null;
    nextYear = null;
    navYear = null;

    super.reset(mapping,request);
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {

    ActionErrors errors = super.validate(actionMapping, httpServletRequest);
    return errors.isEmpty() ? null : errors;
  }

}
