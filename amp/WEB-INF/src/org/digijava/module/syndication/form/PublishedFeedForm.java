/*
 *   PublishedFeedForm.java
 *   @Author Lasha Dolidze lasha@digijava.org
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


package org.digijava.module.syndication.form;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.translator.util.TrnCountry;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.module.common.action.PaginationForm;
import org.digijava.module.syndication.util.SynUtil;

public class PublishedFeedForm
    extends PaginationForm {

    private int itemsPerPage;
    private List publishedFeedList;
    private Date dateAdded;
    private String contentType;
    private Long processingMode;
    private String feedTitle;
    private String feedDescription;
    private String feedUrl;
    private int itemOperation;
    private Long itemId;
    private int action;
    private String[] selectedItems;
    private List country;
    private List language;
    private String selectedCountry;
    private String selectedLanguage;
    private String selectedLanguageName;
    private String selectedCountryName;
    private boolean activateAggregator;

    public Collection getPublishedFeedList() {
        return publishedFeedList;
    }

    public void setPublishedFeedList(List publishedFeedList) {
        this.publishedFeedList = publishedFeedList;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.publishedFeedList = null;
        feedTitle = null;
        feedDescription = null;
        itemOperation = 0;
        selectedItems = null;
        itemId = null;
        action = 0;
        country = null;
        language = null;
        selectedCountry = null;
        selectedLanguage = null;
        selectedLanguageName = null;
        selectedCountryName = null;
        this.activateAggregator = DigiConfigManager.getConfig().isAggregation();
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {

        // set country list
        List sortedCountries = null;
        try {
            sortedCountries = SynUtil.getCountries(httpServletRequest);
            TrnCountry none = new TrnCountry("none", "None");
            sortedCountries.add(0, none);
            setCountry(sortedCountries);

            // set languages
            List sortedLanguages = SynUtil.getLanguages(httpServletRequest);
            setLanguage(sortedLanguages);
        }
        catch (DgException ex) {
        }

        return super.validate(actionMapping, httpServletRequest);
    }

    public int getItemsPerPage() {
        if (itemsPerPage < 5) {
            //Default
            itemsPerPage = 5;
        }
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public String getFeedDescription() {
        return feedDescription;
    }

    public void setFeedDescription(String feedDescription) {
        this.feedDescription = feedDescription;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    public Long getProcessingMode() {
        return processingMode;
    }

    public void setProcessingMode(Long processingMode) {
        this.processingMode = processingMode;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public int getItemOperation() {
        return itemOperation;
    }

    public void setItemOperation(int itemOperation) {
        this.itemOperation = itemOperation;
    }

    public String[] getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(String[] selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public List getCountry() {
        return country;
    }

    public void setCountry(List country) {
        this.country = country;
    }

    public List getLanguage() {
        return language;
    }

    public void setLanguage(List language) {
        this.language = language;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public String getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(String selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public String getSelectedLanguageName() {
        return selectedLanguageName;
    }

    public void setSelectedLanguageName(String selectedLanguageName) {
        this.selectedLanguageName = selectedLanguageName;
    }

    public String getSelectedCountryName() {
        return selectedCountryName;
    }

    public boolean isActivateAggregator() {
        return activateAggregator;
    }

    public void setSelectedCountryName(String selectedCountryName) {
        this.selectedCountryName = selectedCountryName;
    }

    public void setActivateAggregator(boolean activateAggregator) {
        this.activateAggregator = activateAggregator;
    }
}
