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

package org.digijava.module.calendar.form;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.digijava.module.calendar.util.CalendarPopulator;
import org.digijava.module.um.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class CalendarItemForm
    extends CalendarPaginationForm {

    private static final String REQUEST_SCHEME_DELIMITER = "://";


    public class DateForm {
        private String min;
        private String hour;
        private String date;
        private String month;
        private String year;
        private boolean allDayEvent;

        public DateForm() {}

        public void reset(ActionMapping mapping, HttpServletRequest request) {
            allDayEvent = false;
            min = "";
            hour = "";
            date = "";
            month = "";
            year = "";
        }

        public boolean isTBD() {
            return StringUtils.isBlank(getMin()) ||
                StringUtils.isBlank(getHour()) ||
                StringUtils.isBlank(getDate()) ||
                StringUtils.isBlank(getMonth()) ||
                StringUtils.isBlank(getYear());
        }

        /**
         * @return Returns the allDayEvent.
         */
        public boolean isAllDayEvent() {
            return allDayEvent;
        }

        /**
         * @param allDayEvent The allDayEvent to set.
         */
        public void setAllDayEvent(boolean allDayEvent) {
            this.allDayEvent = allDayEvent;
        }

        /**
         * @return Returns the date.
         */
        public String getDate() {
            return date;
        }

        /**
         * @param date The date to set.
         */
        public void setDate(String date) {
            this.date = date;
        }

        /**
         * @return Returns the hour.
         */
        public String getHour() {
            return hour;
        }

        /**
         * @param hour The hour to set.
         */
        public void setHour(String hour) {
            this.hour = hour;
        }

        /**
         * @return Returns the min.
         */
        public String getMin() {
            return min;
        }

        /**
         * @param min The min to set.
         */
        public void setMin(String min) {
            this.min = min;
        }

        /**
         * @return Returns the month.
         */
        public String getMonth() {
            return month;
        }

        /**
         * @param month The month to set.
         */
        public void setMonth(String month) {
            this.month = month;
        }

        /**
         * @return Returns the year.
         */
        public String getYear() {
            return year;
        }

        /**
         * @param year The year to set.
         */
        public void setYear(String year) {
            this.year = year;
        }
    }

    public static class EventStatusInfo {

        /**
         * status code of event item
         */
        private String code;

        /**
         * status name of event item
         */
        private String statusName;

        public EventStatusInfo(String code, String statusName) {
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

    public static class Events {

        private boolean today;
        private boolean monthDay;
        private Date date;
        private String day;
        private boolean more;

        private List events;

        public void setToday(boolean today) {
            this.today = today;
        }

        public boolean isToday() {
            return today;
        }

        public void setMonthDay(boolean monthDay) {
            this.monthDay = monthDay;
        }

        public boolean isMonthDay() {
            return monthDay;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public List getEvents() {
            return events;
        }

        public void setEvents(List events) {
            this.events = events;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDay() {
            return day;
        }

        public boolean getMore() {
            return more;
        }

        public void setMore(boolean more) {
            this.more = more;
        }
    }

    public static class EventInfo {

        /**
         * event item identity
         */
        private Long id;

        /**
         * title of event item
         */
        private String title;

        /**
         * description of event item
         */
        private String description;

        private java.util.Date start;
        /**
         * publication date of event item
         */
        private String startDate;

        private java.util.Date end;

        /**
         * archive date of event item
         */
        private String endDate;

        /**
         * source name of event item
         */
        private String sourceName;

        /**
         * source URL of event item
         */
        private String sourceUrl;

        /**
         * name of event item author's country or residence
         */
        private String country;

        /**
         * key of event item author's country
         */
        private String countryKey;

        /**
         * name of event item author's country or residence
         */
        private String countryName;

        /**
         * location of event item's author
         */
        private String location;

        /**
         * true if event item is selected in order to change its status, false otherwise
         */
        private boolean selected;

        /**
         * status of event item
         */
        private String status;

        /**
         * author user identity
         */
        private Long authorUserId;

        /**
         * First names of event item's author
         */
        private String authorFirstNames;

        /**
         * Last name of event item's author
         */
        private String authorLastName;

        //month view
        private boolean today;
        private boolean monthDay;
        private String day;
        private Date date;

        public EventInfo() {
            this.selected = false;
        }

        public EventInfo(String title, String status, boolean selected) {
            this.title = title;
            this.status = status;
            this.selected = selected;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * @return Returns the end.
         */
        public java.util.Date getEnd() {
            return end;
        }

        /**
         * @param end The end to set.
         */
        public void setEnd(java.util.Date end) {
            this.end = end;
        }

        /**
         * @return Returns the start.
         */
        public java.util.Date getStart() {
            return start;
        }

        /**
         * @param start The start to set.
         */
        public void setStart(java.util.Date start) {
            this.start = start;
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

        public void setCountryKey(String countryKey) {
            this.countryKey = countryKey;
        }

        public String getCountryKey() {
            return countryKey;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocation() {
            return location;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public void setToday(boolean today) {
            this.today = today;
        }

        public boolean isToday() {
            return today;
        }

        public void setMonthDay(boolean monthDay) {
            this.monthDay = monthDay;
        }

        public boolean isMonthDay() {
            return monthDay;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDay() {
            return day;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }

    private DateForm start = new DateForm();

    private DateForm end = new DateForm();

    /**
     * identity of currently active event item
     */
    private Long activeCalendarItem;

    /**
     * active Month identity
     */
    private Long activeMonth;

    /**
     * event item title
     */
    private String title;

    /**
     * event item description
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
     * name of event item author's country or residence
     */
    private String country;

    /**
     * key of event item author's country or residence
     */
    private String countryKey;
    /**
     * name of event item author's country or residence
     */
    private String countryName;

    /**
     * location of event item's author
     */
    private String location;

    /**
     * status of event item
     */
    private String status;

    /**
     * Calendar view (i.e. List View,Month View or Year View)
     */
    private String view;

    /**
     * collection of user languages
     */
    private Collection languages;

    /**
     * language selected by user
     */
    private String selectedLanguage;

    /**
     * source name of event item
     */
    private String sourceName;

    /**
     * source URL of event item
     */
    private String sourceUrl;

    /**
     * collection of months
     */
    private Collection months;

    /**
     * collection of days
     */
    private Collection days;

    /**
     * collection of years
     */
    private Collection years;

    /**
     * collection of hours
     */
    private Collection hours;

    /**
     * collection of minutes
     */
    private Collection mins;

    /**
     * user identity of event item's author
     */
    private Long authorUserId;

    /**
     * First names of event item's author
     */
    private String authorFirstNames;

    /**
     * Last name of event item's author
     */
    private String authorLastName;

    /**
     * true if event item is being previewed,
     * false otherwise
     */
    private boolean preview;

    /**
     * EventInfo instance for event item preveiw
     */
    private EventInfo previewItem;

    /**
     * true if event item is editable for current user - either if user is module Admin or author of an event item,
     * false otherwise
     */
    private boolean editable;

    /**
     * true if either of approve,archive,revoke, publish messages should be sent to the author of an event item,
     * false otherwise
     */
    private boolean sendMessage;

    /**
     * text containig onformation about which event items are being viewed: all event items, user's event items or user's pending event items
     */
    private String infoText;

    /**
     * month - user wants to jump on
     */
    private String jumpToMonth;

    /**
     * year - user wants to jump on
     */
    private String jumpToYear;

    /**
     * list of months used to show months in Year View
     */
    private List monthsList;

    /**
     * date user requested to jump on
     */
    private String jumpToDate;

    /**
     * current year
     */
    private String currentYear;

    /**
     * text containing current(today) date to show in Year View
     */
    private Date todayDate;

    /**
     * list of events
     */
    private List eventsList;

    /**
     * list of available statuses for event items
     */
    private List statusList;

    /**
     * user selected status with which should be updated selected event items statuses
     */
    private String selectedStatus;

    /**
     * collection of event items statuses used when changing status of selected event items (in admin view)
     */
    private Collection calendarStatus;

    /**
     * default message sent to event item's author, when status of an event is changed by Admin
     */
    private String defaultMessage;

    /**
     * text showing status of currently selected event items, status of which is being changed by Admin
     */
    private String statusTitle;

    /**
     * URL where concrete action should redirect to
     */
    private String returnUrl;

    /**
     * true when event items list is views in admin mode, false otherwise
     */
    private boolean adminViewAll;

    //
    private String moduleName;
    private String siteName;
    private String instanceName;
    //
    //pagination by year
    private String prevYear;
    private String nextYear;

    private String navYear;

    //
    private boolean selected;
    private boolean submitted;
    private Collection weekDays;

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

    /**
     * @return Returns the end.
     */
    public DateForm getEnd() {
        return end;
    }

    /**
     * @param end The end to set.
     */
    public void setEnd(DateForm end) {
        this.end = end;
    }

    /**
     * @return Returns the start.
     */
    public DateForm getStart() {
        return start;
    }

    /**
     * @param start The start to set.
     */
    public void setStart(DateForm start) {
        this.start = start;
    }

    public void setEnableSmiles(boolean enambleSiles) {
        this.enableSmiles = enambleSiles;
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

    public Collection getMonths() {
        return months;
    }

    public void setMonths(Collection months) {
        this.months = months;
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
        activeCalendarItem = null;
        sendMessage = false;
        defaultMessage = null;

        preview = false;
        previewItem = null;
        getStart().reset(mapping, request);
        getEnd().reset(mapping, request);

        title = null;
        description = null;
        enableHTML = false;
        enableSmiles = true;
        sourceName = null;
        sourceUrl = null;
        countryResidence = null;
        country = null;
        countryName = null;
        countryKey = null;
        selectedLanguage = null;
        languages = null;

        months = null;
        days = null;
        years = null;
        hours = null;
        mins = null;

        editable = false;
        calendarStatus = null;

        if (adminViewAll) {
            if (eventsList != null) {
                Iterator iter = eventsList.iterator();
                while (iter.hasNext()) {
                    Object item = iter.next();

                    if (item instanceof EventInfo) {
                        ( (EventInfo) item).setSelected(false);
                    }
                    else {
                        List events = ( (Events) item).getEvents();
                        if (events != null) {
                            Iterator iterEvents = events.iterator();
                            while (iterEvents.hasNext()) {
                                EventInfo itemEvent = (EventInfo) iterEvents.
                                    next();
                                itemEvent.setSelected(false);
                            }
                        }
                    }
                }
            }
            adminViewAll = false;
        }

        //
        jumpToMonth = null;
        jumpToYear = null;
        //
        jumpToDate = null;
        //
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
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

    public String[] getSelectedCalendarItems() {
        return null;
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

    public EventInfo getPreviewItem() {
        return previewItem;
    }

    public void setPreviewItem(EventInfo previewItem) {
        this.previewItem = previewItem;
    }

    public String getEndDay() {
        return getEnd().getDate();
    }

    public void setEndDay(String endDay) {
        getEnd().setDate(endDay);
    }

    public String getEndMonth() {
        return getEnd().getMonth();
    }

    public void setEndMonth(String endMonth) {
        getEnd().setMonth(endMonth);
    }

    public String getEndYear() {
        return getEnd().getYear();
    }

    public void setEndYear(String endYear) {
        getEnd().setYear(endYear);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDay() {
        return getStart().getDate();
    }

    public void setStartDay(String startDay) {
        getStart().setDate(startDay);
    }

    public String getStartHour() {
        return getStart().getHour();
    }

    public void setStartHour(String startHour) {
        getStart().setHour(startHour);
    }

    public String getStartMin() {
        return getStart().getMin();
    }

    public void setStartMin(String startMin) {
        getStart().setMin(startMin);
    }

    public String getStartMonth() {
        return getStart().getMonth();
    }

    public void setStartMonth(String startMonth) {
        getStart().setMonth(startMonth);
    }

    public String getStartYear() {
        return getStart().getYear();
    }

    public void setStartYear(String startYear) {
        getStart().setYear(startYear);
    }

    public String getEndHour() {
        return getEnd().getHour();
    }

    public void setEndHour(String endHour) {
        getEnd().setHour(endHour);
    }

    public String getEndMin() {
        return getEnd().getMin();
    }

    public void setEndMin(String endMin) {
        getEnd().setMin(endMin);
    }

    public List getEventsList() {
        return eventsList;
    }

    public void setEventsList(List eventsList) {
        this.eventsList = eventsList;
    }

    public EventInfo getCalendarItem(int index) {
        EventInfo eventItem = null;
        int currentSize = eventsList.size();
        if (index >= currentSize) {
            for (int i = 0; i <= index - currentSize; i++) {
                eventsList.add(new EventInfo());
            }
        }

        return (EventInfo) eventsList.get(index);
    }

    public Collection getCalendarStatus() {
        return calendarStatus;
    }

    public void setCalendarStatus(Collection calendarStatus) {
        this.calendarStatus = calendarStatus;
    }

    public Long getActiveCalendarItem() {
        return activeCalendarItem;
    }

    public void setActiveCalendarItem(Long activeCalendarItem) {
        this.activeCalendarItem = activeCalendarItem;
    }

    public Collection getLanguages() {
        return languages;
    }

    public void setLanguages(Collection languages) {
        this.languages = languages;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
    }

    public Collection getHours() {
        return hours;
    }

    public void setHours(Collection hours) {
        this.hours = hours;
    }

    public Collection getMins() {
        return mins;
    }

    public void setMins(Collection mins) {
        this.mins = mins;
    }

    public boolean isAllEndDayEvent() {
        return getEnd().isAllDayEvent();
    }

    public void setAllEndDayEvent(boolean allEndDayEvent) {
        getEnd().setAllDayEvent(allEndDayEvent);
    }

    public boolean isAllStartDayEvent() {
        return getStart().isAllDayEvent();
    }

    public void setAllStartDayEvent(boolean allStartDayEvent) {
        getStart().setAllDayEvent(allStartDayEvent);
    }

    public String getEndDate() {
        return getEnd().getDate();
    }

    public void setEndDate(String endDate) {
        getEnd().setDate(endDate);
    }

    public String getStartDate() {
        return getStart().getDate();
    }

    public void setStartDate(String startDate) {
        getStart().setDate(startDate);
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

    public String getJumpToMonth() {
        return jumpToMonth;
    }

    public void setJumpToMonth(String jumpToMonth) {
        this.jumpToMonth = jumpToMonth;
    }

    public String getJumpToYear() {
        return jumpToYear;
    }

    public void setJumpToYear(String jumpToYear) {
        this.jumpToYear = jumpToYear;
    }

    public Collection getYears() {
        return years;
    }

    public void setYears(Collection years) {
        this.years = years;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String infoText) {
        this.infoText = infoText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getActiveMonth() {
        return activeMonth;
    }

    public void setActiveMonth(Long activeMonth) {
        this.activeMonth = activeMonth;
    }

    public String getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }

    public String getJumpToDate() {
        return jumpToDate;
    }

    public void setJumpToDate(String jumpToDate) {
        this.jumpToDate = jumpToDate;
    }

    public List getMonthsList() {
        return monthsList;
    }

    public void setMonthsList(List monthsList) {
        this.monthsList = monthsList;
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

    public boolean isAdminViewAll() {
        return adminViewAll;
    }

    public void setAdminViewAll(boolean adminViewAll) {
        this.adminViewAll = adminViewAll;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
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

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getNavYear() {
        return navYear;
    }

    public void setNavYear(String navYear) {
        this.navYear = navYear;
    }

    public String getNextYear() {
        return nextYear;
    }

    public void setNextYear(String nextYear) {
        this.nextYear = nextYear;
    }

    public String getPrevYear() {
        return prevYear;
    }

    public void setPrevYear(String prevYear) {
        this.prevYear = prevYear;
    }

    public Date getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(Date todayDate) {
        this.todayDate = todayDate;
    }

    public ActionErrors validate(ActionMapping actionMapping,
                                 HttpServletRequest httpServletRequest) {
        ActionErrors errors = new ActionErrors();

        if (isEmptyString(title)) {
            errors.add(ActionErrors.GLOBAL_MESSAGE,
                       new ActionMessage("error.calendar.itemTitleEmpty"));
        }

        if (isEmptyString(location)) {
            errors.add(ActionErrors.GLOBAL_MESSAGE,
                       new ActionMessage("error.calendar.itemLocationEmpty"));
        }

        boolean sourceUrlEmpty = false;

        if (isEmptyString(sourceUrl)) {
            sourceUrlEmpty = true;
        }
        else {
            int schemePos = sourceUrl.indexOf(REQUEST_SCHEME_DELIMITER);
            if (schemePos < 0) {
                errors.add(ActionErrors.GLOBAL_MESSAGE,
                           new ActionMessage("error.calendar.noSourceUrlRequestScheme"));
            } else {
                sourceUrlEmpty =
                    sourceUrl.substring(schemePos +
                                        REQUEST_SCHEME_DELIMITER.length()).
                    trim().length() == 0;
            }
        }

        if (sourceUrlEmpty && isEmptyString(sourceName)) {
            if (isEmptyString(description)) {
                errors.add(ActionErrors.GLOBAL_MESSAGE,
                           new ActionMessage("error.calendar.itemDescrAndSourceEmpty"));
            }

        } else {
            if (sourceUrlEmpty && !isEmptyString(sourceName)) {
                errors.add(ActionErrors.GLOBAL_MESSAGE,
                           new ActionMessage("error.calendar.itemSourceUrlEmpty"));
            }
            if (!sourceUrlEmpty && isEmptyString(sourceName)) {
                errors.add(ActionErrors.GLOBAL_MESSAGE,
                           new ActionMessage("error.calendar.itemSourceNameEmpty"));
            }

        }
        CalendarPopulator populator = new CalendarPopulator(this);

        if (populator.getStart().getTime().compareTo(populator.getEnd().getTime()) >=
            0) {
            errors.add(ActionErrors.GLOBAL_MESSAGE,
                       new ActionMessage("error.calendar.endDateMustGreater"));
        }

        return errors.isEmpty() ? null : errors;
    }

    private boolean isEmptyString(String str) {
        return str == null || str.trim().length() == 0;
    }

    public void loadCalendar() {

        months = new ArrayList();
        days = new ArrayList();
        years = new ArrayList();
        hours = new ArrayList();
        mins = new ArrayList();

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
        for (int i = 1; i <= 24; i++) {
            hours.add(new Calendar(new Long(i), Integer.toString(i)));
        }
        for (int i = 0; i <= 60; i++) {
            if (i < 10) {
                mins.add(new Calendar(new Long(i), ":0" + Integer.toString(i)));
            }
            else {
                mins.add(new Calendar(new Long(i), ":" + Integer.toString(i)));
            }
        }
        for (int i = 2003; i <= 2010; i++) {
            years.add(new Calendar(new Long(i), Integer.toString(i)));
        }

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

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setWeekDays(Collection weekDays) {
        this.weekDays = weekDays;
    }

    public Collection getWeekDays() {
        return weekDays;
    }

}
