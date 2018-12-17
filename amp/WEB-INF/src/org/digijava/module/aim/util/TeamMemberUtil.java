/*
 * TeamMemberUtil.java
 * Created : 17-Feb-2006
 */
package org.digijava.module.aim.util;


import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.user.User;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpAnalyticalReport;
import org.digijava.module.aim.dbentity.AmpComments;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamMemberRoles;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.calendar.dbentity.AmpCalendar;
import org.digijava.module.calendar.dbentity.AmpCalendarAttendee;
import org.digijava.module.contentrepository.helper.TeamMemberMail;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;

public class TeamMemberUtil {

    private static Logger logger = Logger.getLogger(TeamMemberUtil.class);

    public static Hashtable<Long, User> users = new Hashtable<Long, User>();
    public static Hashtable<Long, AmpTeamMember> atmUsers = new Hashtable<Long, AmpTeamMember>();

    public static AmpTeamMemberRoles headRole = getAmpTeamHeadRole();

    public static User getUserEntityByTMId(Long teamMemberId) {
        User u = users.get(teamMemberId);
        if (u == null) {
            AmpTeamMember atm = getAmpTeamMemberCached(teamMemberId);
            if (atm != null) {
                u = atm.getUser();
                if (teamMemberId != null && u != null) {
                    users.put(teamMemberId, u);
                }
            }
        }
        return u;

    }

    public static AmpTeamMember getAmpTeamMemberCached(Long id) {

        AmpTeamMember ampMember = TeamMemberUtil.atmUsers.get(id);
        {
            ampMember = getAmpTeamMember(id);
            if (id != null && ampMember != null) {
                Hibernate.initialize(ampMember.getUser().getAssignedOrgs());
                atmUsers.put(id, ampMember);
            }
            return ampMember;
        }
    }

