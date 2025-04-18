package org.digijava.module.aim.helper.fiscalcalendar;

import java.util.Collection;
import java.util.Vector;

public class BaseCalendar {
    private String name;
    private String value;

    public static final BaseCalendar BASE_GREGORIAN = new BaseCalendar("Gregorian", "GREG-CAL");
    public static final BaseCalendar BASE_ETHIOPIAN = new BaseCalendar("Ethiopian ", "ETH-CAL");
    public static final BaseCalendar BASE_NEPALI = new BaseCalendar("Nepali ", "NEP-CAL");

    public static Collection<BaseCalendar> calendarList = null;

    /*
     * Nepali Date to English: Subtract - 56 Years - 8 Months - 17 Days
English Date to Nepali Date: Add - 56 Years - 8 Months - 17 Days
     * */
    // singleton base calendar list
    static {
        calendarList = new Vector<BaseCalendar>();
        calendarList.add(BASE_GREGORIAN);
        calendarList.add(BASE_ETHIOPIAN);
        calendarList.add(BASE_NEPALI);
    }

    public static Collection getCalendarList() {
        return calendarList;
    }

    public static BaseCalendar getBaseCalendar(String key) {
        for (BaseCalendar base : calendarList) {
            if (base.getValue().equalsIgnoreCase(key)) {
                return base;
            }
        }
        return BASE_GREGORIAN;
    }

    public BaseCalendar(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getTrnName() {
        String key = "aim:calendar:basecalender:" + value.toLowerCase().replaceAll(" ", "");
        return key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }



}
