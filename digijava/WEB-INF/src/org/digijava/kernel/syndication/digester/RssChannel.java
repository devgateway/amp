/*
 *   RssChannel.java
 *   @Author Lasha Dolidze lasha@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id: RssChannel.java,v 1.1 2005-07-06 10:34:25 rahul Exp $
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
import java.util.Vector;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class RssChannel {

    private Vector items;
    private String title;
    private String link;
    private String description;
    private String language;
    private String pubDate;
    private Date date;
    private String category;
    private String generator;

    public RssChannel() {
        this.items = new Vector();
    }

    public void addItem(RssItem item) {
        this.items.addElement(item);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Vector getItems() {
        return items;
    }

    public void setItems(Vector items) {
        this.items = items;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getTitle() {
        return title;
    }

    public String getGenerator() {
        return generator;
    }

    public Date getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
