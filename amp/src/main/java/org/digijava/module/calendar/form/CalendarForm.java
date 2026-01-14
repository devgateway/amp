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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public class CalendarForm
    extends ActionForm {

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

        /**
         * publication date of event item
         */
        private String startDate;

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

        public EventInfo() {

        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getEndDate() {
            return endDate;
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

        public void setId(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }
    }

    /**
     * identity of currently active event item
     */
    long activeCalendarId;

    /**
     * collection of event items - NOW including future events
     */
    private Collection eventsList;

    /**
     * number of event items visible in teaser
     */
    private int numOfItemsInTeaser;

    /**
     * collection od event items - not including future events(only events starting earlier oe equal Today)
     */
    private Collection eventsListToday;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.eventsList = null;
        this.eventsListToday = null;
        this.activeCalendarId = 0;
        this.numOfItemsInTeaser = 0;
    }

    public long getActiveCalendarId() {
        return activeCalendarId;
    }

    public void setActiveCalendarId(long activeCalendarId) {
        this.activeCalendarId = activeCalendarId;
    }

    public Collection getEventsList() {
        return eventsList;
    }

    public void setEventsList(Collection eventsList) {
        this.eventsList = eventsList;
    }

    public int getNumOfItemsInTeaser() {
        return numOfItemsInTeaser;
    }

    public void setNumOfItemsInTeaser(int numOfItemsInTeaser) {
        this.numOfItemsInTeaser = numOfItemsInTeaser;
    }

    public Collection getEventsListToday() {
        return eventsListToday;
    }

    public void setEventsListToday(Collection eventsListToday) {
        this.eventsListToday = eventsListToday;
    }
}
