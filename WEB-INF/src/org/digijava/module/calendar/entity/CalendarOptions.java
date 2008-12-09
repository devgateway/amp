package org.digijava.module.calendar.entity;

public class CalendarOptions {
    public static final String CALENDAR_VIEW_YEARLY = "yearly";
    public static final String CALENDAR_VIEW_MONTHLY = "monthly";
    public static final String CALENDAR_VIEW_WEEKLY = "weekly";
    public static final String CALENDAR_VIEW_DAYLY = "daily";
    public static final String CALENDAR_VIEW_CUSTOM = "custom";
    public static final String CALENDAR_VIEW_NONE = "none"; //if all view are turned off from FM,then view="none"

    public static final String defaultView = CALENDAR_VIEW_YEARLY;

    public static final int CALENDAR_TYPE_GREGORIAN = 0;
    public static final int CALENDAR_TYPE_ETHIOPIAN = 1;
    public static final int CALENDAR_TYPE_ETHIOPIAN_FY = 2;

    public static final String[] CALENDAR_TYPE_NAME = {
        "Gregorian",
        "Ethiopian",
        "Ethiopian FY"
    };

    public static final int defaultCalendarType = CALENDAR_TYPE_GREGORIAN;
}
