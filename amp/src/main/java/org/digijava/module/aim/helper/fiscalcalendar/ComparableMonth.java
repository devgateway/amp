package org.digijava.module.aim.helper.fiscalcalendar;

import java.sql.Date;

public class ComparableMonth implements Comparable<ComparableMonth> {
    private int monthId;
    private String monthStr;

    public ComparableMonth(int monthId, String monthStr) {
        this.monthId = monthId;
        this.monthStr = monthStr;
    }

    public int compareTo(ComparableMonth o) {
        if (monthId < o.monthId)
            return -1;
        if (monthId > o.monthId)
            return 1;
        return 0;
    }

    public String toString() {
        return monthStr;
    }

    /**
     * Convert the date that comes from the input to gregorian calendar based on
     * global setting default calendar
     * 
     * @return
     */
    public Date defaultToGregorian(Date date) {
        return null;
    }

    /**
     * Convert the date that comes as gregorian to a date based on global
     * setting default calendar
     * 
     * @return
     */
    public Date gregorianToDefault(Date date) {
        return null;
    }
    
    
    public boolean equals(Object obj) {
            ComparableMonth m = (ComparableMonth) obj;
            return m.monthStr.equalsIgnoreCase(this.monthStr) && m.monthId==this.monthId;
    }

    public int getMonthId() {
        return monthId;
    }

    public void setMonthId(int monthId) {
        this.monthId = monthId;
    }
    
    public String getMonthStr() {
        return this.monthStr;
    }
    
    
}
