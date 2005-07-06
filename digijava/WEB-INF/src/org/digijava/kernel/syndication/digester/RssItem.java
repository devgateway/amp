/*
 *   RssItem.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: RssItem.java,v 1.1 2005-07-06 10:34:25 rahul Exp $
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
package org.digijava.kernel.syndication.digester;

import java.util.Date;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class RssItem {

    private String title;
    private String link;
    private String description;
    private String pubDate;
    private Date date;
    private String archiveDate;
    private String country;


    public RssItem(String title, String link, String description, Date date) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.date = date;
    }

    public RssItem() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(String archiveDate) {
        this.archiveDate = archiveDate;
    }

    public String getCountry() {
        return country;
    }

    public Date getDate() {
        return date;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
