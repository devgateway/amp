package org.digijava.module.calendar.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.digijava.kernel.util.collections.CollectionSynchronizer;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.dbentity.AmpEventType;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.entity.AmpCalendarGraph;
import org.digijava.module.calendar.entity.AmpCalendarGraphItem;
import org.digijava.module.calendar.entity.CalendarOptions;
import org.digijava.module.calendar.entity.DateBreakDown;
import org.digijava.module.calendar.entity.DateNavigator;
import org.digijava.module.calendar.entity.DateNavigatorItem;
import org.digijava.module.calendar.exception.CalendarException;

public class AmpUtil {
    public static CollectionSynchronizer attendeeSyncronizer = new
        AttendeeSyncronizer();

    public static Set getSelectedItems(Collection collection, String[] ids,
                                       BeanIdResolver resolver) {
        Set selectedItems = new HashSet();
        if(collection == null || collection.isEmpty() || ids == null ||
           ids.length == 0) {
            return selectedItems;
        }
        List idList = Arrays.asList(ids);
        Iterator it = collection.iterator();
        while(it.hasNext()) {
            Object o = it.next();
            String id = resolver.getId(o);
            if(idList.contains(id)) {
                selectedItems.add(o);
            }
        }
        return selectedItems;
    }

    public static Set getSelectedDonors(Collection donors, String[] donorIds) {
        Set result = new HashSet();
        try {
            if(donorIds == null || donorIds.length == 0) {
                return result;
            }
            BeanIdResolver resolver = new BeanIdResolver() {
                public String getId(Object o) {
                    return((AmpOrganisation) o).getAmpOrgId().toString();
                }
            };
            result = getSelectedItems(donors, donorIds, resolver);
        } catch(Exception ex) {

        }
        return result;
    }

    public static Set getSelectedDonors(String[] donorIds) {
        Set result = new HashSet();
        try {
//            Collection allDonors = org.digijava.module.aim.util.DbUtil.
//                getDonors();
            Collection allDonors = DbUtil.getAmpOrganisations(true);
            result = getSelectedDonors(allDonors, donorIds);
        } catch(Exception ex) {

        }
        return result;
    }

    public static Set getSelectedAttendeeMembers(AmpCalendar ampCalendar, String[] memberIds) {
        Set result = new HashSet();
        try {
            if(memberIds == null || memberIds.length == 0) {
                return result;
            }
            Collection<AmpTeamMember> members = TeamMemberUtil.getAllTeamMembers();
            BeanIdResolver resolver = new BeanIdResolver() {
                public String getId(Object o) {
                    return((AmpTeamMember) o).getAmpTeamMemId().toString();
                }
            };
            Iterator it = getSelectedItems(members, memberIds, resolver).iterator();
            while(it.hasNext()) {
                result.add(new AmpCalendarAttendee(ampCalendar, (AmpTeamMember) it.next()));
            }
        } catch(Exception ex) {

        }
        return result;
    }

    public static Set getSelectedAttendeeTeams(AmpCalendar ampCalendar,String[] teamIds) {
        Set result = new HashSet();
        try {
            if(teamIds == null || teamIds.length == 0) {
                return result;
            }
            Collection<AmpTeam> teams = TeamUtil.getAllTeams();
            BeanIdResolver resolver = new BeanIdResolver() {
                public String getId(Object o) {
                    return((AmpTeam) o).getAmpTeamId().toString();
                }
            };
            Iterator it = getSelectedItems(teams, teamIds, resolver).iterator();
            while(it.hasNext()) {
                result.add(new AmpCalendarAttendee(ampCalendar, (AmpTeam) it.next()));
            }
        } catch(Exception ex) {

        }
        return result;
    }

    public static Set getSelectedAttendeeGuests(AmpCalendar ampCalendar,String[] guestIds) {
        Set result = new HashSet();
        try {
            if(guestIds == null && guestIds.length == 0) {
                return result;
            }
            for(int i = 0; i < guestIds.length; i++) {
                result.add(new AmpCalendarAttendee(ampCalendar, guestIds[i]));
            }
        } catch(Exception ex) {

        }
        return result;
    }

