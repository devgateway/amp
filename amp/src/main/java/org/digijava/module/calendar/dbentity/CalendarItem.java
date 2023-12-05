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

package org.digijava.module.calendar.dbentity;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;

import java.util.Date;

public class CalendarItem implements LoggerIdentifiable{

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

    /**
     * for Calendar events. 0 - Awaiting Approval 1 - Approved -1 - Not Approved
     */
    private Integer approve = null;

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

    public Integer getApprove() {
        return approve;
    }

    public void setApprove(Integer approve) {
        this.approve = approve;
    }


    @Override
    public String getObjectName() {
        return  getTitle() + " " + getId();
    }

    @Override
    public Object getObjectType() {
        return this.getClass().getName();
    }

    @Override
    public Object getIdentifier() {
        return  getId();
    }
         @Override
        public String getObjectFilteredName() {
        return DbUtil.filter(getObjectName());
    }

}
