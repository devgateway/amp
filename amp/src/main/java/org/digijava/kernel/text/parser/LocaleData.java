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

package org.digijava.kernel.text.parser;

import java.util.ArrayList;

public class LocaleData {

    private boolean frozen;
    private ArrayList monthList;
    private ArrayList shortMonthList;
    private ArrayList eraList;
    private ArrayList weekDayList;
    private ArrayList shortWeekDayList;
    private ArrayList ampmList;
    private ArrayList datePatternList;
    private ArrayList timePatternList;

    /**
     * Initialization
     */
    public LocaleData() {
        frozen = false;
        monthList = new ArrayList();
        shortMonthList = new ArrayList();
        eraList = new ArrayList();
        weekDayList = new ArrayList();
        shortWeekDayList = new ArrayList();
        ampmList = new ArrayList();
        datePatternList = new ArrayList();
        timePatternList = new ArrayList();
    }

    /* Digester methods */
    public void addMonth(String month) {
        if (frozen)
            throw new IllegalStateException(
                "Locale information is already frozen");
        monthList.add(month);
    }

    public void addShortMonth(String shortMonth) {
        if (frozen)
            throw new IllegalStateException(
                "Locale information is already frozen");
        shortMonthList.add(shortMonth);
    }

    public void addEra(String era) {
        if (frozen)
            throw new IllegalStateException(
                "Locale information is already frozen");
        eraList.add(era);
    }

    public void addWeekDay(String weekDay) {
        if (frozen)
            throw new IllegalStateException(
                "Locale information is already frozen");
        weekDayList.add(weekDay);
    }

    public void addShortWeekDay(String shortWeekDay) {
        if (frozen)
            throw new IllegalStateException(
                "Locale information is already frozen");
        shortWeekDayList.add(shortWeekDay);
    }

    public void addAmpm(String ampm) {
        if (frozen)
            throw new IllegalStateException(
                "Locale information is already frozen");
        ampmList.add(ampm);
    }

    public void addDatePattern(String pattern) {
        if (frozen)
            throw new IllegalStateException(
                "Locale information is already frozen");
        datePatternList.add(pattern);
    }

    public void addTimePattern(String pattern) {
        if (frozen)
            throw new IllegalStateException(
                "Locale information is already frozen");
        timePatternList.add(pattern);
    }

    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("Months ");
        buff.append(monthList);
        buff.append("\n");

        buff.append("Short months ");
        buff.append(shortMonthList);
        buff.append("\n");

        buff.append("Week days ");
        buff.append(weekDayList);
        buff.append("\n");

        buff.append("Short week days ");
        buff.append(shortWeekDayList);
        buff.append("\n");

        buff.append("Eras ");
        buff.append(eraList);
        buff.append("\n");

        buff.append("Am/Pm ");
        buff.append(ampmList);
        buff.append("\n");

        buff.append("Date patterns ");
        buff.append(datePatternList);
        buff.append("\n");
        buff.append("dafault: ").append(defaultDateStyle);
        buff.append("\n");

        buff.append("Time patterns ");
        buff.append(timePatternList);
        buff.append("\n");
        buff.append("dafault: ").append(defaultTimeStyle);
        buff.append("\n");

        buff.append("Date/Time pattern ").append(dateTimePattern);
        buff.append("\n");

        return buff.toString();
    }

    public void prepareData() {
        eras = new String[eraList.size()];
        eraList.toArray(eras);

        months = new String[monthList.size()];
        monthList.toArray(months);

        shortMonths = new String[shortMonthList.size()];
        shortMonthList.toArray(shortMonths);

        // Week deys begin from 1, not from 0
        weekdays = new String[weekDayList.size() + 1];
        weekdays[0] = "";
        for (int i = 1; i < weekdays.length; i++) {
            weekdays[i] = (String) weekDayList.get(i - 1);
        }

        // Short week days begin from 1 too
        shortWeekdays = new String[shortWeekDayList.size() + 1];
        shortWeekdays[0] = "";
        for (int i = 1; i < shortWeekdays.length; i++) {
            shortWeekdays[i] = (String) shortWeekDayList.get(i - 1);
        }

        ampms = new String[ampmList.size()];
        ampmList.toArray(ampms);

        datePatterns = new String[datePatternList.size()];
        datePatternList.toArray(datePatterns);

        timePatterns = new String[timePatternList.size()];
        timePatternList.toArray(timePatterns);

        frozen = true;
    }

    public String[] getAmpms() {
        return ampms;
    }

    public String[] getEras() {
        return eras;
    }

    public String[] getMonths() {
        return months;
    }

    public String[] getShortMonths() {
        return shortMonths;
    }

    public String[] getShortWeekdays() {
        return shortWeekdays;
    }

    public String[] getWeekdays() {
        return weekdays;
    }

    public String[][] getZoneStrings() {
        return zoneStrings;
    }

    public String getDateTimePattern() {
        return dateTimePattern;
    }

    public void setDateTimePattern(String dateTimePattern) {
        this.dateTimePattern = dateTimePattern;
    }

    public String[] getDatePatterns() {
        return datePatterns;
    }

    public String[] getTimePatterns() {
        return timePatterns;
    }

    public int getDefaultDateStyle() {
        return defaultDateStyle;
    }

    public void setDefaultDateStyle(int defaultDateStyle) {
        this.defaultDateStyle = defaultDateStyle;
    }

    public int getDefaultTimeStyle() {
        return defaultTimeStyle;
    }

    public void setDefaultTimeStyle(int defaultTimeStyle) {
        this.defaultTimeStyle = defaultTimeStyle;
    }

    private String eras[] = null;
    private String months[] = null;
    private String shortMonths[] = null;
    private String weekdays[] = null;
    private String shortWeekdays[] = null;
    private String ampms[] = null;
    private String zoneStrings[][] = null;
    private String datePatterns[] = null;
    private String timePatterns[] = null;
    private String dateTimePattern;
    private int defaultDateStyle;
    private int defaultTimeStyle;
}
