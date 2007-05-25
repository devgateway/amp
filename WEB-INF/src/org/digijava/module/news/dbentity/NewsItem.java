/*
 *   NewsItem.java
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

package org.digijava.module.news.dbentity;

import java.util.Date;

import org.apache.log4j.Logger;

public class NewsItem {

  /**
   * logging tracer
   */
  private static Logger logger = Logger.getLogger(NewsItem.class);

  /**
   * news identity
   */
  private Long id;

  /**
   * language in which news item was created
   */
  private String language;

  /**
   * news item title
   */
  private String title;

  /**
   * description of news item
   */
  private String description;

  /**
   * client computer IP
   */
  private String creationIp;

  /**
   * user identity of news item's author
   */
  private Long userId;

  /**
   * news item identity
   */
  private News news;

  /**
   * creation date of news item
   */
  private Date creationDate;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public String getCreationIp() {
    return creationIp;
  }

  public void setCreationIp(String creationIp) {
    this.creationIp = creationIp;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public News getNews() {
    return news;
  }

  public void setNews(News news) {
    this.news = news;
  }

}