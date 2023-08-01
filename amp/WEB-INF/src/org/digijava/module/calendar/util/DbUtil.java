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

package org.digijava.module.calendar.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.ModuleInstance;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DgUtil;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.calendar.dbentity.Calendar;
import org.digijava.module.calendar.dbentity.CalendarItem;
import org.digijava.module.calendar.dbentity.CalendarSettings;
import org.digijava.module.calendar.exception.CalendarException;
import org.digijava.module.common.dbentity.ItemStatus;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.type.CalendarType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class DbUtil {

    /**
     * logging tracer
     */
    private static Logger logger = Logger.getLogger(DbUtil.class);

    /**
     * <p>Returns list of calendar events by user identity and status considering pagination: </p>
     * only the sublist,correspondence to the current page is retrieved
     *
     * @param userId user identity
     * @param status status of retrieved events
     * @param request Http Servlet Request
     * @param firstResult beggining index of retrieved events sublist
     * @param maxResult ending index of retrieved events sublist
     * @param direction pagination direction(i.e. forward, backward)
     * @return sublist of events for selected user and status correspondence to the current page
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(Long userId, String status,
            HttpServletRequest request,
            int firstResult, int maxResult,
            int direction) throws
            CalendarException {

        // get module instance
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

        return getCalendarEvents(userId, moduleInstance.getSite().getSiteId(),
                moduleInstance.getInstanceName(), status,
                firstResult, maxResult, direction);
    }

    /**
     * <p>Returns ordered list of calendar events by user identity,status and selected site and instance considering pagination: </p>
     * <p>only the sublist,correspondence to the current page is retrieved</p>
     * <p>when pagination has direction - forward only the events with archive date after current date(including) ordered ascending by event's publication date are retrieved</p>
     * when pagination has direction - backward only the events with archive date before current date ordered descending by event's publication date are retrieved
     *
     * @param userId user identity
     * @param siteId site identity
     * @param instanceId instance name
     * @param status status of retrieved events
     * @param firstResult beggining index of retrieved events sublist
     * @param maxResult ending index of retrieved events sublist
     * @param direction pagination direction(i.e. forward, backward)
     * @return ordered sublist of events for selected user,status,site and instance correspondence to the current page
     * @throws CalendarException if db-access errror occurs
     */

    public static List getCalendarEvents(Long userId, String siteId,
            String instanceId,
            String status, int firstResult,
            int maxResult, int direction) throws
            CalendarException {

        Session session = null;
        List list = null;
        try {
            session = PersistenceManager.getSession();

            String queryString = "select c from " +
            CalendarItem.class.getName() +
            " ci, " + Calendar.class.getName() +
            " c where (ci.calendar.id=c.id) and (c.siteId=:siteId) and (c.instanceId=:instanceId) ";

            if (userId != null) {
                queryString += " and (ci.userId=:userId) ";
            }

            if (status != null) {
                queryString += "and (c.status=:status) ";
            }

            // if direction = 1 forward
            if (direction == 1) {
                queryString +=
                    " and (c.endDate >= :curDate1) order by c.startDate asc";
            }
            else {
                queryString +=
                    " and (c.endDate < :curDate1) order by c.startDate desc";
            }

            logger.debug("Calendar DButil Query: " + queryString);

            Query q = session.createQuery(queryString);

            q.setCacheable(true);
            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);

            // add value -----
            q.setParameter("siteId", siteId, StringType.INSTANCE);
            q.setParameter("instanceId", instanceId, StringType.INSTANCE);

            if (status != null)
                q.setParameter("status", status, StringType.INSTANCE);

            if (userId != null)
                q.setParameter("userId", userId, LongType.INSTANCE);

            java.util.Calendar currentDate = java.util.Calendar.getInstance();
            q.setCalendar("curDate1", currentDate);
            // -----------------

            list = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get calendar event list from database ", ex);
            throw new CalendarException(
                    "Unable to get calendar event list from database ", ex);
        }
        return list;
    }

    /**
     * <p>Returns ordered list of calendar events by status and selected site and instance considering pagination: </p>
     * <p>only the sublist,correspondence to the current page is retrieved</p>
     * <p>when pagination has direction - forward only the events with archive date after current date(including) ordered ascending by event's publication date are retrieved</p>
     * when pagination has direction - backward only the events with archive date before current date ordered descending by event's publication date are retrieved
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @param status status of retrieved events
     * @param firstResult beggining index of retrieved events sublist
     * @param maxResult ending index of retrieved events sublist
     * @param direction pagination direction(i.e. forward, backward)
     * @return ordered sublist of events for selected user,status,site and instance correspondence to the current page
     * @throws CalendarException if db-access errror occurs
     */

    public static List getCalendarEvents(String siteId,
            String instanceId,
            String status, int firstResult,
            int maxResult, int direction) throws
            CalendarException {

        Session session = null;
        List list = null;
        try {
            session = PersistenceManager.getSession();

            String queryString = "from " +
            Calendar.class.getName() +
            " c where (c.siteId=:siteId) and (c.instanceId=:instanceId) ";

            if (status != null) {
                queryString += "and (c.status=:status) ";
            }

            // if direction = 1 forward
            if (direction == 1) {
                queryString +=
                    " and (c.startDate >= :curDate1) order by c.startDate asc";
            }
            else {
                queryString +=
                    " and (c.startDate <= :curDate1) order by c.startDate desc";
            }

            logger.debug("Calendar DButil Query: " + queryString);

            Query q = session.createQuery(queryString);

            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);

            // add value -----
            q.setParameter("siteId", siteId, StringType.INSTANCE);
            q.setParameter("instanceId", instanceId, StringType.INSTANCE);

            if (status != null)
                q.setParameter("status", status, StringType.INSTANCE);

            java.util.Calendar currentDate = java.util.Calendar.getInstance();
            currentDate.set(java.util.Calendar.HOUR, 0);
            currentDate.set(java.util.Calendar.MINUTE, 0);
            currentDate.set(java.util.Calendar.SECOND, 0);
            currentDate.set(java.util.Calendar.MILLISECOND, 0);

            q.setCalendar("curDate1", currentDate);
            // -----------------

            list = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get calendar event list from database ", ex);
            throw new CalendarException(
                    "Unable to get calendar event list from database ", ex);
        }

        return list;
    }

    /**
     * Returns list of calendar events for selected status
     *
     * @param status status of retrieved events
     * @param request Http Servlet Request
     * @return list of events
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(String status,
            HttpServletRequest request) throws
            CalendarException {

        // get module instance
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

        return getCalendarEvents(moduleInstance.getSite().getSiteId(),
                moduleInstance.getInstanceName(), status);
    }

    /**
     * <p>Returns list of calendar events for selected status considering pagination: </p>
     * only the sublist,correspondence to the current page is retrieved
     *
     * @param status status of retrieved events
     * @param request Http Servlet Request
     * @param firstResult beggining index of retrieved events sublist
     * @param maxResult ending index of retrieved events sublist
     * @param today true if events with publication date before current date will be retrieved,
     * if false events with publication date before or after current date will be retrieved,
     * @return sublist of events for selected status correspondence to the current page
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(String status,
            HttpServletRequest request,
            int firstResult, int maxResult,
            boolean today) throws
            CalendarException {

        // get module instance
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

        return getCalendarEvents(moduleInstance.getSite().getSiteId(),
                moduleInstance.getInstanceName(), status,
                firstResult, maxResult, today);
    }

    /**
     * <p>Returns list of calendar events for selected status considering pagination: </p>
     * only the sublist,correspondence to the current page is retrieved
     *
     * @param status status of retrieved events
     * @param request Http Servlet Request
     * @param firstResult beggining index of retrieved events sublist
     * @param maxResult ending index of retrieved events sublist
     * @return sublist of events for selected status correspondence to the current page
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(String status,
            HttpServletRequest request,
            int firstResult, int maxResult) throws
            CalendarException {

        // get module instance
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

        return getCalendarEvents(moduleInstance.getSite().getSiteId(),
                moduleInstance.getInstanceName(), status,
                firstResult, maxResult, true);
    }

    /**
     * Returns list of calendar events with publication date before current date(including) and archive date after current date(including) ordered ascending by event's publication date for selected site,instance and status
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @param status status of retrieved events
     * @return ordered list of events
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(String siteId, String instanceId,
            String status) throws
            CalendarException {

        Session session = null;
        List list = null;
        try {
            session = PersistenceManager.getSession();

            Query q = session.createQuery("from " +
                    Calendar.class.getName() +
                    " rs where (rs.siteId=:siteId) and (rs.instanceId=:instanceId) and (rs.status=:status) " +
            " and (rs.startDate <=:curDate1 and rs.endDate >=:curDate2) order by rs.startDate desc");

            java.util.Calendar currentDate = java.util.Calendar.getInstance();

            q.setParameter("siteId", siteId, StringType.INSTANCE);
            q.setParameter("instanceId", instanceId, StringType.INSTANCE);
            q.setParameter("status", status, StringType.INSTANCE);
            q.setCalendar("curDate1", currentDate);
            q.setCalendar("curDate2", currentDate);

            list = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get calendar event list from database", ex);
            throw new CalendarException(
                    "Unable to get calendar event list from database", ex);
        }
        return list;
    }

    /**
     * <p>Returns list of calendar events with publication date before current date(including) and archive date after current date(including) ordered ascending by event's publication date for selected site, instance and status considering pagination: </p>
     * only the sublist,correspondence to the current page is retrieved
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @param status status of retrieved events
     * @param firstResult beggining index of retrieved events sublist
     * @param maxResult ending index of retrieved events sublist
     * @param today true if events with publication date before current date will be retrieved,
     * if false events with publication date before or after current date will be retrieved,
     * @return ordered sublist of events for selected site, instance and status correspondence to the current page
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(String siteId, String instanceId,
            String status, int firstResult,
            int maxResult, boolean today) throws
            CalendarException {

        Session session = null;
        Query q = null;
        List list = null;
        try {
            session = PersistenceManager.getSession();

            java.util.Calendar currentDate = java.util.Calendar.getInstance();

            if (today) {
                q = session.createQuery("select new org.digijava.module.calendar.form.CalendarTeaserItem(rs.id, rsi.title, rsi.creationDate, rs.sourceUrl, rs.sourceName, rs.startDate, rs.endDate, rsi.description) from " +
                        Calendar.class.getName() +
                        " rs, rs.calendarItem rsi" +
                        " where (rs.siteId=:siteId) and (rs.instanceId=:instanceId) and (rs.status=:status) " +
                " and (rs.startDate <=:curDate1 and rs.endDate >=:curDate2) order by rs.startDate desc");

                q.setCalendar("curDate1", currentDate);
                q.setCalendar("curDate2", currentDate);
            }
            else {
                q = session.createQuery("select new org.digijava.module.calendar.form.CalendarTeaserItem(rs.id, rsi.title, rsi.creationDate, rs.sourceUrl, rs.sourceName, rs.startDate, rs.endDate, rsi.description) from " +
                        Calendar.class.getName() +
                        " rs, rs.calendarItem rsi" +
                        " where (rs.siteId=:siteId) and (rs.instanceId=:instanceId) and (rs.status=:status) " +
                " and (rs.endDate >=:curDate) order by rs.startDate asc");
                q.setCalendar("curDate", currentDate);
            }

            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);

            q.setCacheable(true);
            q.setParameter("siteId", siteId, StringType.INSTANCE);
            q.setParameter("instanceId", instanceId, StringType.INSTANCE);
            q.setParameter("status", status, StringType.INSTANCE);

            list = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get calendar event list from database", ex);
            throw new CalendarException(
                    "Unable to get calendar event list from database", ex);
        }

        return list;
    }

    /**
     * <p>Returns list of calendar events by status considering event's publication and archive dates and pagination: </p>
     * <p>only the events having publication <b>or</b> archive dates between fromDate <b>and</b> toDate parameters are retrieved</p>
     * only the sublist of events,correspondence to the current page is retrieved
     *
     * @param status status of retrieved events
     * @param request Http Servlet Request
     * @param firstResult beggining index of retrieved events sublist
     * @param maxResult ending index of retrieved events sublist
     * @param fromDate date,after which the event should be published or archived
     * @param toDate  date,by which the event should be published or archived
     * @return sublist of events for selected status with publication or archive dates between fromDate and toDate, correspondence to the current page
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(String status,
            HttpServletRequest request,
            int firstResult, int maxResult,
            Date fromDate, Date toDate) throws
            CalendarException {

        // get module instance
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

        return getCalendarEvents(moduleInstance.getSite().getSiteId(),
                moduleInstance.getInstanceName(), status,
                firstResult, maxResult, fromDate, toDate);
    }

    /**
     * Returns list of events for selected site, instance created after selected date
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @param date Date after which should be created retrieved events
     * @return  list of events
     * @throws CalendarException if db-access errror occurs
     */
    public static List getLastCalendarEvents(String siteId, String instanceId,
            Date date) throws
            CalendarException {
        List events = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);

            Query q = session.createQuery("select c from " +
                    Calendar.class.getName() +
                    " c, c.calendarItem ci" +
            " where (c.siteId=:siteId) and (c.instanceId=:instanceId) and ci.calendar.id = c.id and ci.creationDate > :lastIndexDate");

            q.setParameter("siteId", siteId, StringType.INSTANCE);
            q.setParameter("instanceId", instanceId, StringType.INSTANCE);
            q.setParameter("lastIndexDate", calendar, CalendarType.INSTANCE);

            events = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get events list from database", ex);
            throw new CalendarException(
                    "Unable to get events list from database", ex);
        }

        return events;
    }

    /**
     * <p>Returns list of calendar events for selected site, instance and status considering event's publisment and archive dates and pagination: </p>
     * <p>only the events having publication <b>or</b> archive dates between fromDate(including) <b>and</b> toDate(including) parameters ordered descending by event's publication date are retrieved</p>
     * only the sublist od events,correspondence to the current page is retrieved
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @param status status of retrieved events
     * @param firstResult beggining index of retrieved events sublist
     * @param maxResult ending index of retrieved events sublist
     * @param fromDate date,after which the event should be published or archived
     * @param toDate  date,by which the event should be published or archived
     * @return ordered sublist of events for selected site, instance and status with publication or archive dates between fromDate and toDate, correspondence to the current page
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(String siteId, String instanceId,
            String status, int firstResult,
            int maxResult, Date fromDate,
            Date toDate) throws
            CalendarException {

        Session session = null;
        List list = null;
        try {
            session = PersistenceManager.getSession();

            // @toto Fix bug
            Query q = session.createQuery("from " +
                    Calendar.class.getName() +
                    " rs where (rs.siteId=:siteId) and (rs.instanceId=:instanceId) and (rs.status=:status) " +
            " and ( ((rs.endDate >= :fromDate) and (rs.endDate <= :toDate)) or ((rs.startDate >= :fromDate) and (rs.startDate <= :toDate)) ) order by rs.startDate desc");

            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);
            q.setParameter("siteId", siteId, StringType.INSTANCE);
            q.setParameter("instanceId", instanceId, StringType.INSTANCE);

            q.setParameter("status", status, StringType.INSTANCE);

            java.util.Calendar fromDateCalendar = new GregorianCalendar();
            java.util.Calendar toDateCalendar = new GregorianCalendar();

            fromDateCalendar.setTime(fromDate);
            toDateCalendar.setTime(toDate);

            q.setCalendar("toDate", toDateCalendar);
            q.setCalendar("fromDate", fromDateCalendar);

            list = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get event list from database", ex);
            throw new CalendarException(
                    "Unable to get calendar event list from database", ex);
        }

        return list;
    }

    /**
     * <p>Returns list of calendar events for selected site, instance and status considering event's publisment and archive dates: </p>
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @param status status of retrieved events
     * @param fromDate date,after which the event should be published or archived
     * @param toDate  date,by which the event should be published or archived
     * @return ordered sublist of events for selected site, instance and status with publication or archive dates between fromDate and toDate, correspondence to the current page
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(String siteId, String instanceId,
            String status, Date fromDate,
            Date toDate) throws
            CalendarException {

        Session session = null;
        List list = null;
        try {
            session = PersistenceManager.getSession();

            // @toto Fix bug
            Query q = session.createQuery("from " +
                    Calendar.class.getName() +
                    " rs where (rs.siteId=:siteId) and (rs.instanceId=:instanceId) and (rs.status=:status) " +
            " and ( ((rs.endDate >= :fromDate) and (rs.endDate <= :toDate)) or ((rs.startDate >= :fromDate) and (rs.startDate <= :toDate)) ) order by rs.startDate desc");

            q.setParameter("siteId", siteId, StringType.INSTANCE);
            q.setParameter("instanceId", instanceId, StringType.INSTANCE);

            q.setParameter("status", status, StringType.INSTANCE);

            java.util.Calendar fromDateCalendar = new GregorianCalendar();
            java.util.Calendar toDateCalendar = new GregorianCalendar();

            fromDateCalendar.setTime(fromDate);
            toDateCalendar.setTime(toDate);

            q.setCalendar("toDate", toDateCalendar);
            q.setCalendar("fromDate", fromDateCalendar);

            list = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get event list from database", ex);
            throw new CalendarException(
                    "Unable to get calendar event list from database", ex);
        }

        return list;
    }

    /**
     * Returns list of calendar events with publication date before current date(including) and archive date after current date(including) ordered descending by event's publication date for selected site and instance
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @return ordered list of events published before current date and with archive date after current date
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(String siteId,
            String instanceId) throws
            CalendarException {
        List events = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            Query q = session.createQuery("from " +
                    Calendar.class.getName() +
                    " rs where (rs.siteId=:siteId) and (rs.instanceId=:instanceId) " +
            " and (rs.startDate <=:curDate1 and rs.endDate >=:curDate2) order by rs.startDate desc");

            q.setParameter("siteId", siteId, StringType.INSTANCE);
            q.setParameter("instanceId", instanceId, StringType.INSTANCE);

            java.util.Calendar currentDate = java.util.Calendar.getInstance();

            q.setCalendar("curDate1", currentDate);
            q.setCalendar("curDate2", currentDate);

            events = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get event list from database", ex);
            throw new CalendarException(
                    "Unable to get event list from database", ex);
        }

        return events;
    }

    /**
     * <p>Returns list of calendar events for selected site and instance considering event's publisment and archive dates and pagination: </p>
     * <p>only the events published before current date(including) and with archive date after current date(including) ordered descending by event's publication date are retrieved</p>
     * only the sublist od events,correspondence to the current page is retrieved
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @param firstResult beggining index of retrieved events sublist
     * @param maxResult ending index of retrieved events sublist
     * @return ordered list of events published before current date and with archive date after current date
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(String siteId,
            String instanceId, int firstResult,
            int maxResult) throws
            CalendarException {
        List events = null;
        Session session = null;
        try {
            session = PersistenceManager.getSession();

            Query q = session.createQuery("from " +
                    Calendar.class.getName() +
                    " rs where (rs.siteId=:siteId) and (rs.instanceId=:instanceId) " +
            " and (rs.startDate <=:curDate1 and rs.endDate >=:curDate2) order by rs.startDate desc");

            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);
            q.setParameter("siteId", siteId, StringType.INSTANCE);
            q.setParameter("instanceId", instanceId, StringType.INSTANCE);

            java.util.Calendar currentDate = java.util.Calendar.getInstance();

            q.setCalendar("curDate1", currentDate);
            q.setCalendar("curDate2", currentDate);

            events = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get event list from database", ex);
            throw new CalendarException(
                    "Unable to get event list from database", ex);
        }

        return events;
    }

    /**
     *<p>Returns list of calendar events with publication date before current date(including) and archive date after current date(including) ordered descending by event's publication date by user identity and(if not null) by status considering pagination:</p>
     * only the sublist od events,correspondence to the current page is retrieved
     *
     * @param userId user identity
     * @param status status of retrieved events or null
     * @param firstResult beggining index of retrieved events sublist
     * @param maxResult ending index of retrieved events sublist
     * @return ordered list of events published before current date and with archive date after current date
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(Long userId, String status,
            int firstResult, int maxResult) throws
            CalendarException {

        Session session = null;
        Iterator iterator = null;
        List list = new ArrayList();
        Query q = null;
        try {
            session = PersistenceManager.getSession();

            if (status != null) {

                q = session.createQuery("select c from " +
                        CalendarItem.class.getName() +
                        " ci, " + Calendar.class.getName() +
                        " c where (ci.calendar.id=c.id) and (c.status = :status) and (ci.userId = :userId) and " +
                "(c.startDate <=:curDate1 and c.endDate >=:curDate2) order by c.startDate desc");

                q.setParameter("status", status, StringType.INSTANCE);
            }
            else {
                q = session.createQuery("select c from " + CalendarItem.class.getName() + " ci, " + Calendar.class.getName() +
                        " c where (ci.calendar.id=c.id) and (ci.userId = :userId) and " +
                "(c.startDate <=:curDate1 and c.endDate >=:curDate2) order by c.startDate desc");

            }

            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);

            java.util.Calendar currentDate = java.util.Calendar.getInstance();

            q.setParameter("userId", userId, LongType.INSTANCE);

            q.setCalendar("curDate1", currentDate);
            q.setCalendar("curDate2", currentDate);

            list = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get calendar event list from database", ex);
            throw new CalendarException(
                    "Unable to get calendar event list from database", ex);
        }

        return list;
    }

    /**
     * <p>Returns list of calendar events by user identity and status considering event's publisment and archive dates and pagination: </p>
     * <p>only the events having publication <b>or</b> archive dates between fromDate(including) <b>and</b> toDate(including) parameters ordered descending by event's publication date are retrieved</p>
     * only the sublist od events,correspondence to the current page is retrieved
     *
     * @param userId user identity
     * @param status status of retrieved events
     * @param firstResult beggining index of retrieved events sublist
     * @param maxResult ending index of retrieved events sublist
     * @param fromDate date,after which the event should be published or archived
     * @param toDate  date,by which the event should be published or archived
     * @return ordered sublist of events for selected site, instance and status with publication or archive dates between fromDate and toDate, correspondence to the current page
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(Long userId, String status,
            int firstResult, int maxResult,
            Date fromDate, Date toDate) throws
            CalendarException {

        Session session = null;
        Iterator iterator = null;
        List list = new ArrayList();
        Query q = null;
        try {
            session = PersistenceManager.getSession();

            if (status != null) {
                q = session.createQuery("select c from " +
                        CalendarItem.class.getName() +
                        " ci, " + Calendar.class.getName() +
                        " c where (ci.calendar.id=c.id) and (c.status = :status) and (ci.userId = :userId) and " +
                "( ((c.endDate >= :fromDate) and (c.endDate <= :toDate)) or ((c.startDate >= :fromDate) and  (c.startDate <= :toDate)) ) order by c.startDate desc");

                q.setParameter("status", status, StringType.INSTANCE);
            }
            else {
                q = session.createQuery("select c from " +
                        CalendarItem.class.getName() +
                        " ci, " + Calendar.class.getName() +
                        " c where (ci.calendar.id=c.id) and (ci.userId = :userId) and " +
                "( ((c.endDate >= :fromDate) and (c.endDate <= :toDate)) or ((c.startDate >= :fromDate) and  (c.startDate <= :toDate)) ) order by c.startDate desc");

            }

            q.setFirstResult(firstResult);
            q.setMaxResults(maxResult);

            q.setParameter("userId", userId, LongType.INSTANCE);

            java.util.Calendar fromDateCalendar = java.util.Calendar.
            getInstance();
            java.util.Calendar toDateCalendar = java.util.Calendar.getInstance();

            fromDateCalendar.setTime(fromDate);
            toDateCalendar.setTime(toDate);

            q.setCalendar("fromDate", fromDateCalendar);
            q.setCalendar("toDate", toDateCalendar);

            list = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get event list from database", ex);
            throw new CalendarException(
                    "Unable to get event list from database", ex);
        }

        return list;
    }

    /**
     * <p>Returns list of calendar events by user identity and status considering event's publisment and archive dates and pagination: </p>
     *
     * @param userId user identity
     * @param status status of retrieved events
     * @param fromDate date,after which the event should be published or archived
     * @param toDate  date,by which the event should be published or archived
     * @return ordered sublist of events for selected site, instance and status with publication or archive dates between fromDate and toDate, correspondence to the current page
     * @throws CalendarException if db-access errror occurs
     */
    public static List getCalendarEvents(Long userId, String status,
            Date fromDate, Date toDate) throws
            CalendarException {

        Session session = null;
        Iterator iterator = null;
        List list = new ArrayList();
        Query q = null;
        try {
            session = PersistenceManager.getSession();

            if (status != null) {
                q = session.createQuery("select c from " +
                        CalendarItem.class.getName() +
                        " ci, " + Calendar.class.getName() +
                        " c where (ci.calendar.id=c.id) and (c.status = :status) and (ci.userId = :userId) and " +
                "( ((c.endDate >= :fromDate) and (c.endDate <= :toDate)) or ((c.startDate >= :fromDate) and  (c.startDate <= :toDate)) ) order by c.startDate desc");

                q.setParameter("status", status, StringType.INSTANCE);
            }
            else {
                q = session.createQuery("select c from " +
                        CalendarItem.class.getName() +
                        " ci, " + Calendar.class.getName() +
                        " c where (ci.calendar.id=c.id) and (ci.userId = :userId) and " +
                "( ((c.endDate >= :fromDate) and (c.endDate <= :toDate)) or ((c.startDate >= :fromDate) and  (c.startDate <= :toDate)) ) order by c.startDate desc");

            }

            q.setParameter("userId", userId, LongType.INSTANCE);

            java.util.Calendar fromDateCalendar = java.util.Calendar.
            getInstance();
            java.util.Calendar toDateCalendar = java.util.Calendar.getInstance();

            fromDateCalendar.setTime(fromDate);
            toDateCalendar.setTime(toDate);

            q.setCalendar("fromDate", fromDateCalendar);
            q.setCalendar("toDate", toDateCalendar);

            list = q.list();
        }
        catch (Exception ex) {
            logger.debug("Unable to get event list from database", ex);
            throw new CalendarException(
                    "Unable to get event list from database", ex);
        }

        return list;
    }

    public static int getNumberOfCharsInTitle(String siteId, String instanceId) throws
    CalendarException {

        Session session = null;
        List list;
        Long numberOfCharsInTitle = new Long(100);
        int result = 200;
        try {
            session = PersistenceManager.getSession();

            StringBuffer queryString = new StringBuffer();
            logger.debug("CALENDAR NUMBER");
            queryString.append(
                    "select rs.numberOfCharsInTitle from " +
                    CalendarSettings.class.getName() +
            " rs where (rs.siteId=:siteId) and (rs.instanceId=:instanceId)");

            Query q = session.createQuery(queryString.toString());

            q.setParameter("siteId", siteId, StringType.INSTANCE);
            q.setParameter("instanceId", instanceId, StringType.INSTANCE);

            q.setCacheable(true);
            list = q.list();

            Iterator iter = list.iterator();
            while (iter.hasNext()) {
                numberOfCharsInTitle = (Long) iter.next();
                break;
            }
            if (numberOfCharsInTitle != null) {
                result = numberOfCharsInTitle.intValue();
            }
        }
        catch (Exception ex) {
            logger.debug("Unable to get calendar setting from database ", ex);
            throw new CalendarException(
                    "Unable to get calendar setting from database",
                    ex);
        }
        return result;

    }

    /**
     * Get Calendar settings by Http Servlet Request
     *
     * @param request Http Servlter Request
     * @return Calendar settings
     * @throws CalendarException if db-access errror occurs
     */
    public static CalendarSettings getCalendarSettings(HttpServletRequest
            request) throws CalendarException {

        // get module instance
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

        return getCalendarSettings(moduleInstance.getSite().getSiteId(),
                moduleInstance.getInstanceName());
    }

    /**
     * Get Calendar settings by site identity and instance name
     *
     * @param siteId site identity
     * @param instanceId instance name
     * @return Calendar settings
     * @throws CalendarException if db-access errror occurs
     */
    public static CalendarSettings getCalendarSettings(String siteId,
            String instanceId) throws CalendarException {

        Session session = null;
        Iterator iter = null;
        CalendarSettings settings = null;
        try {
            session = PersistenceManager.getSession();

            String queryString = "from " + CalendarSettings.class.getName() + " rs where rs.siteId=:siteId and rs.instanceId=:instanceId";
            Query query = session.createQuery(queryString);
            query.setString("siteId", siteId);
            query.setString("instanceId", instanceId);

            iter = query.iterate();

            while (iter.hasNext()) {
                settings = (CalendarSettings) iter.next();
                break;
            }

            // Get fill default settings
            if (settings == null) {
                settings = new CalendarSettings(siteId, instanceId);
            }
            // ----------------------

        }
        catch (Exception ex) {
            logger.debug("Unable to get calendar setting from database", ex);
            throw new CalendarException(
                    "Unable to get calendar setting from database", ex);
        }
        return settings;
    }

    /**
     * Adds/Updates Event into Database
     *
     * @param event Calendar instance to be updated
     * @throws CalendarException if db-access errror occurs
     */
    public static void updateCalendarEvent(Calendar event) throws
    CalendarException {

        Session session = null;

        try {

            session = PersistenceManager.getSession();
//beginTransaction();

            Iterator iter = event.getCalendarItem().iterator();
            while (iter.hasNext()) {
                CalendarItem item = (CalendarItem) iter.next();
                if (item.getCalendar() == null) {
                    iter.remove();
                    session.delete(item);
                }
            }

            session.saveOrUpdate(event);
            //tx.commit();

        }
        catch (Exception ex) {
            logger.debug(
                    "Unable to update calendar event information into database",
                    ex);
            throw new CalendarException(
                    "Unable to update calendar event information into database",
                    ex);
        }
    }

    /**
     * Add/Update Calendar settings into Database
     *
     * @param setting CalendarSettings instance to be updated
     * @throws CalendarException if db-access errror occurs
     */
    public static void updateSetting(CalendarSettings setting) throws
    CalendarException {

        Session session = null;
        try {
            session = PersistenceManager.getSession();
//beginTransaction();
            session.saveOrUpdate(setting);
            //tx.commit();

        }
        catch (Exception ex) {
            logger.debug("Unable to update calendar setting information into database", ex);
            throw new CalendarException("Unable to update calendar setting information into database", ex);
        }
    }

    /**
     * Update Event's status into Database
     *
     * @param event Calendar instance to be updated
     * @param status status to be updated
     * @throws CalendarException if db-access errror occurs
     */
    public static void updateStatus(Calendar event, String status) throws
    CalendarException {

        // set new Status
        event.setStatus(new ItemStatus(status));

        updateCalendarEvent(event);
    }

    /**
     * Creates Calendar instance by user identity,status,language,title, description and Http Servlet Request
     *
     * @param userId user identity
     * @param statusCode status of event being created
     * @param languageCode language code of event being created
     * @param title title of event being created
     * @param description description of event being created
     * @param request Http Servlet Request
     * @return new Calendar instance
     * @throws CalendarException if db-access errror occurs
     */
    public static Calendar createCalendarEvent(Long userId, String statusCode,
            String languageCode,
            String title,
            String description,
            HttpServletRequest request) {

        Calendar event = createCalendarEvent(userId, statusCode, languageCode,
                title, description);
        CalendarItem eventItem = event.getFirstCalendarItem();

        // get module instance
        ModuleInstance moduleInstance = DgUtil.getRealModuleInstance(request);

        event.setInstanceId(moduleInstance.getInstanceName());
        event.setSiteId(moduleInstance.getSite().getSiteId());

        // fill calendar item object
        eventItem.setCreationIp(RequestUtils.getRemoteAddress(request));
        eventItem.setCreationDate(new Date());

        return event;
    }

    /**
     * Creates Calendar instance by user identity,status,language,title and description
     *
     * @param userId user identity
     * @param statusCode status of event being created
     * @param languageCode language code of event being created
     * @param title title of event being created
     * @param description description of event being created
     * @return new Calendar instance
     * @throws CalendarException if db-access errror occurs
     */
    public static Calendar createCalendarEvent(Long userId, String statusCode,
            String languageCode,
            String title,
            String description) {

        Calendar event = new Calendar();
        CalendarItem eventItem = new CalendarItem();

        // populate calendar object
        HashSet eventItems = new HashSet();
        eventItems.add(eventItem);
        event.setCalendarItem(eventItems);
        event.setStatus(new ItemStatus(statusCode));

        // populate calendar item object
        eventItem.setCalendar(event);
        eventItem.setDescription(description);
        eventItem.setLanguage(languageCode);
        eventItem.setTitle(title);
        eventItem.setUserId(userId);

        return event;
    }

    /**
     * Get Event by identity
     *
     * @param id Event identity
     * @return event with specified identity or null
     * @throws CalendarException if db-access errror occurs
     */
    public static Calendar getCalendarItem(Long id) throws CalendarException {

        Session session = null;
        Calendar calendarItem = null;
        try {

            session = PersistenceManager.getSession();

            calendarItem = (Calendar) session.load(Calendar.class, id);

        }
        catch (Exception ex) {
            logger.debug("Unable to get event item from database", ex);
            throw new CalendarException(
                    "Unable to get event item from database",
                    ex);
        }
        return calendarItem;
    }

    /**
     * Gets Number of News items for site and instance by specified status
     * @param siteId site identity
     * @param instanceId instance name
     * @param status status of retrieved items
     * @return Number of News items for site and instance by specified status
     * @throws CalendarException if db-access errror occurs
     */
    public static int getNumOfEventItems(String siteId,
            String instanceId,
            String status) throws
            CalendarException {

        Session session = null;
        List list = null;
        int result = 0;

        try {
            session = PersistenceManager.getSession();

            String queryString = "select count(c.status) from " +
            Calendar.class.getName() +
            " c, where (c.siteId=:siteId) and (c.instanceId=:instanceId) ";

            if (status != null) {
                queryString += "and (c.status=:status) ";
            }

            Query q = session.createQuery(queryString);

            // set query parameters

            q.setParameter("siteId", siteId, StringType.INSTANCE);
            q.setParameter("instanceId", instanceId, StringType.INSTANCE);

            if (status != null)
                q.setParameter("status", status, StringType.INSTANCE);

            Integer uniqueResult = ( (Integer) q.uniqueResult());
            logger.debug("uniquie result:" + uniqueResult);

            if (uniqueResult != null) {
                result = uniqueResult.intValue();
            }

        }
        catch (Exception ex) {
            logger.debug("Unable to get number of event items from database ",
                    ex);
            throw new CalendarException(
                    "Unable to get number of  event items from database ", ex);
        }
        return result;
    }
    
    
    public static List getCalendarEvents(String siteId, String instanceId,Long userId) throws CalendarException {
            List events = null;
            Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();

            Query q = session.createQuery("select c from " +
            CalendarItem.class.getName() +
            " ci, " + Calendar.class.getName() +
            " c where (ci.id=c.id) and (c.siteId=:siteId) and (c.instanceId=:instanceId) order by c.startDate desc");
    
            q.setParameter("siteId", siteId, StringType.INSTANCE);
            q.setParameter("instanceId", instanceId, StringType.INSTANCE);
            //q.setParameter("userId", userId, LongType.INSTANCE);
            events = q.list();

        }
        catch (Exception ex) {
            logger.debug("Unable to get event list from database", ex);
            throw new CalendarException(
                    "Unable to get event list from database", ex);
        }

        return events;
    }
    
    public static int getFiscalCalendarCount(String name,Long id) throws Exception{
        int retValue=0;
        Session session=null;
        Query query=null;
        try {
            session=PersistenceManager.getRequestDBSession();
            Criteria criteria = session.createCriteria(AmpFiscalCalendar.class).add(Restrictions.ilike("name", name))
            .add(Restrictions.or(Restrictions.idEq(null), Restrictions.ne("id", id)));
            criteria.setProjection(Projections.rowCount());
            criteria.list();
            retValue =(Integer) criteria.uniqueResult();
            } catch (Exception e) {
            e.printStackTrace();
        }
        return retValue;
    }
    
}
