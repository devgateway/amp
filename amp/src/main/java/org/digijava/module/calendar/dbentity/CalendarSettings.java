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

package org.digijava.module.calendar.dbentity;

public class CalendarSettings {

    /**
     * <p>constant indocator for <b>List View</b></p>
     * <p>Description: </p>All the events in the calendar are displayed chronologically.
     */
    public final static String LIST_VIEW = "lv";

    /**
     * <p>constant indicator for <b>Month View</b></p>
     * <p>Description: </p>The current month is displayed with all the events listed under that month.
     */
    public final static String MONTH_VIEW = "mv";

    /**
     * <p>constant indicator for <b>Year View</b></p>
     * <p>Description: </p>Events are displayed as a small table(year) view with events listing for the selected month.
     */
    public final static String YEAR_VIEW = "yv";

    /**
     * event identity
     */
    private Long id;

    /**
     * instance name
     */
    private String instanceId;

    /**
     * site name
     */
    private String siteId;

    /**
     * language in which event item was created
     */
    private String language;

    /**
     * <p>true when Calendar is moderated, false when Calendar is unmoderated</p>
     * <p> Description:</p><p>When moderated any registered user can post events, they go live only after Admin approves them (user is informed about this). Anybody can view them. Admin get alerts about new items.</p>
     * When Unmoderated -  Any registered user can post events, they go live immediately. Anybody can view them.
     */
    private boolean moderated;

    /**
     * true when Calendar is private, false when Calendar is public
     * <p> Description:</p>When private only members can post or view events.
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
     * true if reject message should be sent,false otherwise
     */
    private boolean sendRejectMessage;

    /**
     * default revoke message
     */
    private String revokeMessage;

    /**
     * true if revoke message should be sent,false otherwise
     */
    private boolean sendRevokeMessage;

    /**
     * Calendar default view
     */
    private String defaultView;

    /**
     * number of charachters in visible title text for event item
     */
    private Long numberOfCharsInTitle;

    /**
     * number of visible events per page
     */
    private Long numberOfItemsPerPage;

    /**
     * Default constructor. Needed for Hibernate
     */
    private CalendarSettings() {}

    /**
     * Construct settings and initialize fields with default values. It's
     * important to initialize ALL fields, because module instance may not have
     * configuration defined in DB. In this case it must use default settings.
     * @param siteId String Site identifier (a.k.a. Site Key)
     * @param instanceId String Instance Name (default, political, etc)
     */
    public CalendarSettings(String siteId, String instanceId) {
        this.siteId = siteId;
        this.instanceId = instanceId;

        /** @todo What's about "language" field? Mikheil */
        moderated = true;
        privateItem = true;
        approveMessage = "default approve message";
        sendApproveMessage = true;
        rejectMessage = "default reject message";
        sendRejectMessage = true;
        revokeMessage = "default revoke message";
        sendRevokeMessage = true;
        defaultView = CalendarSettings.LIST_VIEW;
        numberOfCharsInTitle = new Long(100);
        numberOfItemsPerPage = new Long(10);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isModerated() {
        return moderated;
    }

    public void setModerated(boolean moderated) {
        this.moderated = moderated;
    }

    public boolean isPrivateItem() {
        return privateItem;
    }

    public void setPrivateItem(boolean privateItem) {
        this.privateItem = privateItem;
    }

    public String getApproveMessage() {
        return approveMessage;
    }

    public void setApproveMessage(String approveMessage) {
        this.approveMessage = approveMessage;
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

    public String getDefaultView() {
        return defaultView;
    }

    public void setDefaultView(String defaultView) {
        this.defaultView = defaultView;
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

}
