/*
 *   NewsItemForm.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.common.action.PaginationForm;
import org.digijava.module.um.util.Calendar;

public class NewsItemForm
      extends PaginationForm {

    public static class NewsStatusInfo {

	/**
	 * status code of news item
	 */
	private String code;

	/**
	 * status name of news item
	 */
	private String statusName;

	public NewsStatusInfo(String code, String statusName) {
	    this.code = code;
	    this.statusName = statusName;
	}

	public void setCode(String code) {
	    this.code = code;
	}

	public String getCode() {
	    return code;
	}

	public void setStatusName(String statusName) {
	    this.statusName = statusName;
	}

	public String getStatusName() {
	    return statusName;
	}
    }

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
	 * archive date of news item
	 */
	private String archiveDate;

	/**
	 * source name of news item
	 */
	private String sourceName;

	/**
	 * source URL of news item
	 */
	private String sourceUrl;

	/**
	 * iso code of news item author's country or residence
	 */
	private String country;

	/**
	 * name of news item author's country or residence
	 */
	private String countryName;

	/**
	 * key of news item author's country
	 */
	private String countryKey;

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

	/**
	       * true if news item is selected in order to change its status, false otherwise
	 */
	private boolean selected;

	/**
	 * status of news item
	 */
	private String status;

	/**
	 * name of language in which news item was created
	 */
	private String language;

	/**
	 * key of language in which news item was created
	 */
	private String languageKey;

//	private boolean autoArchived;

	public NewsInfo() {
	    this.selected = false;
//	    this.autoArchived = false;
	}

	public NewsInfo(String title, String status, boolean selected) {
	    this.title = title;
	    this.status = status;
	    this.selected = selected;
	}

	public String getAuthorFirstNames() {
	    return authorFirstNames;
	}

	public void setAuthorFirstNames(String authorFirstNames) {
	    this.authorFirstNames = authorFirstNames;
	}

	public String getAuthorLastName() {
	    return authorLastName;
	}

	public void setAuthorLastName(String authorLastName) {
	    this.authorLastName = authorLastName;
	}

	public Long getAuthorUserId() {
	    return authorUserId;
	}

	public void setAuthorUserId(Long authorUserId) {
	    this.authorUserId = authorUserId;
	}

	public String getReleaseDate() {
	    return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
	    this.releaseDate = releaseDate;
	}

	public String getTitle() {
	    return title;
	}

	public void setTitle(String title) {
	    this.title = title;
	}

	public boolean isSelected() {
	    return selected;
	}

	public void setSelected(boolean selected) {
	    this.selected = selected;
	}

	public void setStatus(String status) {
	    this.status = status;
	}

	public String getStatus() {
	    return status;
	}

	public void setId(Long id) {
	    this.id = id;
	}

	public Long getId() {
	    return id;
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

	public void setCountry(String country) {
	    this.country = country;
	}

	public String getCountry() {
	    return country;
	}

	public void setCountryName(String countryName) {
	    this.countryName = countryName;
	}

	public String getCountryName() {
	    return countryName;
	}

	public void setCountryKey(String countryKey) {
	    this.countryKey = countryKey;
	}

	public String getCountryKey() {
	    return countryKey;
	}

	public String getDescription() {
	    return description;
	}

	public void setDescription(String description) {
	    this.description = description;
	}

	public String getLanguage() {
	    return language;
	}

	public void setLanguage(String language) {
	    this.language = language;
	}

	public String getLanguageKey() {
	    return languageKey;
	}

	public void setLanguageKey(String languageKey) {
	    this.languageKey = languageKey;
	}

	public String getArchiveDate() {
	    return archiveDate;
	}

	public void setArchiveDate(String archiveDate) {
	    this.archiveDate = archiveDate;
	}

	/*	public void setAutoArchived(boolean autoArchived) {
	     this.autoArchived = autoArchived;
	 }
	 public boolean isAutoArchived() {
	     return autoArchived;
	 }*/
    }

    /**
     * identity of currently active news item
     */
    private Long activeNewsItem;

    /**
     * title of news item
     */
    private String title;

    /**
     * description of news item
     */
    private String description;

    /**
     * true if html should be enabled when parsimg BBCode, false otherwise
     * when enabled only safe html tags - b,u,i,a,pre are parsed
     */
    private boolean enableHTML;

    /**
     * true if smiles should be parsed by BBCodeParser,false otherwise
     */
    private boolean enableSmiles;

    /**
     * list of countries
     */
    private List countryResidence;

    /**
     * iso code of news item author's country or residence
     */
    private String country;

    /**
     * name of news item author's country or residence
     */
    private String countryName;

    /**
     * key of news item author's country or residence
     */
    private String countryKey;

    /**
     * source name of news item
     */
    private String sourceName;

    /**
     * source URL of news item
     */
    private String sourceUrl;

    /**
     * collection of user languages
     */
    private Collection languages;

    /**
     * language selected by user
     */
    private String selectedLanguage;

    /**
     * key of language selected by user
     */
    private String languageKey;

    /**
     * collection of months
     */
    private Collection months;

    /**
     * collection of days
     */
    private Collection days;

    /**
     * publication month of news item
     */
    private String releaseMonth;

    /**
     * publication day of news item
     */
    private String releaseDay;

    /**
     * publication year of news item
     */
    private String releaseYear;

    /**
     * archive month of news item
     */
    private String archiveMonth;

    /**
     * archive day of news item
     */
    private String archiveDay;

    /**
     * archive year of news ite
     */
    private String archiveYear;

    /**
     * true if news item is never archived, false otherwise
     */
    private boolean neverArchive;

    /**
     * true if news item is being previewed,
     * false otherwise
     */
    private boolean preview;

    /**
     * NewsInfo instance for news item preveiw
     */
    private NewsInfo previewItem;

    /**
     * publication date of news item
     */
    private String releaseDate;

    /**
     * archive date of news item
     */
    private String archiveDate;

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

    /**
     * true if news item is editable for current user - either if user is module Admin or author of the news item,
     * false otherwise
     */
    private boolean editable;

    /**
     * true if either of approve,archive,revoke, publish messages should be sent to the author of the news item,
     * false otherwise
     */
    private boolean sendMessage;

    /**
     * list of news
     */
    private List newsList;

    /**
     * list of available statuses for news items
     */
    private List statusList;

    /**
	   * user selected status with which should be updated selected news items statuses
     */
    private String selectedStatus;

    /**
     * collection of news items statuses used when changing status of selected news items (in admin view)
     */
    private Collection itemStatus;

    /**
     * default message sent to news item's author, when status of the news is changed by Admin
     */
    private String defaultMessage;

    /**
     * text showing status of currently selected news items,  status of which is being changed by Admin
     */
    private String statusTitle;

    /**
     * status of the news item
     */
    private String status;

    /**
     * URL where concrete action should redirect to
     */
    private String returnUrl;
    //
    //
    private String moduleName;
    private String siteName;
    private String instanceName;
    //
    private boolean selected;
    private boolean submitted;
    //