    public static List getAmpCalendarGraphs(Collection ampCalendarEvents,
                                            DateNavigator navigator,
                                            String view) throws
        CalendarException {
        List result = new ArrayList();
        if(ampCalendarEvents == null || ampCalendarEvents.isEmpty() ||
           navigator == null || navigator.getItems() == null ||
           navigator.getItems().isEmpty()) {
            return result;
        }
        List navigatorItems = new ArrayList();
        Iterator it = navigator.getItems().iterator();
        while(it.hasNext()) {
            List row = (List) it.next();
            if(view.equals(CalendarOptions.CALENDAR_VIEW_WEEKLY) ||
               view.equals(CalendarOptions.CALENDAR_VIEW_DAYLY)) {
                Iterator itemIt = row.iterator();
                while(itemIt.hasNext()) {
                    DateNavigatorItem item = (DateNavigatorItem) itemIt.next();
                    if(item.isSelected()) {
                        navigatorItems.add(item);
                    }
                }
            } else {
                navigatorItems.addAll(row);
            }
        }
        DateNavigatorItem item = null;
        if(view.equals(CalendarOptions.CALENDAR_VIEW_DAYLY) ||
           view.equals(CalendarOptions.CALENDAR_VIEW_CUSTOM)) {
            item = (DateNavigatorItem) navigatorItems.get(0);
        }
        it = ampCalendarEvents.iterator();
        while(it.hasNext()) {
            AmpCalendar ampCalendar = (AmpCalendar) it.next();
            AmpCalendarGraph ampCalendarGraph;
            if(view.equals(CalendarOptions.CALENDAR_VIEW_DAYLY) ||
               view.equals(CalendarOptions.CALENDAR_VIEW_CUSTOM)) {
                ampCalendarGraph = AmpUtil.getAmpCalendarGraph(ampCalendar,
                    item.getSubItemLeftTimestamp(),
                    item.getSubItemRightTimestamp());
            } else {
                ampCalendarGraph = AmpUtil.getAmpCalendarGraph(ampCalendar,
                    navigatorItems, view);
            }
            if(ampCalendarGraph != null) {
                result.add(ampCalendarGraph);
            }
        }
        return result;
    }

    private static AmpCalendarGraph getAmpCalendarGraph(AmpCalendar ampCalendar,
        List navigatorItems, String view) throws CalendarException {
        AmpCalendarGraph ampCalendarGraph = new AmpCalendarGraph(ampCalendar);
        Calendar calendar = ampCalendar.getCalendarPK().getCalendar();
        int calendarStartTimestamp = (int) (calendar.getStartDate().getTime() /
                                            1000);
        int calendarEndTimestamp = (int) (calendar.getEndDate().getTime() /
                                          1000);
        Iterator it = navigatorItems.iterator();
        while(it.hasNext()) {
            DateNavigatorItem navigatorItem = (DateNavigatorItem) it.next();
            int itemStartTimestamp = getNavigatorItemLeftTimestamp(
                navigatorItem.getDateBreakDown(), view);
            int itemEndTimestamp = getNavigatorItemRightTimestamp(navigatorItem.
                getDateBreakDown(), view);
            ampCalendarGraph.getGraphItems().add(getAmpCalendarGraphItem(
                ampCalendar.getEventType().getColor(), calendarStartTimestamp,
                calendarEndTimestamp, itemStartTimestamp, itemEndTimestamp));
        }
        return ampCalendarGraph;
    }

    public static void deleteEventType(Long eventTypeId) throws CalendarException{
        AmpEventType eventType=AmpDbUtil.getEventType(eventTypeId);
        AmpDbUtil.deleteEventType(eventType);
    }

    public static void createEventType(String name,String color) throws CalendarException{
        AmpEventType newEventType=new AmpEventType();
        newEventType.setName(name);
        newEventType.setColor(color);
        AmpDbUtil.saveEventType(newEventType);
    }

