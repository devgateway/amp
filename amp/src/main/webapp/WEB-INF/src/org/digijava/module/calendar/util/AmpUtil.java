package org.digijava.module.calendar.util;

import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.digijava.kernel.util.collections.CollectionSynchronizer;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.entity.AmpCalendarGraph;
import org.digijava.module.calendar.entity.AmpCalendarGraphItem;
import org.digijava.module.calendar.entity.CalendarOptions;
import org.digijava.module.calendar.entity.DateBreakDown;
import org.digijava.module.calendar.entity.DateNavigator;
import org.digijava.module.calendar.entity.DateNavigatorItem;
import org.digijava.module.calendar.exception.CalendarException;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

public class AmpUtil {
    public static CollectionSynchronizer attendeeSyncronizer = new
        AttendeeSyncronizer();

    private final static Collator collator = Collator.getInstance();
    public final static Comparator<String> CharUnicodeComparator = new Comparator<String>() {
        public final int compare(String ch1, String ch2) {
            if (Character.isDigit(ch1.charAt(0)) && Character.isLetter(ch2.charAt(0))) {
                return -1;
            } else if (Character.isDigit(ch2.charAt(0)) && Character.isLetter(ch1.charAt(0)) ) {
                return 1;
            }
            return collator.compare(ch1, ch2);
        }
    };

    static {
        collator.setStrength(Collator.TERTIARY);
    }

