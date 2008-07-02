package org.digijava.module.calendar.util;

import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.calendar.dbentity.AmpCalendarPK;
import org.digijava.module.calendar.dbentity.AmpEventType;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.exception.CalendarException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import java.util.Collection;
import java.util.*;

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

      String queryString = "select c from "+Calendar.class.getName()+" c where :startDate = c.startDate";

      Query query = session.createQuery(queryString);
      query.setDate("startDate", startDate);

      return query.list();
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

      String queryString = "select c from "+Calendar.class.getName()+" c where :endDate = c.endDate";

      Query query = session.createQuery(queryString);
      query.setDate("endDate", endDate);

      return query.list();
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
      }
      else {
        Set attendees = ampCalendar.getAttendees();
        if (attendees != null) {
          Iterator it = attendees.iterator();
          while (it.hasNext()) {
            AmpCalendarAttendee attendee = (AmpCalendarAttendee) it.
                next();
            Long attendeeId = attendee.getId();
            if (attendeeId != null && attendeeId < 0) {
              attendee.setId( -attendeeId);
              session.delete(attendee);
              it.remove();
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

  public static List getAmpCalendarEvents(GregorianCalendar startDate,
                                          GregorianCalendar endDate,
                                          String[] selectedEventTypeIds,
                                          String[] selectedDonorIds,
                                          Long userId,
                                          boolean showPublicEvents,
                                          String instanceId, String siteId) throws
      CalendarException {
    try {
      Session session = PersistenceManager.getRequestDBSession();
      String queryString = "select ac from " +
          AmpCalendar.class.getName() + " ac left  join  ac.eventType et left  join ac.donors don, "+ Calendar.class.getName()   +" c ";
      /*String queryString = "select ac from " +
          AmpCalendar.class.getName() + " ac, " +
          Calendar.class.getName() + " c";
       */
      /*if(userId != null && !showPublicEvents) {
          queryString += ", " + AmpTeamMember.class.getName() + " t1" +
              ", " + AmpTeamMember.class.getName() + " t2";
                   }
       */
     /* if (userId != null) {
        queryString += "join ac.user u";
      }
      */

      queryString += " where c.id=ac.calendarPK.calendar.id and " +
          "(:startDate <= c.startDate and c.startDate <= :endDate or " +
          ":startDate <= c.endDate and c.endDate <= :endDate or " +
          "c.startDate <= :startDate and :endDate <= c.endDate)";
      if (!showPublicEvents) {
        queryString += " and ac.privateEvent=true";
      }

      /* queryString += " where " +
           "ac.calendarPK.calendar.id = c.id and " +
           "(:startDate <= c.startDate and c.startDate <= :endDate or " +
           ":startDate <= c.endDate and c.endDate <= :endDate or " +
           "c.startDate <= :startDate and :endDate <= c.endDate)";
       */
      if (selectedEventTypeIds != null) {
        queryString += " and et.id in (:selectedEventTypes)";
      }
      else {
        queryString += " and 0 = 1";
      }

		if (selectedDonorIds != null && selectedDonorIds.length != 0) {
			queryString += " and (don.id in (:selectedDonorIds)";

			// FFerreyra: Add clause to where for "None" donor selected. See AMP-2691

			for(int index=0;index<selectedDonorIds.length;index++){
				if(selectedDonorIds[index].equals("None")){
					queryString += " or don.id is null ";
					break;
				}
			}
			queryString += ") ";
		}

      /* if(userId != null && !showPublicEvents) {
           queryString += " and ac.user.id = t1.user.id" +
               " and t1.ampTeam.ampTeamId = t2.ampTeam.ampTeamId" +
               " and t2.user.id = :userId and ac.privateEvent = true";
       } else if(userId == null) {
           queryString += " and ac.privateEvent = false";
       }
       */
      if (instanceId != null) {
        queryString += " and c.instanceId = :instanceId";
      }
      if (siteId != null) {
        queryString += " and c.siteId = :siteId";
      }
      queryString +=" group by c.id";
      Query query = session.createQuery(queryString);
      query.setCalendar("startDate", startDate);
      query.setCalendar("endDate", endDate);
      if (selectedEventTypeIds != null) {
        query.setParameterList("selectedEventTypes",
                               selectedEventTypeIds);
      }
      if (selectedDonorIds != null && selectedDonorIds.length != 0 ) {
        query.setParameterList("selectedDonorIds",
                               selectedDonorIds);
      }

      /*   if(userId != null && !showPublicEvents) {
             query.setLong("userId", userId.longValue());
         }
       */
      if (instanceId != null) {
        query.setString("instanceId", instanceId);
      }
      if (siteId != null) {
        query.setString("siteId", siteId);
      }

      //System.out.println("\n\n\n\n\n\n" + query.getQueryString());


      List events = query.list();
      /*if(events != null && !events.isEmpty() && selectedDonorIds != null &&
         selectedDonorIds.length != 0) {
          Iterator it = events.iterator();
          while(it.hasNext()) {
              Set donors = ((AmpCalendar) it.next()).getDonors();
              if(donors != null && !donors.isEmpty()) {
                  Set selectedDonors = AmpUtil.getSelectedDonors(donors,
                      selectedDonorIds);
                  if(selectedDonors.isEmpty()) {
                      it.remove();
                  }
              }
          }
                   }*/
      return events;
    }
    catch (Exception ex) {
      logger.debug("Unable to get amp calendar events", ex);
      throw new CalendarException("Unable to get amp calendar events", ex);
    }
  }
}
