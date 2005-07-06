/*
*   Calendar.java
*   @Author Lasha Dolidze lasha@digijava.org
*   Created:
*   CVS-ID: $Id: Calendar.java,v 1.1 2005-07-06 10:34:14 rahul Exp $
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

package org.digijava.module.calendar.dbentity;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.digijava.module.common.dbentity.IntegerPersistentEnum;
import org.digijava.module.common.dbentity.ItemStatus;

public class Calendar {
	
	
	public static class TBD extends IntegerPersistentEnum {
		public static final TBD MINUTE = new TBD(java.util.Calendar.MINUTE);
		public static final TBD HOUR = new TBD(java.util.Calendar.HOUR);
		public static final TBD DAY = new TBD(java.util.Calendar.DATE);
		public static final TBD MONTH = new TBD(java.util.Calendar.MONTH);
		public static final TBD YEAR = new TBD(java.util.Calendar.YEAR);
		
		public TBD() {
			super();
		}
		
		private TBD(int code) {
			super(code);
		}
		
		public static TBD getTBD(int code) {
			return (TBD) getEnum(TBD.class,new Integer(code));
		}

	}	
	
    /**
     * event identity
     */
    private Long id;

    /**
     * site identity
     */
    private String siteId;

    /**
     * instance name
     */
    private String instanceId;

    /**
     * status of event item
     */
    private ItemStatus status;

    /**
     * location of event item's author
     */
    private String location;

    /**
     * mailing address, where remind message about an event item is sent
     */
    private String mailTo;

    /**
     * country or residence key of event item's author
     */
    private String country;

    /**
     * source name of event item
     */
    private String sourceName;

    /**
     * source URL of event item
     */
    private String sourceUrl;

    /**
     * publication date of event item
     */
    private Date startDate;

    /**
     * archive date of event item
     */
    private Date endDate;

    /**
     * true if html should be enabled when parsimg BBCode, false otherwise
     * when enabled only safe html tags - b,u,i,a,pre are parsed
     */
    private boolean enableHTML;
    
    
    /**
     * determines if start date is TBD
     */
    private Calendar.TBD startTBD = null;
    
    
    
    /**
     * determines if end date is TBD
     */
    private Calendar.TBD endTBD = null;

    /**
     * true if smiles should be parsed by BBCodeParser,false otherwise
     */
    private boolean enableSmiles;

    /**
     * Set of event items from current event
     */
    private Set calendarItem;

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

}