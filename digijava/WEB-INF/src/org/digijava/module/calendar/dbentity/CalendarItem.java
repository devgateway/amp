/*
 *   CalendarItem.java
 *   @Author Lasha Dolidze lasha@digijava.org
 *   Created:
 *   CVS-ID: $Id: CalendarItem.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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

package org.digijava.module.calendar.dbentity;

import java.util.Date;

import org.apache.log4j.Logger;

public class CalendarItem {

  /**
   * logging tracer
   */
  private static Logger logger = Logger.getLogger(CalendarItem.class);

  /**
   * event identity
   */
  private Long id;

  /**
   * event item identity
   */
  private Calendar calendar;

  /**
   * user identity of event item's author
   */
  private Long userId;

  /**
   * language in which event item was created
   */
  private String language;

  /**
   * title of event item
   */
  private String title;

  /**
   * description of event item
   */
  private String description;

  /**
   * client computer IP
   */
  private String creationIp;

  /**
   * creation date of event item
   */
  private Date creationDate;

  public CalendarItem() {
  }

  public String getCreationIp() {
    return creationIp;
  }

  public void setCreationIp(String creationIp) {
    this.creationIp = creationIp;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Calendar getCalendar() {
    return calendar;
  }

  public void setCalendar(Calendar calendar) {
    this.calendar = calendar;
  }
}