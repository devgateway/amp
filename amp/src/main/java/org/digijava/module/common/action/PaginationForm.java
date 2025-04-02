/*
 *   PaginationForm.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created: Nov 17, 2003
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
package org.digijava.module.common.action;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.action.ActionErrors;
import java.util.List;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PaginationForm
    extends ValidatorForm {

  public static class PageInfo {

    private boolean currPage;
    private String pageNo;

    public void setPageNo(String pageNo) {
      this.pageNo = pageNo;
    }

    public String getPageNo() {
      return pageNo;
    }

    public void setCurrPage(boolean currPage) {
      this.currPage = currPage;
    }

    public boolean isCurrPage() {
      return currPage;
    }
  }

  // Pagination
  private String prev;
  private String next;

  private String nav;

  private String pageNo;
  private List pages;

  public int wholeSize; //indicates whole number of items, which are shown on the pages

  public String getNav() {
    return nav;
  }

  public void setNav(String nav) {
    this.nav = nav;
  }

  public String getNext() {
    return next;
  }

  public void setNext(String next) {
    this.next = next;
  }

  public String getPrev() {
    return prev;
  }

  public void setPrev(String prev) {
    this.prev = prev;
  }

  public List getPages() {
    return pages;
  }

  public void setPages(List pages) {
    this.pages = pages;
  }

  public String getPageNo() {
    return pageNo;
  }

  public void setPageNo(String pageNo) {
    this.pageNo = pageNo;
  }

  public int getWholeSize() {
    return wholeSize;
  }

  public void setWholeSize(int wholeSize) {
    this.wholeSize = wholeSize;
  }


  // ---------------

  public void reset(ActionMapping mapping, HttpServletRequest request) {
    this.prev = null;
    this.next = null;
    this.nav = null;
    this.pages = null;
    this.pageNo = null;
    this.wholeSize = 0;
  }

  public ActionErrors validate(ActionMapping actionMapping,
                               HttpServletRequest httpServletRequest) {

    ActionErrors errors = super.validate(actionMapping, httpServletRequest);
    return errors.isEmpty() ? null : errors;
  }


}
