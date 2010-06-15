package org.digijava.module.calendar.entity;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.util.LabelValueBean;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.module.calendar.exception.CalendarException;
import org.digijava.module.calendar.util.AmpUtil;

public class DateNavigator {
    private int leftTimestamp;
    private int rightTimestamp;
    private List items;

    public List getItems() {
        return items;
    }

    public int getLeftTimestamp() {
        return leftTimestamp;
    }

    public int getRightTimestamp() {
        return rightTimestamp;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public void setLeftTimestamp(int leftTimestamp) {
        this.leftTimestamp = leftTimestamp;
    }

    public void setRightTimestamp(int rightTimestamp) {
        this.rightTimestamp = rightTimestamp;
    }

    private GregorianCalendar add(GregorianCalendar calendar, int type,
                                  int field, int count, HttpServletRequest request) throws
        CalendarException, WorkerException {
        GregorianCalendar clone = (GregorianCalendar) calendar.clone();
        if(field == clone.YEAR) {
            clone.add(clone.YEAR, count);
        } else if(field == clone.MONTH) {
            if(type == CalendarOptions.CALENDAR_TYPE_GREGORIAN) {
                clone.add(clone.MONTH, count);
            } else {
                boolean moveRight = count > 0 ? true : false;
                count = Math.abs(count);
                while(count > 0) {
                    DateBreakDown cloneBreakDown = new DateBreakDown(clone,
                        type,request);
                    int today = cloneBreakDown.getDayOfMonth();
                    int daysInCurrentMonth = cloneBreakDown.
                        getDaysInEthiopianMonth(cloneBreakDown.getMonth());
                    if(moveRight) {
                        int daysInNextMonth = cloneBreakDown.
                            getDaysInEthiopianMonth(cloneBreakDown.getNextMonth());
                        if(today <= daysInNextMonth) {
                            clone.add(clone.DAY_OF_MONTH, daysInCurrentMonth);
                        } else {
                            clone.add(clone.DAY_OF_MONTH,
                                      daysInCurrentMonth - today +
                                      daysInNextMonth);
                        }
                    } else {
                        int daysInPreviousMonth = cloneBreakDown.
                            getDaysInEthiopianMonth(cloneBreakDown.
                            getPreviousMonth());
                        if(today <= daysInPreviousMonth) {
                            clone.add(clone.DAY_OF_MONTH, -daysInPreviousMonth);
                        } else {
                            clone.add(clone.DAY_OF_MONTH,
                                      -today - daysInPreviousMonth);
                        }
                    }
                    count--;
                }
            }
        } else if(field == clone.WEEK_OF_YEAR) {
            clone.add(clone.WEEK_OF_MONTH, count);
        } else if(field == clone.DAY_OF_MONTH) {
            clone.add(clone.DAY_OF_MONTH, count);
        }
        return clone;
    }

    private void setRollerTimestamps(GregorianCalendar baseDate, int type,
                                     int field, HttpServletRequest request) throws CalendarException, WorkerException {
        // left roller
        GregorianCalendar leftDate = add(baseDate, type, field, -1,request);
        setLeftTimestamp((int) (leftDate.getTimeInMillis() / 1000));
        // right roller
        GregorianCalendar rightDate = add(baseDate, type, field, 1,request);
        setRightTimestamp((int) (rightDate.getTimeInMillis() / 1000));
    }

    private List yearNavigatorItems(GregorianCalendar baseDate, int type, HttpServletRequest request) throws
        CalendarException, WorkerException {
        List items = new ArrayList();

        DateBreakDown baseDateBreakDown = new DateBreakDown(baseDate, type, request);
        int currentMonth = baseDateBreakDown.getMonth();
        List row = null;
        int lastMonth = type == CalendarOptions.CALENDAR_TYPE_GREGORIAN ? 12 :
            13;
        for(int i = 1; i <= lastMonth; i++) {
            if(row == null) {
                row = new ArrayList();
            }
            int monthOffset = i - currentMonth;
            DateNavigatorItem item = new DateNavigatorItem();
            item.setEnabled(true);
            if(monthOffset != 0) {
                GregorianCalendar clone = add(baseDate, type, baseDate.MONTH,
                                              monthOffset,request);
                DateBreakDown cloneBreakDown = new DateBreakDown(clone, type, request);
                item.setMonth(cloneBreakDown.getMonthNameShort());
                item.setTimestamp((int) (clone.getTimeInMillis() / 1000));
                item.setDateBreakDown(cloneBreakDown);
            } else {
                item.setMonth(baseDateBreakDown.getMonthNameShort());
                item.setTimestamp((int) (baseDate.getTimeInMillis() / 1000));
                item.setDateBreakDown(baseDateBreakDown);
                item.setSelected(true);
                item.setNolink(true);
            }
            row.add(item);
            if(i % 4 == 0 || i == lastMonth) {
                items.add(row);
                row = null;
            }
        }
        return items;
    }

    private List monthNavigatorItems(GregorianCalendar baseDate, int type,
                                     String view, HttpServletRequest request) throws CalendarException, WorkerException {
        List items = new ArrayList();

        DateBreakDown baseDateBreakDown = new DateBreakDown(baseDate, type, request);
        int dayDiff = baseDateBreakDown.getDayOfMonth() - 1;
        GregorianCalendar calendar = (GregorianCalendar) baseDate.clone();
        calendar.add(calendar.DAY_OF_MONTH, -dayDiff);
        DateBreakDown calendarBreakDown = new DateBreakDown(calendar, type, request);
        if(calendarBreakDown.getDayOfWeek() > calendar.MONDAY) {
            dayDiff += calendarBreakDown.getDayOfWeek() - calendar.MONDAY;
        } else if(calendarBreakDown.getDayOfWeek() == calendar.SUNDAY) {
            dayDiff += 6;
        } else {
            dayDiff = 0;
        }
        if(dayDiff > 0) {
            calendar = (GregorianCalendar) baseDate.clone();
            calendar.add(calendar.DAY_OF_MONTH, -dayDiff);
            calendarBreakDown = new DateBreakDown(calendar, type, request);
        }
        int counter = 0;
        List row = null;
        while(true) {
            GregorianCalendar clone = (GregorianCalendar) calendar.clone();
            DateBreakDown cloneBreakDown = new DateBreakDown(clone, type, request);
            if(counter == 0) {
                row = new ArrayList();
            }
            DateNavigatorItem item = new DateNavigatorItem();
            item.setMonth(cloneBreakDown.getMonthNameShort());
            item.setDayOfMonth(cloneBreakDown.getDayOfMonth());
            item.setDayOfWeek(cloneBreakDown.getDayOfWeekName());
            item.setTimestamp((int) (clone.getTimeInMillis() / 1000));
            item.setDateBreakDown(cloneBreakDown);
            if(cloneBreakDown.getMonth() == baseDateBreakDown.getMonth()) {
                item.setEnabled(true);
            }
            if(cloneBreakDown.getDayOfYear() == baseDateBreakDown.getDayOfYear()) {
                item.setNolink(true);
                item.setSelected(true);
            }
            if(view.equals(CalendarOptions.CALENDAR_VIEW_WEEKLY) &&
               clone.get(clone.WEEK_OF_YEAR) ==
               baseDate.get(baseDate.WEEK_OF_YEAR)) {
                item.setSelected(true);
            }
            row.add(item);
            if(view.equals(CalendarOptions.CALENDAR_VIEW_DAYLY)) {
                // create subTimestamps array
                int zeroTimestamp = AmpUtil.getNavigatorItemLeftTimestamp(
                    cloneBreakDown, view);
                int[] subItemLeftTimestamps = new int[24];
                int[] subItemRightTimestamps = new int[24];
                for(int i = 0; i < 24; i++) {
                    subItemLeftTimestamps[i] = zeroTimestamp + i * 3600;
                    subItemRightTimestamps[i] = zeroTimestamp + (i + 1) * 3600 - 1;
                }
                item.setSubItemLeftTimestamp(subItemLeftTimestamps);
                item.setSubItemRightTimestamp(subItemRightTimestamps);
            }
            calendar.add(calendar.DAY_OF_MONTH, 1);
            calendarBreakDown = new DateBreakDown(calendar, type, request);
            if(counter == 6) {
                items.add(row);
                if(cloneBreakDown.getMonth() != baseDateBreakDown.getMonth() ||
                   calendarBreakDown.getMonth() != baseDateBreakDown.getMonth()) {
                    break;
                } else {
                    counter = 0;
                    continue;
                }
            }
            counter++;
        }
        return items;
    }

    public DateNavigator(GregorianCalendar baseDate, int type, String view, HttpServletRequest request) throws
        CalendarException, WorkerException {
        if(view.equals(CalendarOptions.CALENDAR_VIEW_YEARLY) || view.equals(CalendarOptions.CALENDAR_VIEW_NONE)) {
            // rollers
            setRollerTimestamps(baseDate, type, baseDate.YEAR,request);
            // items
            setItems(yearNavigatorItems(baseDate, type, request));
        } else if(view.equals(CalendarOptions.CALENDAR_VIEW_MONTHLY)) {
            // rollers
            setRollerTimestamps(baseDate, type, baseDate.MONTH,request);
            // items
            setItems(monthNavigatorItems(baseDate, type, view, request));
        } else if(view.equals(CalendarOptions.CALENDAR_VIEW_WEEKLY)) {
            // rollers
            setRollerTimestamps(baseDate, type, baseDate.WEEK_OF_YEAR,request);
            // items
            setItems(monthNavigatorItems(baseDate, type, view, request));
        } else if(view.equals(CalendarOptions.CALENDAR_VIEW_DAYLY)) {
            // rollers
            setRollerTimestamps(baseDate, type, baseDate.DAY_OF_MONTH,request);
            // items
            setItems(monthNavigatorItems(baseDate, type, view, request));
        }
    }

    public DateNavigator(GregorianCalendar startDate, GregorianCalendar endDate) throws
        CalendarException {
        DateNavigatorItem item = new DateNavigatorItem();
        int startDateTimestamp = (int) (startDate.getTimeInMillis() / 1000);
        int endDateTimestamp = (int) (endDate.getTimeInMillis() / 1000);
        int intervalLength = (endDateTimestamp - startDateTimestamp) / 4;
        int[] subItemLeftTimestamps = new int[4];
        int[] subItemRightTimestamps = new int[4];
        for (int i = 0; i < 4; i++) {
            subItemLeftTimestamps[i] = startDateTimestamp + i * intervalLength;
            subItemRightTimestamps[i] = subItemLeftTimestamps[i] + intervalLength - 1;
        }
        item.setSubItemLeftTimestamp(subItemLeftTimestamps);
        item.setSubItemRightTimestamp(subItemRightTimestamps);
        List row = new ArrayList();
        row.add(item);
        List items = new ArrayList();
        items.add(row);
        setItems(items);
    }

    public static GregorianCalendar getStartDate(String view,
                                                 GregorianCalendar baseDate) {
        GregorianCalendar clone = (GregorianCalendar) baseDate.clone();
        if(view.equals(CalendarOptions.CALENDAR_VIEW_YEARLY)) {
            clone.set(clone.MONTH, clone.getActualMinimum(clone.MONTH));
            clone.set(clone.DAY_OF_MONTH,
                      clone.getActualMinimum(clone.DAY_OF_MONTH));
        } else if(view.equals(CalendarOptions.CALENDAR_VIEW_MONTHLY)) {
            clone.set(clone.DAY_OF_MONTH,
                      clone.getActualMinimum(clone.DAY_OF_MONTH));
        } else if(view.equals(CalendarOptions.CALENDAR_VIEW_WEEKLY)) {
            clone.set(clone.DAY_OF_WEEK, clone.MONDAY);
        }
        clone.set(clone.HOUR_OF_DAY, clone.getActualMinimum(clone.HOUR_OF_DAY));
        clone.set(clone.MINUTE, clone.getActualMinimum(clone.MINUTE));
        clone.set(clone.SECOND, clone.getActualMinimum(clone.SECOND));
        clone.set(clone.MILLISECOND, clone.getActualMinimum(clone.MILLISECOND));
        return(clone);
    }

    public static GregorianCalendar getEndDate(String view,
                                               GregorianCalendar baseDate) {
        GregorianCalendar clone = (GregorianCalendar) baseDate.clone();
        if(view.equals(CalendarOptions.CALENDAR_VIEW_YEARLY)) {
            clone.set(clone.MONTH, clone.getActualMaximum(clone.MONTH));
            clone.set(clone.DAY_OF_MONTH,
                      clone.getActualMaximum(clone.DAY_OF_MONTH));
        } else if(view.equals(CalendarOptions.CALENDAR_VIEW_MONTHLY)) {
            clone.set(clone.DAY_OF_MONTH,
                      clone.getActualMaximum(clone.DAY_OF_MONTH));
        } else if(view.equals(CalendarOptions.CALENDAR_VIEW_WEEKLY)) {
            clone.set(clone.DAY_OF_WEEK, clone.SUNDAY);
        }
        clone.set(clone.HOUR_OF_DAY, clone.getActualMaximum(clone.HOUR_OF_DAY));
        clone.set(clone.MINUTE, clone.getActualMaximum(clone.MINUTE));
        clone.set(clone.SECOND, clone.getActualMaximum(clone.SECOND));
        clone.set(clone.MILLISECOND, clone.getActualMaximum(clone.MILLISECOND));
        return(clone);
    }

    public static List getCalendarTypes() throws CalendarException {
        List calendarTypesList = new ArrayList();
        for(int type = CalendarOptions.CALENDAR_TYPE_GREGORIAN;
            type <= CalendarOptions.CALENDAR_TYPE_ETHIOPIAN_FY; type++) {
            LabelValueBean lvb = new LabelValueBean(CalendarOptions.
                CALENDAR_TYPE_NAME[type], String.valueOf(type));
            calendarTypesList.add(lvb);
        }
        return calendarTypesList;
    }
}