//    private int countAutoArchived;

    private String shortVersionDelimiter;

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public boolean isEnableHTML() {
	return enableHTML;
    }

    public void setEnableHTML(boolean enableHTML) {
	this.enableHTML = enableHTML;
    }

    public boolean isEnableSmiles() {
	return enableSmiles;
    }

    public void setEnableSmiles(boolean enambleSiles) {
	this.enableSmiles = enambleSiles;
    }

    public boolean isNeverArchive() {
	return neverArchive;
    }

    public void setNeverArchive(boolean neverArchive) {
	this.neverArchive = neverArchive;
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
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Collection getDays() {
	return days;
    }

    public void setDays(Collection days) {
	this.days = days;
    }

    public Collection getLanguages() {
	return languages;
    }

    public void setLanguages(Collection languages) {
	this.languages = languages;
    }

    public Collection getMonths() {
	return months;
    }

    public void setMonths(Collection months) {
	this.months = months;
    }

    public String getSelectedLanguage() {
	return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
	this.selectedLanguage = selectedLanguage;
    }

    public boolean isPreview() {
	return preview;
    }

    public void setPreview(boolean preview) {
	this.preview = preview;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

	super.reset(mapping, request);

//        preview = false;
	activeNewsItem = null;
	sendMessage = false;
	defaultMessage = null;

	preview = false;
	previewItem = null;

	title = null;
	description = null;
	enableHTML = false;
	enableSmiles = true;
	sourceName = null;
	sourceUrl = null;
	languages = null;
	countryResidence = null;
	neverArchive = false;
	selectedLanguage = null;
	country = null;
	countryName = null;
	countryKey = null;

	months = null;
	days = null;

	editable = false;

//	    itemStatus = null;

	if (newsList != null) {
	    Iterator iter = newsList.iterator();
	    while (iter.hasNext()) {
		NewsInfo item = (NewsInfo) iter.next();
		/*		if (!(countAutoArchived > 0 &&
		      item.isSelected() && item.isAutoArchived())) {*/
		item.setSelected(false);
//		}
	    }
	}
	activeNewsItem = null;

	shortVersionDelimiter = null;
    }

    public String getArchiveDay() {
	return archiveDay;
    }

    public void setArchiveDay(String archiveDay) {
	this.archiveDay = archiveDay;
    }

    public String getArchiveMonth() {
	return archiveMonth;
    }

    public void setArchiveMonth(String archiveMonth) {
	this.archiveMonth = archiveMonth;
    }

    public String getArchiveYear() {
	return archiveYear;
    }

    public void setArchiveYear(String archiveYear) {
	this.archiveYear = archiveYear;
    }

    public String getReleaseDay() {
	return releaseDay;
    }

    public void setReleaseDay(String releaseDay) {
	this.releaseDay = releaseDay;
    }

    public String getReleaseMonth() {
	return releaseMonth;
    }

    public void setReleaseMonth(String releaseMonth) {
	this.releaseMonth = releaseMonth;
    }

    public String getReleaseYear() {
	return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
	this.releaseYear = releaseYear;
    }

    public String getReleaseDate() {
	return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
	this.releaseDate = releaseDate;
    }

    public String getArchiveDate() {
	return archiveDate;
    }

    public void setArchiveDate(String archiveDate) {
	this.archiveDate = archiveDate;
    }

    public Long getAuthorUserId() {
	return authorUserId;
    }

    public void setAuthorUserId(Long authorUserId) {
	this.authorUserId = authorUserId;
    }

    public Long getActiveNewsItem() {
	return activeNewsItem;
    }

    public void setActiveNewsItem(Long activeNewsItem) {
	this.activeNewsItem = activeNewsItem;
    }

    public String getAuthorFirstNames() {
	return authorFirstNames;
    }

    public void setAuthorFirstNames(String authorFirstNames) {
	this.authorFirstNames = authorFirstNames;
    }

    public String getAuthorLastName() {
	return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
	this.authorLastName = authorLastName;
    }

    public boolean isEditable() {
	return editable;
    }

    public void setEditable(boolean editable) {
	this.editable = editable;
    }

    public List getNewsList() {
	return newsList;
    }

    public void setNewsList(List newsList) {
	this.newsList = newsList;
    }

    public NewsInfo getNewsItem(int index) {
	NewsInfo newsItem = null;
	int currentSize = newsList.size();
	if (index >= currentSize) {
	    for (int i = 0; i <= index - currentSize; i++) {
		newsList.add(new NewsInfo());
	    }
	}

	return (NewsInfo) newsList.get(index);
    }

    public List getCountryResidence() {
	return countryResidence;
    }

    public void setCountryResidence(List countryResidence) {
	this.countryResidence = countryResidence;
    }

    public String getCountry() {
	return country;
    }

    public void setCountry(String country) {
	this.country = country;
    }

    public String[] getSelectedNewsItems() {
	return null;
    }

    public Collection getItemStatus() {
	return itemStatus;
    }

    public void setItemStatus(Collection itemStatus) {
	this.itemStatus = itemStatus;
    }

    public String getSelectedStatus() {
	return selectedStatus;
    }

    public void setSelectedStatus(String selectedStatus) {
	this.selectedStatus = selectedStatus;
    }

    public List getStatusList() {
	return statusList;
    }

    public void setStatusList(List statusList) {
	this.statusList = statusList;
    }

    public boolean isSendMessage() {
	return sendMessage;
    }

    public void setSendMessage(boolean sendMessage) {
	this.sendMessage = sendMessage;
    }

    public String getDefaultMessage() {
	return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
	this.defaultMessage = defaultMessage;
    }

    public String getStatusTitle() {
	return statusTitle;
    }

    public void setStatusTitle(String statusTitle) {
	this.statusTitle = statusTitle;
    }

    public NewsInfo getPreviewItem() {
	return previewItem;
    }

    public void setPreviewItem(NewsInfo previewItem) {
	this.previewItem = previewItem;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getCountryKey() {
	return countryKey;
    }

    public void setCountryKey(String countryKey) {
	this.countryKey = countryKey;
    }

    public String getReturnUrl() {
	return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
	this.returnUrl = returnUrl;
    }

    public String getLanguageKey() {
	return languageKey;
    }

    public void setLanguageKey(String languageKey) {
	this.languageKey = languageKey;
    }

    public ActionErrors validate(ActionMapping actionMapping,
				 HttpServletRequest httpServletRequest) {

	ActionErrors errors = new ActionErrors();

	if ( (title == null) || (title.trim().length() == 0)) {
	    errors.add(null,
		       new ActionError("error.news.itemTitleEmpty"));
	}

	if ( (description == null) || (description.trim().length() == 0)) {
	    if ( (sourceName == null) || (sourceName.trim().length() == 0)) {
		if ( (sourceUrl == null) || (sourceUrl.trim().length() == 0) ||
		    (sourceUrl.equals( (String) (httpServletRequest.getScheme() +
						 "://")))) {
		    errors.add(null,
			       new ActionError(
			  "error:news:itemDescrAndSourceEmpty"));

		}
		else {
		    errors.add(null,
			       new ActionError("error:news:itemSourceNameEmpty"));
		}
	    }
	    else {
		if ( (sourceUrl == null) || (sourceUrl.trim().length() == 0) ||
		    (sourceUrl.equals( (String) (httpServletRequest.getScheme() +
						 "://")))) {
		    errors.add(null,
			       new ActionError("error:news:itemSourceUrlEmpty"));
		}
	    }

	}

	GregorianCalendar releaseDate = new GregorianCalendar();
	releaseDate.set(java.util.Calendar.MINUTE, 0);
	releaseDate.set(java.util.Calendar.HOUR_OF_DAY, 0);
	releaseDate.set(java.util.Calendar.SECOND, 0);
	releaseDate.set(java.util.Calendar.MONTH,
			Integer.parseInt(getReleaseMonth()) - 1);
	releaseDate.set(java.util.Calendar.DAY_OF_MONTH,
			Integer.parseInt(getReleaseDay()));
	releaseDate.set(java.util.Calendar.YEAR,
			Integer.parseInt(getReleaseYear()));

	GregorianCalendar archiveDate = new GregorianCalendar();
	archiveDate.set(java.util.Calendar.MINUTE, 0);
	archiveDate.set(java.util.Calendar.HOUR_OF_DAY, 0);
	archiveDate.set(java.util.Calendar.SECOND, 0);
	archiveDate.set(java.util.Calendar.MONTH,
			Integer.parseInt(getArchiveMonth()) - 1);
	archiveDate.set(java.util.Calendar.DAY_OF_MONTH,
			Integer.parseInt(getArchiveDay()));
	archiveDate.set(java.util.Calendar.YEAR,
			Integer.parseInt(getArchiveYear()));

	if (releaseDate.getTime().compareTo(archiveDate.getTime()) > 0 &&
	    !isNeverArchive()) {
	    errors.add(null,
		       new ActionError("error.news.archiveDateMustGreater"));
	}

	return errors.isEmpty() ? null : errors;
    }

    public void loadCalendar() {

	months = new ArrayList();
	days = new ArrayList();

	months.add(new Calendar(new Long(1), "January"));
	months.add(new Calendar(new Long(2), "February"));
	months.add(new Calendar(new Long(3), "March"));
	months.add(new Calendar(new Long(4), "April"));
	months.add(new Calendar(new Long(5), "May"));
	months.add(new Calendar(new Long(6), "June"));
	months.add(new Calendar(new Long(7), "July"));
	months.add(new Calendar(new Long(8), "August"));
	months.add(new Calendar(new Long(9), "September"));
	months.add(new Calendar(new Long(10), "October"));
	months.add(new Calendar(new Long(11), "November"));
	months.add(new Calendar(new Long(12), "December"));

	for (int i = 1; i <= 31; i++) {
	    days.add(new Calendar(new Long(i), Integer.toString(i)));
	}
    }

    public String getInstanceName() {
	return instanceName;
    }

    public void setInstanceName(String instanceName) {
	this.instanceName = instanceName;
    }

    public String getModuleName() {
	return moduleName;
    }

    public void setModuleName(String moduleName) {
	this.moduleName = moduleName;
    }

    public String getSiteName() {
	return siteName;
    }

    public void setSiteName(String siteName) {
	this.siteName = siteName;
    }

    public String getCountryName() {
	return countryName;
    }

    public void setCountryName(String countryName) {
	this.countryName = countryName;
    }

    public boolean isSelected() {
	return selected;
    }

    public void setSelected(boolean selected) {
	this.selected = selected;
    }

    public boolean isSubmitted() {
	return submitted;
    }

    public void setSubmitted(boolean submitted) {
	this.submitted = submitted;
    }

    public String getShortVersionDelimiter() {
	return shortVersionDelimiter;
    }

    public void setShortVersionDelimiter(String shortVersionDelimiter) {
	this.shortVersionDelimiter = shortVersionDelimiter;
    }

    /*    public int getCountAutoArchived() {
     return countAutoArchived;
      }
      public void setCountAutoArchived(int countAutoArchived) {
     this.countAutoArchived = countAutoArchived;
      }*/

}