    private static AmpCalendarGraph getAmpCalendarGraph(AmpCalendar ampCalendar,
        int[] subItemLeftTimestamps, int[] subItemRightTimestamp) throws
        CalendarException {
        AmpCalendarGraph ampCalendarGraph = new AmpCalendarGraph(ampCalendar);
        Calendar calendar = ampCalendar.getCalendarPK().getCalendar();
        int calendarStartTimestamp = (int) (calendar.getStartDate().getTime() /
                                            1000);
        int calendarEndTimestamp = (int) (calendar.getEndDate().getTime() /
                                          1000);
        for(int i = 0; i < subItemLeftTimestamps.length; i++) {
            ampCalendarGraph.getGraphItems().add(getAmpCalendarGraphItem(
                ampCalendar.getEventType().getColor(), calendarStartTimestamp,
                calendarEndTimestamp, subItemLeftTimestamps[i],
                subItemRightTimestamp[i]));
        }
        return ampCalendarGraph;
    }

    private static AmpCalendarGraphItem getAmpCalendarGraphItem(String color,
        int calendarStartTimestamp, int calendarEndTimestamp,
        int itemStartTimestamp, int itemEndTimestamp) {
        long a, b, c;
        if(calendarStartTimestamp > itemEndTimestamp ||
           calendarEndTimestamp < itemStartTimestamp) {
            a = 50;
            b = 0;
            c = 50;
        } else if(itemStartTimestamp <= calendarStartTimestamp &&
                  calendarStartTimestamp <= itemEndTimestamp &&
                  calendarEndTimestamp > itemEndTimestamp) {
            a = (calendarStartTimestamp - itemStartTimestamp) * 100L /
                (itemEndTimestamp - itemStartTimestamp);
            b = 100 - a;
            c = 0;
        } else if(itemStartTimestamp <= calendarEndTimestamp &&
                  calendarEndTimestamp <= itemEndTimestamp &&
                  calendarStartTimestamp < itemStartTimestamp) {
            a = 0;
            b = (calendarEndTimestamp - itemStartTimestamp) * 100L /
                (itemEndTimestamp - itemStartTimestamp);
            c = 100 - b;
        } else if(itemStartTimestamp <= calendarStartTimestamp &&
                  calendarStartTimestamp <= itemEndTimestamp &&
                  itemStartTimestamp <= calendarEndTimestamp &&
                  calendarEndTimestamp <= itemEndTimestamp) {
            a = (calendarStartTimestamp - itemStartTimestamp) * 100L /
                (itemEndTimestamp - itemStartTimestamp);
            b = (calendarEndTimestamp - calendarStartTimestamp) * 100L /
                (itemEndTimestamp - itemStartTimestamp);
            c = 100 - a - b;
        } else {
            a = 0;
            b = 100;
            c = 0;
        }
        return new AmpCalendarGraphItem(color, (int) a, (int) b, (int) c);
    }

    public static int getNavigatorItemLeftTimestamp(DateBreakDown
        dateBreakDown, String view) {
        int result;
        try {
            int type = dateBreakDown.getType();
            GregorianCalendar calendar = DateBreakDown.
                createValidGregorianCalendar(type,
                                             dateBreakDown.formatDateString(),
                                             dateBreakDown.formatTimeString());
            DateBreakDown clone = new DateBreakDown(calendar, type);
            if(view.equals(CalendarOptions.CALENDAR_VIEW_YEARLY)) {
                clone.setDayOfMonth(calendar.getActualMinimum(calendar.
                    DAY_OF_MONTH));
            }
            clone.setHour(calendar.getActualMinimum(calendar.HOUR));
            clone.setMinute(calendar.getActualMinimum(calendar.MINUTE));
            result = (int) (clone.getGregorianCalendar().getTimeInMillis() /
                            1000);
        } catch(Exception ex) {
            result = (int) ((new GregorianCalendar()).getTimeInMillis() / 1000);
        }
        return result;
    }

