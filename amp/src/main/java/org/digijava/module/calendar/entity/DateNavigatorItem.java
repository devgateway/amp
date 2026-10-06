package org.digijava.module.calendar.entity;

public class DateNavigatorItem {
    private boolean enabled;
    private boolean selected;
    private boolean nolink;
    private String month;
    private int dayOfMonth;
    private String dayOfWeek;
    private int timestamp;
    private DateBreakDown dateBreakDown;
    private int[] subItemLeftTimestamp;
    private int[] subItemRightTimestamp;

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getMonth() {
        return month;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isNolink() {
        return nolink;
    }

    public DateBreakDown getDateBreakDown() {
        return dateBreakDown;
    }

    public int[] getSubItemLeftTimestamp() {
        return subItemLeftTimestamp;
    }

    public int[] getSubItemRightTimestamp() {
        return subItemRightTimestamp;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setNolink(boolean nolink) {
        this.nolink = nolink;
    }

    public void setDateBreakDown(DateBreakDown dateBreakDown) {
        this.dateBreakDown = dateBreakDown;
    }

    public void setSubItemLeftTimestamp(int[] subItemLeftTimestamp) {
        this.subItemLeftTimestamp = subItemLeftTimestamp;
    }

    public void setSubItemRightTimestamp(int[] subItemRightTimestamp) {
        this.subItemRightTimestamp = subItemRightTimestamp;
    }

}