    public static String SimpleEthipianToGregorian(String date, CalendarConversor convert){
        
        Integer ethday = Integer.parseInt(date.substring(0,2)); 
        Integer ethmonth = Integer.parseInt(date.substring(3,5));
        Integer ethyear = Integer.parseInt(date.substring(6,10));
        String ethtime = date.substring(11,16);
        Integer gregorianday =  convert.toGregorian(ethmonth,ethday,ethyear).getGregorianDate();
        Integer gregorianmonth =  convert.toGregorian(ethmonth,ethday,ethyear).getGregorianMonth();
        Integer gregorianyear =  convert.toGregorian(ethmonth,ethday,ethyear).getGregorianYear();
        
        String result = gregorianday.toString() + "/" + gregorianmonth.toString() + "/" + gregorianyear.toString()+ " " + ethtime;
        return result;
        
    }
    /***
     * 
     * @param date 2009-10-08 09:00:00.0
     * @param convert
     * @return
     */
public static Long SimpleGregorianToEthiopian(String date, CalendarConversor convert){
        
        Integer greday = Integer.parseInt(date.substring(8,10)); 
        Integer gremonth = Integer.parseInt(date.substring(5,7));
        Integer greyear = Integer.parseInt(date.substring(0,4));
        String gretime = date.substring(11,16);
        Integer ethiopianday =  convert.toEthiopian(gremonth,greday,greyear).getDate();
        Integer ethiopianmonth = convert.toEthiopian(gremonth,greday,greyear).getMonth();
        Integer ethiopianyear =  convert.toEthiopian(gremonth,greday,greyear).getYear();
        String dtformat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(dtformat);
        
        String result = ethiopianday.toString() + "/" + ethiopianmonth.toString() + "/" + ethiopianyear.toString()+ " " + gretime;
        try {
            return sdf.parse(result).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

public static Date SimpleDateGregorianToEthiopian(String date, CalendarConversor convert){
    
    Integer greday = Integer.parseInt(date.substring(0,2)); 
    Integer gremonth = Integer.parseInt(date.substring(3,5));
    Integer greyear = Integer.parseInt(date.substring(6,10));
    String gretime = date.substring(11,16);
    Integer ethiopianday =  convert.toEthiopian(gremonth,greday,greyear).getDate();
    Integer ethiopianmonth = convert.toEthiopian(gremonth,greday,greyear).getMonth();
    Integer ethiopianyear =  convert.toEthiopian(gremonth,greday,greyear).getYear();
    String dtformat = "dd/MM/yyyy HH:mm";
    SimpleDateFormat sdf = new SimpleDateFormat(dtformat);
    
    String result = ethiopianday.toString() + "/" + ethiopianmonth.toString() + "/" + ethiopianyear.toString()+ " " + gretime;
    
    try {
        return sdf.parse(result);
    } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return null;
}
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
            Collection allDonors = DbUtil.getAmpOrganisations();
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
                                            String view, int calendartype) throws
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
                    item.getSubItemLeftTimestamp(),item.getSubItemRightTimestamp(),calendartype);
            } else {
                ampCalendarGraph = AmpUtil.getAmpCalendarGraph(ampCalendar,
                    navigatorItems, view,calendartype);
            }
            if(ampCalendarGraph != null) {
                result.add(ampCalendarGraph);
            }
        }
        return result;
    }

    private static AmpCalendarGraph getAmpCalendarGraph(AmpCalendar ampCalendar,
        List navigatorItems, String view, int calendartype) throws CalendarException {
        AmpCalendarGraph ampCalendarGraph = new AmpCalendarGraph(ampCalendar);
        Calendar calendar = ampCalendar.getCalendarPK().getCalendar();
        int calendarStartTimestamp=0;
        int calendarEndTimestamp=0;
        
         String dtformat = "dd/MM/yyyy HH:mm";
         SimpleDateFormat sdf = new SimpleDateFormat(dtformat);
        
         if (calendartype == CalendarOptions.CALENDAR_TYPE_GREGORIAN){
             calendarStartTimestamp = (int) (calendar.getStartDate().getTime() /1000);
             calendarEndTimestamp = (int) (calendar.getEndDate().getTime() /1000);
        }else{
            CalendarConversor convert = new CalendarConversor(Integer.parseInt(calendar.getStartDate().toString().substring(0,4)));
            calendar.setStartDate(AmpUtil.SimpleDateGregorianToEthiopian(sdf.format(calendar.getStartDate()).toString(), convert));
            calendar.setEndDate(AmpUtil.SimpleDateGregorianToEthiopian(sdf.format(calendar.getEndDate()).toString(), convert));
         }
        Iterator it = navigatorItems.iterator();
        while(it.hasNext()) {
            DateNavigatorItem navigatorItem = (DateNavigatorItem) it.next();
            int itemStartTimestamp = getNavigatorItemLeftTimestamp(navigatorItem.getDateBreakDown(), view);
            int itemEndTimestamp = getNavigatorItemRightTimestamp(navigatorItem.getDateBreakDown(), view);
            String  eventTypeName=null;
            if(ampCalendar.getEventsType()!=null){
                AmpCategoryValue ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromDb(ampCalendar.getEventsType().getId());
                if (ampCategoryValue != null){
                    eventTypeName=ampCategoryValue.getValue();
                }
            }
            ampCalendarGraph.getGraphItems().add(getAmpCalendarGraphItem(eventTypeName, calendarStartTimestamp,
                calendarEndTimestamp, itemStartTimestamp, itemEndTimestamp));
        }
        return ampCalendarGraph;
    }

 static AmpCalendarGraph getAmpCalendarGraph(AmpCalendar ampCalendar,
        int[] subItemLeftTimestamps, int[] subItemRightTimestamp,int calendartype ) throws
        CalendarException {
        AmpCalendarGraph ampCalendarGraph = new AmpCalendarGraph(ampCalendar);
        Calendar calendar = ampCalendar.getCalendarPK().getCalendar();
        
        int calendarStartTimestamp=0; 
        int calendarEndTimestamp=0;
        
        String dtformat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(dtformat);
    
        if (calendartype == CalendarOptions.CALENDAR_TYPE_GREGORIAN){
         calendarStartTimestamp = (int) (calendar.getStartDate().getTime() /1000);
         calendarEndTimestamp = (int) (calendar.getEndDate().getTime() /1000);
       }else{
        CalendarConversor convert = new CalendarConversor(Integer.parseInt(calendar.getStartDate().toString().substring(0,4)));
        calendar.setStartDate(AmpUtil.SimpleDateGregorianToEthiopian(sdf.format(calendar.getStartDate()).toString(), convert));
        calendar.setEndDate(AmpUtil.SimpleDateGregorianToEthiopian(sdf.format(calendar.getEndDate()).toString(), convert));
         }
        
        for(int i = 0; i < subItemLeftTimestamps.length; i++) {
            String  eventTypeName=null;
            if(ampCalendar.getEventsType()!=null){
                AmpCategoryValue ampCategoryValue = CategoryManagerUtil.getAmpCategoryValueFromDb(ampCalendar.getEventsType().getId());
                if (ampCategoryValue != null){
                    eventTypeName=ampCategoryValue.getValue();
                }
            }
            
            ampCalendarGraph.getGraphItems().add(getAmpCalendarGraphItem(eventTypeName, calendarStartTimestamp,calendarEndTimestamp, subItemLeftTimestamps[i],
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
