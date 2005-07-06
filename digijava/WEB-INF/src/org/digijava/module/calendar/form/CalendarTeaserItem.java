/*
 *   CalendarTeaserItem.java
 *   @Author Lahs Dolidze lasha@digijava.org
 * 	 Created: Jun 18, 2004
 * 	 CVS-ID: $Id: CalendarTeaserItem.java,v 1.1 2005-07-06 10:34:24 rahul Exp $
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

import java.util.Date;
import org.digijava.kernel.util.DgUtil;

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