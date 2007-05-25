/*
 *   NewsForm.java
 *   @Author Maka Kharalashvili maka@digijava.org
 * 	 Created: Nov 10, 2003
 * 	 CVS-ID: $Id$
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

import java.util.Collection;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class NewsForm
    extends ActionForm {

    public static class NewsInfo {

        /**
         * news item identity
         */
        private Long id;

        /**
         * title of news item
         */
        private String title;

        /**
         * description of news item
         */
        private String description;

        /**
         * publication date of news item
         */
        private String releaseDate;

        /**
         * source name of news item
         */
        private String sourceName;

        /**
         * source URL of event item
         */
        private String sourceUrl;

        /**
         * user identity of news item's author
         */
        private Long authorUserId;

        /**
         * First names of news item's author
         */
        private String authorFirstNames;

        /**
         * Last name of news item's author
         */
        private String authorLastName;

        public NewsInfo() {

        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        public String getSourceName() {
            return sourceName;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setAuthorFirstNames(String authorFirstNames) {
            this.authorFirstNames = authorFirstNames;
        }

        public String getAuthorFirstNames() {
            return authorFirstNames;
        }

        public String getAuthorLastName() {
            return authorLastName;
        }

        public void setAuthorLastName(String authorLastName) {
            this.authorLastName = authorLastName;
        }

        public void setAuthorUserId(Long authorUserId) {
            this.authorUserId = authorUserId;
        }

        public Long getAuthorUserId() {
            return authorUserId;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    /**
     * identity of currently active news item
     */
    private long activeNewsId;

    /**
     * collection of news items
     */
    private Collection newsList;

    /**
     *  number of news items visible in teaser
     */
    private int numOfItemsInTeaser;

    public Collection getNewsList() {
        return newsList;
    }

    public void setNewsList(Collection newsList) {
        this.newsList = newsList;
    }

    public long getActiveNewsId() {
        return activeNewsId;
    }

    public void setActiveNewsId(long activeNewsId) {
        this.activeNewsId = activeNewsId;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.newsList = null;
        this.activeNewsId = 0;
        this.numOfItemsInTeaser = 0;
    }

    public int getNumOfItemsInTeaser() {
        return numOfItemsInTeaser;
    }

    public void setNumOfItemsInTeaser(int numOfItemsInTeaser) {
        this.numOfItemsInTeaser = numOfItemsInTeaser;
    }

}