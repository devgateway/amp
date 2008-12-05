package org.digijava.module.calendar.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.dbentity.AmpCalendarPK;
import org.digijava.module.calendar.dbentity.AmpEventType;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.exception.CalendarException;

public class AmpDbUtil {
  private static Logger logger = Logger.getLogger(AmpDbUtil.class);

  public static List getEventTypes() throws CalendarException {
    try {
      Session session = PersistenceManager.getRequestDBSession();
      String queryString = "from " + AmpEventType.class.getName() +
          " order by id";
      Query query = session.createQuery(queryString);
      return query.list();
    }
    catch (Exception e) {
      logger.debug("Unable to get AmpEventType's from database", e);
      throw new CalendarException(
          "Unable to get AmpEventType's from database", e);
    }
  }

  public static void updateEventType(AmpEventType eventType) throws
      CalendarException {
    Transaction tx = null;
    try {
      Session session = PersistenceManager.getRequestDBSession();
      tx = session.beginTransaction();
      session.update(eventType);
      tx.commit();
    }
    catch (Exception ex) {
      if (tx != null) {
        try {
          tx.rollback();
        }
        catch (Exception ex2) {
          throw new CalendarException("Cannot rallback EventType update", ex2);
        }
      }
      throw new CalendarException("Cannot update EventType", ex);
    }
  }

  public static void saveEventType(AmpEventType eventType) throws
      CalendarException {
    Transaction tx = null;
    try {
      Session session = PersistenceManager.getRequestDBSession();
      tx = session.beginTransaction();
      session.save(eventType);
      tx.commit();
    }
    catch (Exception ex) {
      if (tx != null) {
        try {
          tx.rollback();
        }
        catch (Exception ex2) {
          throw new CalendarException("Cannot rallback EventType update", ex2);
        }
      }
      throw new CalendarException("Cannot insert EventType", ex);
    }
  }

  public static void deleteEventType(AmpEventType eventType) throws
      CalendarException {
      Transaction tx = null;
      try {
          Collection calCol = getAmpCalendarsByEventType(eventType.getId());
          if (calCol != null) {
              for (Iterator calItr = calCol.iterator(); calItr.hasNext(); ) {
                  AmpCalendar item = (AmpCalendar) calItr.next();
                  item.setEventType(null);
                  updateAmpCalendar(item);
              }
          }

          Session session = PersistenceManager.getRequestDBSession();
          tx = session.beginTransaction();
          session.delete(eventType);

          tx.commit();
      } catch (Exception ex) {
          try {
              tx.rollback();
          } catch (Exception ex2) {
              throw new CalendarException("Cannot rallback EventType update", ex2);
          }
          throw new CalendarException("Cannot Delete or update EventType", ex);
      }
  }

  public static AmpEventType getEventType(Long id) throws CalendarException {
    try {
      Session session = PersistenceManager.getRequestDBSession();
      String queryString = "from " + AmpEventType.class.getName() +
          " et where et.id = :id";
      Query query = session.createQuery(queryString);
      query.setLong("id", id.longValue());
      return (AmpEventType) query.uniqueResult();
    }catch (Exception e) {
      logger.debug("Unable to get AmpEventType from database", e);
      throw new CalendarException(
          "Unable to get AmpEventType from database", e);
    }
  }

  public static List getUsers() throws CalendarException {
    try {
      Session session = PersistenceManager.getRequestDBSession();
      String queryString = "from " + User.class.getName();
      Query query = session.createQuery(queryString);
      return query.list();
    }
    catch (Exception e) {
      logger.debug("Unable to get users from database", e);
      throw new CalendarException("Unable to get users from database", e);
    }
  }

  public static Collection getAmpCalendarsByEventType(Long eventTypeId) {
    try {
        Session session = PersistenceManager.getRequestDBSession();
        String queryString = "from " + AmpCalendar.class.getName() +
            " et where et.EVENT_TYPE_ID = :eventTypeId";
        Query query = session.createQuery(queryString);
        query.setLong("eventTypeId", eventTypeId.longValue());

        return query.list();
    }
    catch (Exception e) {
      logger.debug("Unable to get AmpCalendar by Id", e);
      return null;
    }
  }

