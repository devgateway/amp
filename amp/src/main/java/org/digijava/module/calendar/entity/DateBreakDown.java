package org.digijava.module.calendar.entity;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.calendar.exception.CalendarException;
import org.digijava.module.calendar.util.CalendarThread;

public class DateBreakDown {
    private int type;
    private int year;
    private boolean leap;
    private int month;
    private String monthNameShort;
    private String monthNameLong;
    private int dayOfYear;
    private int dayOfMonth;
    private int dayOfWeek;
    private String dayOfWeekName;
    private int hour;
    private int minute;
    private String dateInLocaleWeek;
    private String dateInLocaleMonth;
    private String dateInLocaleDay;
    
    public DateBreakDown() {

    }

    public DateBreakDown(Date date, int type) throws CalendarException, WorkerException {
        this(getGregorianCalendar(date), type, null);
    }
    
    public DateBreakDown(Date date, int type, HttpServletRequest request) throws CalendarException, WorkerException {
        this(getGregorianCalendar(date), type, request);
    }

    public DateBreakDown(GregorianCalendar calendar, int type) throws CalendarException, WorkerException {
        this(calendar, type, null);
    }
    public DateBreakDown(GregorianCalendar calendar, int type, HttpServletRequest request) throws
        CalendarException, WorkerException {
        if(type != CalendarOptions.CALENDAR_TYPE_GREGORIAN &&
           type != CalendarOptions.CALENDAR_TYPE_ETHIOPIAN &&
           type != CalendarOptions.CALENDAR_TYPE_ETHIOPIAN_FY) {
            throw new CalendarException("Unknown calendar type");
        }
        this.type = type;
        if(this.type == CalendarOptions.CALENDAR_TYPE_GREGORIAN) {
            parseGregorianCalendar(calendar);
        } else {
            parseEthiopianCalendar(calendar);
        }
        monthNameShort = getMonthName(month - 1, this.type, false);
        monthNameLong = getMonthName(month - 1, this.type, true);
        dayOfWeek = calendar.get(calendar.DAY_OF_WEEK);
        dayOfWeekName = getDayOfWeekName(dayOfWeek - 1, this.type);
        hour = calendar.get(calendar.HOUR_OF_DAY);
        minute = calendar.get(calendar.MINUTE);
        //
        Long siteId;
        String locale = ""; 
        if (request == null){
            siteId = CalendarThread.getSite().getId();
            locale = CalendarThread.getLocale().getCode();
        }else{
           siteId = RequestUtils.getSite(request).getId();
           locale =  RequestUtils.getNavigationLanguage(request).getCode().toLowerCase();
        }
        
        if (locale != null) {
            String ms = TranslatorWorker.translateText(monthNameShort, locale, siteId);
            String ml = TranslatorWorker.translateText(monthNameLong, locale, siteId);
            dateInLocaleWeek = dayOfMonth + " " + ms + " " + year;
            dateInLocaleDay = dayOfMonth + " " + ml + " " + year;
            dateInLocaleMonth = ml + " " + year;
        } else {
            // default to english
            String ms = TranslatorWorker.translateText(monthNameShort, locale, siteId);
            String ml = TranslatorWorker.translateText(monthNameLong, locale, siteId);
            dateInLocaleWeek = ms + " " + dayOfMonth + ", " + year;
            dateInLocaleDay = ml + " " + dayOfMonth + ", " + year;
            dateInLocaleMonth = ml + ", " + year;
        }
    }

    private static GregorianCalendar getGregorianCalendar(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return calendar;
    }

    private void parseGregorianCalendar(GregorianCalendar calendar) {
        year = calendar.get(calendar.YEAR);
        leap = calendar.isLeapYear(year);
        dayOfYear = calendar.get(calendar.DAY_OF_YEAR);
        month = calendar.get(calendar.MONTH) + 1;
        dayOfMonth = calendar.get(calendar.DAY_OF_MONTH);
    }

