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

import org.digijava.kernel.util.DgUtil;

import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class CalendarTeaserItem {
    private Date releaseDate;
    private String sourceUrl;
    private String sourceName;
    private String title;
    private String description;
    private Date itemStartDate;
    private Date itemEndDate;
    private Long id;
    private int numOfCharsInTitle;

    private String startDate;
    private String endDate;

    public CalendarTeaserItem(Long id, String title, Date creationDate,
                  String sourceUrl, String sourceName,
                  Date itemStartDate, Date itemEndDate,
String description) {

    this.releaseDate = releaseDate;
    this.sourceUrl = sourceUrl;
    this.sourceName = sourceName;
    this.title = title;
    this.description = description;
    this.itemStartDate = itemStartDate;
    this.itemEndDate = itemEndDate;
    this.id = id;

    this.startDate = DgUtil.formatDateShortly(this.itemStartDate);
    this.endDate = DgUtil.formatDateShortly(this.itemEndDate);
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

    public String getSourceName() {
    return sourceName;
    }

    public void setSourceName(String sourceName) {
    this.sourceName = sourceName;
    }

    public String getSourceUrl() {
    return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
    this.sourceUrl = sourceUrl;
    }

    public String getTitle() {
    String retVal = null;
    if (title != null) {
        if (title.length() > numOfCharsInTitle) {
        retVal = title.substring(0, numOfCharsInTitle);
        }
        else {
        retVal = title;
        }
    }
    return retVal;
    }

    public void setTitle(String title) {
    this.title = title;
    }

    public int getNumOfCharsInTitle() {
    return numOfCharsInTitle;
    }

    public void setNumOfCharsInTitle(int numOfCharsInTitle) {
    this.numOfCharsInTitle = numOfCharsInTitle;
    }

    public String getEndDate() {
    return endDate;
    }

    public void setEndDate(String endDate) {
    this.endDate = endDate;
    }

    public Date getItemEndDate() {
    return itemEndDate;
    }

    public void setItemEndDate(Date itemEndDate) {
    this.itemEndDate = itemEndDate;
    }

    public Date getItemStartDate() {
    return itemStartDate;
    }

    public void setItemStartDate(Date itemStartDate) {
    this.itemStartDate = itemStartDate;
    }

    public Date getReleaseDate() {
    return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
    this.releaseDate = releaseDate;
    }

    public String getStartDate() {
    return startDate;
    }

    public void setStartDate(String startDate) {
    this.startDate = startDate;
    }
}