  public static AmpCalendar getAmpCalendar(Long ampCalendarId) {
    if (ampCalendarId == null) {
      return null;
    }
    try {
      Session session = PersistenceManager.getRequestDBSession();
      Calendar calendar = (Calendar) session.load(Calendar.class,ampCalendarId);

      AmpCalendarPK ampCalendarPK = new AmpCalendarPK(calendar);
      AmpCalendar ampCalendar = (AmpCalendar) session.load(AmpCalendar.class,ampCalendarPK);
      return ampCalendar;
    }
    catch (Exception e) {
      logger.debug("Unable to get AmpCalendar by Id", e);
      return null;
    }
  }

  public static List<AmpCalendar> getAmpCalendarsByStartDate(Date startDate) {
      if (startDate == null) {
          return null;
      }
      try {
          Session session = PersistenceManager.getRequestDBSession();

          List<AmpCalendar> ampCalLst = new ArrayList<AmpCalendar> ();

          List<Calendar> clst = getCalendarsByStartDate(startDate);
          for (Calendar cal : clst) {
              AmpCalendarPK ampCalendarPK = new AmpCalendarPK(cal);
              AmpCalendar ampCalendar = (AmpCalendar) session.load(AmpCalendar.class, ampCalendarPK);
              ampCalLst.add(ampCalendar);
          }
          return ampCalLst;
      } catch (Exception e) {
          logger.debug("Unable to get AmpCalendars by Start Date", e);
          return null;
      }
  }

  public static List<AmpCalendar> getAmpCalendarsByEndDate(Date endDate) {
      if (endDate == null) {
          return null;
      }
      try {
          Session session = PersistenceManager.getRequestDBSession();

          List<AmpCalendar> ampCalLst = new ArrayList<AmpCalendar> ();

          List<Calendar> clst = getCalendarsByEndDate(endDate);
          for (Calendar cal : clst) {
              AmpCalendarPK ampCalendarPK = new AmpCalendarPK(cal);
              AmpCalendar ampCalendar = (AmpCalendar) session.load(AmpCalendar.class, ampCalendarPK);
              ampCalLst.add(ampCalendar);
          }
          return ampCalLst;
      } catch (Exception e) {
          logger.debug("Unable to get Calendars by Start Date", e);
          return null;
      }
  }


  public static List<Calendar> getCalendarsByStartDate(Date startDate) {
    if(startDate == null) {
      return null;
    }
    try{
      Session session = PersistenceManager.getRequestDBSession();
      String queryString = "select c from "+Calendar.class.getName()+" c";
      Query query = session.createQuery(queryString);

      List<Calendar> exList=query.list();
      List<Calendar> rList=new ArrayList<Calendar>();

      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      String strStartDate=sdf.format(startDate);
      for(Calendar exCal:exList){
          String exStrStartDate=sdf.format(exCal.getStartDate());
          if(strStartDate.equals(exStrStartDate)){
              rList.add(exCal);
          }
      }
      return rList;
    }catch (Exception e) {
      logger.debug("Unable to get AmpCalendars by Start Date", e);
      return null;
    }
  }

  public static List<Calendar> getCalendarsByEndDate(Date endDate) {
    if(endDate == null) {
      return null;
    }
    try{
        Session session = PersistenceManager.getRequestDBSession();
        String queryString = "select c from "+Calendar.class.getName()+" c";
        Query query = session.createQuery(queryString);

        List<Calendar> exList=query.list();
        List<Calendar> rList=new ArrayList<Calendar>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String strEndDate=sdf.format(endDate);
        for(Calendar exCal:exList){
            String exStrEndDate=sdf.format(exCal.getEndDate());
            if(strEndDate.equals(exStrEndDate)){
                rList.add(exCal);
            }
        }
      return rList;
    }catch (Exception e) {
      logger.debug("Unable to get AmpCalendars by Start Date", e);
      return null;
    }
  }
  public static AmpCalendar getAmpCalendar(Long ampCalendarId,
                                           String instanceId, String siteId) {
    AmpCalendar ampCalendar = getAmpCalendar(ampCalendarId);
    Calendar calendar = ampCalendar != null ?
        ampCalendar.getCalendarPK().getCalendar() : null;
    if (calendar == null || calendar.getInstanceId() == null ||
        calendar.getSiteId() == null) {
      return null;
    }
    if (calendar.getInstanceId().equals(instanceId) &&
        calendar.getSiteId().equals(siteId)) {
      return ampCalendar;
    }
    else {
      return null;
    }
  }

