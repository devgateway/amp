/*
 *   Highlight.java
 *   @Author Maka Kharalashvili maka@digijava.org
 *   Created: Oct 10, 2003
 *   CVS-ID: $Id: Highlight.java,v 1.1 2005-07-06 10:34:25 rahul Exp $
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

package org.digijava.module.highlights.dbentity;

import java.sql.SQLException;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;

public class Highlight {
  /**
   * logging tracer
   */
  private static Logger logger = Logger.getLogger(Highlight.class);

  /**
   * number of visible Highlights per page
   */
  public static final int ITEMS_PER_PAGE = 6;

  /**
   * number of charachters in visible topic's text for active Highlight
   */
  public static final int MAX_VISIBLE_TEXT_LENGTH = 260;

  /**
   * maximal size limit for highlight image 30K
   */
  public static final int MAX_IMG_SIZE = 30*1024;

  /**
   * Highlight identity
   */
  private Long id;

  /**
   * Highlight author identity
   */
  private Long authorUserId;

  /**
   * Highlight last updater identity
   */
  private Long updaterUserId;

  /**
   * site identity
   */
  private String siteId;

  /**
   * instance name
   */
  private String instanceId;

  /**
   * Highlight title
   */
  private String title;

  /**
   * Highlight description
   */
  private String description;

  /**
   * Highlight topic
   */
  private String topic;

  /**
   * Set of Highlight links
   */
  private Set links;

  /**
   * true if Highlight is active,
   * false if Highlight is archived
   */
  private boolean active;

  /**
   * Highlight creation date
   */
  private Date creationDate;

  /**
   * Highlight last updation date
   */
  private Date updationDate;

  /**
   * Highlight layout:
   * 1 - for Leyout_1,
   * 2 - for Leyout_2,
   * 3 - for Leyout_3
   */
  private int layout;

  /**
   * Shortened topic length
   */
  private int shortTopicLength;

  /**
   * Highlight image height
   */
  private int imageHeight;

  /**
   * Highlight image width
   */
  private int imageWidth;

  /**
   * Highlight image
   */
  private byte[] image;

  /**
   * type of image
   */
  private String contentType;

  /**
   * true if Highligh image should be visible,
   * false otherwise
   */
  private boolean showImage;
  /**
   * true if Highligh should be visible for users,(Highlight is Public)
   * false, if
   */
  private boolean isPublic;

  public Highlight() {

  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Set getLinks() {
    return links;
  }

  public void setLinks(Set links) {
    this.links = links;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Long getAuthorUserId() {
    return authorUserId;
  }

  public void setAuthorUserId(Long authorUserId) {
    this.authorUserId = authorUserId;
  }

  public int getLayout() {
    return layout;
  }

  public void setLayout(int layout) {
    this.layout = layout;
  }

  public int getShortTopicLength() {
    return shortTopicLength;
  }

  public void setShortTopicLength(int shortTopicLength) {
    this.shortTopicLength = shortTopicLength;
  }

  public Long getUpdaterUserId() {
    return updaterUserId;
  }

  public void setUpdaterUserId(Long updaterUserId) {
    this.updaterUserId = updaterUserId;
  }

  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }

  public String getSiteId() {
    return siteId;
  }

  public void setSiteId(String siteId) {
    this.siteId = siteId;
  }

  public byte[] getImage() {
    return image;
  }

  public void setImage(byte[] image) {
    this.image = image;
  }

  public int getImageHeight() {
    return imageHeight;
  }

  public void setImageHeight(int imageHeight) {
    this.imageHeight = imageHeight;
  }

  public int getImageWidth() {
    return imageWidth;
  }

  public void setImageWidth(int imageWidth) {
    this.imageWidth = imageWidth;
  }

  public Date getUpdationDate() {
    return updationDate;
  }

  public void setUpdationDate(Date updationDate) {
    this.updationDate = updationDate;
  }

  public String getTopicText() {
    if (getTopic() != null)
      return getTopic();

    return null;
  }

  public boolean isShowImage() {
    return showImage;
  }

  public void setShowImage(boolean showImage) {
    this.showImage = showImage;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public boolean isIsPublic() {
    return isPublic;
  }

  public void setIsPublic(boolean isPublic) {
    this.isPublic = isPublic;
  }

}