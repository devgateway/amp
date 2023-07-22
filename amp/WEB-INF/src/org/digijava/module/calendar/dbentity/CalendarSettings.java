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
import javax.persistence.*;

@Entity
@Table(name = "DG_CALENDAR_SETTINGS")
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
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dg_calendar_settings_seq")
    @SequenceGenerator(name = "dg_calendar_settings_seq", sequenceName = "dg_calendar_settings_seq", allocationSize = 1)
    private Long id;

    @Column(name = "MODULE_INSTANCE_ID")
    private String instanceId;

    @Column(name = "SITE_ID")
    private String siteId;

    @Column(name = "LANGUAGE")
    private String language;

    @Column(name = "MODERATED")
    private Boolean moderated;

    @Column(name = "PRIVATE")
    private Boolean privateItem;

    @Column(name = "APPROVE_MSG")
    private String approveMessage;

    @Column(name = "SEND_APPROVE_MSG")
    private Boolean sendApproveMessage;

    @Column(name = "REJECT_MSG")
    private String rejectMessage;

    @Column(name = "SEND_REJECT_MSG")
    private Boolean sendRejectMessage;

    @Column(name = "REVOKE_MSG")
    private String revokeMessage;

    @Column(name = "SEND_REVOKE_MSG")
    private Boolean sendRevokeMessage;

    @Column(name = "DEFAULT_VIEW")
    private String defaultView;

    @Column(name = "NUMBER_OF_CHARS_IN_TITLE")
    private Long numberOfCharsInTitle;

    @Column(name = "NUMBER_OF_ITEMS_PER_PAGE")
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