  public static List<AmpCalendarAttendee> getAmpCalendarAttendees(AmpCalendar cal) {
      try {
          Session session = PersistenceManager.getRequestDBSession();
          String queryString = "from " + AmpCalendarAttendee.class.getName() +
              " att where att.CALENDAR_ID = :calendarId";
          Query query = session.createQuery(queryString);
          query.setLong("calendarId", cal.getCalendarPK().getCalendar().getId());

          return query.list();
      }
      catch (Exception e) {
        logger.debug("Unable to get AmpCalendarAttendees by AmpCalendar", e);
        return null;
    }
  }

  public static void deleteAmpCalendar(Long ampCalendarId) throws
      CalendarException {
    Transaction tx = null;
    try {
      Session session = PersistenceManager.getRequestDBSession();
      tx = session.beginTransaction();
      AmpCalendar cal = getAmpCalendar(ampCalendarId);
      session.delete(cal);
      tx.commit();
    }
    catch (Exception ex) {
      if (tx != null) {
        try {
          tx.rollback();
        }
        catch (Exception ex1) {
          throw new CalendarException(
              "Cannot rollback delete AMPCalendar", ex1);
        }
      }
      throw new CalendarException("Canot delete AMPCalendar", ex);
    }
  }

  public static void updateAmpCalendar(AmpCalendar ampCalendar) throws
      CalendarException {
    Transaction tx = null;
    try {
        Session session = PersistenceManager.getRequestDBSession();
        tx = session.beginTransaction();
        Calendar calendar = ampCalendar.getCalendarPK().getCalendar();
        // calendar
        if (calendar.getId() == null) {
            session.save(calendar);
            session.save(ampCalendar);
        } else {
            if (calendar.getCalendarItem() != null) {
                for (Iterator itemIter = calendar.getCalendarItem().iterator(); itemIter.hasNext(); ) {
                    CalendarItem item = (CalendarItem) itemIter.next();
                    if(item.getId()<0){
                        item.setId(-item.getId());
                        session.delete(item);
                        itemIter.remove();
                    }
                }
            }

            if (ampCalendar.getAttendees() != null) {
                for (Iterator attIter = ampCalendar.getAttendees().iterator(); attIter.hasNext(); ) {
                    AmpCalendarAttendee att = (AmpCalendarAttendee) attIter.next();
                    if(att.getId()<0){
                        att.setId(-att.getId());
                        session.delete(att);
                        attIter.remove();
                    }
                }
            }

            session.update(calendar);
            session.update(ampCalendar);
        }
        tx.commit();
    }
    catch (Exception e) {
      if (tx != null) {
        try {
          tx.rollback();
        }
        catch (Exception ex) {
          logger.warn("Unable to rollback AmpCalendar object", ex);
        }
      }
      logger.debug("Unable to update AmpCalendar object", e);
      throw new CalendarException("Unable to update AmpCalendar object",
                                  e);
    }
  }

