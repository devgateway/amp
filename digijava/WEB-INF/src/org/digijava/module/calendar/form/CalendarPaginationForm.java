/*
 *   CalendarPaginationForm.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: May 04, 2004
 * 	 CVS-ID: $Id: CalendarPaginationForm.java,v 1.1 2005-07-06 10:34:24 rahul Exp $
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