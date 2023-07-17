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

import java.util.Date;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;
import javax.persistence.*;

@Entity
@Table(name = "DG_CALENDAR_ITEM")
public class CalendarItem implements LoggerIdentifiable{

  /**
   * logging tracer
   */
  private static Logger logger = Logger.getLogger(CalendarItem.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dg_calendar_item_seq")
  @SequenceGenerator(name = "dg_calendar_item_seq", sequenceName = "dg_calendar_item_seq", allocationSize = 1)
  @Column(name = "ID")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "CALENDAR_ID")
  private Calendar calendar;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "LANGUAGE")
  private String language;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "CREATION_IP")
  private String creationIp;

  @Column(name = "CREATION_DATE")
  private Date creationDate;

  @Column(name = "approve")
  private Integer approve;


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