    private void parseEthiopianCalendar(GregorianCalendar calendar) {
        boolean dateInEndYear = false;
        boolean startYearLeap = false;
        boolean endYearLeap = false;
        int startYearFirstDay = 11;
        int endYearFirstDay = 11;

        int year = calendar.get(calendar.YEAR);
        if(calendar.isLeapYear(year + 1)) {
            startYearFirstDay = 12;
            endYearLeap = true;
        } else if(calendar.isLeapYear(year + 2)) {
            startYearLeap = true;
        } else if(calendar.isLeapYear(year)) {
            endYearFirstDay = 12;
        }
        GregorianCalendar startDate = (GregorianCalendar) calendar.clone();
        startDate.set(startDate.MONTH, startDate.SEPTEMBER);
        startDate.set(startDate.DAY_OF_MONTH, startYearFirstDay);
        if(calendar.getTimeInMillis() < startDate.getTimeInMillis()) {
            this.year = year - 8;
            dateInEndYear = true;
        } else {
            this.year = year - 7;
        }
        int firstDay;
        if(dateInEndYear) {
            this.leap = endYearLeap;
            firstDay = endYearFirstDay;
            year--;
        } else {
            this.leap = startYearLeap;
            firstDay = startYearFirstDay;
        }
        GregorianCalendar ethStartDate = (GregorianCalendar) calendar.clone();
        ethStartDate.set(ethStartDate.YEAR, year);
        ethStartDate.set(ethStartDate.MONTH, ethStartDate.SEPTEMBER);
        ethStartDate.set(ethStartDate.DAY_OF_MONTH, firstDay);
        this.dayOfYear = (int) ((calendar.getTimeInMillis() -
                                 ethStartDate.getTimeInMillis()) /
                                (1000 * 3600 * 24)) + 1;
        int div = this.dayOfYear / 30;
        int mod = this.dayOfYear % 30;
        if(mod != 0) {
            this.month = div + 1;
            this.dayOfMonth = mod;
        } else {
            this.month = div;
            this.dayOfMonth = 30;
        }
        if(this.dayOfYear <= 300 &&
           this.type == CalendarOptions.CALENDAR_TYPE_ETHIOPIAN_FY) {
            this.year--;
        }
    }

    public GregorianCalendar getGregorianCalendar() {
        if(this.type == CalendarOptions.CALENDAR_TYPE_GREGORIAN) {
            return new GregorianCalendar(this.year, this.month - 1, this.dayOfMonth,
                                         this.hour, this.minute);
        } else {
            GregorianCalendar calendar = new GregorianCalendar();
            int gregorianYear = this.year + 7;
            int firstDay = 11;
            if(calendar.isLeapYear(gregorianYear + 1)) {
                firstDay++;
            }
            int dayOfYear = (this.month - 1) * 30 + this.dayOfMonth;
            //
            if(dayOfYear <= 300 &&
               this.type == CalendarOptions.CALENDAR_TYPE_ETHIOPIAN_FY) {
                gregorianYear++;
            }
            calendar = new GregorianCalendar(gregorianYear, calendar.SEPTEMBER,
                                             firstDay);
            calendar.add(calendar.DAY_OF_MONTH, dayOfYear - 1);
            calendar.set(calendar.HOUR, this.hour);
            calendar.set(calendar.MINUTE, this.minute);
            return calendar;
        }
    }

    public static String getMonthName(int index, int type, boolean longMonth) {
        String monthNameLong[][] = {
            {
            "January", "February", "March", "April", "May", "June", "July",
            "August", "September", "October", "November", "December"}, {
            "Meskerem", "Tikemet", "Hidar", "Tahesas", "Tir", "Yekatit",
            "Megabit", "Miyaza", "Ginbot", "Sene", "Hamle", "Nehase", "Pegume"}
        };
        String monthNameShort[][] = {
            {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep",
            "Oct",
            "Nov", "Dec"}, {
            "Mes", "Tik", "Hid", "Tah", "Tir", "Yek", "Meg", "Miy", "Gin",
            "Sen",
            "Ham", "Neh", "Peg"}
        };
        int calendarType = type == CalendarOptions.CALENDAR_TYPE_GREGORIAN ?
            CalendarOptions.CALENDAR_TYPE_GREGORIAN :
            CalendarOptions.CALENDAR_TYPE_ETHIOPIAN;
        if(calendarType == CalendarOptions.CALENDAR_TYPE_GREGORIAN &&
           (index < 0 || index > 11)) {
            index = 0;
        }
        if(calendarType == CalendarOptions.CALENDAR_TYPE_ETHIOPIAN &&
           (index < 0 || index > 12)) {
            index = 0;
        }
        if(longMonth) {
            return monthNameLong[calendarType][index];
        } else {
            return monthNameShort[calendarType][index];
        }
    }

    private static String getDayOfWeekName(int index, int type) {
        String dayName[][] = {
            {
            "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}, {
            "E", "S", "M", "R", "H", "A", "K"}
        };
        int calendarType = type == CalendarOptions.CALENDAR_TYPE_GREGORIAN ?
            CalendarOptions.CALENDAR_TYPE_GREGORIAN :
            CalendarOptions.CALENDAR_TYPE_ETHIOPIAN;
        if(index < 0 || index > 6) {
            index = 0;
        }
        return dayName[calendarType][index];
    }