    //this returns the last one -- it should return a list of all team members associated to a userID
    public static AmpTeamMember getAmpTeamMemberByUserId(Long userId) {
        AmpTeamMember ampMember = null;
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            // modified by Priyajith
            // desc:used select query instead of session.load
            // start
            String queryString = "select t from "
                    + AmpTeamMember.class.getName() + " t "
                    + "where (t.deleted is null or t.deleted = false) and (t.user=:userId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("userId", userId, LongType.INSTANCE);
            qry.setCacheable(true);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                ampMember = (AmpTeamMember) itr.next();
            }
            // end
        } catch (Exception ex) {
            logger.error("Unable to get team member ", ex);
        }


        return ampMember;
    }

    public static AmpTeamMember getAmpTeamMember(Long id) {
        AmpTeamMember ampMember = null;
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            // modified by Priyajith
            // desc:used select query instead of session.load
            // start
            String queryString = "select t from "
                    + AmpTeamMember.class.getName() + " t "
                    + "where (t.deleted is null or t.deleted = false) and (t.ampTeamMemId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, LongType.INSTANCE);
            qry.setCacheable(true);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                ampMember = (AmpTeamMember) itr.next();
            }
            // end
        } catch (Exception ex) {
            logger.error("Unable to get team member ", ex);
        }


        return ampMember;
    }

    public static AmpTeamMember getMember(String email) {

        User user = DbUtil.getUser(email);
        if (user == null) {
            return null;
        }

        Session session = null;
        Query qry = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = "select tm from "
                    + AmpTeamMember.class.getName()
                    + " tm where (tm.deleted is null or tm.deleted = false) and (tm.user=:user)";
            qry = session.createQuery(queryString);
            qry.setParameter("user", user.getId(), LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                member = (AmpTeamMember) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get team member", e);
        }
        return member;

    }

    public static Collection<AmpTeamMember> getAllTeamMembers() {
        Session session = null;
        Query qry = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = "select tm from " + AmpTeamMember.class.getName() + " tm where (tm.deleted is null or" +
                    " tm.deleted = false) ";
            qry = session.createQuery(queryString);
            return qry.list();
        } catch (Exception e) {
            logger.error("Unable to get team members", e);
        }
        return null;
    }

    public static TeamMember getTMTeamHead(Long teamId) {
        TeamMember tm = null;
        AmpTeamMember ampMem = getTeamHead(teamId);
        if (ampMem != null) {
            tm = new TeamMember(ampMem);
        }
        return tm;

    }

    public static TeamMember getTeamMember(Long ampTeamMemberId) {
        return new TeamMember(getAmpTeamMember(ampTeamMemberId));
    }

    public static AmpTeamMember getTeamHead(Long teamId) {

        Session session = null;
        Query qry = null;
        AmpTeamMember member = null;

        try {

            AmpTeamMemberRoles ampRole = getAmpTeamHeadRole();
            session = PersistenceManager.getSession();
            String queryString = "select tm from "
                    + AmpTeamMember.class.getName()
                    + " tm where (tm.deleted is null or tm.deleted = false) and (tm.ampTeam=:teamId) and (tm" +
                    ".ampMemberRole=:role)";
            qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, LongType.INSTANCE);
            qry.setParameter("role", ampRole.getAmpTeamMemRoleId(),
                    LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                member = (AmpTeamMember) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get team member", e);
        }
        return member;
    }

    public static List<AmpTeamMember> getTeamHeadAndApprovers(Long teamId) {

        Session session = null;
        Query qry = null;
        List<AmpTeamMember> members = new ArrayList<AmpTeamMember>();
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select teamMember from "
                    + AmpTeamMember.class.getName()
                    + " teamMember inner join teamMember.ampTeam t "
                    + " inner join teamMember.ampMemberRole role "
                    + " where (teamMember.deleted is null or teamMember.deleted = false) and t.ampTeamId=:teamId and "
                    + " (role.teamHead=true or role.approver=true)";
            qry = session.createQuery(queryString);
            qry.setLong("teamId", teamId);
            @SuppressWarnings("unchecked")
            List<AmpTeamMember> list = (List<AmpTeamMember>) qry.list();
            if (list != null) {
                members.addAll(list);
            }
        } catch (Exception e) {
            logger.error("Unable to get team member", e);
        }
        return members;
    }

    private static void deleteTeamMember(AmpTeamMember teamMember) {
        Session session = PersistenceManager.getSession();
        boolean softRemove = false;

        softRemove = hasInfoRelatedToAmpTeamMember(session, teamMember.getAmpTeamMemId());

        if (softRemove) {
            teamMember.setDeleted(true);
        } else {
            session.delete(teamMember);
        }
    }

    private static boolean hasInfoRelatedToAmpTeamMember(Session session, Long ampTeamMemberId) {
        boolean hasInfoRelated = false;

        Collection<AmpActivityVersion> relatedActivities = ActivityUtil.getActivitiesRelatedToAmpTeamMember(session,
                ampTeamMemberId);

        hasInfoRelated = (relatedActivities.size() > 0);

        // Verify for calendar info
        hasInfoRelated |= hasInfo(session, "select count(*) from " + AmpCalendar.class.getName() + " cal "
                + " where (cal.member.ampTeamMemId=:memberId) ", ampTeamMemberId);

        // Verify for contacts that are created by this user
        hasInfoRelated |= hasInfo(session, "select count(*) from " + AmpContact.class.getName() + " cont where "
                + " (cont.creator=:memberId) ", ampTeamMemberId);

        // Verify for calendar attendee info
        hasInfoRelated |= hasInfo(session, "select count(*) from " + AmpCalendarAttendee.class.getName() + " calatt "
                + "where calatt.member.ampTeamMemId=:memberId ", ampTeamMemberId);

        // Verify for reports that are owned by this user
        hasInfoRelated |= hasInfo(session, "select count(*) from " + AmpReports.class.getName() + " rep " + "where rep"
                + ".ownerId=:memberId ", ampTeamMemberId);

        return hasInfoRelated;
    }

    private static boolean hasInfo(Session session, String queryString, Long ampTeamMemberId) {
        Query query = session.createQuery(queryString).setLong("memberId", ampTeamMemberId);
        return ((Integer)query.uniqueResult() > 0);
    }

    public static Collection getMembersUsingRole(Long roleId) {
        Session session = null;
        Query qry = null;
        Collection members = new ArrayList();

        try {
            session = PersistenceManager.getSession();
            String queryString = "select tm from "
                    + AmpTeamMember.class.getName()
                    + " tm where (tm.deleted is null or tm.deleted = false) and (tm.ampMemberRole=:roleId)";

            qry = session.createQuery(queryString);
            qry.setParameter("roleId", roleId, LongType.INSTANCE);
            members = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get all team members", e);
        }
        logger.debug("returning members");
        return members;
    }

    public static List<TeamMember> getAllTeamMembers(Long teamId) {
        try {
            
            Session session = PersistenceManager.getSession();
            String queryString = "select distinct tm from " + AmpTeamMember.class.getName() + " tm "
                    + "where (tm.deleted is null or tm.deleted = false) ";

            if (teamId != null) {
                queryString += " and (tm.ampTeam=:teamId)";
            }

            Query qry = session.createQuery(queryString);

            if (teamId != null) {
                qry.setParameter("teamId", teamId, LongType.INSTANCE);
            }

            List<AmpTeamMember> atms = qry.list();
            List<TeamMember> members = new ArrayList<>();
            for (AmpTeamMember atm : atms) {
                members.add(new TeamMember(atm));
                
            }
            Collections.sort((List<TeamMember>) members, new TeamMemberUtil.TeamMemberComparator());

            return members;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static List<TeamMemberMail> getAllTeamMembersMail(Long teamId) {
        try {

            Session session = PersistenceManager.getSession();
            String queryString = "select distinct tm from " + AmpTeamMember.class.getName() + " tm "
                    + "where (tm.deleted is null or tm.deleted = false) ";

            if (teamId != null) {
                queryString += " and (tm.ampTeam=:teamId)";
            }

            Query qry = session.createQuery(queryString);

            if (teamId != null) {
                qry.setParameter("teamId", teamId, LongType.INSTANCE);
            }

            List<AmpTeamMember> atms = qry.list();
            List<TeamMemberMail> members = new ArrayList<>();
            for (AmpTeamMember atm : atms) {
                members.add(new TeamMemberMail(atm.getAmpTeamMemId(),
                        atm.getAmpTeam().getAmpTeamId(), atm.getUser().getEmail()));
            }

            return members;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<Long, List<TeamMember>> getAllTeamsWithMembers() {
        Map<Long, List<TeamMember>> result = new HashMap<>();
        List<TeamMember> teamMembers = getAllTeamMembers(null);

        for (TeamMember atm : teamMembers) {
            Long teamId = atm.getTeamId();
            if (result.get(teamId) == null) {
                result.put(teamId, new ArrayList<TeamMember>());
            }

            result.get(teamId).add(atm);
        }

        Iterator<Long> iterator = result.keySet().iterator();

        return result;
    }

    public static List<TeamMember> getAllMembersExcludingTL(Long teamId) {
        Session session = null;
        Query qry = null;
        List<TeamMember> members = null;
        AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select tm from " + AmpTeamMember.class.getName() + " tm where (tm.deleted is null or" +
                    " tm.deleted = false) and tm.ampTeam=:teamId and tm.ampMemberRole!=" + headRole
                    .getAmpTeamMemRoleId() + " order by tm.user.firstNames,tm.user.lastName";
            qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, LongType.INSTANCE);
            List<AmpTeamMember> ampTeamMembers = qry.list();
            if (ampTeamMembers != null) {
                Iterator<AmpTeamMember> itr = ampTeamMembers.iterator();
                while (itr.hasNext()) {
                    TeamMember tm = new TeamMember((AmpTeamMember) itr.next());
                    if (members == null) {
                        members = new ArrayList<TeamMember>();
                    }
                    members.add(tm);
                }
            }
        } catch (Exception e) {
            logger.error("Unable to get all team members", e);
        }
        return members;
    }

    /**
     * checks whether some tm's have right to make document public and if they shouldn't have this right, changes them
     *
     * @param teamId
     * @param memberIds
     */
    public static void removeTeamMembersResourcePublishingRights(Long teamId, List<Long> memberIds) {
        Session session = null;
        Query qry = null;
        AmpTeamMemberRoles headRole = getAmpTeamHeadRole();
        try {
            session = PersistenceManager.getRequestDBSession();
            String qhl = "update " + AmpTeamMember.class.getName() + " tm set tm.publishDocPermission=false where tm" +
                    ".ampTeam=" + teamId + " and tm.publishDocPermission=true and tm.ampMemberRole!=" + headRole
                    .getAmpTeamMemRoleId();
            if (memberIds != null && memberIds.size() > 0) {
                qhl += " and tm.ampTeamMemId not in (:memberIds)";
            }
            qry = session.createQuery(qhl);
            if (memberIds != null && memberIds.size() > 0) {
                qry.setParameterList("memberIds", memberIds);
            }
            qry.executeUpdate();
        } catch (Exception e) {
            logger.error("couldn't update team members", e);
        }
    }

    /**
     * checks whether some tm's have no rights to make document public and if they should have this right, changes them
     *
     * @param teamId
     * @param selectedMemberIds
     */
    public static void grantMembersResourcePublishingRights(Long teamId, List<Long> selectedMemberIds) {
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String qhl = "update " + AmpTeamMember.class.getName() + " tm set tm.publishDocPermission=true where (tm" +
                    ".deleted is null or tm.deleted = false) and tm.ampTeam=" + teamId + " and (tm" +
                    ".publishDocPermission is null or tm.publishDocPermission=false)";
            if (selectedMemberIds != null && selectedMemberIds.size() > 0) {
                qhl += " and tm.ampTeamMemId in (:memberIds)";
            }
            qry = session.createQuery(qhl);
            if (selectedMemberIds != null && selectedMemberIds.size() > 0) {
                qry.setParameterList("memberIds", selectedMemberIds);
            }
            qry.executeUpdate();
        } catch (Exception e) {
            logger.error("couldn't update team members", e);
        }
    }

    private static class TeamMemberComparator implements Comparator<TeamMember> {
        Locale locale;
        Collator collator;

        public TeamMemberComparator() {
            this.locale = new Locale("en", "EN");
        }

        public TeamMemberComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(TeamMember o1, TeamMember o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);

            int result = (o1.getMemberName() == null || o2.getMemberName() == null) ? 0 : collator.compare
                    (o1.getMemberName().toLowerCase(), o2.getMemberName().toLowerCase());
            return result;
        }
    }


    public static List<String> getAllTeamMemberUserMails() {
        return PersistenceManager.getSession().doReturningWork(conn -> {
            String query =  "SELECT DISTINCT email FROM dg_user u "
                    + "JOIN amp_team_member tm ON tm.user_ = u.id "
                    + "WHERE tm.deleted IS NOT TRUE";
            return SQLUtils.fetchAsList(conn, query, 1);
        });
    }

    public static Collection getAllMembersUsingActivity(Long activityId) {
        Session session = null;
        Collection col = null;

        try {

            session = PersistenceManager.getSession();
            // modified by Priyajith
            // desc:used select query instead of
            // session.load
            // start
            String queryString = "select a from " + AmpActivityVersion.class.getName()
                    + " a " + "where (a.ampActivityId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", activityId, LongType.INSTANCE);
            Iterator itrTemp = qry.list().iterator();
            AmpActivityVersion ampActivity = null;
            while (itrTemp.hasNext()) {
                ampActivity = (AmpActivityVersion) itrTemp.next();
            }
            // end

            Iterator itr = ampActivity.getMember().iterator();
            col = new ArrayList();
            while (itr.hasNext()) {
                col.add(itr.next());
            }
        } catch (Exception e) {
            logger.error("Exception from getAllMembersUsingActivity()", e);
        }
        return col;
    }

    public static Collection<AmpActivityVersion> getAllMemberActivities(Long memberId) {
        Session session = null;
        Collection<AmpActivityVersion> col = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            AmpTeamMember ampMember = (AmpTeamMember) session.load(AmpTeamMember.class, memberId);
            col = ampMember.getActivities();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return col;
    }


    public static Collection getAllMemberAmpActivities(Long memberId) {
        Session session = null;
        Collection col = new ArrayList();

        try {

            session = PersistenceManager.getSession();
            // modified by Priyajith
            // Desc: removed the usage of session.load and used the select query
            // start
            String queryString = "select t from "
                    + AmpTeamMember.class.getName() + " t "
                    + "where (t.deleted is null or t.deleted = false) and (t.ampTeamMemId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", memberId, LongType.INSTANCE);

            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                AmpTeamMember member = (AmpTeamMember) itr.next();
                col.addAll(member.getActivities());
            }

        } catch (Exception e) {
            logger.error("Exception from getAllMemberActivities()", e);
        }
        return col;
    }

    public static ArrayList getAllMemberReports(Long id) {
        Session session = null;
        ArrayList col = new ArrayList();

        try {

            session = PersistenceManager.getSession();
            // modified by Priyajith
            // Desc: removed the usage of session.load and used the select query
            // start
            String queryString = "select t from "
                    + AmpTeamMember.class.getName() + " t "
                    + "where (t.deleted is null or t.deleted = false) and (t.ampTeamMemId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, LongType.INSTANCE);

            Iterator itrTemp = qry.list().iterator();
            AmpTeamMember ampTeamMember = null;
            while (itrTemp.hasNext()) {
                ampTeamMember = (AmpTeamMember) itrTemp.next();
            }
            // end

            Iterator itr = ampTeamMember.getReports().iterator();
            while (itr.hasNext()) {
                AmpReports ampReports = (AmpReports) itr.next();
                {
                    logger.debug("inside iter");
                    col.add(ampReports);
                }
            }
            Collections.sort(col);
        } catch (Exception e) {
            logger.error("Exception from getAllMemberReports()");
            logger.error(e.toString());
            e.printStackTrace(System.out);
        }
        return col;
    }

    public static AmpTeamMember getAmpTeamMember(User user) {
        Session session = null;
        Query qry = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = "select tm from "
                    + AmpTeamMember.class.getName()
                    + " tm where (tm.deleted is null or tm.deleted = false) and (tm.user=:user)";
            qry = session.createQuery(queryString);
            qry.setParameter("user", user.getId(), LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                member = (AmpTeamMember) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get team Member", e);
        }
        return member;
    }

    public static Collection<AmpTeamMember> getAllAmpTeamMembersByAmpTeamMemberId(Collection<Long> ampTeamMemberIds) {
        Session session = null;
        Query qry = null;
        Collection<AmpTeamMember> result = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "select tm from "
                    + AmpTeamMember.class.getName()
                    + " tm where (tm.deleted is null or tm.deleted = false) and (tm.ampTeamMemId in" +
                    "(:ampTeamMemberIds))";
            qry = session.createQuery(queryString);

            qry.setParameterList("ampTeamMemberIds", ampTeamMemberIds);

            result = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get team Member", e);
        }
        return result;
    }

    public static Collection<AmpTeamMember> getAllAmpTeamMembersByUser(User user) {
        Session session = null;
        Query qry = null;
        Collection<AmpTeamMember> result = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "select tm from "
                    + AmpTeamMember.class.getName()
                    + " tm where (tm.deleted is null or tm.deleted = false) and (tm.user=:user)";
            qry = session.createQuery(queryString);
            qry.setParameter("user", user.getId(), LongType.INSTANCE);
            result = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get team Member", e);
        }
        return result;
    }

    public static AmpTeamMember getAmpTeamMemberByUserByTeam(User user, AmpTeam ampTeam) {
        Session session = null;
        Query qry = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = "select tm from "
                    + AmpTeamMember.class.getName()
                    + " tm where (tm.user=:user) and  (tm.ampTeam=:ampTeam) ";
            qry = session.createQuery(queryString);
            qry.setLong("user", user.getId());
            qry.setLong("ampTeam", ampTeam.getAmpTeamId());
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                member = (AmpTeamMember) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get team Member", e);
        }
        return member;
    }

    public static Collection<TeamMember> getTeamMembers(Long teamId) {
        Session session = null;
        Collection<TeamMember> col = new ArrayList();

        try {
            session = PersistenceManager.getSession();
            AmpTeamMember mem = getTeamHead(teamId);
            String queryString = "select tm from "
                    + AmpTeamMember.class.getName()
                    + " tm where (tm.deleted is null or tm.deleted = false) and (tm.ampTeam=:teamId) "
                    + " and (tm.ampTeamMemId!=:memId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, LongType.INSTANCE);
            qry.setParameter("memId", mem.getAmpTeamMemId(), LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                TeamMember tm = new TeamMember((AmpTeamMember) itr.next());
                col.add(tm);
            }
        } catch (Exception e) {
            logger.error("Exception from getTeamMembers()", e);
        }
        return col;
    }

    public static List<TeamMember> getMemberInformation(Long userId) {
        logger.debug("In getMemberInformation() : " + userId);
        Session session = null;
        List<TeamMember> helpers = null;
        try {
            Query q = null;
            session = PersistenceManager.getRequestDBSession();
            String query = "select new org.digijava.module.aim.helper.TeamMember(team.name,role.role) from " +
                    AmpTeamMember.class.getName() + " m"
                    + " inner join m.ampTeam team inner join m.ampMemberRole role"
                    + " where (m.deleted is null or m.deleted = false) and m.user=:memberId";
            q = session.createQuery(query);
            q.setLong("memberId", userId);
            helpers = q.list();

        } catch (Exception e) {
            logger.error("Exception in getTeamMemberInformation() : ", e);
            e.printStackTrace(System.out);
        }

        return helpers;
    }

    public static AmpTeamMemberRoles getAmpTeamHeadRole() {
        Session session = null;
        Query qry = null;
        AmpTeamMemberRoles ampRole = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = "select r from "
                    + AmpTeamMemberRoles.class.getName()
                    + " r where r.teamHead = true";
            qry = session.createQuery(queryString);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                ampRole = (AmpTeamMemberRoles) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get role", e);
        }
        return ampRole;
    }


    public static Collection<AmpTeamMemberRoles> getAllTeamMemberRoles() {
        return getAllTeamMemberRoles(true);
    }

    /**
     * searches for a "workspace member" role indirectly, through his capabilities of not being a team head and not
     * being an approver. Does not search by name, as those can be i18n-ed
     *
     * @return
     */
    public static AmpTeamMemberRoles getWorkspaceMemberTeamMemberRole() {
        for (AmpTeamMemberRoles role : getAllTeamMemberRoles()) {
            if ((!role.getTeamHead()) && (!role.isApprover())) {
                return role;
            }
        }
        // oopsie, shouldn't happen
        throw new RuntimeException("could not find a non-team-head non-approver workspace member role!");
    }


    public static Collection<AmpTeamMemberRoles> getAllTeamMemberRoles(boolean includeApprover) {
        Session session = null;
        Query qry = null;
        Collection<AmpTeamMemberRoles> roles = new ArrayList<AmpTeamMemberRoles>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select r from "
                    + AmpTeamMemberRoles.class.getName() + " r ";
            if (!includeApprover) {
                queryString += "where r.approver=false or r.teamHead=true";
            }
            qry = session.createQuery(queryString);
            roles = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get all roles", e);
        }
        return roles;
    }


    public static AmpTeamMemberRoles getAmpTeamMemberRole(Long id) {
        Session session = null;
        Query qry = null;
        AmpTeamMemberRoles role = null;

        try {
            session = PersistenceManager.getSession();
            String queryString = "select r from "
                    + AmpTeamMemberRoles.class.getName()
                    + " r where (r.ampTeamMemRoleId=:id)";
            qry = session.createQuery(queryString);
            qry.setParameter("id", id, LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                role = (AmpTeamMemberRoles) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get role", e);
        }
        return role;
    }

    public static List<AmpTeamMember> getNonManagementTeamMembers(Collection<Long> userIds) {
        return getTeamMembersByUserId(userIds).values()
                .stream()
                .flatMap(Collection::stream)
                .filter(tm -> !tm.getAmpTeam().getAccessType().equals(Constants.ACCESS_TYPE_MNGMT))
                .collect(Collectors.toList());
    }

    /**
     * Map<User.id, List<AmpTeamMember>>
     *
     * @return
     */
    public static Map<Long, List<AmpTeamMember>> getTeamMembersByUserId(Collection<Long> userIds) {
        Map<Long, List<AmpTeamMember>> res = new HashMap<>();

        if (userIds != null && !userIds.isEmpty()) {
            for (long userId : userIds) {
                res.put(userId, new ArrayList<AmpTeamMember>());
            }

            List<Object[]> teamUsers = PersistenceManager.getSession()
                    .createQuery("select tm, tm.user.id from " + AmpTeamMember.class.getName() + " tm where (tm" +
                            ".deleted is null or tm.deleted = false) and tm.user.id in :userIds")
                    .setParameterList("userIds", userIds)
                    .list();

            for (Object[] teamUser : teamUsers) {
                long userId = PersistenceManager.getLong(teamUser[1]);
                res.get(userId).add((AmpTeamMember) teamUser[0]);
            }
        }

        return res;
    }

    public static Collection<AmpTeamMember> getTeamMembers(String email) {
        User user = org.digijava.module.aim.util.DbUtil.getUser(email);
        if (user == null) {
            return null;
        }

        Session session = null;
        Query qry = null;
        List<AmpTeamMember> col = new ArrayList<AmpTeamMember>();

        try {
            session = PersistenceManager.getSession();
            String queryString = "select tm from " + AmpTeamMember.class.getName() +
                    " tm where (tm.deleted is null or tm.deleted = false) and (tm.user.id=:user)";
            qry = session.createQuery(queryString);
            qry.setCacheable(true);
            qry.setLong("user", user.getId());
            col = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get TeamMembers" + e.getMessage());
            e.printStackTrace(System.out);
        }
        Collections.sort(col, alphabeticalTeamComp);
        return col;
    }

    public static AmpTeamMember getAmpTeamMemberByEmailAndTeam(String email, Long teamId) {
        AmpTeamMember retVal = null;
        Session session = null;
        Query qry = null;
        String queryString = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select tm from " + AmpTeamMember.class.getName() + " tm where (tm.deleted is null or tm" +
                    ".deleted = false) and tm.user.email=:usermail and tm.ampTeam=" + teamId;
            qry = session.createQuery(queryString);
            qry.setString("usermail", email);
            retVal = (AmpTeamMember) qry.uniqueResult();
        } catch (Exception e) {
            logger.error("Unable to get TeamMember" + e.getMessage());
            e.printStackTrace(System.out);
        }
        return retVal;
    }

    public static AmpTeamMember getAmpTeamMemberByEmailAndTeam(String email, String teamName) {

        try {
            Session session = null;
            Query qry = null;
            String queryString = null;
            session = PersistenceManager.getRequestDBSession();
            session.flush();
            queryString = "select tm from " + AmpTeamMember.class.getName() + " tm where (tm.deleted is null or "
                    + "tm.deleted = false) and tm.user.email=:usermail and tm.ampTeam.name=:teamName";
            qry = session.createQuery(queryString);
            qry.setString("usermail", email);
            qry.setString("teamName", teamName);
            return (AmpTeamMember) qry.uniqueResult();
        } catch (Exception e) {
            logger.error("Unable to get TeamMember ", e);
        }
        return null;
    }

    /**
     * null is the smallest number
     *
     * @param a
     * @param b
     * @return
     */
    public static int nullCompare(Object a, Object b) {
        if (a == null) {
            if (b == null) {
                return -1; // null <= null (avoid crash)
            }
            return -1; // null < [non-null]
        }
        // gone till here -> a != null
        if (b == null) {
            return 1; // [non-null] > null
        }

        return 0; // [non-null] == [non-null]
    }

    public static Comparator<AmpTeamMember> alphabeticalTeamComp =
            new Comparator<AmpTeamMember>() {
                public int compare(AmpTeamMember o1,
                                   AmpTeamMember o2) {

                    int delta = nullCompare(o1, o2);
                    if (delta != 0) {
                        return delta;
                    }

                    delta = nullCompare(o1.getAmpTeam(), o2.getAmpTeam());
                    if (delta != 0) {
                        return delta;
                    }

                    delta = nullCompare(o1.getAmpTeam().getName(), o2.getAmpTeam().getName());
                    if (delta != 0) {
                        return delta;
                    }

                    return o1.getAmpTeam().getName().toLowerCase().compareTo(o2.getAmpTeam().getName().toLowerCase());
                }
            };

    public static void assignActivitiesToMember(Long memberId, Long activities[]) {
        Session session = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getSession();
            member = (AmpTeamMember) session.load(AmpTeamMember.class, memberId);
            if (member != null) {

                for (int i = 0; i < activities.length; i++) {
                    if (activities[i] != null) {
                        AmpActivityVersion activity = (AmpActivityVersion) session.load(
                                AmpActivityVersion.class, activities[i]);
                        member.getActivities().add(activity);
                    }
                }
                session.update(member);
            }
        } catch (Exception e) {
            logger.error("Unable to assign activities" + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public static void removeActivitiesFromMember(Long memberId, Long activities[]) {
        Session session = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getSession();
            member = (AmpTeamMember) session.load(AmpTeamMember.class, memberId);
            if (member != null) {

                for (int i = 0; i < activities.length; i++) {
                    if (activities[i] != null) {
                        AmpActivityVersion activity = (AmpActivityVersion) session.load(
                                AmpActivityVersion.class, activities[i]);
                        member.getActivities().remove(activity);
                    }
                }
                session.update(member);

            }
        } catch (Exception e) {
            logger.error("Unable to remove activities" + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public static Collection getUnassignedMemberActivities(Long teamId, Long memberId) {
        Collection col = null;
        Collection col1 = new ArrayList();
        Session session = null;
        Query qry = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getSession();

            String queryString = "select act from " + AmpActivity.class.getName() +
                    " act where (act.team=:id)";
            qry = session.createQuery(queryString);
            qry.setParameter("id", teamId, LongType.INSTANCE);
            col1 = qry.list();

            member = (AmpTeamMember) session.load(AmpTeamMember.class, memberId);

            col1.removeAll(member.getActivities());

        } catch (Exception e) {
            logger.error("Unable to remove activities" + e.getMessage());
            e.printStackTrace(System.out);
        }
        return col1;
    }

    public static Collection getUnassignedMemberReports(Long teamId, Long memberId) {
        Collection col = new ArrayList();

        Session session = null;
        Query qry = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getSession();

            String queryString = "select rep.report from " + AmpTeamReports.class.getName() + " rep "
                    + " where (rep.team=:id)";
            qry = session.createQuery(queryString);
            qry.setParameter("id", teamId, LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                AmpReports rep = (AmpReports) itr.next();
                col.add(rep);
            }

            member = (AmpTeamMember) session.load(AmpTeamMember.class, memberId);
            col.removeAll(member.getReports());
        } catch (Exception e) {
            logger.error("Unable to remove activities" + e.getMessage());
            e.printStackTrace(System.out);
        }
        return col;
    }

    public static void assignReportsToMember(Long memberId, Long reports[]) {
        Session session = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getSession();
            member = (AmpTeamMember) session.load(AmpTeamMember.class, memberId);
            if (member != null) {

                for (int i = 0; i < reports.length; i++) {
                    if (reports[i] != null) {
                        AmpReports report = (AmpReports) session.load(
                                AmpReports.class, reports[i]);
                        member.getReports().add(report);
                    }
                }
                session.update(member);

            }
        } catch (Exception e) {
            logger.error("Unable to assign reports" + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public static void removeReportsFromMember(Long memberId, Long reports[]) {
        Session session = null;
        AmpTeamMember member = null;

        try {
            session = PersistenceManager.getSession();
            member = (AmpTeamMember) session.load(AmpTeamMember.class, memberId);
            if (member != null) {

                for (int i = 0; i < reports.length; i++) {
                    if (reports[i] != null) {
                        AmpReports report = (AmpReports) session.load(
                                AmpReports.class, reports[i]);
                        member.getReports().remove(report);
                    }
                }
                session.update(member);

            }
        } catch (Exception e) {
            logger.error("Unable to remove reports" + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    public static Collection getAllTeamMembersToDesktop(Long teamId) {
        Session session = null;
        Query qry = null;
        Collection members = new ArrayList();

        try {
            session = PersistenceManager.getSession();

            // get the team leader of the team
            String queryString = "select t.ampTeamMemId from " + AmpTeamMember.class.getName() +
                    " t where (t.deleted is null or t.deleted = false) and (t.ampTeam=:teamid AND t" +
                    ".ampMemberRole=:roleid)";
            qry = session.createQuery(queryString);
            qry.setParameter("teamid", teamId, LongType.INSTANCE);
            qry.setParameter("roleid", 1, IntegerType.INSTANCE);
            Iterator itr = qry.list().iterator();
            Long id = new Long(-1);
            if (itr.hasNext()) {
                Object idObj = itr.next();
                id = (Long) idObj;
            }
            logger.debug("Got team leader " + id);

            // get all members of the team and also set the team leader
            // flag of the member who is the team leader
            queryString = "select tm.ampTeamMemId,usr.firstNames,"
                    + "usr.lastName from " + AmpTeamMember.class.getName()
                    + " tm, " + User.class.getName() + " usr "
                    + "where (tm.deleted is null or tm.deleted = false) and tm.user=usr.id and (tm.ampTeam=:teamId)";
            qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, LongType.INSTANCE);
            itr = qry.list().iterator();

            Object temp[] = null;
            while (itr.hasNext()) {
                temp = (Object[]) itr.next();
                TeamMember tm = new TeamMember();
                Long memId = (Long) temp[0];
                tm.setMemberId(memId);
                tm.setMemberName((String) temp[1] + " " + (String) temp[2]);
                if (memId.equals(id)) {
                    {
                        tm.setTeamHead(true);
                    }
                }
                members.add(tm);
            }
        } catch (Exception e) {
            logger.error("Unable to get all team members", e);
            e.printStackTrace(System.out);
        }
        logger.debug("returning members");
        return members;
    }


    public static void removeTeamMembers(Long id[]) {

        Session session = PersistenceManager.getSession();

        for (Long anId : id) {
            if (anId != null) {
                AmpTeamMember ampMember = (AmpTeamMember) session.load(AmpTeamMember.class, anId);
                if (isTeamLead(ampMember)) {
                    AmpTeam team = ampMember.getAmpTeam();
                    team.setTeamLead(null);
                    session.update(team);
                }

                String qryStr = "select com from " + AmpComments.class.getName()
                        + " com where (com.memberId.ampTeamMemId=:memberId)";
                Query qry = session.createQuery(qryStr).setParameter("memberId", anId, LongType.INSTANCE);
                List<AmpComments> memComments = qry.list();
                for (AmpComments comm : memComments) {
                    comm.setMemberId(null);
                    session.saveOrUpdate(comm);
                }

                qryStr = "select atr from " + AmpAnalyticalReport.class.getName() + " atr "
                        + " where (atr.owner=:memberId) ";
                qry = session.createQuery(qryStr).setLong("memberId", anId);
                List<AmpAnalyticalReport> ampAnalyticalReports = qry.list();
                if (ampAnalyticalReports != null && ampAnalyticalReports.size() > 0) {
                    for (AmpAnalyticalReport ampAnRep : ampAnalyticalReports) {
                        session.delete(ampAnRep);
                    }
                }

                deleteTeamMember(ampMember);

            }
        }
    }

    private static boolean isTeamLead(AmpTeamMember member) {
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            AmpTeam ampTeam = (AmpTeam) session.load(AmpTeam.class,
                    member.getAmpTeam().getAmpTeamId());
            if (ampTeam != null) {
                if (ampTeam.getTeamLead() != null) {
                    if (ampTeam.getTeamLead().getAmpTeamMemId() != null) {
                        if (ampTeam.getTeamLead().getAmpTeamMemId().
                                equals(member.getAmpTeamMemId())) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Unable to update team page filters" + e.getMessage());
            e.printStackTrace(System.out);
        }
        return false;
    }

    /**
     * Retrieves current TeamMember from request
     *
     * @param HttpServletRequest request
     * @return AmpTeamMember currentAmpTeamMember
     */
    public static AmpTeamMember getCurrentAmpTeamMember(HttpServletRequest request) {
        TeamMember currentTeamMember = (TeamMember) request.getSession().getAttribute(Constants.CURRENT_MEMBER);
        AmpTeamMember currentAmpTeamMember = getAmpTeamMember(currentTeamMember.getMemberId());
        return currentAmpTeamMember;
    }

    public static List getAmpTeamMembersbyDgUserId(Long userId) throws Exception {

        Session session = null;
        Query qry = null;
        List teamMembers = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select tm from "
                    + AmpTeamMember.class.getName()
                    + " tm where (tm.deleted is null or tm.deleted = false) and (tm.user.id=:user)";
            qry = session.createQuery(queryString);
            qry.setParameter("user", userId, LongType.INSTANCE);
            teamMembers = qry.list();
        } catch (HibernateException ex) {
            logger.error("Unable to get team member", ex);
            throw ex;
        }

        return teamMembers;

    }

    public static List<AmpTeam> getAllTeamsForUser(String email) {

        User user = DbUtil.getUser(email);
        if (user == null) {
            return Collections.emptyList();
        }

        Session session = null;
        Query qry = null;

        ArrayList<AmpTeam> result = new ArrayList<AmpTeam>();

        try {
            session = PersistenceManager.getSession();
            String queryString = "select tm from " + AmpTeamMember.class.getName() + " tm "
                    + " where (tm.deleted is null or"
                    + " tm.deleted = false) and (tm.user=:user)";
            qry = session.createQuery(queryString);
            qry.setParameter("user", user.getId(), LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                AmpTeamMember member = (AmpTeamMember) itr.next();
                result.add(member.getAmpTeam());
            }
        } catch (Exception e) {
            logger.error("Unable to get team member", e);
        }
        return result;

    }

    public static void addDesktopTab(Long reportId, Long teamMemberId, Integer position) {

        Session dbSession = null;
        AmpDesktopTabSelection sel = null;
        try {
            dbSession = PersistenceManager.getRequestDBSession();

            AmpTeamMember atm = (AmpTeamMember) dbSession.load(AmpTeamMember.class, teamMemberId);
            sel = new AmpDesktopTabSelection();
            sel.setIndex(position);
            sel.setOwner(atm);
            AmpReports report = (AmpReports) dbSession.load(AmpReports.class, reportId);
            sel.setReport(report);
            if (atm.getDesktopTabSelections() == null) {
                atm.setDesktopTabSelections(new TreeSet<AmpDesktopTabSelection>(AmpDesktopTabSelection
                        .tabOrderComparator));
            }
            Set<AmpDesktopTabSelection> tabs = atm.getDesktopTabSelections();
            Iterator<AmpDesktopTabSelection> iter = tabs.iterator();
            while (iter.hasNext()) {
                AmpDesktopTabSelection tab = iter.next();
                AmpReports rep = tab.getReport();
                if (rep.getAmpReportId().equals(reportId)) {
                    iter.remove();
                    report.getDesktopTabSelections().remove(tab);
                    dbSession.delete(tab);
                    break;
                }
            }
            tabs.add(sel);
            report.getDesktopTabSelections().add(sel);
            dbSession.save(sel);
        } catch (Exception e) {
            logger.error("unable to save tab", e);

        }
    }

    public static void removeDesktopTab(Long reportId, Long teamMemberId, Integer position) {

        Session dbSession = null;
        AmpDesktopTabSelection sel = null;
        try {
            dbSession = PersistenceManager.getRequestDBSession();

            AmpTeamMember atm = (AmpTeamMember) dbSession.load(AmpTeamMember.class, teamMemberId);
            Set<AmpDesktopTabSelection> tabs = atm.getDesktopTabSelections();
            AmpDesktopTabSelection tabToRemove = null;
            Iterator<AmpDesktopTabSelection> iter = tabs.iterator();
            while (iter.hasNext()) {
                AmpDesktopTabSelection tab = iter.next();
                AmpReports report = tab.getReport();
                if (report.getAmpReportId().equals(reportId)) {
                    iter.remove();
                    report.getDesktopTabSelections().remove(tab);
                    dbSession.delete(tab);
                }
            }
        } catch (Exception e) {
            logger.error("unable to save tab", e);
        }
    }

    /**
     * TODO: find all 104 reimplementations of the same thing in AMP and change them to calls of this function
     *
     * @return
     */
    public static TeamMember getLoggedInTeamMember() {
        TeamMember tm = (TeamMember) TLSUtils.getRequest().getSession().getAttribute(Constants.CURRENT_MEMBER);
        return tm;
    }

    public static boolean isHeadRole(AmpTeamMemberRoles role) {
        return (headRole == null || role == null) ? false : headRole.getAmpTeamMemRoleId().equals(role
                .getAmpTeamMemRoleId());
    }

    public static boolean isManagementWorkspace(TeamMember tm) {
        return tm != null && tm.getTeamAccessType() != null
                && "Management".equalsIgnoreCase(tm.getTeamAccessType());
    }

    public static void updateMember(AmpTeamMember atm) {
        Session session = PersistenceManager.getRequestDBSession();
        session.save(atm);
    }

    public static void assignMember(AmpTeam ampTeam, User user, Site site, AmpTeamMember atm, AmpTeamMemberRoles
            role) {
        if (atm == null) {
            AmpTeamMember newMember = new AmpTeamMember();
            newMember.setUser(user);
            newMember.setAmpTeam(ampTeam);
            newMember.setAmpMemberRole(role);

            try {
                TeamUtil.addTeamMember(newMember, site);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("error when trying to add a new member: " + newMember.getUser().getEmail() + " from "
                        + "team: "
                        + newMember.getAmpTeam().getName());
            }
        } else {
            atm.setDeleted(false);
            atm.setAmpMemberRole(role);
            updateMember(atm);
        }
    }

    public static void getActivitiesWsByTeamMember(Map<Long, Set<Long>> activitiesWs, AmpTeamMember atm) {
        TeamMember teamMember = new TeamMember(atm);
        List<Long> editableIds = ActivityUtil.getEditableActivityIdsNoSession(teamMember);
        processActivitiesId(activitiesWs, teamMember, Optional.ofNullable(editableIds)
                .orElse(Collections.emptyList()).stream());

    }

    private static void processActivitiesId(Map<Long, Set<Long>> activitiesWs, TeamMember teamMember,
                                            Stream<Long> activityStream) {
        activityStream.forEach(actId -> {
            if (!activitiesWs.containsKey(actId)) {
                activitiesWs.put(actId, new HashSet<Long>());
            }
            activitiesWs.get(actId).add(teamMember.getTeamId());
        });
    }
}