  public static Collection<AmpCalendar> getAmpCalendarEventsByMember(GregorianCalendar startDate,
                                          GregorianCalendar endDate,
                                          String[] selectedEventTypeIds,
                                          String[] selectedDonorIds,
                                          AmpTeamMember curMember,
                                          boolean showPublicEvents,
                                          String instanceId, String siteId) throws
      CalendarException {
      try{
         // AmpTeam curMemTeam=curMember.getAmpTeam();

          Hashtable<Long, AmpCalendar> retEvents=new Hashtable<Long,AmpCalendar>();

          List events = getAmpCalendarEvents(startDate, endDate, selectedEventTypeIds, selectedDonorIds, Long.valueOf(0), showPublicEvents, instanceId, siteId);
          if (events != null) {
              for (Iterator eventItr = events.iterator(); eventItr.hasNext(); ) {
                  AmpCalendar ampCal = (AmpCalendar) eventItr.next();
                  if (ampCal.getMember() != null) {
                	  //AmpTeam calTeam = ampCal.getMember().getAmpTeam();
                	  
                	  //creator of the event should see this event
                	  if(ampCal.getMember().getAmpTeamMemId().equals(curMember.getAmpTeamMemId())){
                    		retEvents.put(ampCal.getCalendarPK().getCalendar().getId(), ampCal);
                	  } 
//                	  if(showPublicEvents){
//                		  retEvents.put(ampCal.getCalendarPK().getCalendar().getId(), ampCal);
//                		  continue;  		
//                	  }else{
//                		//If the event is not public
//                		if(ampCal.getAttendees()!=null && ampCal.getAttendees().size()>0){
//                        	for (Object attendee : ampCal.getAttendees()) {
//                          		AmpCalendarAttendee att=(AmpCalendarAttendee)attendee;
//                          		if(att.getMember()!=null && att.getMember().getAmpTeamMemId().equals(curMember.getAmpTeamMemId())){
//                          			retEvents.put(ampCal.getCalendarPK().getCalendar().getId(), ampCal);
//                          			continue;
//                          		}
//                          	}
//                		}
//                	  }
                	  
                	  /**
                	   * if showPublicEvents=true, then public and private events should show up on the page. (AMP-4860)
                	   * In this case , everyone should see public events, but if event is private, 
                	   * then it should show up only if it's attendee is current user.
                	   */
                	  if(showPublicEvents && !ampCal.isPrivateEvent()){
                		  retEvents.put(ampCal.getCalendarPK().getCalendar().getId(), ampCal);
            			  continue;
                	  }
                	  if(!showPublicEvents || (showPublicEvents && ampCal.isPrivateEvent())){
                		//If the event is not public
                  		if(ampCal.getAttendees()!=null && ampCal.getAttendees().size()>0){
                          	for (Object attendee : ampCal.getAttendees()) {
                            		AmpCalendarAttendee att=(AmpCalendarAttendee)attendee;
                            		if(att.getMember()!=null && att.getMember().getAmpTeamMemId().equals(curMember.getAmpTeamMemId())){
                            			retEvents.put(ampCal.getCalendarPK().getCalendar().getId(), ampCal);
                            			continue;
                            		}
                            	}
                  		}
                	  }
                     
//                      if (calTeam != null && calTeam.getAmpTeamId().equals(curMemTeam.getAmpTeamId())) {
//                          retEvents.put(ampCal.getCalendarPK().getCalendar().getId(), ampCal);
//                          continue;
//                      }
//                      if (ampCal.getAttendees() != null) {
//                          for (Iterator attItr = ampCal.getAttendees().iterator(); attItr.hasNext(); ) {
//                              AmpCalendarAttendee att = (AmpCalendarAttendee) attItr.next();
//                              AmpTeamMember member = null;
//                              if (att.getMember() != null) {
//                                  member = att.getMember();
//                              }
//                              if (member != null &&
//                                  member.getAmpTeamMemId().equals(curMember.getAmpTeamMemId()) &&
//                                  !retEvents.containsKey(ampCal.getCalendarPK().getCalendar().getId())) {
//                                  retEvents.put(ampCal.getCalendarPK().getCalendar().getId(), ampCal);
//                                  break;
//                              }
//                          }
//                      }
                  }
              }
          }
          return retEvents.values();
      } catch (Exception ex) {
          throw new RuntimeException(ex);
      }
  }

