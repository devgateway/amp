/*
 *   PaginationAction.java
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

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import org.apache.struts.action.Action;

/**
 * <p>Title: DiGiJava</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class PaginationAction
    extends Action {

  private static Logger logger = Logger.getLogger(PaginationAction.class);

  // infinite Pagination
  public static int INFINIT = 0xFFFF;

  public static int DIRECTION_FORWARD = 1;

  public static int DIRECTION_BACKWARD = 0;

  public String navigation;
  public String pageNo;
  public PaginationForm form;
  public int count;
  public int maxCount;
  public int offset;
  int direction;

  /**
   *
   * @param form
   * @param maxCount
   */
  public void doStartPagination(PaginationForm form, int maxCount) {

    this.form = form;
    this.count = maxCount;
    this.maxCount = maxCount;
    this.offset = 0;

    navigation = form.getNav();

    logger.debug("Navigation: " + navigation);

    if (navigation == null) {
      navigation = "0";
    }
    pageNo = form.getPageNo();

    logger.debug("pageNo: " + pageNo);

    if ( (navigation != null && Integer.parseInt(navigation) >= 0)) {
      direction = DIRECTION_FORWARD;
    }
    else {
      direction = DIRECTION_BACKWARD;
    }

    if (navigation != null) {
      int count = Integer.parseInt(navigation);
      if (pageNo != null) {
        count = Integer.parseInt(pageNo) - 1;
        navigation = Integer.toString(count);
      }
      else {
        pageNo = Integer.toString( (Integer.parseInt(navigation) + 1));
      }
      if (count < 0)
        count++;
      this.offset = (Math.abs(count)) * maxCount;

      logger.debug("Offset: " + this.offset);
    }
  }

  /**
   *
   * @param form
   */
  public void doEndPagination(int size) {

    // set prev and next value
    form.setNext(Integer.toString( (Integer.parseInt(navigation) + 1)));
    form.setPrev(Integer.toString( (Integer.parseInt(navigation) - 1)));
    // -------

    if (size < this.count + 1) {
      if (direction == DIRECTION_FORWARD)
        form.setNext(null);
      else
        form.setPrev(null);
    }
  }

  /**
   *
   * @param form
   */
  public void doEndPagination(PaginationForm form,int size) {

    //situation,when next and prev both are null
    logger.debug("WHOLE SIZE:"+form.getWholeSize());

    if ( form.getWholeSize() == 0 ||
        size >= form.getWholeSize() ) {
      form.setNext(null);
      form.setPrev(null);
      return;
    }
    // set prev and next value
    form.setNext(Integer.toString( (Integer.parseInt(navigation) + 1)));
    form.setPrev(Integer.toString( (Integer.parseInt(navigation) - 1)));
    // -------

    if (size < this.count + 1) {
      if (direction == DIRECTION_FORWARD)
        form.setNext(null);
      else
        form.setPrev(null);
    }
  }

  /**
   *
   * @param form
   */
  public void endPagination(int size) {

    // set prev and next value
//        if ((Integer.parseInt(navigation) + 1) < size) {
    if (maxCount < size) {
      form.setNext(Integer.toString( (Integer.parseInt(navigation) + 1)));
    }
    else {
      form.setNext(null);
    }
    if ( (Integer.parseInt(navigation) - 1) >= 0) {
      form.setPrev(Integer.toString( (Integer.parseInt(navigation) - 1)));
    }
    else {
      form.setPrev(null);
    }
  }

  public void genPagesList(PaginationForm form, int size) {

    int maxPages;
    if ( (size % maxCount) == 0) {
      maxPages = (int) size / maxCount;
    }
    else {
      maxPages = ( (int) size / maxCount) + 1;
    }

    List pages = new ArrayList();
    for (int i = 1; i <= maxPages; i++) {
      PaginationForm.PageInfo pi = new PaginationForm.PageInfo();
      pi.setPageNo(Integer.toString(i));
      pi.setCurrPage(false);
      if ( (pageNo != null) &&
          (i == Integer.parseInt(pageNo))) {
        pi.setCurrPage(true);
      }
      pages.add(pi);
    }
    form.setPages(pages);
  }

  public int getOffset() {
    return offset;
  }

  public int getDirection() {
    return direction;
  }

  public int getCount() {
    return count;
  }

  public int getNavigation() {
    return Integer.parseInt(navigation);
  }

}
