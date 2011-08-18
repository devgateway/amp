package org.digijava.module.calendar.entity;

import java.util.List;

public class EventsFilter {
    private List eventTypes;
    private String[] selectedEventTypes;
    private List donors;
    private String[] selectedDonors;
    private Integer showPublicEvents;

    public List getDonors() {
        return donors;
    }

    public List getEventTypes() {
        return eventTypes;
    }

    public Integer getShowPublicEvents() {
        return showPublicEvents;
    }

    public String[] getSelectedEventTypes() {
        return selectedEventTypes;
    }

    public String[] getSelectedDonors() {
        return selectedDonors;
    }

    public void setDonors(List donors) {
        this.donors = donors;
    }

    public void setEventTypes(List eventTypes) {
        this.eventTypes = eventTypes;
    }

    public void setShowPublicEvents(Integer showPublicEvents) {
        this.showPublicEvents = showPublicEvents;
    }

    public void setSelectedEventTypes(String[] selectedEventTypes) {
        this.selectedEventTypes = selectedEventTypes;
    }

    public void setSelectedDonors(String[] selectedDonors) {
        this.selectedDonors = selectedDonors;
    }
}
