/*
 *   CollectorFeedForm.java
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.module.common.action.PaginationForm;
import org.digijava.module.syndication.util.Schedule;

public class CollectorFeedForm
    extends PaginationForm {

    private int itemsPerPage;
    private List collectorFeedList;
    private Date dateAdded;
    private String contentType;
    private Long processingMode;
    private String feedTitle;
    private String feedDescription;
    private String feedUrl;
    private String sourceName;
    private String sourceUrl;
    private ArrayList schedules;
    private ArrayList scheduleTimes;
    private Integer selectedSchedule;
    private Long selectedTime;
    private boolean status;
    private Long itemId;
    private int action;
    private String[] selectedItems;
    private int itemOperation;
    private boolean activateAggregator;

    public Collection getCollectorFeedList() {
        return collectorFeedList;
    }

    public void setCollectorFeedList(List collectorFeedList) {
        this.collectorFeedList = collectorFeedList;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.collectorFeedList = null;
        feedTitle = null;
        feedDescription = null;
        feedUrl = null;
        sourceName = null;
        sourceUrl = null;
        schedules = null;
        scheduleTimes = null;
        selectedSchedule = null;
        selectedTime = null;
        status = false;
        selectedItems = null;
        itemOperation = 0;
        this.activateAggregator = DigiConfigManager.getConfig().isAggregation();
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {
        loadSchedule();
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

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public Integer getSelectedSchedule() {
        return selectedSchedule;
    }

    public void setSelectedSchedule(Integer selectedSchedule) {
        this.selectedSchedule = selectedSchedule;
    }

    public Long getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(Long selectedTime) {
        this.selectedTime = selectedTime;
    }

    public ArrayList getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList schedules) {
        this.schedules = schedules;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public ArrayList getScheduleTimes() {
        return scheduleTimes;
    }

    public void setScheduleTimes(ArrayList scheduleTimes) {
        this.scheduleTimes = scheduleTimes;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void loadSchedule() {

        schedules = new ArrayList();
        scheduleTimes = new ArrayList();

        // schedule
        schedules.add(new Schedule(new Long(0), "Daily"));
        //schedules.add(new Schedule(new Long(1), "Weekly"));

        // schedule time

        scheduleTimes.add(new Schedule(new Long(0), "12 AM"));
        scheduleTimes.add(new Schedule(new Long(1), "1 AM"));
        scheduleTimes.add(new Schedule(new Long(2), "2 AM"));
        scheduleTimes.add(new Schedule(new Long(3), "3 AM"));
        scheduleTimes.add(new Schedule(new Long(4), "4 AM"));
        scheduleTimes.add(new Schedule(new Long(5), "5 AM"));
        scheduleTimes.add(new Schedule(new Long(6), "6 AM"));
        scheduleTimes.add(new Schedule(new Long(7), "7 AM"));
        scheduleTimes.add(new Schedule(new Long(8), "8 AM"));
        scheduleTimes.add(new Schedule(new Long(9), "9 AM"));
        scheduleTimes.add(new Schedule(new Long(10), "10 AM"));
        scheduleTimes.add(new Schedule(new Long(11), "11 AM"));
        scheduleTimes.add(new Schedule(new Long(12), "12 PM"));
        scheduleTimes.add(new Schedule(new Long(13), "1 PM"));
        scheduleTimes.add(new Schedule(new Long(14), "2 PM"));
        scheduleTimes.add(new Schedule(new Long(15), "3 PM"));
        scheduleTimes.add(new Schedule(new Long(16), "4 PM"));
        scheduleTimes.add(new Schedule(new Long(17), "5 PM"));
        scheduleTimes.add(new Schedule(new Long(18), "6 PM"));
        scheduleTimes.add(new Schedule(new Long(19), "7 PM"));
        scheduleTimes.add(new Schedule(new Long(20), "8 PM"));
        scheduleTimes.add(new Schedule(new Long(21), "9 PM"));
        scheduleTimes.add(new Schedule(new Long(22), "10 PM"));
        scheduleTimes.add(new Schedule(new Long(23), "11 PM"));

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

    public String[] getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(String[] selectedItems) {
        this.selectedItems = selectedItems;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
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

    public int getItemOperation() {
        return itemOperation;
    }

    public boolean isActivateAggregator() {
        return activateAggregator;
    }

    public void setItemOperation(int itemOperation) {
        this.itemOperation = itemOperation;
    }

    public void setActivateAggregator(boolean activateAggregator) {
        this.activateAggregator = activateAggregator;
    }

}
