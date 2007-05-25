/*
 *   NewsSettings.java
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

public class NewsSettings {

    /**
     * instance name
     */
    private String instanceId;

    /**
     * site identity
     */
    private String siteId;

    /**
     * news identity
     */
    private Long id;

    /**
     * language in which news item was created
     */
    private String language;

    /**
     * <p>true when News is moderated, false when News is unmoderated</p>
     * <p> Description:</p><p>When moderated any registered user can post news items, they go live only after Admin approves them (user is informed about this). Anybody can view them. Admin get alerts about new items.</p>
     * When Unmoderated -  Any registered user can post news, they go live immediately. Anybody can view them.
     */
    private boolean moderated;

    private boolean syndication;

    /**
     * true when News is private, false when News is public
	   * <p> Description:</p>When private only members can post or view news items.
     */
    private boolean privateItem;

    /**
     * default approve message
     */
    private String approveMessage;

    /**
     * true if approve message should be sent, false otherwise
     */
    private boolean sendApproveMessage;

    /**
     * default reject message
     */
    private String rejectMessage;

    /**
     * true if reject message should be sent, false otherwise
     */
    private boolean sendRejectMessage;

    /**
     * default revoke message
     */
    private String revokeMessage;

    /**
     * true if revoke message should be sent, false otherwise
     */
    private boolean sendRevokeMessage;

    /**
     * default archive message
     */
    private String archiveMessage;

    /**
     * true if archive message should be sent, false otherwise
     */
    private boolean sendArchiveMessage;

    /**
     *  number of charachters in visible title text for news item
     */
    private Long numberOfCharsInTitle;

    /**
     *  number of visible news per page
     */
    private Long numberOfItemsPerPage;
    /**
     * short version delimiter charachter
     */
    private String shortVersionDelimiter;

    /**
     * Default constructor. Needed for Hibernate
     */
    private NewsSettings() {}

    /**
     * Construct settings and initialize fields with default values. It's
     * important to initialize ALL fields, because module instance may not have
     * configuration defined in DB. In this case it must use default settings.
     * @param siteId String Site identifier (a.k.a. Site Key)
     * @param instanceId String Instance Name (default, political, etc)
     */
    public NewsSettings(String siteId, String instanceId) {
	this.siteId = siteId;
	this.instanceId = instanceId;

	/** @todo What's about "language" field? Mikheil */
	moderated = true;
	syndication = true;
	privateItem = true;
	approveMessage = "default approve message";
	sendApproveMessage = true;
	archiveMessage = "default archive message";
	sendArchiveMessage = true;
	rejectMessage = "default reject message";
	sendRejectMessage = true;
	revokeMessage = "default revoke message";
	sendRevokeMessage = true;
	numberOfCharsInTitle = new Long(100);
	numberOfItemsPerPage = new Long(10);
	shortVersionDelimiter = "!#";
    }

    public String getApproveMessage() {
	return approveMessage;
    }

    public void setApproveMessage(String approveMessage) {
	this.approveMessage = approveMessage;
    }

    public String getArchiveMessage() {
	return archiveMessage;
    }

    public void setArchiveMessage(String archiveMessage) {
	this.archiveMessage = archiveMessage;
    }

    public boolean isModerated() {
	return moderated;
    }

    public void setModerated(boolean moderated) {
	this.moderated = moderated;
    }

    public String getInstanceId() {
	return instanceId;
    }

    public void setInstanceId(String instanceId) {
	this.instanceId = instanceId;
    }

    public boolean isPrivateItem() {
	return privateItem;
    }

    public void setPrivateItem(boolean privateItem) {
	this.privateItem = privateItem;
    }

    public String getRejectMessage() {
	return rejectMessage;
    }

    public void setRejectMessage(String rejectMessage) {
	this.rejectMessage = rejectMessage;
    }

    public String getRevokeMessage() {
	return revokeMessage;
    }

    public void setRevokeMessage(String revokeMessage) {
	this.revokeMessage = revokeMessage;
    }

    public boolean isSendApproveMessage() {
	return sendApproveMessage;
    }

    public void setSendApproveMessage(boolean sendApproveMessage) {
	this.sendApproveMessage = sendApproveMessage;
    }

    public boolean isSendArchiveMessage() {
	return sendArchiveMessage;
    }

    public void setSendArchiveMessage(boolean sendArchiveMessage) {
	this.sendArchiveMessage = sendArchiveMessage;
    }

    public boolean isSendRejectMessage() {
	return sendRejectMessage;
    }

    public void setSendRejectMessage(boolean sendRejectMessage) {
	this.sendRejectMessage = sendRejectMessage;
    }

    public boolean isSendRevokeMessage() {
	return sendRevokeMessage;
    }

    public void setSendRevokeMessage(boolean sendRevokeMessage) {
	this.sendRevokeMessage = sendRevokeMessage;
    }

    public String getSiteId() {
	return siteId;
    }

    public void setSiteId(String siteId) {
	this.siteId = siteId;
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

    public Long getNumberOfCharsInTitle() {
	return numberOfCharsInTitle;
    }

    public void setNumberOfCharsInTitle(Long numberOfCharsInTitle) {
	this.numberOfCharsInTitle = numberOfCharsInTitle;
    }

    public Long getNumberOfItemsPerPage() {
	return numberOfItemsPerPage;
    }

    public void setNumberOfItemsPerPage(Long numberOfItemsPerPage) {
	this.numberOfItemsPerPage = numberOfItemsPerPage;
    }

    public boolean isSyndication() {
	return syndication;
    }

    public void setSyndication(boolean syndication) {
	this.syndication = syndication;
    }

    public String getShortVersionDelimiter() {
	return shortVersionDelimiter;
    }

    public void setShortVersionDelimiter(String shortVersionDelimiter) {
	this.shortVersionDelimiter = shortVersionDelimiter;
    }

}