package org.digijava.kernel.ampapi.endpoints.datafreeze;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.ValueWrapper;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivityFrozen;
import org.digijava.module.aim.dbentity.AmpDataFreezeExclusion;
import org.digijava.module.aim.dbentity.AmpDataFreezeSettings;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.util.AmpDateUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

public final class DataFreezeUtil {
    private static Logger logger = Logger.getLogger(DataFreezeUtil.class);

    private DataFreezeUtil() {
    }

    public static AmpDataFreezeSettings getDataFreezeEventById(Long id) {
        return (AmpDataFreezeSettings) PersistenceManager.getSession().get(AmpDataFreezeSettings.class, id);
    }

    public static void saveDataFreezeEvent(AmpDataFreezeSettings dataFreezeEvent) {
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            session.saveOrUpdate(dataFreezeEvent);
        } catch (Exception e) {
            logger.error("Exception from saveDataFreezeEvent: " + e.getMessage());
        }
    }

    public static void deleteDataFreezeEvent(Long id) {
        AmpDataFreezeSettings dataFreezeEvent = getDataFreezeEventById(id);
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            session.delete(dataFreezeEvent);
        } catch (Exception e) {
            logger.error("Exception from deleteDataFreezeEvent: " + e.getMessage());
        }
    }

    public static Integer getFreezeEventsTotalCount() {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select count(*) from " + AmpDataFreezeSettings.class.getName();
        Query query = dbSession.createQuery(queryString);
        return (Integer) query.uniqueResult();
    }

    public static AmpDataFreezeSettings getLatestFreezingConfiguration() {
        String queryString = " select ampDataFreezeSettings from " + AmpDataFreezeSettings.class.getName()
                + " ampDataFreezeSettings where ampDataFreezeSettings.freezingDate = ("
                + "select max(s.freezingDate) from " + AmpDataFreezeSettings.class.getName() + " s ) "
                + "and ampDataFreezeSettings.executed = true ";
        Query q = createHibernateQuery(queryString);
        return (AmpDataFreezeSettings) q.uniqueResult();
    }

    public static AmpActivityFrozen getAmpActivityFrozenForActivity(Long ampActivityId) {
        String queryString = "select ampActivityFrozen from " + AmpActivityFrozen.class.getName()
                + " ampActivityFrozen "
                + " where ampActivityFrozen.activityGroup.ampActivityLastVersion.ampActivityId=:ampActivityId "
                + " and ampActivityFrozen.frozen=true and "
                + " (ampActivityFrozen.deleted = false or ampActivityFrozen.deleted = null)";
        Query query = createHibernateQuery(queryString);
        query.setLong("ampActivityId", ampActivityId);
        return (AmpActivityFrozen) query.uniqueResult();
    }

    private static Query createHibernateQuery(String queryString) {
        Session dbSession = PersistenceManager.getSession();
        return dbSession.createQuery(queryString);
    }

    public static List<AmpDataFreezeSettings> getDataFreeEventsList(Integer offset, Integer count, String orderBy,
            String sort, Integer total) {
        Integer maxResults = count == null ? DataFreezeConstants.DEFAULT_RECORDS_PER_PAGE : count;
        Integer startAt = (offset == null || offset > total) ? DataFreezeConstants.DEFAULT_OFFSET : offset;
        String orderByColumn = (orderBy == null) ? DataFreezeConstants.DEFAULT_SORT_COLUMN : orderBy;
        String sortOrder = (sort == null) ? DataFreezeConstants.ORDER_DESC : sort;

        Session dbSession = PersistenceManager.getSession();
        String queryString = "select dataFreezeEvents from " + AmpDataFreezeSettings.class.getName()
                + " dataFreezeEvents order by " + orderByColumn + " " + sortOrder;
        Query query = dbSession.createQuery(queryString);
        query.setFirstResult(startAt);
        query.setMaxResults(maxResults);

        return query.list();
    }

    /**
     * we mark all frozen activities as deleted since we can have only on set of
     * frozen activities
     */
    public static void disablePreviousFrozenActivities() {
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                SQLUtils.executeQuery(conn, String.format("UPDATE AMP_ACTIVITY_FROZEN SET DELETED = %s", "TRUE"));
            }
        });

    }

    public static Set<Long> getFrozenActivities() {
        final Set<Long> frozenActivities = new LinkedHashSet<>();

        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                String query = "select ag.amp_activity_last_version_id from amp_activity_frozen af,amp_activity_group ag where "
                        + " ag.amp_activity_group_id=af.amp_activity_group_id and af.frozen=true and af.deleted=false";
                frozenActivities.addAll(SQLUtils.fetchLongs(conn, query));
            }
        });
        return frozenActivities;
    }

    public static void freezeActivitiesForFreezingDate(AmpDataFreezeSettings currentFreezingEvent,
            Set<Long> activitiesId) {
        SimpleDateFormat fullDateNoHourFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
        final String fullDate = fullDateNoHourFormatter.format(currentFreezingEvent.getFreezingDate()) + " 23:59:59";
        final Integer year = Integer.parseInt(yearFormatter.format(currentFreezingEvent.getFreezingDate()));
        final Long freezingId = currentFreezingEvent.getAmpDataFreezeSettingsId();
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                String query = "INSERT INTO AMP_ACTIVITY_FROZEN SELECT nextval('AMP_ACTIVITY_FROZEN_SEQ'), "
                        + " amp_activity_group_id, %s,true,false FROM amp_activity_group ag "
                        + " WHERE ag.amp_activity_last_version_id IN (SELECT amp_activity_id "
                        + " FROM amp_funding f WHERE exists (SELECT * FROM amp_funding_detail fd "
                        + " WHERE fd.transaction_date <= '%s' "
                        + " AND fd.amp_funding_id=f.amp_funding_id) OR exists (SELECT 1 "
                        + " FROM AMP_FUNDING_MTEF_PROJECTION fp "
                        + " WHERE date_part('year', fp.projection_date) <= %s "
                        + " AND fp.amp_funding_id=f.amp_funding_id) ) and" + " ag.amp_activity_last_version_id in (%s)";

                SQLUtils.executeQuery(conn,
                        String.format(query, freezingId, fullDate, year, Util.toCSStringForIN(activitiesId)));
            }
        });
        currentFreezingEvent.setExecuted(true);
        DataFreezeUtil.saveDataFreezeEvent(currentFreezingEvent);
    }

    public static void unfreezeAll() {
        try {
            Session dbSession = PersistenceManager.getSession();
            String queryString = "update " + AmpActivityFrozen.class.getName() + " af set af.frozen = false";
            dbSession.createQuery(queryString).executeUpdate();
        } catch (Exception e) {
            logger.error("Exception from unfreezeAll: " + e.getMessage());
        }
    }

    /**
     * Return a freezing event if today is the freezing date
     * 
     * @return
     */
    public static AmpDataFreezeSettings getCurrentFreezingEvent() {
        Long ampDataFreezeSettingsId = getLastNonExecutedFreezingEvent();
        if (ampDataFreezeSettingsId.equals(0L)) {
            return null;
        } else {
            return getDataFreezeEventById(ampDataFreezeSettingsId);
        }
    }

    private static Long getLastNonExecutedFreezingEvent() {
        final ValueWrapper<Long> freezingEventId = new ValueWrapper<Long>(0L);

        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                String todaysFreezingEventQuery = "SELECT max(id) FROM  amp_data_freeze_settings "
                        + " WHERE CURRENT_DATE >=(freezing_date::date + coalesce(grace_period, 0)) "
                        + " AND executed = FALSE AND enabled = TRUE "
                        + "  AND (freezing_date::date + coalesce(grace_period, 0)) = "
                        + " (SELECT min((freezing_date::date + coalesce(grace_period, 0))) "
                        + " FROM amp_data_freeze_settings WHERE executed = FALSE AND enabled = TRUE "
                        + " and CURRENT_DATE <=(freezing_date::date + coalesce(grace_period, 0)))";
                RsInfo rsi = SQLUtils.rawRunQuery(conn, todaysFreezingEventQuery, null);
                if (rsi.rs.next()) {
                    freezingEventId.value = rsi.rs.getLong(1);
                }
                rsi.close();
            }
        });
        return freezingEventId.value;
    }

    public static List<AmpDataFreezeSettings> getEnabledDataFreezeEvents(
            AmpDataFreezeSettings.FreezeOptions freezeOption) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select dataFreezeEvent from " + AmpDataFreezeSettings.class.getName()
                + " dataFreezeEvent where dataFreezeEvent.enabled = true";
        if (freezeOption != null) {
            queryString += " and dataFreezeEvent.freezeOption = :freezeOption";
        }

        queryString += " order by ampDataFreezeSettingsId asc";

        Query query = dbSession.createQuery(queryString);
        if (freezeOption != null) {
            query.setParameter("freezeOption", freezeOption);
        }
        return query.list();
    }

    /**
     * Get list of active users and assigned to workspaces
     * 
     * @return List<User> users
     */
    public static List<User> getUsers() {
        Session session = PersistenceManager.getRequestDBSession();
        String teamMembersQuery = "select team from " + AmpTeamMember.class.getName() + " team "
                + "join team.user user "
                + "where user.banned = false";
        Query query = session.createQuery(teamMembersQuery);
        List<AmpTeamMember> teamMembers = query.list();
        
        Set<User> users = teamMembers.stream()
                .map(AmpTeamMember::getUser)
                .collect(Collectors.toSet());
        
        return new ArrayList<User>(users);
    }

    public static AmpDataFreezeExclusion findDataFreezeExclusion(Long activityId, Long dataFreezeEventId) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select exclusion from " + AmpDataFreezeExclusion.class.getName()
                + " exclusion where exclusion.activity.ampActivityId = :ampActivityId and exclusion.dataFreezeEvent.ampDataFreezeSettingsId = :ampDataFreezeSettingsId";
        Query query = dbSession.createQuery(queryString);
        query.setParameter("ampActivityId", activityId);
        query.setParameter("ampDataFreezeSettingsId", dataFreezeEventId);

        List<AmpDataFreezeExclusion> lst = query.list();
        return (lst.size() > 0) ? lst.get(0) : null;
    }

    public static List<AmpDataFreezeExclusion> findAllDataFreezeExclusion() {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select exclusion from " + AmpDataFreezeExclusion.class.getName() + " exclusion";
        Query query = dbSession.createQuery(queryString);
        return query.list();
    }

    public static void unfreezeActivities(Set<Long> activitiesIdToUnFreeze) {
        Session dbSession = PersistenceManager.getSession();
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                String query = " update amp_activity_frozen set frozen = false "
                        + " where deleted = false and amp_activity_group_id "
                        + " in (select amp_activity_group_id from amp_activity_group "
                        + " where amp_activity_last_version_id in (%s)) ";
                SQLUtils.executeQuery(conn, String.format(query, Util.toCSStringForIN(activitiesIdToUnFreeze)));
            }
        });

    }

    public static Date getFreezingDate(AmpDataFreezeSettings event) {
        Date freezingDate;
        if (event.getGracePeriod() != null) {
            freezingDate = AmpDateUtils.getDateAfterDays(event.getFreezingDate(), event.getGracePeriod());
        } else {
            freezingDate = event.getFreezingDate();
        }
        return freezingDate;
    }

    public static boolean freezeDateExists(Long ampDataFreezeSettingsId, Date freezingDate) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select event from " + AmpDataFreezeSettings.class.getName()
                + " event where event.freezingDate = :freezingDate ";
        if (ampDataFreezeSettingsId != null) {
            queryString += " and event.ampDataFreezeSettingsId != :ampDataFreezeSettingsId ";
        }

        Query query = dbSession.createQuery(queryString);
        query.setParameter("freezingDate", freezingDate);
        if (ampDataFreezeSettingsId != null) {
            query.setParameter("ampDataFreezeSettingsId", ampDataFreezeSettingsId);
        }
        return query.list().size() > 0;

    }

    public static boolean openPeriodOverlaps(Long ampDataFreezeSettingsId, Date openPeriodStart, Date openPeriodEnd) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select event from " + AmpDataFreezeSettings.class.getName()
                + " event where event.openPeriodStart <= :openPeriodEnd and event.openPeriodEnd >= :openPeriodStart ";
        if (ampDataFreezeSettingsId != null) {
            queryString += " and event.ampDataFreezeSettingsId != :ampDataFreezeSettingsId ";
        }

        Query query = dbSession.createQuery(queryString);
        query.setParameter("openPeriodStart", openPeriodStart);
        query.setParameter("openPeriodEnd", openPeriodEnd);
        if (ampDataFreezeSettingsId != null) {
            query.setParameter("ampDataFreezeSettingsId", ampDataFreezeSettingsId);
        }
        return query.list().size() > 0;
    }
}
