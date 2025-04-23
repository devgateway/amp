package org.digijava.module.calendar.entity;

import org.digijava.module.calendar.dbentity.AmpCalendar;

import java.util.ArrayList;
import java.util.List;

public class AmpCalendarGraph {
    private AmpCalendar ampCalendar;
    private List graphItems;

    public AmpCalendarGraph() {

    }

    public AmpCalendarGraph(AmpCalendar ampCalendar) {
        this.ampCalendar = ampCalendar;
        this.graphItems = new ArrayList();
    }

    public AmpCalendar getAmpCalendar() {
        return ampCalendar;
    }

    public List getGraphItems() {
        return graphItems;
    }

    public void setAmpCalendar(AmpCalendar ampCalendar) {
        this.ampCalendar = ampCalendar;
    }

    public void setGraphItems(List graphItems) {
        this.graphItems = graphItems;
    }
}
