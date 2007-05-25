/*
 *   News.java
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
import java.util.Iterator;
import java.util.Set;

import org.digijava.module.common.dbentity.ItemStatus;

public class News {

    public static final String noneCountryIso = "none";
    public static final String noneCountryName = "None";
    /**
     * site identity
     */
    private String siteId;

    /**
     * instance name
     */
    private String instanceId;

    /**
     * news identity
     */
    private Long id;

    /**
     * country or residence key of news item's author
     */
    private String country;

    /**
     * source name of news item
     */
    private String sourceName;

    /**
     * source URL of news item
     */
    private String sourceUrl;

    /**
     * publication date of news item
     */
    private Date releaseDate;

    /**
     * archive date of news item
     */
    private Date archiveDate;

    /**
     * status of news item
     */
    private ItemStatus status;

    /**
     * Set of news items from current event
     */
    private Set newsItem;

    private boolean syndication;

    /**
     * true if html should be enabled when parsimg BBCode, false otherwise
     * when enabled only safe html tags - b,u,i,a,pre are parsed
     */
    private boolean enableHTML;

    /**
     * true if smiles should be parsed by BBCodeParser,false otherwise
     */
    private boolean enableSmiles;

    public Date getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
        this.archiveDate = archiveDate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
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

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set getNewsItem() {
        return newsItem;
    }

    public void setNewsItem(Set newsItem) {
        this.newsItem = newsItem;
    }

    //
    public NewsItem getFirstNewsItem() {
        if (newsItem != null) {
            Iterator iter = newsItem.iterator();
            while (iter.hasNext()) {
                return (NewsItem) iter.next();
            }
        }

        return null;
    }

    //
    public boolean isEnableHTML() {
        return enableHTML;
    }

    public void setEnableHTML(boolean enableHTML) {
        this.enableHTML = enableHTML;
    }

    public boolean isEnableSmiles() {
        return enableSmiles;
    }

    public void setEnableSmiles(boolean enableSmiles) {
        this.enableSmiles = enableSmiles;
    }

    public boolean isSyndication() {
        return syndication;
    }

    public void setSyndication(boolean syndication) {
        this.syndication = syndication;
    }

}