    public static int getNavigatorItemRightTimestamp(DateBreakDown
        dateBreakDown, String view) {
        int result;
        try {
            int type = dateBreakDown.getType();
            GregorianCalendar calendar = DateBreakDown.
                createValidGregorianCalendar(type,
                                             dateBreakDown.formatDateString(),
                                             dateBreakDown.formatTimeString());
            DateBreakDown clone = new DateBreakDown(calendar,
                dateBreakDown.getType());

            if(view.equals(CalendarOptions.CALENDAR_VIEW_YEARLY)) {
                if(type == CalendarOptions.CALENDAR_TYPE_GREGORIAN) {
                    clone.setDayOfMonth(calendar.getActualMaximum(calendar.
                        DAY_OF_MONTH));
                } else {
                    clone.setDayOfMonth(dateBreakDown.getDaysInEthiopianMonth());
                }
            }
            clone.setHour(calendar.getActualMaximum(calendar.HOUR));
            clone.setMinute(calendar.getActualMaximum(calendar.MINUTE));
            result = (int) (clone.getGregorianCalendar().getTimeInMillis() /
                            1000);
        } catch(Exception ex) {
            result = (int) ((new GregorianCalendar()).getTimeInMillis() / 1000);
        }
        return result;
    }
}

class AttendeeSyncronizer
    implements CollectionSynchronizer {
    // removeEvent
    public boolean removeEvent(Object o) {
        AmpCalendarAttendee attendee = (AmpCalendarAttendee) o;
        attendee.setId(new Long( -attendee.getId().longValue()));
        return false;
    }

    // synchronizeEvent
    public boolean synchronizeEvent(Object o1, Object o2) {
        AmpCalendarAttendee me = (AmpCalendarAttendee) o1;
        AmpCalendarAttendee other = (AmpCalendarAttendee) o2;
        if(me != other) {
            me.setAmpCalendar(other.getAmpCalendar());
            me.setMember(other.getMember());
            me.setTeam(other.getTeam());
            me.setGuest(other.getGuest());
        }
        return true;
    }

    // compare
    public int compare(Object a, Object b) {
        int result;
        AmpCalendarAttendee A = (AmpCalendarAttendee) a;
        AmpCalendarAttendee B = (AmpCalendarAttendee) b;
        // compare by calendar id
        Long calendarIdA = A != null && A.getAmpCalendar() != null &&
            A.getAmpCalendar().getCalendarPK() != null &&
            A.getAmpCalendar().getCalendarPK().getCalendar() != null &&
            A.getAmpCalendar().getCalendarPK().getCalendar().getId() != null ?
            A.getAmpCalendar().getCalendarPK().getCalendar().getId() :
            new Long(0);
        Long calendarIdB = B != null && B.getAmpCalendar() != null &&
            B.getAmpCalendar().getCalendarPK() != null &&
            B.getAmpCalendar().getCalendarPK().getCalendar() != null &&
            B.getAmpCalendar().getCalendarPK().getCalendar().getId() != null ?
            B.getAmpCalendar().getCalendarPK().getCalendar().getId() :
            new Long(0);
        result = calendarIdA.compareTo(calendarIdB);
        if(result != 0) {
            return result;
        }
        // compare by user id
        Long userIdA = A != null && A.getMember() != null && A.getMember().getAmpTeamMemId() != null ?
            A.getMember().getAmpTeamMemId() : new Long(0);
        Long userIdB = B != null && B.getMember() != null && B.getMember().getAmpTeamMemId() != null ?
            B.getMember().getAmpTeamMemId() : new Long(0);
        result = userIdA.compareTo(userIdB);
        if(result != 0) {
            return result;
        }
        // compare by guest
        String guestA = A != null && A.getGuest() != null ? A.getGuest() : "";
        String guestB = B != null && B.getGuest() != null ? B.getGuest() : "";
        return guestA.compareTo(guestB);
    }
}
