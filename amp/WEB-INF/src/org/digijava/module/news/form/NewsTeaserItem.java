/*
 *   NewsTeaserItem.java
 *   @Author George Kvizhinadze gio@digijava.org
 *   Created: Jun 18, 2004
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

package org.digijava.module.news.form;

import java.util.Date;

public class NewsTeaserItem {
    private String title;
    private Date releaseDate;
    private String sourceUrl;
    private String description;
    private int numOfCharsInTitle;
    private Long id;
    private boolean enableSmiles;
    private boolean enableHTML;

    public NewsTeaserItem(Long id, String title, Date date, String sourceUrl,
                          String description, boolean enableHTML,
                          boolean enableSmiles) {
        this.id = id;
        this.title = title;
        this.releaseDate = date;
        this.sourceUrl = sourceUrl;
        this.description = description;
        this.enableHTML = enableHTML;
        this.enableSmiles = enableSmiles;
    }

    public NewsTeaserItem(Long id, String title, Date date, String sourceUrl,
                          String description) {
        this.id = id;
        this.title = title;
        this.releaseDate = date;
        this.sourceUrl = sourceUrl;
        this.description = description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
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

    public int getNumOfCharsInTitle() {
        return numOfCharsInTitle;
    }

    public boolean isEnableHTML() {
        return enableHTML;
    }

    public boolean isEnableSmiles() {
        return enableSmiles;
    }

    public void setNumOfCharsInTitle(int numOfCharsInTitle) {
        this.numOfCharsInTitle = numOfCharsInTitle;
    }

    public void setEnableHTML(boolean enableHTML) {
        this.enableHTML = enableHTML;
    }

    public void setEnableSmiles(boolean enableSmiles) {
        this.enableSmiles = enableSmiles;
    }
}
