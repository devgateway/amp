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

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.digijava.module.common.dbentity.ItemStatus;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "DG_CALENDAR")
public class Calendar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SITE_ID")
    private String siteId;

    @Column(name = "MODULE_INSTANCE_ID")
    private String instanceId;

    @ManyToOne
    @JoinColumn(name = "STATUS")
    private ItemStatus status;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "MAIL_TO")
    private String mailTo;

    @Column(name = "COUNTRY_ISO")
    private String country;

    @Column(name = "SOURCE_NAME")
    private String sourceName;

    @Column(name = "SOURCE_URL")
    private String sourceUrl;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "ENABLE_HTML")
    private boolean enableHTML;

    @Column(name = "start_tbd")
    private TBD startTBD;

    @Column(name = "end_tbd")
    private TBD endTBD;

    @Column(name = "ENABLE_SMILES")
    private boolean enableSmiles;

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CalendarItem> calendarItem = new HashSet<>();

    @OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RecurrCalEvent> recurrCalEvent = new HashSet<>();
    @Transient
    public static final String noneCountryIso = "none";
    @Transient
    public static final String noneCountryName = "None";

    public static class TBD
    extends org.digijava.module.common.dbentity.Enum {
        public static final TBD MINUTE = new TBD(java.util.Calendar.MINUTE);
        public static final TBD HOUR = new TBD(java.util.Calendar.HOUR);
        public static final TBD DAY = new TBD(java.util.Calendar.DATE);
        public static final TBD MONTH = new TBD(java.util.Calendar.MONTH);
        public static final TBD YEAR = new TBD(java.util.Calendar.YEAR);

        private TBD(int value) {
            super(String.valueOf(value), value);
        }

        public static TBD getResult(String name){
            TBD result =(TBD) get(name);
            if(result == null ){
                throw new RuntimeException("Result for Name = " + name + " not defind" );
            }
            return result;
        }

        public static TBD getTBD(int value){
            TBD result =(TBD) get(value);
            if(result == null ){
                throw new RuntimeException("Result for Value = " + value + " not defind" );
            }
            return result;
        }
/*
        public TBD() {
            super();
        }

        private TBD(int code) {
            super(code);
        }

        public static TBD getTBD(int code) {
            return (TBD) getEnum(TBD.class, new Integer(code));
        }
*/
    }



    public Calendar() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set getCalendarItem() {
        return calendarItem;
    }

    /**
     * @return Returns the endTBD.
     */
    public TBD getEndTBD() {
        return endTBD;
    }

    /**
     * @param endTBD The endTBD to set.
     */
    public void setEndTBD(TBD endTBD) {
        this.endTBD = endTBD;
    }

    /**
     * @return Returns the startTBD.
     */
    public TBD getStartTBD() {
        return startTBD;
    }

    /**
     * @param startTBD The startTBD to set.
     */
    public void setStartTBD(TBD startTBD) {
        this.startTBD = startTBD;
    }

    public void setCalendarItem(Set calendarItem) {
        this.calendarItem = calendarItem;
    }

    public CalendarItem getFirstCalendarItem() {
        if (calendarItem != null) {
            Iterator iter = calendarItem.iterator();
            while (iter.hasNext()) {
                return (CalendarItem) iter.next();
            }
        }

        return null;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public void setEnableSmiles(boolean enableSmiles) {
        this.enableSmiles = enableSmiles;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }

    /**
     * siteName, e.g. 'amp'
     * @return
     */
    public String getSiteId() {
        return siteId;
    }

    /**
     * siteName, e.g. 'amp'
     * @param siteId
     */
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public Set getRecurrCalEvent() {
        return recurrCalEvent;
    }

    public void setRecurrCalEvent(Set recurrCalEvent) {
        this.recurrCalEvent = recurrCalEvent;
    }
}