    public int getDaysInEthiopianMonth() throws CalendarException {
        return getDaysInEthiopianMonth(this.month);
    }

    public int getDaysInEthiopianMonth(int month) throws CalendarException {
        if(type != CalendarOptions.CALENDAR_TYPE_GREGORIAN) {
            if(month == 13) {
                return this.leap ? 6 : 5;
            } else {
                return 30;
            }
        } else {
            return -1;
        }
    }

    public int getNextMonth() {
        int firstMonth = 1;
        int lastMonth = this.type == CalendarOptions.CALENDAR_TYPE_GREGORIAN ?
            12 : 13;
        return this.month == lastMonth ? firstMonth : this.month + 1;
    }

    public int getPreviousMonth() {
        int firstMonth = 1;
        int lastMonth = this.type == CalendarOptions.CALENDAR_TYPE_GREGORIAN ?
            12 : 13;
        return this.month == firstMonth ? lastMonth : this.month - 1;
    }

    public static GregorianCalendar createGregorianCalendar(int type,
        String date) {
        GregorianCalendar calendar = null;
        String[] tokens = date.split("/");
        try {
       
            DateBreakDown calendarBreakDown = new DateBreakDown();
            calendarBreakDown.setType(type);
            
            GregorianCalendar tmpCalendar= FormatHelper.parseDate(date);
            
            calendarBreakDown.setDayOfMonth(tmpCalendar.get(GregorianCalendar.DAY_OF_MONTH));
            calendarBreakDown.setMonth(tmpCalendar.get(GregorianCalendar.MONTH)+1);
            calendarBreakDown.setYear(tmpCalendar.get(GregorianCalendar.YEAR));
            calendar = calendarBreakDown.getGregorianCalendar();
        } catch(Exception e) {
            calendar = new GregorianCalendar();
        }
        return calendar;
    }

    public static boolean isValidDate(int type, String date) { //
        if (date == null || !date.matches("^[0-9]{2}\\/[0-9]{2}\\/[0-9]{4}$")) {
            return false;
        }
        try {
            GregorianCalendar calendar = createGregorianCalendar(type, date);
            DateBreakDown calendarBreakDown = new DateBreakDown(calendar, type);
            String resultStr = calendarBreakDown.formatDateString();
            return date.equals(resultStr);
        } catch(Exception ex) {
            return false;
        }
    }

    public static boolean isValidTime(String time) {
        if (time == null || !time.matches("^([0-1][0-9]|2[0-3]):[0-5][0-9]$")) {
            return false;
        } else {
            return true;
        }
    }

    public static GregorianCalendar createValidGregorianCalendar(int type,
        String date, String time) {
        GregorianCalendar result = null;
        if(isValidDate(type, date)) {
            result = createGregorianCalendar(type, date);
        } else {
            result = new GregorianCalendar();
        }
        if(isValidTime(time)) {
            String[] tokens = time.split(":");
            result.set(result.HOUR_OF_DAY, Integer.parseInt(tokens[0]));
            result.set(result.MINUTE, Integer.parseInt(tokens[1]));
        }
        return result;
    }

    public String formatDateString() {
        return FormatHelper.formatDate(this.getGregorianCalendar().getTime());
    }

    public String formatTimeString() {
        String hh = formatDecimal("00", this.getHour());
        String mm = formatDecimal("00", this.getMinute());
        return hh + ":" + mm;
    }

    private static String formatDecimal(String format, int decimal) {
        DecimalFormat df = new DecimalFormat(format);
        Object o = new Integer(decimal);
        return df.format(o);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getMonthNameLong() {
        return monthNameLong;
    }

    public String getMonthNameShort() {
        return monthNameShort;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public String getDayOfWeekName() {
        return dayOfWeekName;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public boolean isLeap() {
        return leap;
    }

    public int getType() {
        return type;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    
    public String getDateInLocaleWeek() {
        return this.dateInLocaleWeek;
    }

    public void setDateInLocaleWeek(String dateInLocaleWeek) {
        this.dateInLocaleWeek = dateInLocaleWeek;
    }

    public String getDateInLocaleMonth() {
        return this.dateInLocaleMonth;
    }

    public void setDateInLocaleMonth(String dateInLocaleMonth) {
        this.dateInLocaleMonth = dateInLocaleMonth;
    }

    public String getDateInLocaleDay() {
        return this.dateInLocaleDay;
    }

    public void setDateInLocaleDay(String dateInLocaleDay) {
        this.dateInLocaleDay = dateInLocaleDay;
    }

}