  public static List getAmpCalendarEvents(GregorianCalendar startDate,GregorianCalendar endDate,String[] selectedEventTypeIds,String[] selectedDonorIds,
                                          Long userId,boolean showPublicEvents,String instanceId, String siteId) throws CalendarException {
      try {
          Session session = PersistenceManager.getRequestDBSession();

          StringBuffer queryString = new StringBuffer();
          queryString.append("select ac from ").append(AmpCalendar.class.getName());
          queryString.append(" ac left join ac.organisations org, ").append(Calendar.class.getName()).append(" r ");
          queryString.append(" inner join r.recurrCalEvent recc, ").append(Calendar.class.getName()).append(" c ");

          queryString.append("where c.id=ac.calendarPK.calendar.id and ").
          				append ("((:startDate <= c.startDate and c.startDate <= :endDate) or ").
          				append("(:startDate <= c.endDate and c.endDate <= :endDate) or ").
          				append("(c.startDate <= :startDate and :endDate <= c.endDate) or ").
                        append("(recc.recurrStartDate <= :startDate and :endDate <= recc.recurrEndDate) or ").
                        append("(:startDate <= recc.recurrStartDate and recc.recurrStartDate <= :endDate) or ").
                        append("(:startDate <= recc.recurrEndDate and recc.recurrEndDate <= :endDate))");
       

//          if(showPublicEvents){
//        	  queryString.append(" and ac.privateEvent=false");
//          }else{
//        	  queryString.append(" and ac.privateEvent=true");
//          }
          if(!showPublicEvents){
        	  queryString.append(" and ac.privateEvent=true");
          }

          if ( (selectedEventTypeIds != null) && (selectedEventTypeIds.length > 0) ) {
        	  queryString.append(" and ac.eventType.id in (:selectedEventTypes)");
          }else{
        	  queryString.append(" and 0 = 1");
          }

          List <Long> selectedDonors = new ArrayList<Long>();
          if(selectedDonorIds != null && selectedDonorIds.length != 0) {
        	  boolean includeNullOrganizations = false;
              // FFerreyra: Add clause to where for "None" donor selected. See AMP-2691
              for(int index = 0; index < selectedDonorIds.length; index++){
                  if(selectedDonorIds[index].equals("None")){
                	  includeNullOrganizations = true;
                  } else {
                	  selectedDonors.add(Long.parseLong(selectedDonorIds[index]));
                  }
              }
              
              queryString.append(" and (");
              if (selectedDonors.size() > 0) {
            	  queryString.append("org.id in (:selectedDonorIds)");
              }
              if (includeNullOrganizations) {
            	  if (selectedDonors.size() > 0) {
            		  queryString.append(" or ");
            	  }
            	  queryString.append("org is null");
              }
              queryString.append(")");
          }

          if (instanceId != null) {
        	  queryString.append(" and c.instanceId = :instanceId");
          }
          if (siteId != null) {
        	  queryString.append(" and c.siteId = :siteId");
          }
          //queryString += " group by c.id";
          Query query = session.createQuery(queryString.toString());
          query.setCalendar("startDate", startDate);
          query.setCalendar("endDate", endDate);
          if ( (selectedEventTypeIds != null) && (selectedEventTypeIds.length > 0) ) {
        	  List <Long> selectedEventType = new ArrayList<Long>();
        	  for(int index = 0; index < selectedEventTypeIds.length; index++) {
        		  selectedEventType.add(Long.parseLong(selectedEventTypeIds[index]));
        	  }
              query.setParameterList("selectedEventTypes", selectedEventType);
          }
          if (selectedDonorIds != null && selectedDonorIds.length != 0) {
              query.setParameterList("selectedDonorIds", selectedDonors);
          }

          if (instanceId != null) {
              query.setString("instanceId", instanceId);
          }
          if (siteId != null) {
              query.setString("siteId", siteId);
          }

          return query.list();
      } catch (Exception ex) {
          logger.debug("Unable to get amp calendar events", ex);
          throw new CalendarException("Unable to get amp calendar events", ex);
      }
  }
}
