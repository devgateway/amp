package org.digijava.module.aim.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.algo.AmpCollections;
import org.dgfoundation.amp.ar.viewfetcher.InternationalizedModelDescription;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.dbentity.Country;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.user.Group;
import org.digijava.kernel.user.User;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpComponent;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.*;
import org.digijava.module.aim.helper.fiscalcalendar.BaseCalendar;
import org.digijava.module.aim.util.caching.AmpCaching;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.Hibernate;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.query.Query;
import org.hibernate.type.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.Collator;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class DbUtil {
    private static Logger logger = Logger.getLogger(DbUtil.class);

    public static String filter(String text) {
        return filter(text, false);

    }



    /**
     * Used in the AMP-23713.xml patch. Can be reused for other tables, but
     * highly inadvisable to be edited itself.
     *
     * @param tableName
     *            the tablename
     */
    public static void removeReferencedConstraints(String tableName) {
        for (String query : generateFkeyConRemovalQueries(tableName)) {
            PersistenceManager.getSession().doWork(conn -> {
                SQLUtils.executeQuery(conn, query);
            });
        }
    }

    /**
     * Generates queries that would drop all foreign key constraints pointing to
     * specified table. Used in AMP-23713.xml, reuse, but do not edit
     *
     * @param tableName
     *            the table name to have foreign keys stripped
     * @return
     */
    private static List<String> generateFkeyConRemovalQueries(String tableName) {
        return PersistenceManager.getSession().doReturningWork(conn -> {
            String query = "SELECT 'ALTER TABLE '|| clazz.relname ||' DROP CONSTRAINT '||constr.conname||';' AS query "
                    + "FROM pg_constraint constr, pg_class clazz, pg_class clazz_f "
                    + "WHERE constr.conrelid = clazz.oid " + "AND constr.contype ='f' "
                    + "AND constr.confrelid = clazz_f.oid " + "AND clazz_f.relname ='%s'; ";
            query = String.format(query, tableName);
            return SQLUtils.fetchAsList(conn, query, 1);
        });
    }

    public static String filter(String text, boolean acute) {

        String result = null;

        if (text != null) {
            result = text.replaceAll("&", "&amp;");
            result = result.replaceAll(">", "&gt;");
            result = result.replaceAll("<", "&lt;");
            if (acute) {
                result = result.replaceAll("'", "&acute;");// "&acute;");
            } else {
                result = result.replaceAll("'", "\'");// "&acute;");
            }
            result = result.replaceAll("\"", "&quot;");

        }
        return result;

    }

    public static String deFilter(String text, boolean acute) {
        String result = null;

        if (text != null) {
            result = text.replaceAll("&amp;", "&");
            result = result.replaceAll("&gt;", ">");
            result = result.replaceAll("&lt;", "<");
            if (acute) {
                result = result.replaceAll("&acute;", "'");// "&acute;");
            } else {
                result = result.replaceAll("\'", "'");// "&acute;");
            }
            result = result.replaceAll("&quot;", "\"");

        }
        return result;

    }

    /**
     * Removes the team-reports and member-reports association table.
     * 
     * @param reportId
     *            A Long array of the reports to be updated
     * @param teamId
     *            The teamId of the team whose association with the specified
     *            reports must be removed. When the teams are dissociated with
     *            the reports, the association from the members of that team
     *            also gets removed.
     */
    public static void removeTeamReports(Long[] reportId, Long teamId) {
        Session session = null;

        if (reportId == null || reportId.length == 0)
            return;

        try {
            session = PersistenceManager.getRequestDBSession();
            // beginTransaction();
            //
            String queryString = "select tm from " + AmpTeamMember.class.getName() + " tm where (tm.ampTeam=:teamId)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, LongType.INSTANCE);
            //
            Collection col = qry.list();
            if (col != null && col.size() > 0) {
                for (Long aLong : reportId) {
                    if (aLong != null) {
                        queryString = "select r from " + AmpReports.class.getName() + " r where (r.ampReportId=:repId)";
                        qry = session.createQuery(queryString);
                        qry.setParameter("repId", aLong, LongType.INSTANCE);
                        Iterator itr = qry.list().iterator();
                        if (itr.hasNext()) {
                            AmpReports ampReport = (AmpReports) itr.next();
                            if (ampReport.getMembers() != null) {
                                /*
                                 * removing the team members association with
                                 * the report
                                 */
                                col.forEach(ampReport.getMembers()::remove);
                                if (ampReport.getDesktopTabSelections() != null) {
                                    for (AmpDesktopTabSelection adts : ampReport.getDesktopTabSelections()) {
                                        if (adts.getOwner().getAmpTeam().getAmpTeamId().equals(teamId)) {
                                            adts.getOwner().getDesktopTabSelections().remove(adts);
                                            adts.setOwner(null);
                                            adts.setReport(null);
                                            ampReport.getDesktopTabSelections().remove(adts);
                                        }
                                    }
                                }
                                session.update(ampReport);
                            }
                        }

                        /*
                         * removing the teams association with the report
                         */
                        queryString = "select tr from " + AmpTeamReports.class.getName()
                                + " tr where (tr.team=:teamId) and " + " (tr.report=:repId)";
                        qry = session.createQuery(queryString);
                        qry.setParameter("teamId", teamId, LongType.INSTANCE);
                        qry.setParameter("repId", aLong, LongType.INSTANCE);
                        itr = qry.list().iterator();
                        if (itr.hasNext()) {
                            AmpTeamReports ampTeamRep = (AmpTeamReports) itr.next();
                            ampTeamRep.setReport(null);
                            ampTeamRep.setTeam(null);
                            session.save(ampTeamRep);
                            session.delete(ampTeamRep);
                        }
                    }
                }
            }
            // tx.commit();
        } catch (Exception e) {
            logger.error("Exception from updateMemberReports");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Associated the reports with the given team
     * 
     * @param reportId
     *            The Long array of reportIds which are to be associated with
     *            the given team
     * @param teamId
     *            The team id of the team to which the reports are to be
     *            assigned
     * @param teamMemberId
     *            the teamMemer
     */
    public static void addTeamReports(Long[] reportId, Long teamId, Long ampMemberId) {
        Session session = null;

        try {
            session = PersistenceManager.getSession();
            // beginTransaction();

            String queryString = "select tm from " + AmpTeam.class.getName() + " tm where (tm.ampTeamId=:teamId)";

            Query qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            AmpTeam team = null;
            if (itr.hasNext()) {
                team = (AmpTeam) itr.next();
            }
            if (team != null) {
                if (reportId != null && reportId.length > 0) {
                    queryString = "select rep from " + AmpReports.class.getName() + " rep where rep.ampReportId in (";
                    StringBuffer temp = new StringBuffer();
                    for (int i = 0; i < reportId.length; i++) {
                        temp.append(reportId[i]);
                        if ((i + 1) != reportId.length) {
                            temp.append(",");
                        }
                    }
                    temp.append(")");
                    queryString += temp;
                    qry = session.createQuery(queryString);
                    logger.debug("Query :" + qry.getQueryString());
                    itr = qry.list().iterator();
                    while (itr.hasNext()) {
                        AmpReports report = (AmpReports) itr.next();
                        if (report != null) {
                            String tempQry = "select teamRep from " + AmpTeamReports.class.getName()
                                    + " teamRep where (teamRep.team=:tId) and " + " (teamRep.report=:rId)";
                            Query tmpQry = session.createQuery(tempQry);
                            tmpQry.setParameter("tId", team.getAmpTeamId(), LongType.INSTANCE);
                            tmpQry.setParameter("rId", report.getAmpReportId(), LongType.INSTANCE);
                            Iterator tmpItr = tmpQry.list().iterator();
                            if (!tmpItr.hasNext()) {
                                AmpTeamReports tr = new AmpTeamReports();
                                tr.setTeam(team);
                                tr.setReport(report);
                                tr.setTeamView(false);
                                session.save(tr);
                            }
                        }

                        // //here we should

                        AmpTeamMember ampTeamMember = null;
                        // if(report.getOwnerId()!=null){
                        // ampTeamMember=(AmpTeamMember)
                        // session.get(AmpTeamMember.class,
                        // report.getOwnerId().getAmpTeamMemId());
                        // }else {
                        ampTeamMember = (AmpTeamMember) session.get(AmpTeamMember.class, ampMemberId);
                        // }
                        Set reportSet = ampTeamMember.getReports();
                        // reportSet.add(ampReports); // Not needed because it
                        // is set from ampReports object
                        report.getMembers().add(ampTeamMember);
                        session.saveOrUpdate(ampTeamMember);

                    }
                }
            }
            // tx.commit();
        } catch (Exception e) {
            logger.error("Exception from addTeamReports()");
            logger.error(e.getMessage());
        }
    }

    public static AmpReports getAmpReports(Long id) {
        Session session = null;
        AmpReports report = null;
        try {
            session = PersistenceManager.getSession();
            report = (AmpReports) session.get(AmpReports.class, id);
        } catch (Exception ex) {
            logger.error("Unable to get AmpReports by Id :" + ex);
        }
        return report;
    }

    public static Collection getDisbursementsFundingOfIPAContract(IPAContract c) {
        Session session = null;
        Collection<AmpFundingDetail> fundingDetails = new ArrayList<AmpFundingDetail>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from " + AmpFundingDetail.class.getName() + " f "
                    + "where (f.contract=:cId) and f.transactionType=1";
            Query qry = session.createQuery(queryString);
            qry.setParameter("cId", c.getId(), LongType.INSTANCE);
            fundingDetails = qry.list();

        } catch (Exception ex) {
            logger.error("Unable to get fundingDetails (disbursements) of an IPA contract:" + ex);
        }
        return fundingDetails;
    }

    public static Collection getActivityInternalId(Long actId) {
        Session session;
        Collection col = new ArrayList<>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select aaii.* from amp_activity_internal_id aaii "
                    + "where aaii.amp_activity_id=:actId";
            Query qry = session.createNativeQuery(queryString).addEntity(AmpActivityInternalId.class);
            qry.setParameter("actId", actId, LongType.INSTANCE);
            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get Activity Internal Id :" + ex);
        }
        return col;
    }

    public static AmpRole getAmpRole(String roleCode) {
        Session session = null;
        AmpRole role = null;

        try {
            session = PersistenceManager.getRequestDBSession();

            String queryString = "select r from " + AmpRole.class.getName() + " r " + "where (r.roleCode=:code)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("code", roleCode, StringType.INSTANCE);
            for (Object o : qry.list()) role = (AmpRole) o;

        } catch (Exception e) {
            logger.error("Uanble to get role :" + e);
        }
        return role;
    }

    /**
     * this is probably useless
     *
     * @param c
     * @param id
     * @return
     */
    public static Object getObject(Class<?> c, Serializable id) {
        return PersistenceManager.getSession().load(c, id);
    }

    public static <T> T getObjectOrNull(Class<T> c, Serializable id) {
        return (T) PersistenceManager.getSession().get(c, id);
    }

    /**
     * returns null on non-existing organisation
     *
     * @param id
     * @return
     */
    public static AmpOrganisation getOrganisation(Long id) {
        Session session;
        AmpOrganisation organization = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            organization = (AmpOrganisation) session.load(AmpOrganisation.class, id);
            Hibernate.initialize(organization.getRecipients());
            Hibernate.initialize(organization.getOrganizationBudgetInfos());
            if (organization.getOrganizationBudgetInfos() != null) {
                for (AmpOrganizationBudgetInformation budgetInfo : organization.getOrganizationBudgetInfos()) {
                    Hibernate.initialize(budgetInfo.getOrganizations());
                }
            }
        } catch (Exception ex) {
            // logger.error("Unable to get organisation from database", ex);
        }
        // logger.debug("Getting organisation successfully ");
        return organization;
    }

    public static List<AmpFunding> getAmpFunding(Long ampActivityId) {
        logger.debug("getAmpFunding() with ampActivityId=" + ampActivityId);
        Session session = null;
        Query q = null;
        List<AmpFunding> ampFundings = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString;
            queryString = "select f from " + AmpFunding.class.getName() + " f where (f.ampActivityId=:ampActivityId) ";
            q = session.createQuery(queryString);
            q.setParameter("ampActivityId", ampActivityId, LongType.INSTANCE);
            ampFundings = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get AmpFunding collection from database", ex);
        }
        logger.debug("DbUtil : getAmpFunding(ampActivityId) returning collection of size  "
                + (ampFundings != null ? ampFundings.size() : 0));
        return ampFundings;
    }

    public static AmpComponent getAmpComponentDescription(Long cid) {
        return (AmpComponent) PersistenceManager.getSession().get(AmpComponent.class, cid);
    }

    public static List<AmpActivity> getCreatedOrEditedActivities(Long ampTeamId) {
        String queryString = "select act from " + AmpActivity.class.getName() + " act where (act.team=:ampTeamId)"
                + " and (act.approvalStatus in (" + Constants.ACTIVITY_NEEDS_APPROVAL_STATUS + ") ) "
                + " and act.draft != :draftValue";
        Query q = PersistenceManager.getSession().createQuery(queryString);
        q.setParameter("ampTeamId", ampTeamId, LongType.INSTANCE);
        q.setParameter("draftValue", true, BooleanType.INSTANCE);

        return q.list();
    }

    public static ApprovalStatus getActivityApprovalStatus(Long actId) {
        String qry = "select act.approvalStatus from " + AmpActivityVersion.class.getName()
                + " act where act.ampActivityId=:actId";
        Query q = PersistenceManager.getSession().createQuery(qry);
        q.setParameter("actId", actId, LongType.INSTANCE);
        List res = q.list();
        if (!res.isEmpty())
            return (ApprovalStatus) res.get(0);
        return null;
    }

    public static List<AmpFiscalCalendar> getAllFisCalenders() {
        Session session = null;
        Query qry = null;
        List<AmpFiscalCalendar> fisCals = new ArrayList<AmpFiscalCalendar>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from " + AmpFiscalCalendar.class.getName() + " f";
            qry = session.createQuery(queryString);
            fisCals = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get all fiscal calendars");
            logger.debug("Exceptiion " + e);
        }
        return fisCals;
    }

    public static Long getBaseFiscalCalendar() {
        Session session;
        Query qry;
        Long fid = 4L;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "from " + AmpFiscalCalendar.class.getName()
                    + " where startMonthNum=:start and startDayNum=:start and yearOffset=:offset";
            qry = session.createQuery(queryString);
            qry.setParameter("start", 1, IntegerType.INSTANCE);
            qry.setParameter("offset", 0, IntegerType.INSTANCE);
            if (qry.list().size() != 0)
                fid = ((AmpFiscalCalendar) qry.list().get(0)).getAmpFiscalCalId();
        } catch (Exception ex) {
            logger.error("Unable to get base fiscal calendar" + ex);
            ex.printStackTrace(System.out);
        }
        return fid;
    }

    public static AmpFiscalCalendar getGregorianCalendar() {
        Session session = null;
        Query qry = null;
        AmpFiscalCalendar calendar = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from " + AmpFiscalCalendar.class.getName() + " f "
                    + "where f.baseCal=:baseCalParam order by f.name";
            qry = session.createQuery(queryString);
            qry.setParameter("baseCalParam", BaseCalendar.BASE_GREGORIAN.getValue(), StringType.INSTANCE);

            if (qry.list() != null)
                calendar = ((AmpFiscalCalendar) qry.list().get(0));
        } catch (Exception ex) {
            logger.error("Unable to get fiscal calendar" + ex);
        }
        return calendar;
    }

    public static List<String> getAmpIdsByOrg(Long orgId) {
        String queryString = "select a.ampId "
                + "from " + AmpOrgRole.class.getName() + " role, " + AmpActivity.class.getName() + " a "
                + "where role.organisation=:orgId "
                + "and role.activity = a.ampActivityId";
        return PersistenceManager.getRequestDBSession()
                .createQuery(queryString)
                .setParameter("orgId", orgId, LongType.INSTANCE)
                .list();
    }

    public static List<String> getAmpIdsByInternalIdOrg(Long orgId) {
        String queryString = "select f.ampActivity.ampId "
                + "from " + AmpActivityInternalId.class.getName() + " f," + AmpActivityGroup.class.getName() + " g "
                + "where f.ampActivity = g.ampActivityLastVersion "
                + "and (f.organisation=:orgId)";
        return PersistenceManager.getRequestDBSession()
                .createQuery(queryString)
                .setParameter("orgId", orgId, LongType.INSTANCE)
                .list();
    }

    public static AmpFiscalCalendar getAmpFiscalCalendar(Long ampFisCalId) {
        return (AmpFiscalCalendar) PersistenceManager.getSession().get(AmpFiscalCalendar.class, ampFisCalId);
    }

    public static User getUser(Long userId) {
        return (User) PersistenceManager.getSession().get(User.class, userId);
    }

    /**
     * @author Arty
     * @param reportId
     *            Sets the the defaultTeamReport to null for all the AppSettings
     *            that were referencing the
     */
    public static void updateAppSettingsReportDeleted(Long reportId) {
        Session session = null;
        Query qry = null;
        AmpApplicationSettings ampAppSettings = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select a from " + AmpApplicationSettings.class.getName()
                    + " a where (a.defaultTeamReport=:repId)";
            qry = session.createQuery(queryString);
            qry.setParameter("repId", reportId, LongType.INSTANCE);
            for (Object o : qry.list()) {
                ampAppSettings = (AmpApplicationSettings) o;
                ampAppSettings.setDefaultTeamReport(null);
                update(ampAppSettings);
                // //////System.out.println("Am updatat: " +
                // ampAppSettings.getAmpAppSettingsId());
            }
        } catch (Exception e) {
            logger.error("Unable to get TeamAppSettings");
            logger.debug("Exceptiion " + e);
        }
    }

    public static AmpApplicationSettings getTeamAppSettings(Long teamId) {

        String queryString = "from " + AmpApplicationSettings.class.getName() + " a where (a.team.ampTeamId=:teamId)";
        AmpApplicationSettings ampAppSettings = (AmpApplicationSettings) PersistenceManager.getSession()
                .createQuery(queryString).setParameter("teamId", teamId, LongType.INSTANCE).uniqueResult();
        return ampAppSettings;
    }

    public static List<AmpApplicationSettings> getTeamAppSettings(List<Long> teamIds) {
        if (teamIds.isEmpty()) {
            return Collections.emptyList();
        }

        String queryString = "from "
                + AmpApplicationSettings.class.getName()
                + " a where a.team.ampTeamId in (:teamIds)";

        return PersistenceManager.getRequestDBSession()
                .createQuery(queryString)
                .setParameterList("teamIds", teamIds)
                .list();
    }

    public static AmpApplicationSettings getTeamAppSettingsMemberNotNull(Long teamId) {
        Session session = null;
        Query qry = null;
        AmpApplicationSettings ampAppSettings = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select a from " + AmpApplicationSettings.class.getName()
                    + " a where (a.team=:teamId) ";
            qry = session.createQuery(queryString);
            qry.setParameter("teamId", teamId, LongType.INSTANCE);
            for (Object o : qry.list()) {
                ampAppSettings = (AmpApplicationSettings) o;
                if (ampAppSettings != null)
                    break;
            }

        } catch (Exception e) {
            logger.error("Unable to get TeamAppSettings", e);
        }
        return ampAppSettings;
    }

    /*
     * Get all reports if tabs = null (all) tab = true only get tabs tab = false
     * reports which aren't tabs
     */
    public static List<AmpReports> getAllReports(Boolean tabs) {
        String queryString = "select r from " + AmpReports.class.getName() + " r";
        if (tabs != null) {
            if (tabs) {
                queryString += " where r.drilldownTab=true ";
            } else {
                queryString += " where r.drilldownTab=false ";
            }
        }
        return PersistenceManager.getRequestDBSession().createQuery(queryString).list();
    }

    public static AmpReports getAmpReport(Long id) {
        AmpReports ampReports = null;
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select r from " + AmpReports.class.getName() + " r " + "where (r.ampReportId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, LongType.INSTANCE);
            for (Object o : qry.list()) {
                ampReports = (AmpReports) o;
            }
            // end
        } catch (Exception ex) {
            logger.error("Unable to get reports " + ex);
        }
        return ampReports;
    }

    public static AmpReportLog getAmpReportLog(Long report_id, Long member_id) {
        AmpReportLog ampReportMemberLog = null;
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select r from " + AmpReportLog.class.getName() + " r "
                    + "where (r.report=:id and r.member.ampTeamMemId=:member)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", report_id, LongType.INSTANCE);
            qry.setParameter("member", member_id, LongType.INSTANCE);
            for (Object o : qry.list()) {
                ampReportMemberLog = (AmpReportLog) o;
            }
            // end
        } catch (Exception ex) {
            logger.error("Unable to get reportmemberlog " + ex);
        }
        return ampReportMemberLog;
    }

    public static List<AmpTeamMember> getMembersUsingReport(Long id) {
        AmpReports report = (AmpReports) PersistenceManager.getSession().get(AmpReports.class, id);
        List<AmpTeamMember> res = new ArrayList<>();
        if (report != null)
            res.addAll(report.getMembers());
        return res;
    }

    /**
     * Loads all objects of T from database, using request session. TODO there
     * are several methods like this, let's refactor to one.
     * 
     * @param <T>
     * @param object
     * @return
     * @throws DgException
     */
    public static <T> Collection<T> getAll(Class<T> object) {
        return getAll(object, PersistenceManager.getSession());
    }

    /**
     * Loads all objects of T from database. Client should care about opening
     * and releasing session which is passed as parameter to this method.
     * 
     * @param <T>
     * @param object
     * @param session
     *            database session. Client should handle session - opening and
     *            releasing, including transactions if required.
     * @return
     * @throws DgException
     */
    public static <T> Collection<T> getAll(Class<T> object, Session session) {
        try {
            String queryString = "from " + object.getName();
            Query qry = session.createQuery(queryString);
            return qry.list();
        } catch (Exception e) {
            logger.error("Exception from getAll()", e);
            throw new RuntimeException(e);
        }
    }

    public static Country getDgCountry(String iso) {
        Session session = null;
        Country country = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select c from " + Country.class.getName() + " c " + "where (c.iso=:iso)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("iso", iso, StringType.INSTANCE);
            for (Object o : qry.list()) {
                country = (Country) o;
            }

        } catch (Exception e) {
            logger.error("Exception from getDgCountry()", e);
        }
        return country;
    }

    public static Collection<AmpOrganisation> getFiscalCalOrgs(Long fiscalCalId) {

        Session sess = null;
        Collection<AmpOrganisation> col = null;
        Query qry = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpOrganisation.class.getName()
                    + " o where (o.ampFiscalCalId=:ampFisCalId) and (o.deleted is null or o.deleted = false) ";
            qry = sess.createQuery(queryString);
            qry.setParameter("ampFisCalId", fiscalCalId, LongType.INSTANCE);
            Iterator<AmpOrganisation> itr = qry.list().iterator();
            col = new ArrayList<AmpOrganisation>();
            while (itr.hasNext()) {
                col.add(itr.next());
            }
        } catch (Exception e) {
            logger.error("Exception from getFiscalCalOrgs()", e);
        }
        return col;

    }

    public static List<AmpCurrency> getFiscalCalConstantCurrencies(Long fiscalCalId) {

        Session sess = null;
        List<AmpCurrency> list = null;
        Query qry = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpCurrency.class.getName()
                    + " o where (o.calendar=:ampFisCalId) and o.activeFlag = 1 and virtual = true";
            qry = sess.createQuery(queryString);
            qry.setParameter("ampFisCalId", fiscalCalId, LongType.INSTANCE);
            list = qry.list();

        } catch (Exception e) {
            logger.error("Exception from getFiscalCalConstantCurrencies()", e);
        }
        return list;
    }

    public static Collection<AmpApplicationSettings> getFiscalCalSettings(Long fiscalCalId) {

        Session sess = null;
        Collection<AmpApplicationSettings> col = null;
        Query qry = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpApplicationSettings.class.getName()
                    + " o where (o.fiscalCalendar=:ampFisCalId)";
            qry = sess.createQuery(queryString);
            qry.setParameter("ampFisCalId", fiscalCalId, LongType.INSTANCE);
            Iterator<AmpApplicationSettings> itr = qry.list().iterator();
            col = new ArrayList<>();
            while (itr.hasNext()) {
                col.add(itr.next());
            }
        } catch (Exception e) {
            logger.error("Exception from getFiscalCalSettings()", e);
        }
        return col;
    }

    public static List<AmpOrganisation> searchForOrganisation(String keyword, Long orgType) {
        return searchForOrganisation(keyword, orgType, (long[]) null);
    }

    public static List<AmpOrganisation> searchForOrganisation(String keyword, Long orgType, long[] excludeIds) {
        keyword = keyword.toLowerCase();
        StringBuilder queryString = new StringBuilder();

        String organizationName = AmpOrganisation.hqlStringForName("org");
        queryString.append(" select org from ").append(AmpOrganisation.class.getName()).append(" org ")
                .append(" inner join org.orgGrpId grp ").append(" where(lower(org.acronym) like '%").append(keyword).append("%' or lower(").append(organizationName).append(") like '%").append(keyword)
                .append("%') and grp.orgType=:orgType and (org.deleted is null or org.deleted = false)");

        appendNotIn("org.ampOrgId", excludeIds, queryString);

        Query qry = PersistenceManager.getSession().createQuery(queryString.toString());
        qry.setParameter("orgType", orgType, LongType.INSTANCE);
        return qry.list();
    }

    public static List<AmpOrganisation> searchForOrganisation(String keyword) {
        return searchForOrganisation(keyword, (long[]) null);
    }

    public static List<AmpOrganisation> searchForOrganisation(String keyword, long[] excludeIds) {
        keyword = keyword.toLowerCase();
        StringBuilder queryString = new StringBuilder();

        String organizationName = AmpOrganisation.hqlStringForName("org");
        // .append(keyword)
        queryString.append("select distinct org from ").append(AmpOrganisation.class.getName()).append(" org ")
                .append("where (lower(acronym) like :keyword").append(" or lower(").append(organizationName).append(") like :keyword")// .append(keyword)
                .append(") and (org.deleted is null or org.deleted = false) ");

        appendNotIn("org.ampOrgId", excludeIds, queryString);
        Query q = PersistenceManager.getSession().createQuery(queryString.toString()).setParameter("keyword",
                '%' + keyword + '%');

        return q.list();
    }

    /**
     * This function gets all organizations whose names begin with
     * namesFirstLetter and name or acronym contain keyword
     * 
     * @author Dare
     */
    public static List<AmpOrganisation> searchForOrganisation(String namesFirstLetter, String keyword) {
        if (keyword.length() != 0) {
            keyword = keyword.toLowerCase();
        }
        namesFirstLetter = namesFirstLetter.toLowerCase();

        String organizationName = AmpOrganisation.hqlStringForName("org");
        String queryString = "select distinct org from " + AmpOrganisation.class.getName() + " org "
                + "where ((lower(acronym) like '%" + keyword + "%' and lower(" + organizationName + ") like '"
                + namesFirstLetter + "%') or lower(" + organizationName + ") like '" + namesFirstLetter + "%" + keyword
                + "%') and (org.deleted is null or org.deleted = false)";
        return PersistenceManager.getSession().createQuery(queryString).list();
    }

    /**
     * This function gets all organizations whose names begin with
     * namesFirstLetter and name or acronym contain keyword and organisation
     * type is orgType
     * 
     * @author Mouhamad
     */
    public static List<AmpOrganisation> searchForOrganisation(String namesFirstLetter, String keyword, Long orgType) {
        if (keyword.length() != 0) {
            keyword = keyword.toLowerCase();
        }
        namesFirstLetter = namesFirstLetter.toLowerCase();

        String organizationName = AmpOrganisation.hqlStringForName("org");
        String queryString = "select distinct org from " + AmpOrganisation.class.getName()
                + " org inner join org.orgGrpId grp " + "where grp.orgType=:orgType and ((lower(acronym) like '%"
                + keyword + "%' and lower(" + organizationName + ") like '" + namesFirstLetter + "%') or lower("
                + organizationName + ") like '" + namesFirstLetter + "%" + keyword
                + "%') and (org.deleted is null or org.deleted = false)";
        Query qry = PersistenceManager.getSession().createQuery(queryString);
        qry.setParameter("orgType", orgType, LongType.INSTANCE);
        return qry.list();
    }

    public static List<AmpOrganisation> searchForOrganisationByType(Long orgType) {
        return searchForOrganisationByType(orgType, null);
    }

    /**
     * returns list of all Organisations which belong to a group which belongs
     * to a type and do not have an id in the exclusion area
     *
     * @param orgType
     * @param excludeIds
     * @return
     */
    public static List<AmpOrganisation> searchForOrganisationByType(Long orgType, long[] excludeIds) {

        try {
            Session session = PersistenceManager.getRequestDBSession();
            StringBuilder queryString = new StringBuilder();
            queryString.append("select distinct org from ").append(AmpOrganisation.class.getName()).append(" org ")
                    .append(" inner join org.orgGrpId grp where grp.orgType=:orgType and (org.deleted is null or org.deleted = false)");

            appendNotIn("org.ampOrgId", excludeIds, queryString);

            Query qry = session.createQuery(queryString.toString());
            qry.setParameter("orgType", orgType, LongType.INSTANCE);
            return qry.list();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Appends the NOT IN criteria to the queryString
     *
     * @param columnName
     * @param excludeIds
     * @param queryString
     * @return
     */
    private static StringBuilder appendNotIn(String columnName, long[] excludeIds, StringBuilder queryString) {
        if (excludeIds != null && excludeIds.length > 0) {
            queryString.append(" and (").append(columnName).append(" not in (");
            for (long orgId : excludeIds) {
                queryString.append(orgId).append(",");
            }
            // remove last comma
            queryString.setLength(queryString.length() - 1);
            queryString.append("))");
        }
        return queryString;
    }

    public static ArrayList<AmpOrganisation> getAmpOrganisations() {
        return getAmpOrganisations(null);
    }

    /**
     * Returns list of amp organizations, excluding the <code>excludeIds</code>
     *
     * @param excludeIds
     *            if not null, the organizations with these ids will be excluded
     *            from the search result
     * @return
     */
    public static ArrayList<AmpOrganisation> getAmpOrganisations(long[] excludeIds) {

        Session session = null;
        Query q = null;
        ArrayList<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
        StringBuilder queryString = new StringBuilder();

        try {
            session = PersistenceManager.getRequestDBSession();

            queryString.append(" select org from ").append(AmpOrganisation.class.getName()).append(" org ");
            queryString.append(" where (org.deleted is null or org.deleted = false)");

            appendNotIn("org.ampOrgId", excludeIds, queryString);

            queryString.append(" order by org.name");

            q = session.createQuery(queryString.toString());
            if (q.list() != null && q.list().size() > 0) {
                organizations.addAll(q.list());
            }

        } catch (Exception ex) {
            logger.error("Unable to get Amp organisation names  from database " + ex.getMessage());
        }
        return organizations;
    }

    public static List<AmpOrganisation> getAmpOrganisations(Long orgId, Long groupId, Long typeId) {
        Session session = null;
        Query q = null;
        List<AmpOrganisation> organizations = null;
        StringBuilder queryString = new StringBuilder();

        try {
            session = PersistenceManager.getRequestDBSession();
            queryString.append(" select org from ");
            queryString.append(AmpOrganisation.class.getName());
            queryString.append(" org inner join org.orgGrpId grp ");
            queryString.append("  inner join grp.orgType type ");

            boolean orgCond = orgId != null && orgId != -1;
            boolean groupCond = groupId != null && groupId != -1;
            boolean typeCond = typeId != null && typeId != -1;

            queryString.append(" where (org.deleted is null or org.deleted = false) ");
            /*
             * if (orgCond || groupCond || typeCond) { queryString.append(
             * " where 1=1 "); }
             */
            if (groupCond) {
                queryString.append(" and grp.ampOrgGrpId=:groupId ");
            }
            if (typeCond) {
                queryString.append(" and type.ampOrgTypeId=:typeId ");
            }
            if (orgCond) {
                queryString.append(" and org.ampOrgId=:orgId ");
            }
            queryString.append(" order by org.name ");
            q = session.createQuery(queryString.toString());
            if (orgCond) {
                q.setParameter("orgId", orgId, LongType.INSTANCE);
            }
            if (groupCond) {
                q.setParameter("groupId", groupId, LongType.INSTANCE);
            }
            if (typeCond) {
                q.setParameter("typeId", typeId, LongType.INSTANCE);
            }

            organizations = q.list();

        } catch (Exception ex) {
            logger.error("Unable to get Amp organisation names  from database " + ex.getMessage());
        }
        return organizations;
    }

    /**
     * returns a sorted-by-name list of @link {@link OrganizationSkeleton}
     * instances selected by a HQL query <br />
     * the query should be of the for, "SELECT ampOrgId, ampOrgName ..."
     *
     * @param query
     * @return
     */
    public static List<OrganizationSkeleton> getOrgSkeletonsFromQuery(Query query) {
        ArrayList<OrganizationSkeleton> res = new ArrayList<OrganizationSkeleton>();
        List<Object[]> lst = query.list();
        for (Object[] item : lst) {
            OrganizationSkeleton skel = new OrganizationSkeleton();
            skel.setAmpOrgId(PersistenceManager.getLong(item[0]));
            skel.setName(PersistenceManager.getString(item[1]));
            res.add(skel);
        }
        Collections.sort(res);
        return res;
    }

    /**
     * returns a representation of the organizations in the database
     * (lightweight, composed of of @link {@link OrganizationSkeleton}).<br />
     * they are grouped by ampOrgGroup
     *
     * @return
     */
    public static java.util.Map<Long, Set<OrganizationSkeleton>> getOrgSkeletonGroupedByGroupId() {
        java.util.Map<Long, Set<OrganizationSkeleton>> res = new java.util.HashMap<Long, Set<OrganizationSkeleton>>();

        String queryString = "select org.orgGrpId.ampOrgGrpId, org.ampOrgId, " + AmpOrganisation.hqlStringForName("org")
                + " FROM " + AmpOrganisation.class.getName()
                + " org where (org.deleted is null or org.deleted = false)";

        Query q = PersistenceManager.getSession().createQuery(queryString);

        List<Object[]> lst = q.list();
        for (Object[] item : lst) {
            OrganizationSkeleton skel = new OrganizationSkeleton();
            skel.setAmpOrgId(PersistenceManager.getLong(item[1]));
            skel.setName(PersistenceManager.getString(item[2]));

            Long orgGrpId = PersistenceManager.getLong(item[0]);
            if (!res.containsKey(orgGrpId))
                res.put(orgGrpId, new java.util.TreeSet<OrganizationSkeleton>());
            res.get(orgGrpId).add(skel);
        }
        return res;
    }

    /**
     * returns a sorted-by-name list of @link {@link OrganizationSkeleton}
     *
     * @param orgGroupId
     *            - orgGroupId to filter by. If equals null -> no filtering
     * @return
     */
    public static List<OrganizationSkeleton> getOrgSkeletonByGroupId(Long orgGroupId) {
        String orgGrpCondition = orgGroupId == null ? "1=1" : "org.orgGrpId=:orgGroupId";

        String queryString = "select org.ampOrgId, " + AmpOrganisation.hqlStringForName("org") + " FROM "
                + AmpOrganisation.class.getName() + " org where " + orgGrpCondition
                + " and (org.deleted is null or org.deleted = false)";

        Query q = PersistenceManager.getSession().createQuery(queryString);
        if (orgGroupId != null)
            q.setParameter("orgGroupId", orgGroupId, LongType.INSTANCE);
        return getOrgSkeletonsFromQuery(q);
    }

    public static List<AmpOrganisation> getOrganisationByGroupId(Long orgGroupId) {
        Session session;
        Query q;
        List<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
        String queryString;

        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = " select org from " + AmpOrganisation.class.getName()
                    + " org where org.orgGrpId=:orgGroupId and (org.deleted is null or org.deleted = false) order by org.name ";
            q = session.createQuery(queryString);
            q.setParameter("orgGroupId", orgGroupId, LongType.INSTANCE);
            organizations = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get Amp organisation names  from database " + ex.getMessage());
        }
        return organizations;
    }

    public static List<OrganizationSkeleton> getDonorOrganisationByGroupId(Long orgGroupId, boolean publicView) {
        List<OrganizationSkeleton> organizations = new ArrayList<OrganizationSkeleton>();
        StringBuilder queryString = new StringBuilder(
                "SELECT DISTINCT(orgRole.organisation.ampOrgId) FROM " + AmpOrgRole.class.getName() + " orgRole");

        queryString.append(
                " where orgRole.role.roleCode='DN' and (orgRole.activity.draft = false OR orgRole.activity.draft is null) ");

        if (orgGroupId != null && orgGroupId != -1) {
            queryString.append(" and orgRole.organisation.orgGrpId=:orgGroupId ");
        }

        if (publicView) {
            queryString.append(String.format(
                    " and orgRole.activity.approvalStatus in ('%s', '%s') and orgRole.activity.team.parentTeamId is not null ",
                    ApprovalStatus.approved.getDbName(), ApprovalStatus.startedapproved.getDbName()));
        }

        Query query = PersistenceManager.getSession().createQuery(queryString.toString());
        if (orgGroupId != null && orgGroupId != -1) {
            query.setParameter("orgGroupId", orgGroupId, LongType.INSTANCE);
        }

        String orgIds = Util.toCSStringForIN(query.list());

        return getOrgSkeletonsFromQuery(PersistenceManager.getSession()
                .createQuery("SELECT org.ampOrgId, " + AmpOrganisation.hqlStringForName("org") + " FROM "
                        + AmpOrganisation.class.getName() + " org WHERE org.ampOrgId IN (" + orgIds + ")"));
    }

    /**
     * 
     * @return List of Mul and Bil organization groups
     */

    public static Collection<AmpOrgGroup> getBilMulOrgGroups() {
        Collection<AmpOrgGroup> orgGroups = new ArrayList<AmpOrgGroup>();
        Collection<AmpOrgGroup> bilOrgGroups;
        Collection<AmpOrgGroup> mulOrgGroups;

        try {
            AmpOrgType tBil = getAmpOrgTypeByCode("BIL");
            AmpOrgType tMul = getAmpOrgTypeByCode("MUL");
            bilOrgGroups = searchForOrganisationGroupByType(tBil.getAmpOrgTypeId());
            mulOrgGroups = searchForOrganisationGroupByType(tMul.getAmpOrgTypeId());
            if (bilOrgGroups != null) {
                orgGroups.addAll(bilOrgGroups);
            }
            if (mulOrgGroups != null) {
                orgGroups.addAll(mulOrgGroups);
            }

        } catch (Exception ex) {
            logger.error("Unable to get Amp organisation names  from database " + ex.getMessage());

        }
        return orgGroups;
    }

    /*
     * gets all organisation groups excluding goverment groups
     */
    public static Collection<AmpOrgGroup> getAllNonGovOrgGroups() {
        Session session;
        Query qry;
        Collection<AmpOrgGroup> groups = new ArrayList<AmpOrgGroup>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select distinct gr from " + AmpOrgGroup.class.getName() + " gr "
                    + " inner join gr.orgType t where t.orgTypeIsGovernmental is NULL or t.orgTypeIsGovernmental=false order by gr.orgGrpName asc";
            qry = session.createQuery(queryString);
            groups = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get all organisation groups", e);
        }
        return groups;
    }

    public static void updateIndicator(AmpAhsurveyIndicator ind) {
        AmpAhsurveyIndicator oldInd = new AmpAhsurveyIndicator();
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            oldInd = (AmpAhsurveyIndicator) session.load(AmpAhsurveyIndicator.class, ind.getAmpIndicatorId());

            oldInd.setAmpIndicatorId(ind.getAmpIndicatorId());
            oldInd.setCalcFormulas(ind.getCalcFormulas());
            oldInd.setIndicatorCode(ind.getIndicatorCode());
            oldInd.setIndicatorNumber(ind.getIndicatorNumber());
            oldInd.setName(ind.getName());
            oldInd.setQuestions(ind.getQuestions());
            oldInd.setStatus(ind.getStatus());
            oldInd.setTotalQuestions(ind.getTotalQuestions());

            update(oldInd);
        } catch (Exception ex) {
            logger.error("Unable to get survey indicator : ", ex);
        }
    }

    public static AmpOrgType getAmpOrgTypeByCode(String ampOrgTypeCode) {
        Session session = null;
        Query qry = null;
        AmpOrgType ampOrgType = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from " + AmpOrgType.class.getName()
                    + " f where (f.orgTypeCode=:ampOrgTypeCode)";
            qry = session.createQuery(queryString);
            qry.setParameter("ampOrgTypeCode", ampOrgTypeCode,StringType.INSTANCE);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                ampOrgType = (AmpOrgType) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get Org Type", e);
        }
        return ampOrgType;
    }

    public static ArrayList<AmpOrgType> getAmpOrgTypes() {
        Session session = null;
        Query qry = null;
        ArrayList<AmpOrgType> org_types = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select f from " + AmpOrgType.class.getName() + " f ";
            qry = session.createQuery(queryString);
            org_types = (ArrayList<AmpOrgType>) qry.list();

        } catch (Exception e) {
            logger.error("Unable to get Org Type", e);
        }
        return org_types;
    }

    public static List<AmpFundingDetail> getFundingDetWithCurrId(Long currId) {
        String queryString = "select f from " + AmpFundingDetail.class.getName() + " f where (f.ampCurrencyId=:currId)";
        Query qry = PersistenceManager.getSession().createQuery(queryString);
        qry.setParameter("currId", currId, LongType.INSTANCE);
        return qry.list();
    }

    public static void add(Object object) {
        PersistenceManager.getSession().save(object);
    }

    public static void update(Object object) {
        PersistenceManager.getSession().update(object);
    }

    public static void updateField(String className, Long id, String fieldName, Object newValue) {
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String idName = PersistenceManager.sf().getClassMetadata(className).getIdentifierPropertyName();
            Query query = session
                    .createQuery("update " + className + " c set c." + fieldName + "=:val where c." + idName + "=:id");
            query.setParameter("val", newValue);
            query.setParameter("id", id);
            query.executeUpdate();
        } catch (Exception ex) {
            logger.error(String.format("Could not update \"%s.%s\"=\"%s\". Cause: %s", className, fieldName,
                    String.valueOf(newValue), ex.getMessage()));
        }
    }

    public static AmpSectorScheme getAmpSectorSchemeById(Long schemeId) {
        return (AmpSectorScheme) PersistenceManager.getSession().get(AmpSectorScheme.class, schemeId);
    }

    /**
     * general function to save/update object
     * 
     * @param object
     * @throws Exception
     */
    public static void saveOrUpdateObject(Object object) throws Exception {
        PersistenceManager.getSession().saveOrUpdate(object);
    }

    public static void saveOrg(AmpOrganisation org) throws DgException {
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            // beginTransaction();
            Set<AmpOrganisationContact> organisationContacts = org.getOrganizationContacts(); // form
                                                                                                // org
                                                                                                // contacts
            /**
             * contact information
             */

            if (org.getAmpOrgId() != null) { // edit
                List<AmpOrganisationContact> orgDBContacts = ContactInfoUtil.getOrganizationContacts(org.getAmpOrgId());
                // if organization contains contact,which is not in form contact
                // list, we should remove it
                if (orgDBContacts != null && orgDBContacts.size() > 0) {
                    for (AmpOrganisationContact dbOrgContact : orgDBContacts) {
                        int count = 0;
                        if (organisationContacts != null) {
                            for (AmpOrganisationContact formOrgCont : organisationContacts) {
                                if (formOrgCont.getId() != null && formOrgCont.getId().equals(dbOrgContact.getId())) {
                                    count++;
                                    break;
                                }
                            }
                        }
                        if (count == 0) { // if organization contains
                            // contact,which is not in contact
                            // list, we should remove it
                            AmpOrganisationContact orgCont = (AmpOrganisationContact) sess
                                    .get(AmpOrganisationContact.class, dbOrgContact.getId());
                            AmpContact cont = orgCont.getContact();
                            sess.delete(orgCont);
                            cont.getOrganizationContacts().remove(orgCont);
                            sess.update(cont);
                            org.getOrganizationContacts().remove(orgCont);
                        }
                    }
                }
            }

            if (organisationContacts != null) {
                // this will remove all organisation contact which are linked to
                // this organisation
                Iterator<AmpOrganisationContact> organisationContactIterator = organisationContacts.iterator();

                while (organisationContactIterator.hasNext()) {
                    AmpOrganisationContact ampOrganisationContact = organisationContactIterator.next();

                    if(org.getAmpOrgId() != null) {
                        if(ampOrganisationContact.getId() != null) {
                            // AmpContact
                            // cont=ampOrganisationContact.getContact();
                            AmpOrganisationContact contToBeRemoved =
                                    (AmpOrganisationContact) sess
                                            .get(AmpOrganisationContact.class,
                                                    ampOrganisationContact.getId());
                            if(contToBeRemoved != null) {
                                AmpContact ampContact = contToBeRemoved.getContact();
                                sess.delete(contToBeRemoved);
                                organisationContactIterator.remove();
                                ampContact.getOrganizationContacts()
                                        .remove(contToBeRemoved);
                                sess.update(ampContact);

                            }

                        }
                    }
                }

                // Since you've modified the original set, need to get a new iterator
                organisationContactIterator = organisationContacts.iterator();

                while (organisationContactIterator.hasNext()) {
                    AmpOrganisationContact organizationContact = organisationContactIterator.next();
                    AmpContact contact = organizationContact.getContact();
                    AmpContact ampContact = null;

                    if (contact.getId() != null) { // contact already exists.
                        ampContact = (AmpContact) sess.get(AmpContact.class, contact.getId());
                        ampContact.setName(contact.getName());
                        ampContact.setLastname(contact.getLastname());
                        ampContact.setTitle(contact.getTitle());
                        ampContact.setOrganisationName(contact.getOrganisationName());
                        ampContact.setCreator(contact.getCreator());
                        ampContact.setShared(true);
                        ampContact.setOfficeaddress(contact.getOfficeaddress());
                        ampContact.setFunction(contact.getFunction());
                        // remove old properties
                        if (ampContact.getProperties() != null) {
                            ampContact.getProperties().clear();
                        }
                        sess.update(ampContact);
                    } else {
                        sess.save(contact);
                    }

                    // save properties
                    if (contact.getProperties() != null) {
                        for (AmpContactProperty formProperty : contact.getProperties()) {
                            if (ampContact != null) {
                                formProperty.setContact(ampContact);
                            } else {
                                formProperty.setContact(contact);
                            }
                            sess.save(formProperty);
                        }
                    }

                    // link org to cont
                    organizationContact.setOrganisation(org);
                    organizationContact.setPrimaryContact(organizationContact.getPrimaryContact());
                    if (ampContact != null) {
                        organizationContact.setContact(ampContact);
                    } else {
                        organizationContact.setContact(contact);
                    }
                    sess.save(organizationContact);
                }
            }

            sess.saveOrUpdate(org);
            // session.flush();
            // tx.commit();
        } catch (Exception e) {
            throw new DgException(e);
        }
    }

    public static void delete(Object object) throws JDBCException {
        PersistenceManager.getSession().delete(object);
    }

    public static void deleteOrg(AmpOrganisation org) throws JDBCException {
        Session sess = PersistenceManager.getSession();
        org.setDeleted(true);
        sess.saveOrUpdate(org);
    }

    public static void deleteAllStamps(final String idxName) {
        Connection con;
        try {
            Session session = PersistenceManager.getSession();
            session.doWork(new Work() {
                @Override
                public void execute(Connection con) throws SQLException {
                    con.setAutoCommit(false);
                    con.createStatement().execute("DELETE FROM amp_lucene_index WHERE idxName like '" + idxName + "'");
                    con.commit();

                }
            });
        } catch (Exception e) {
            logger.error("Error while trying to delete Lucene db stamps: ", e);
        }
    }

    public static void deleteAllNoUpdateOrgs(final boolean toExclude) {
        Connection con;
        try {
            Session session = PersistenceManager.getSession();
            session.doWork(new Work() {
                @Override
                public void execute(Connection con) throws SQLException {
                    con.setAutoCommit(false);
                    con.createStatement()
                            .execute("DELETE FROM amp_scorecard_organisation where to_exclude=" + toExclude);
                    con.commit();

                }
            });
        } catch (Exception e) {
            logger.error("Error while trying to delete Lucene db stamps: ", e);
        }
    }

    public static Collection<AmpOrganisation> getDonors() {
        Session session = null;
        Query q = null;
        Collection<Object[]> donors = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString;
            queryString = "select distinct f.ampDonorOrgId, f.ampDonorOrgId.name  from " + AmpFunding.class.getName()
                    + " f  order by f.ampDonorOrgId.name";
            q = session.createQuery(queryString);
            logger.debug("No of Donors : " + q.list().size());
            donors = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get Donors from database", ex);
        }
        Collection<AmpOrganisation> retVal = null;
        if (donors != null) {
            retVal = new ArrayList<AmpOrganisation>();
            for (Object[] org : donors) {
                retVal.add((AmpOrganisation) org[0]);
            }
        }
        return retVal;
    }

    public static List<AmpOrganisation> getOrganisations() {
        String queryString = "select distinct org from " + AmpOrganisation.class.getName()
                + " org  join  org.calendar  cal where (org.deleted is null or org.deleted = false) ";
        return PersistenceManager.getSession().createQuery(queryString).list();
    }

    public static List<AmpOrganisation> getAllDonorOrgs() {
        String queryString = "select distinct org from " + AmpFunding.class.getName()
                + " f inner join f.ampDonorOrgId org " + " order by org.acronym asc";
        return PersistenceManager.getSession().createQuery(queryString).list();
    }

    /**
     * gets the skeletons of all the OrgGroups in the database
     *
     * @return
     */
    public static List<OrgGroupSkeleton> getAllOrgGroupSkeletons() {
        List<OrgGroupSkeleton> col = new ArrayList<OrgGroupSkeleton>();

        String orgGrpNameHql = AmpOrgGroup.hqlStringForName("c");
        String queryString = "select c.ampOrgGrpId, " + orgGrpNameHql + " FROM " + AmpOrgGroup.class.getName() + " c";
        Query qry = PersistenceManager.getSession().createQuery(queryString);
        for (Object[] grpInfo : ((List<Object[]>) qry.list())) {
            OrgGroupSkeleton skel = new OrgGroupSkeleton();
            skel.setAmpOrgGrpId(PersistenceManager.getLong(grpInfo[0]));
            skel.setOrgGrpName(PersistenceManager.getString(grpInfo[1]));
            col.add(skel);
        }
        Collections.sort(col);
        return col;
    }

    public static List<AmpOrgGroup> getAllVisibleOrgGroups() {
        return getAllVisibleOrgGroups(null);
    }
    /**
     *
     * generates a list of all AmpOrgGroup elements which have deleted =null or
     * deleted = false
     *
     * @param ampOrgGroupIds if not null will filter the list for the given ids
     * @return
     */
    public static List<AmpOrgGroup> getAllVisibleOrgGroups(List<Long> ampOrgGroupIds) {
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String orgGrpNameHql = AmpOrgGroup.hqlStringForName("c");
            String queryString = "select c from " + AmpOrgGroup.class.getName() + " c"
                    + " WHERE (c.deleted IS NULL OR c.deleted = false)";
            if (ampOrgGroupIds != null && ampOrgGroupIds.size() > 0) {
                queryString += " AND c.ampOrgGrpId IN (:ids) ";
            }
            queryString += " order by lower(" + orgGrpNameHql + ") asc";

            Query qry = session.createQuery(queryString);
            if (ampOrgGroupIds != null && ampOrgGroupIds.size() > 0) {
                qry.setParameterList("ids", ampOrgGroupIds);
            }
            return qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getAllOrgGroups()");
            logger.debug(e.toString());
            return null;
        }
    }

    public static List<AmpOrgGroup> getAllOrgGroups() {
        try {
            Session session = PersistenceManager.getRequestDBSession();
            String orgGrpNameHql = AmpOrgGroup.hqlStringForName("c");
            String queryString = "select c from " + AmpOrgGroup.class.getName() + " c order by lower(" + orgGrpNameHql
                    + ") asc";
            Query qry = session.createQuery(queryString);
            return qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getAllOrgGroups()");
            logger.debug(e.toString());
            return null;
        }
    }

    public static List<OrgTypeSkeleton> getAllOrgTypesFaster() {
        return OrgTypeSkeleton.populateTypeSkeletonList();
    }

    public static Collection<AmpOrgType> getAllOrgTypes() {
        Session session = null;
        Collection<AmpOrgType> col = new ArrayList<AmpOrgType>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String orgTypeHql = AmpOrgType.hqlStringForName("c");
            String queryString = "select c from " + AmpOrgType.class.getName() + " c order by " + orgTypeHql + " asc";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception e) {
            logger.error("Exception from getAllOrgTypes()");
            logger.error(e.toString());
        }
        return col;
    }

    public static Collection<AmpOrgGroup> getAllContractingAgencyGroupsOfPortfolio() {
        if (AmpCaching.getInstance().allContractingAgencyGroupsOfPortfolio != null)
            return new ArrayList<AmpOrgGroup>(AmpCaching.getInstance().allContractingAgencyGroupsOfPortfolio);

        Session session = null;
        List<AmpOrgGroup> col = new ArrayList<>();
        try {
            session = PersistenceManager.getRequestDBSession();
            String rewrittenColumns = SQLUtils.rewriteQuery("amp_org_group", "aog", new HashMap<String, String>() {
                {
                    put("org_grp_name", InternationalizedModelDescription
                            .getForProperty(AmpOrgGroup.class, "orgGrpName").getSQLFunctionCall("aog.amp_org_grp_id"));
                }
            });
            String idsQueryString = String.format("SELECT DISTINCT amp_org_grp_id FROM v_contracting_agency_groups");
            String queryString = "SELECT " + rewrittenColumns + " FROM amp_org_group aog WHERE aog.amp_org_grp_id IN ("
                    + idsQueryString + ")";

            Query qry = session.createNativeQuery(queryString).addEntity(AmpOrgGroup.class);
            col = qry.list();
        } catch (Exception e) {
            logger.error("Exception from getAllContractingAgencyGroupsOfPortfolio()", e);
        }
        AmpCaching.getInstance().allContractingAgencyGroupsOfPortfolio = new ArrayList<AmpOrgGroup>(col);
        return col;
    }

    public static List<OrgGroupSkeleton> getAllOrgGroupsOfPortfolioFaster() {
        // if (AmpCaching.getInstance().allOrgGroupsOfPortfolio != null)
        // return new
        // ArrayList<OrgGroupSkeleton>(AmpCaching.getInstance().allOrgGroupsOfPortfolio);

        Session session = null;
        List<OrgGroupSkeleton> col = null;

        try {
            col = OrgGroupSkeleton.populateSkeletonOrgGroupsList();
        } catch (Exception e) {
            logger.error("Got exception from getAllOrgGroupsOfPortfolio()", e);
        }
        return col;
    }

    /**
     * fetches DONOR org groups of the database portfolio
     *
     * @return
     */
    public static List<AmpOrgGroup> getAllOrgGroupsOfPortfolio() {
        if (AmpCaching.getInstance().allOrgGroupsOfPortfolio != null)
            return new ArrayList<AmpOrgGroup>(AmpCaching.getInstance().allOrgGroupsOfPortfolio);

        Session session = null;
        List<AmpOrgGroup> col = new ArrayList<AmpOrgGroup>();
        try {
            session = PersistenceManager.getRequestDBSession();
            String rewrittenColumns = SQLUtils.rewriteQuery("amp_org_group", "aog", new HashMap<String, String>() {
                {
                    put("org_grp_name", InternationalizedModelDescription
                            .getForProperty(AmpOrgGroup.class, "orgGrpName").getSQLFunctionCall("aog.amp_org_grp_id"));
                }
            });
            String queryString = "select distinct " + rewrittenColumns + " from amp_org_group aog "
                    + "inner join amp_organisation ao on (ao.org_grp_id = aog.amp_org_grp_id) "
                    + "inner join amp_funding af on (af.amp_donor_org_id = ao.amp_org_id) "
                    + "inner join amp_activity aa on (aa.amp_activity_id = af.amp_activity_id) where (ao.deleted is null or ao.deleted = false) ";
            Query qry = session.createNativeQuery(queryString).addEntity(AmpOrgGroup.class);
            col = qry.list();
        } catch (Exception e) {
            logger.error("Got exception from getAllOrgGroupsOfPortfolio()", e);
        }
        AmpCaching.getInstance().allOrgGroupsOfPortfolio = new ArrayList<AmpOrgGroup>(col);
        return col;
    }

    public static List<AmpOrgType> getAllOrgTypesOfPortfolio() {
        if (AmpCaching.getInstance().allOrgTypesOfPortfolio != null)
            return new ArrayList<AmpOrgType>(AmpCaching.getInstance().allOrgTypesOfPortfolio);

        Session session = null;
        List<AmpOrgType> col = new ArrayList<AmpOrgType>();
        try {
            session = PersistenceManager.getRequestDBSession();
            String rewrittenColumns = SQLUtils.rewriteQuery("amp_org_type", "aot", new HashMap<String, String>() {
                {
                    put("org_type", InternationalizedModelDescription.getForProperty(AmpOrgType.class, "orgType")
                            .getSQLFunctionCall("aot.amp_org_type_id"));
                }
            });

            String queryString = "select distinct " + rewrittenColumns + " from amp_org_type aot "
                    + "inner join amp_org_group aog on (aot.amp_org_type_id=aog.org_type ) "
                    + "inner join amp_organisation ao on (aog.amp_org_grp_id=ao.org_grp_id ) "
                    + "inner join amp_funding af on (af.amp_donor_org_id = ao.amp_org_id) "
                    + "inner join amp_activity aa on (aa.amp_activity_id = af.amp_activity_id) where (ao.deleted is null or ao.deleted = false) ";
            Query qry = session.createNativeQuery(queryString).addEntity(AmpOrgType.class);
            col = qry.list();
        } catch (Exception e) {
            logger.error("Exception from getAllOrgTypesOfPortfolio()", e);
        }
        AmpCaching.getInstance().allOrgTypesOfPortfolio = new ArrayList<AmpOrgType>(col);
        return col;
    }

    public static AmpOrgType getAmpOrgType(Long ampOrgTypeId) {
        return (AmpOrgType) PersistenceManager.getSession().get(AmpOrgType.class, ampOrgTypeId);
    }

    public static AmpOrgGroup getAmpOrgGroup(Long id) {
        return (AmpOrgGroup) PersistenceManager.getSession().get(AmpOrgGroup.class, id);
    }

    public static AmpOrgGroup getAmpOrgGroupByName(String name) {
        Session session;
        AmpOrgGroup grp = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String orgGrpName = AmpOrgGroup.hqlStringForName("l");
            String queryString = "select l from " + AmpOrgGroup.class.getName() + " l " + "where (" + orgGrpName
                    + "=:name)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("name", name, StringType.INSTANCE);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                grp = (AmpOrgGroup) itr.next();
            }

        } catch (Exception ex) {
            logger.error("Unable to get Org Group" + ex);
        }
        return grp;
    }

    /**
     * Gets the deleted amp org group with specified name, if it exists
     *
     * @param name
     *            name of the amp org group
     * @param id
     *            not sure we need this
     * @return the amp org group if it exists, null otherwise
     */
    public static AmpOrgGroup getDeletedAmpOrgGroups(String name, Long id) {
        // boolean deletedOrgGroupExists = false;
        AmpOrgGroup result = null;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String orgGrpName = AmpOrgGroup.hqlStringForName("l");
            String queryString = "select l from " + AmpOrgGroup.class.getName() + " l " + "where upper(" + orgGrpName
                    + ") like upper(:name) " + "AND (l.deleted = true)";
            // if (id != null) {
            // queryString += " and l.ampOrgGrpId!=" + id;
            // }
            Query qry = session.createQuery(queryString);
            qry.setParameter("name", name, StringType.INSTANCE);
            result = (AmpOrgGroup) qry.uniqueResult();
            // Integer amount = (Integer) qry.uniqueResult();
            // if (amount != null && amount.intValue() > 0) {
            // deletedOrgGroupExists = true;
            // }
        } catch (Exception e) {
            logger.error("Unable to get Org Group" + e);
        }
        return result;
        // return deletedOrgGroupExists;

    }

    public static boolean checkAmpOrgGroupDuplication(String name, Long id) {
        boolean duplicateName = false;
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String orgGrpName = AmpOrgGroup.hqlStringForName("l");
            String queryString = "select count(l) from " + AmpOrgGroup.class.getName() + " l " + "where upper("
                    + orgGrpName + ") like upper(:name) ";
            if (id != null) {
                queryString += " and l.ampOrgGrpId!=" + id;
            }
            Query qry = session.createQuery(queryString);
            qry.setParameter("name", name, StringType.INSTANCE);
            Long longValue = (Long) qry.uniqueResult();
            Integer amount= longValue.intValue();
            if (amount > 0) {
                duplicateName = true;
            }
        } catch (Exception e) {
            logger.error("Unable to get Org Group" + e);
        }

        return duplicateName;
    }

    public static Collection<AmpOrgGroup> searchForOrganisationGroupByType(Long orgType) {
        Session session = null;
        Collection col = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String orgGrpName = AmpOrgGroup.hqlStringForName("org");
            String queryString = "select org from " + AmpOrgGroup.class.getName() + " org "
                    + " where org.orgType=:orgType" + " AND org.deleted IS NULL OR org.deleted = false";
            queryString += "  order by " + orgGrpName;
            Query qry = session.createQuery(queryString);
            qry.setParameter("orgType", orgType, LongType.INSTANCE);
            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to search " + ex);
        }
        return col;
    }

    public static Collection<AmpOrgGroup> searchForOrganisationGroup(String keyword, Long orgType) {
        Session session = null;
        Collection col = null;
        keyword = keyword.toLowerCase();

        try {
            session = PersistenceManager.getRequestDBSession();
            String orgGrpName = AmpOrgGroup.hqlStringForName("org");
            String queryString = "select org from " + AmpOrgGroup.class.getName() + " org " + " where lower("
                    + orgGrpName + ") like '%" + keyword + "%') and org.orgType=:orgType";
            Query qry = session.createQuery(queryString);
            qry.setParameter("orgType", orgType, LongType.INSTANCE);
            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to search " + ex);
        }
        return col;
    }

    public static Collection<AmpOrgGroup> searchForOrganisationGroup(String keyword) {
        Session session = null;
        Collection col = null;
        keyword = keyword.toLowerCase();

        try {
            session = PersistenceManager.getRequestDBSession();
            String orgGrpName = AmpOrgGroup.hqlStringForName("org");
            String queryString = "select distinct org from " + AmpOrgGroup.class.getName() + " org " + " where lower("
                    + orgGrpName + ") like '%" + keyword + "%' or lower(org.orgGrpCode) like '%" + keyword + "%'";
            Query qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to search " + ex);
        }
        return col;
    }

    public static Collection<AmpOrgGroup> getAllOrganisationGroup() {
        Session session = null;
        Query qry = null;
        Collection organisation = new ArrayList<>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String orgName = AmpOrganisation.hqlStringForName("o");
            String queryString = "select o from " + AmpOrgGroup.class.getName() + " o order by "
                    + AmpOrgGroup.hqlStringForName("o") + " asc";
            qry = session.createQuery(queryString);
            organisation = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get all organisation groups");
            logger.error("Exceptiion " + e);
        }
        return organisation;
    }

    public static List<AmpOrganisation> getOrgByGroup(Long Id) {
        String queryString = "select o from " + AmpOrganisation.class.getName()
                + " o where (o.orgGrpId=:orgGrpId) and (o.deleted is null or o.deleted = false) ";
        Query qry = PersistenceManager.getSession().createQuery(queryString);
        qry.setParameter("orgGrpId", Id, LongType.INSTANCE);
        return qry.list();
    }

    public static boolean chkOrgTypeReferneces(Long Id) {

        Session sess;
        Query qry = null;
        String queryString = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            queryString = "select o from " + AmpOrgGroup.class.getName() + " o where (o.orgType=:orgTypeId)";
            qry = sess.createQuery(queryString);
            qry.setParameter("orgTypeId", Id, LongType.INSTANCE);
            if (null != qry.list() && qry.list().size() > 0) {
                return true;
            }

        } catch (Exception e) {
            logger.debug("Exception from chkOrgTypeReferneces(): " + e);
            e.printStackTrace(System.out);
        }
        return false;
    }

    public static Collection getOrgByCode(String action, String code, Long id) {
        return getOrgByCodeAndAcronym(action, code, null, id);
    }

    public static Collection getOrgByAcronym(String action, String acronym, Long id) {
        return getOrgByCodeAndAcronym(action, null, acronym, id);
    }

    public static Collection getOrgByCodeAndAcronym(String action, String code, String acronym, Long id) {

        Session sess;
        Collection col = new ArrayList<>();
        Query qry;
        String queryString;

        try {
            sess = PersistenceManager.getRequestDBSession();
            queryString = "select o from " + AmpOrganisation.class.getName()
                    + " o where (o.deleted is null or o.deleted = false) ";
            if (code != null) {
                queryString += " AND (o.orgCode=:code) ";
            }
            if (acronym != null) {
                queryString += " AND (o.acronym=:acronym) ";
            }
            if ("edit".equals(action)) {

                queryString += " and (o.ampOrgId!=:id) ";
            }
            qry = sess.createQuery(queryString);
            if (code != null) {
                qry.setParameter("code", code, StringType.INSTANCE);
            }
            if (acronym != null) {
                qry.setParameter("acronym", acronym, StringType.INSTANCE);
            }
            if ("edit".equals(action)) {
                qry.setParameter("id", id, LongType.INSTANCE);
            }
            col = qry.list();
        } catch (Exception e) {
            logger.debug("Exception from getOrgByCode()");
            logger.debug(e.toString());
        }
        return col;
    }

    public static List<AmpOrganisation> getOrganisationAsCollection(Long id) {
        String queryString = "select o from " + AmpOrganisation.class.getName() + " o "
                + "where (o.ampOrgId=:id) and (o.deleted is null or o.deleted = false) ";
        Query qry = PersistenceManager.getSession().createQuery(queryString);
        qry.setParameter("id", id, LongType.INSTANCE);
        return qry.list();
    }

    public static AmpField getAmpFieldByName(String com) {
        Session session;
        Query qry;
        AmpField comments = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpField.class.getName() + " o " + "where (o.fieldName=:com)";
            qry = session.createQuery(queryString);
            qry.setParameter("com", com);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                comments = (AmpField) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get all comments");
            logger.debug("Exceptiion " + e);
        }
        return comments;
    }

    public static List<AmpField> getAmpFields() {
        String queryString = "select o from " + AmpField.class.getName() + " o ";
        return PersistenceManager.getRequestDBSession().createQuery(queryString).list();
    }

    public static List<AmpComments> getAllCommentsByField(Long fid, Long aid) {
        Session session;
        Query qry;

        session = PersistenceManager.getSession();
        String queryString = "select o from " + AmpComments.class.getName() + " o "
                + "where (o.ampFieldId=:fid) and (o.ampActivityId=:aid)";
        qry = session.createQuery(queryString);
        qry.setParameter("fid", fid, LongType.INSTANCE);
        qry.setParameter("aid", aid, LongType.INSTANCE);
        return qry.list();
    }

    public static List<AmpComments> getAllCommentsByActivityId(Long aid) {
        return getAllCommentsByActivityId(aid, PersistenceManager.getSession());
    }

    public static List<AmpComments> getAllCommentsByActivityId(Long aid, Session session) {
        String queryString = "select o from " + AmpComments.class.getName() + " o " + "where (o.ampActivityId=:aid)";
        Query qry = session.createQuery(queryString);
        qry.setParameter("aid", aid, LongType.INSTANCE);
        return qry.list();
    }

    public static List<AmpActivityBudgetStructure> getBudgetStructure(Long aid) {
        Session session = null;
        Query qry = null;
        ArrayList budgetStructure = new ArrayList<>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpActivityBudgetStructure.class.getName() + " o "
                    + "where (o.activity=:aid)";
            qry = session.createQuery(queryString);
            qry.setParameter("aid", aid, LongType.INSTANCE);
            for (Object o : qry.list()) {
                AmpActivityBudgetStructure bs = (AmpActivityBudgetStructure) o;
                budgetStructure.add(bs);
            }
        } catch (Exception e) {
            logger.error("Unable to get all budget structures");
            logger.debug("Exceptiion " + e);
        }
        return budgetStructure;
    }

    public static Group getGroup(String key, Long siteId) {
        Session session;
        Group group = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String qryStr = "select grp from " + Group.class.getName() + " grp "
                    + "where (grp.key=:key) and (grp.site=:sid)";
            Query qry = session.createQuery(qryStr);
            qry.setParameter("key", key, StringType.INSTANCE);
            qry.setParameter("sid", siteId, LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                group = (Group) itr.next();
            }
        } catch (Exception ex) {
            logger.error("Unable to get Group " + ex.getMessage());
        }
        return group;
    }

    /**
     * to be removed as soon as NOONE is using PI
     *
     * @param surveyId
     * @param activityId
     * @return
     */
    public static List getResposesBySurvey(Long surveyId, Long activityId) {
        ArrayList responses = new ArrayList();
        List response;
        Collection fundingSet;
        Session session;
        Iterator iter1;
        boolean flag = true;

        try {
            session = PersistenceManager.getRequestDBSession();
            String qry = "select indc from " + AmpAhsurveyIndicator.class.getName()
                    + " indc order by indicator_number asc";
            Collection indicatorColl = session.createQuery(qry).list();

            AmpAhsurvey svy = (AmpAhsurvey) session.get(AmpAhsurvey.class, surveyId);
            // response = svy.getResponses();
            /*
             * qry = "select res from " + AmpAhsurvey.class.getName() +
             * " res where (res.ampAHSurveyId=:surveyId)"; Query query =
             * session.createQuery(qry); query.setParameter("surveyId",
             * surveyId, LongType.INSTANCE); response = ( (AmpAhsurvey)
             * query.list().get(0)).getResponses();
             */

            // TODO: The whole logic for saving the first survey data and future
            // retrieving must be redone.
            // This query is necesary because of the lazy="false" which is
            // necesary because the way the PI reports are created.
            qry = "select resp from " + AmpAhsurveyResponse.class.getName()
                    + " resp where resp.ampAHSurveyId=:surveyId";
            Query query = session.createQuery(qry);
            query.setParameter("surveyId", surveyId, LongType.INSTANCE);
            response = query.list();

            qry = "select fund from " + AmpFunding.class.getName()
                    + " fund where (fund.ampDonorOrgId=:donorId) and (fund.ampActivityId=:activityId)";
            query = session.createQuery(qry);
            query.setParameter("donorId", svy.getAmpDonorOrgId().getAmpOrgId(), LongType.INSTANCE);
            query.setParameter("activityId", svy.getAmpActivityId().getAmpActivityId(), LongType.INSTANCE);
            fundingSet = query.list();

            if (response.size() < 1) // new survey
                flag = false;
            Iterator iter2 = null;
            boolean ansFlag = false;
            iter1 = indicatorColl.iterator();
            while (iter1.hasNext()) {
                AmpAhsurveyIndicator indc = (AmpAhsurveyIndicator) iter1.next();
                Indicator ind = new Indicator();
                ind.setIndicatorCode(indc.getIndicatorCode());
                ind.setName(indc.getName());
                ind.setQuestion(new ArrayList<>());
                // iter2 = session.createFilter(((AmpAhsurveyIndicator)
                // session.load(AmpAhsurveyIndicator.class,
                // indc.getAmpIndicatorId())).getQuestions(),
                // "order by this.questionNumber asc").list().iterator();
                Iterator iter3;
                iter2 = session.createFilter(indc.getQuestions(), "order by this.questionNumber asc").list().iterator();
                while (iter2.hasNext()) {
                    AmpAhsurveyQuestion q = (AmpAhsurveyQuestion) iter2.next();
                    Question ques = new Question();
                    ques.setQuestionType(q.getAmpTypeId().getName());
                    ques.setQuestionId(q.getAmpQuestionId());
                    ques.setQuestionText(q.getQuestionText());
                    if (flag) { // response is blank in case of new survey
                        iter3 = response.iterator();
                        while (iter3.hasNext()) {
                            AmpAhsurveyResponse res = (AmpAhsurveyResponse) iter3.next();
                            if (res.getAmpQuestionId().getAmpQuestionId().equals(q.getAmpQuestionId())) {
                                if (q.getQuestionNumber() == 1) {
                                    if ("yes".equalsIgnoreCase(res.getResponse()))
                                        ansFlag = true;
                                }

                                /*
                                 * -------------------------------- Defunct now
                                 * -------------------------------------
                                 */
                                // if answer to question #1 of survey is yes
                                // then calculate
                                // difference(%) between planned & actual
                                // disbursement(s)
                                if ("calculated".equalsIgnoreCase(q.getAmpTypeId().getName())) {
                                    if (q.getQuestionNumber() == 10) {
                                        if (ansFlag) {
                                            Iterator itr5 = null;
                                            double actual = 0.0;
                                            double planned = 0.0;
                                            AmpFundingDetail fd = null;
                                            for (Object o : fundingSet) {
                                                AmpFunding ampf = (AmpFunding) o;
                                                itr5 = ampf.getFundingDetails().iterator();
                                                while (itr5.hasNext()) {
                                                    fd = (AmpFundingDetail) itr5.next();
                                                    if (fd.getTransactionType() == 1) {
                                                        if (fd.getAdjustmentType().getValue()
                                                                .equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED
                                                                        .getValueKey()))
                                                            planned += fd.getTransactionAmount().floatValue();
                                                        else if (fd.getAdjustmentType().getValue().equals(
                                                                CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()))
                                                            actual += fd.getTransactionAmount().floatValue();
                                                    }
                                                }
                                            }

                                            if (planned == 0.0)
                                                res.setResponse("nil");
                                            else {
                                                NumberFormat formatter = new DecimalFormat("#.##");
                                                Double percent = (actual * 100) / planned;
                                                res.setResponse(formatter.format(percent));
                                            }
                                        } else
                                            res.setResponse(null);
                                    }
                                }
                                /*
                                 * -------------------------------- Defunct now
                                 * -------------------------------------
                                 */

                                ques.setResponse(res.getResponse());
                                ques.setResponseId(res.getAmpReponseId());
                                break;
                            }
                        }
                    }
                    ind.getQuestion().add(ques);
                }
                responses.add(ind);
            }
        } catch (Exception ex) {
            logger.error("Unable to get survey responses : ", ex);
        }
        logger.debug("responses.size() : " + responses.size());
        return responses;
    }

    public static AmpAhsurvey getAhSurvey(Long surveyId) {
        AmpAhsurvey survey = new AmpAhsurvey();
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String qry = "select svy from " + AmpAhsurvey.class.getName() + " svy where (svy.ampAHSurveyId=:surveyId)";
            Query q = session.createQuery(qry);
            q.setParameter("surveyId", surveyId, LongType.INSTANCE);
            survey = (AmpAhsurvey) q.list().get(0);
        } catch (Exception ex) {
            logger.debug("Unable to get survey : ", ex);
        }
        return survey;
    }

    public static Collection<AmpAhsurveyIndicator> getAllAhSurveyIndicators() {
        Collection responses = new ArrayList();
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String qry = "select indc from " + AmpAhsurveyIndicator.class.getName()
                    + " indc order by indicator_number asc";
            responses = session.createQuery(qry).list();

        } catch (Exception ex) {
            logger.error("Unable to get survey indicators : ", ex);
        }
        return responses;
    }

    public static AmpAhsurveyIndicator getIndicatorById(Long id) {
        AmpAhsurveyIndicator indc = new AmpAhsurveyIndicator();
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String qry = "select indc from " + AmpAhsurveyIndicator.class.getName()
                    + " indc where (indc.ampIndicatorId=:id)";
            indc = (AmpAhsurveyIndicator) session.createQuery(qry).setParameter("id", id, LongType.INSTANCE).list()
                    .get(0);

        } catch (Exception ex) {
            logger.debug("Unable to get survey indicator : " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return indc;
    }

    public static Collection<AmpGPISurveyIndicator> getAllGPISurveyIndicators(boolean onlyWithQuestions) {
        Collection responses = new ArrayList();
        Session session = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String qry = "select indc from " + AmpGPISurveyIndicator.class.getName() + " indc ";
            if (onlyWithQuestions) {
                qry += " where total_question > 0 ";
            }
            qry += " order by indicator_code asc";
            responses = session.createQuery(qry).list();

        } catch (Exception ex) {
            logger.error("Unable to get survey indicators : ", ex);
        }
        return responses;
    }

    /*
     * get amp ME indicator value of a particular activity specified by ampActId
     */
    public static Collection getActivityMEIndValue(Long ampActId) {
        Session session = null;
        Collection col = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select indAct from " + IndicatorActivity.class.getName() + " indAct "
                    + " where (indAct.activity=:ampActId)";
            qry = session.createQuery(queryString);
            qry.setParameter("ampActId", ampActId, LongType.INSTANCE);
            col = qry.list();
        } catch (Exception e1) {
            logger.error("could not retrieve AmpReportSector " + e1.getMessage());
            e1.printStackTrace(System.out);
        }
        return col;
    }

    public static Collection<CountryBean> getTranlatedCountries(HttpServletRequest request) {
        Collection<CountryBean> trnCnCol;
        org.digijava.kernel.entity.Locale navLang = RequestUtils.getNavigationLanguage(request);

        try {
            trnCnCol = new ArrayList<>();
            List<Country> cnCol = FeaturesUtil.getAllDgCountries();
            updateCountryNameTranslations(request, cnCol);

            for (Country cn : cnCol) {
                CountryBean trnCn = new CountryBean();
                trnCn.setId(cn.getCountryId());
                trnCn.setIso(cn.getIso());
                trnCn.setIso3(cn.getIso3());
                trnCn.setName(cn.getCountryName());
                trnCnCol.add(trnCn);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        List<CountryBean> sortedCountrieList = new ArrayList<>(trnCnCol);
        sortedCountrieList.sort(new HelperTrnCountryNameComparator(navLang.getCode()));
        return sortedCountrieList;
    }

    public static void updateCountryNameTranslations(HttpServletRequest request, Collection<Country> countries) {
        org.digijava.kernel.entity.Locale navLang = RequestUtils.getNavigationLanguage(request);
        Site site = RequestUtils.getSite(request);
        Map<String, List<Country>> countriesByMsgKey = AmpCollections.distribute(countries,
                Country.DISTRIBUTE_BY_MSGKEY);

        try {
            long start = System.currentTimeMillis();
            String queryString = "select msg from " + Message.class.getName() + " msg"
                    + " where (msg.key IN (:msgLangKey)) and (msg.siteId=:siteId) and (msg.locale=:locale)";

            Query qry = PersistenceManager.getSession().createQuery(queryString);
            qry.setParameter("siteId", site.getId().toString(), StringType.INSTANCE);
            qry.setParameter("locale", navLang.getCode(), StringType.INSTANCE);
            qry.setParameterList("msgLangKey", countriesByMsgKey.keySet());

            List<Message> translations = qry.list();
            logger.error("found " + translations.size() + " entries in " + (System.currentTimeMillis() - start)
                    + " millies");
            for (Message msg : translations) {
                if (msg.getKey() != null && !msg.getKey().isEmpty() && countriesByMsgKey.containsKey(msg.getKey())) {
                    for (Country c : countriesByMsgKey.get(msg.getKey()))
                        c.setCountryName(msg.getMessage());
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    // public static Country getTranlatedCountry(HttpServletRequest request,
    // Country country) {
    // Session session = null;
    // Collection msgCol = null;
    // Query qry = null;
    //
    // org.digijava.kernel.entity.Locale navLang = RequestUtils
    // .getNavigationLanguage(request);
    // Site site = RequestUtils.getSite(request);
    //
    // try {
    // if (country != null) {
    // session = PersistenceManager.getRequestDBSession();
    // long start = System.currentTimeMillis();
    // String queryString = "select msg "
    // + " from "
    // + Message.class.getName()
    // + " msg"
    // + " where (msg.key=:msgLangKey) and (msg.siteId=:siteId) and
    // (msg.locale=:locale)";
    //
    // qry = session.createQuery(queryString);
    // qry.setParameter("siteId", site.getId().toString(),
    // StringType.INSTANCE);
    // qry.setParameter("locale", navLang.getCode(), StringType.INSTANCE);
    // qry.setParameter("msgLangKey", country.getMessageLangKey(),
    // StringType.INSTANCE);
    // msgCol = qry.list();
    // logger.error("found " + msgCol.size() + " entries in " +
    // (System.currentTimeMillis() - start) + " millies");
    //
    // if (msgCol != null && msgCol.size() != 0) {
    // for (Iterator msgIter = msgCol.iterator(); msgIter
    // .hasNext();) {
    // Message msg = (Message) msgIter.next();
    // if (msg != null) {
    // country.setCountryName(msg.getMessage());
    // break;
    // }
    // }
    // }
    // }
    // } catch (Exception ex) {
    // throw new RuntimeException(ex);
    // }
    // return country;
    // }

    public static void deleteUserExt(AmpOrgGroup orgGrp, AmpOrgType orgType, AmpOrganisation org) {
        Session session = null;
        Query query;
        if (orgGrp != null || orgType != null || org != null) {
            try {
                Object relatedObj = null;
                session = PersistenceManager.getRequestDBSession();
                String qhl = "delete from " + AmpUserExtension.class.getName() + " ext where ";
                if (orgGrp != null) {
                    qhl += " ext.orgGroup=:relatedObj";
                    relatedObj = orgGrp;
                }
                if (orgType != null) {
                    qhl += " ext.orgType=:relatedObj";
                    relatedObj = orgType;
                }
                if (org != null) {
                    qhl += " ext.organization=:relatedObj";
                    relatedObj = org;
                }
                query = session.createQuery(qhl);
                query.setParameter("relatedObj", relatedObj, ObjectType.INSTANCE);
                query.executeUpdate();
            } catch (Exception e) {
                logger.error("Delete Failed: " + e.toString());
            }
        }
    }

    public static class HelperUserNameComparator implements Comparator {
        private Order order;

        public HelperUserNameComparator(Order order) {
            this.order = order;

        }

        public int compare(Object obj1, Object obj2) {
            User user1 = (User) obj1;
            User user2 = (User) obj2;
            int result = user1.getName().compareToIgnoreCase(user2.getName());
            if (Order.DESC.equals(order)) {
                result *= -1;
            }
            return result;
        }
    }

    /**
     * //for sorting users by Email
     * 
     * @author dare
     * 
     */
    public static class HelperEmailComparator implements Comparator {
        private Order order;

        public HelperEmailComparator(Order order) {
            this.order = order;

        }

        public int compare(Object obj1, Object obj2) {
            User user1 = (User) obj1;
            User user2 = (User) obj2;
            int result = user1.getEmail().compareToIgnoreCase(user2.getEmail());
            if (Order.DESC.equals(order)) {
                result *= -1;
            }
            return result;
        }
    }

    public static class HelperTrnCountryNameComparator implements Comparator<CountryBean> {
        Locale locale;
        Collator collator;

        public HelperTrnCountryNameComparator() {
            this.locale = new Locale("en", "EN");
        }

        public HelperTrnCountryNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(CountryBean o1, CountryBean o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);

            return collator.compare(o1.getName(), o2.getName());
        }
    }

    public static class HelperAmpOrgGroupNameComparator implements Comparator<AmpOrgGroup> {
        Locale locale;
        Collator collator;

        public HelperAmpOrgGroupNameComparator() {
            this.locale = new Locale("en", "EN");
        }

        public HelperAmpOrgGroupNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpOrgGroup o1, AmpOrgGroup o2) {
            if (collator == null) {
                collator = Collator.getInstance(locale);
                collator.setStrength(Collator.TERTIARY);
            }

            return collator.compare(o1.getOrgGrpName(), o2.getOrgGrpName());
        }
    }

    public static class HelperAmpSectorNameComparator implements Comparator<AmpSector> {
        Locale locale;
        Collator collator;

        public HelperAmpSectorNameComparator() {
            this.locale = new Locale("en", "EN");
        }

        public HelperAmpSectorNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        @Override
        public int compare(AmpSector o1, AmpSector o2) {
            if (collator == null) {
                collator = Collator.getInstance(locale);
                collator.setStrength(Collator.TERTIARY);
            }
            int result = collator.compare(o1.getName(), o2.getName());
            return result;
        }
    }

    /**
     * This class is used for sorting AmpOrgGroup by code.
     * 
     * @author Dare Roinishvili
     * 
     */
    public static class HelperAmpOrgGroupCodeComparator implements Comparator<AmpOrgGroup> {
        Locale locale;
        Collator collator;

        public HelperAmpOrgGroupCodeComparator() {
            this.locale = new Locale("en", "EN");
        }

        public HelperAmpOrgGroupCodeComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpOrgGroup o1, AmpOrgGroup o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);

            int result = (o1.getOrgGrpCode() != null && o2.getOrgGrpCode() != null)
                    ? collator.compare(o1.getOrgGrpCode(), o2.getOrgGrpCode()) : 0;
            return result;
        }
    }

    /**
     * This class is used for sorting AmpOrgGroup by Type.
     * 
     * @author Dare Roinishvili
     * 
     */
    public static class HelperAmpOrgGroupTypeComparator implements Comparator<AmpOrgGroup> {
        public int compare(AmpOrgGroup o1, AmpOrgGroup o2) {
            AmpOrgType o1Type = o1.getOrgType();
            AmpOrgType o2Type = o2.getOrgType();
            return new HelperAmpOrgTypeNameComparator().compare(o1Type, o2Type);
        }
    }

    public static class HelperAmpOrgTypeNameComparator implements Comparator<AmpOrgType> {
        Locale locale;
        Collator collator;

        public HelperAmpOrgTypeNameComparator() {
            this.locale = new Locale("en", "EN");
        }

        public HelperAmpOrgTypeNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpOrgType o1, AmpOrgType o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);

            return collator.compare(o1.getOrgType(), o2.getOrgType());
        }
    }

    /**
     * This class is used for sorting organisations by name.
     * 
     * @author Dare Roinishvili
     * 
     */
    public static class HelperAmpOrganisationNameComparator implements Comparator<AmpOrganisation> {
        Locale locale;
        Collator collator;

        public HelperAmpOrganisationNameComparator() {
            this.locale = new Locale("en", "EN");
        }
        public HelperAmpOrganisationNameComparator(Locale locale) {
            this.locale = locale;
        }

        public HelperAmpOrganisationNameComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpOrganisation o1, AmpOrganisation o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);

            int result = (o1.getName() == null || o2.getName() == null) ? 0
                    : collator.compare(o1.getName().toLowerCase(), o2.getName().toLowerCase());
            return result;
        }
    }

    /**
     * This class is used for sorting organisations by acronym.
     * 
     * @author Dare Roinishvili
     * 
     */
    public static class HelperAmpOrganisatonAcronymComparator implements Comparator<AmpOrganisation> {
        Locale locale;
        Collator collator;

        public HelperAmpOrganisatonAcronymComparator() {
            this.locale = new Locale("en", "EN");
        }

        public HelperAmpOrganisatonAcronymComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpOrganisation o1, AmpOrganisation o2) {

            int result = 0;
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);

            if (o1.getAcronym() != null && o2.getAcronym() != null) {
                result = collator.compare(o1.getAcronym(), o2.getAcronym());
            } else if (o1.getAcronym() == null && o2.getAcronym() == null) {
                result = 0;
            } else if (o1.getAcronym() == null) {
                result = collator.compare("", o2.getAcronym());
            } else if (o2.getAcronym() == null) {
                result = collator.compare(o1.getAcronym(), "");
            }
            return result;
        }

    }

    /**
     * This class is used for sorting organisation by group. such long and
     * complicated case is necessary because orgGroup maybe empty for
     * organisation
     * 
     * @author Dare Roinishvili
     * 
     */
    public static class HelperAmpOrganisationGroupComparator implements Comparator<AmpOrganisation> {
        Locale locale;
        Collator collator;

        public HelperAmpOrganisationGroupComparator() {
            this.locale = new Locale("en", "EN");
        }

        public HelperAmpOrganisationGroupComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpOrganisation o1, AmpOrganisation o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            int result = 0;
            // such long and complicated case is necessary because orgType maybe
            // empty for organisation

            AmpOrgGroup oo1 = o1.getOrgGrpId();
            AmpOrgGroup oo2 = o2.getOrgGrpId();
            if (oo1 != null && oo2 != null) {
                AmpOrgType orgType1 = oo1.getOrgType();
                AmpOrgType orgType2 = oo2.getOrgType();

                if (orgType1 != null && orgType2 != null) {
                    result = new HelperAmpOrgTypeNameComparator().compare(orgType1, orgType2);
                } else if (orgType2 == null && orgType1 != null) {
                    result = collator.compare(orgType1.getOrgType(), "");
                } else if (orgType1 == null && orgType2 != null) {
                    result = collator.compare("", orgType2.getOrgType());
                }
            } else {
                if (oo1 == null && oo2 != null) {
                    AmpOrgType orgType2 = oo2.getOrgType();
                    result = collator.compare("", orgType2.getOrgType());
                } else {
                    if (oo2 == null && oo1 != null) {
                        AmpOrgType orgType1 = oo1.getOrgType();
                        result = collator.compare(orgType1.getOrgType(), "");
                    }
                }
            }
            return result;

        }
    }

    /**
     * This class is used for sorting organisation by Type. such long and
     * complicated case is necessary because orgType maybe empty for
     * organisation
     * 
     * @author Dare Roinisvili
     * 
     */
    public static class HelperAmpOrganisationTypeComparator implements Comparator<AmpOrganisation> {
        Locale locale;
        Collator collator;

        public HelperAmpOrganisationTypeComparator() {
            this.locale = new Locale("en", "EN");
        }

        public HelperAmpOrganisationTypeComparator(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpOrganisation o1, AmpOrganisation o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            int result = 0;
            // such long and complicated case is necessary because orgType maybe
            // empty for organisation
            AmpOrgType orgType1 = o1.getOrgGrpId().getOrgType();
            AmpOrgType orgType2 = o2.getOrgGrpId().getOrgType();
            if (orgType1 != null && orgType2 != null) {
                result = new HelperAmpOrgTypeNameComparator().compare(orgType1, orgType2);
            } else if (orgType2 == null && orgType1 != null) {
                result = collator.compare(orgType1.getOrgType(), "");
            } else if (orgType1 == null && orgType2 != null) {
                result = collator.compare("", orgType2.getOrgType());
            }
            return result;

        }
    }

    public static AmpOrganisation getOrganisationByName(String name) {
        AmpOrganisation obResult = null;
        Session sess = null;
        Query qry = null;
        String queryString = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            String orgName = AmpOrganisation.hqlStringForName("o");
            queryString = "select o from " + AmpOrganisation.class.getName() + " o where (TRIM(" + orgName
                    + ")=:orgName) and (o.deleted is null or o.deleted = false) ";
            qry = sess.createQuery(queryString);
            qry.setParameter("orgName", name,StringType.INSTANCE);

            List result = qry.list();
            if (result.size() > 0) {
                obResult = (AmpOrganisation) result.get(0);
            }
            // //System.out.println("DBUTIL.GETORGANISATIONBYNAME() : " +
            // qry.getQueryString());
        } catch (Exception e) {
            logger.debug("Exception from getOrganisationByName(): " + e);
            e.printStackTrace(System.out);
        }
        return obResult;
    }

    /**
     * Compares Values by type(actual,base,target) Used in Multi Program Manager
     * to sort them in order: base,actual,target of the same year
     * 
     * @author dare
     * 
     */
    public static class IndicatorValuesComparatorByTypeAndYear implements Comparator<AmpPrgIndicatorValue> {
        public int compare(AmpPrgIndicatorValue o1, AmpPrgIndicatorValue o2) {
            int retValue = 0;
            String o1Year = "";
            String o2Year = "";
            // getting year from creation date
            if (o1.getCreationDate() != null) {
                int length = o1.getCreationDate().length();
                o1Year = o1.getCreationDate().substring(length - 4, length);
            }
            if (o2.getCreationDate() != null) {
                int length = o2.getCreationDate().length();
                o2Year = o2.getCreationDate().substring(length - 4, length);
            }
            // o1's creation year is greater than o2's
            if (o1Year.compareTo(o2Year) == 1) {
                retValue = 1;
            } else if (o1Year.compareTo(o2Year) == -1) {// creation year of o1
                                                        // is less than o2's
                retValue = -1;
            } else if (o1Year.compareTo(o2Year) == 0) { // creation years are
                                                        // equal. So we have to
                                                        // sort them in
                                                        // order:base actual
                                                        // target
                retValue = -(new Integer(o1.getValueType()).compareTo(new Integer(o2.getValueType())));
            }
            return retValue;
        }
    }

    public static class AmpIndicatorValuesComparatorByTypeAndYear implements Comparator<AmpIndicatorValue> {

        public int compare(AmpIndicatorValue o1, AmpIndicatorValue o2) {
            AmpPrgIndicatorValue val1 = new AmpPrgIndicatorValue();
            AmpPrgIndicatorValue val2 = new AmpPrgIndicatorValue();

            val1.setValueType(o1.getValueType());
            val1.setCreationDate(DateConversion.convertDateToString(o1.getValueDate()));

            val2.setValueType(o2.getValueType());
            val2.setCreationDate(DateConversion.convertDateToString(o2.getValueDate()));
            return new IndicatorValuesComparatorByTypeAndYear().compare(val1, val2);
        }

    }

    public static int getOrgTypesAmount(String name, Long groupId) throws Exception {
        Session sess = null;
        Query qry = null;
        int count = 0;
        try {
            sess = PersistenceManager.getRequestDBSession();
            String orgTypeName = AmpOrgType.hqlStringForName("o");
            String queryString = "select count(*) from " + AmpOrgType.class.getName() + " o where upper(" + orgTypeName
                    + ") like upper('" + name + "')";
            if (groupId != null && groupId != 0) {
                queryString += " and o.ampOrgTypeId!=" + groupId;
            }
            qry = sess.createQuery(queryString);
            Long longValue = (Long) qry.uniqueResult();
            count= longValue.intValue();
        } catch (Exception e) {
            logger.error("Exception while getting org types amount:" + e.getMessage());
        }
        return count;
    }

    public static int getOrgTypesByCode(String code, Long typeId) throws Exception {
        Session sess = null;
        Query qry = null;
        int count = 0;
        try {
            sess = PersistenceManager.getRequestDBSession();
            String queryString = "select count(*) from " + AmpOrgType.class.getName()
                    + " o where upper(o.orgTypeCode) like upper('" + code + "')";
            if (typeId != null && typeId != 0) {
                queryString += " and o.ampOrgTypeId!=" + typeId;
            }
            qry = sess.createQuery(queryString);
            Long longValue = (Long) qry.uniqueResult();
            count= longValue.intValue();
        } catch (Exception e) {
            logger.error("Exception while getting org types by code:" + e.getMessage());
        }
        return count;
    }

    public static class HelperAmpOrgRecipientByOrgName implements Comparator<AmpOrgRecipient> {

        Locale locale;
        Collator collator;

        public HelperAmpOrgRecipientByOrgName() {
            this.locale = new Locale("en", "EN");
        }

        public HelperAmpOrgRecipientByOrgName(String iso) {
            this.locale = new Locale(iso.toLowerCase(), iso.toUpperCase());
        }

        public int compare(AmpOrgRecipient o1, AmpOrgRecipient o2) {
            collator = Collator.getInstance(locale);
            collator.setStrength(Collator.TERTIARY);
            return collator.compare(o1.getOrganization().getName().toLowerCase(),
                    o2.getOrganization().getName().toLowerCase());

        }
    }

    public static class HelperUserNameComparatorAsc implements Comparator {
        public int compare(Object obj1, Object obj2) {
            User user1 = (User) obj1;
            User user2 = (User) obj2;
            return user1.getName().trim().compareToIgnoreCase(user2.getName().trim());
        }
    }

    public static class HelperUserNameComparatorDesc implements Comparator {
        public int compare(Object obj1, Object obj2) {
            User user1 = (User) obj1;
            User user2 = (User) obj2;
            return user2.getName().trim().compareToIgnoreCase(user1.getName().trim());
        }
    }

    /**
     * //for sorting users by Email
     * 
     * @author dare
     * 
     */
    public static class HelperEmailComparatorAsc implements Comparator {
        public int compare(Object obj1, Object obj2) {
            User user1 = (User) obj1;
            User user2 = (User) obj2;
            return user1.getEmail().compareTo(user2.getEmail());
        }
    }

    public static class HelperEmailComparatorDesc implements Comparator {
        public int compare(Object obj1, Object obj2) {
            User user1 = (User) obj1;
            User user2 = (User) obj2;
            return user2.getEmail().compareTo(user1.getEmail());
        }
    }

    public enum UserManagerSorting {
        NAMEASCENDING, NAMEDESCENDING, EMAILASCENDING, EMAILDESCENDING
    }

    public enum Order {
        ASC, DESC
    }

    public static Comparator sortUsers(UserManagerSorting criteria) {
        Comparator comparator = null;
        switch (criteria) {
        case NAMEASCENDING:
            comparator = new HelperUserNameComparator(Order.ASC);
            break;
        case NAMEDESCENDING:
            comparator = new HelperUserNameComparator(Order.DESC);
            break;
        case EMAILASCENDING:
            comparator = new HelperEmailComparator(Order.ASC);
            break;
        case EMAILDESCENDING:
            comparator = new HelperEmailComparator(Order.DESC);
            break;
        }
        return comparator;

    }

    public static String getValidationFromTeamAppSettings(Long ampTeamId) {
        String validation = (String) PersistenceManager.getSession().createQuery("select a.validation from "
                + AmpApplicationSettings.class.getName() + " a where (a.team=" + ampTeamId + ") ").uniqueResult();
        return StringUtils.isNotEmpty(validation) ? validation : null;
    }

    public static AmpStructureImg getStructureImage(Long structureId, Long imgId) {
        Session session = null;
        Query qry = null;
        AmpStructureImg image = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpStructureImg.class.getName() + " o "
                    + "where (o.structure.ampStructureId=:structureId and o.id=:imgId)";
            qry = session.createQuery(queryString);
            qry.setParameter("structureId", structureId, LongType.INSTANCE);
            qry.setParameter("imgId", imgId, LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                image = (AmpStructureImg) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get structure image");
            logger.debug("Exceptiion " + e);
        }
        return image;
    }

    public static AmpStructureImg getMostRecentlyUploadedStructureImage(Long structureId) {
        Session session = null;
        Query qry = null;
        AmpStructureImg image = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from " + AmpStructureImg.class.getName() + " o "
                    + "where o.structure.ampStructureId=:structureId and o.creationTime="
                    + "(select max(o1.creationTime) from  " + AmpStructureImg.class.getName() + " o1 "
                    + " where o1.structure.ampStructureId=:structureId)";
            qry = session.createQuery(queryString);
            qry.setParameter("structureId", structureId, LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            if (itr.hasNext()) {
                image = (AmpStructureImg) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get structure image");
            logger.debug("Exceptiion " + e);
        }
        return image;
    }

    public static void saveOrUpdate(List<?> list) throws Exception {
        for (Object obj : list)
            saveOrUpdateObject(obj);
    }

    public static void clearPendingChanges() {
        PersistenceManager.getSession().clear();
    }

    /**
     * get colors ordered by threshold
     */
    public static List<AmpColorThreshold> getColorThresholds() {
        Session session = PersistenceManager.getRequestDBSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<AmpColorThreshold> criteriaQuery = builder.createQuery(AmpColorThreshold.class);
        Root<AmpColorThreshold> root = criteriaQuery.from(AmpColorThreshold.class);
        criteriaQuery.select(root).orderBy(builder.asc(root.get("thresholdStart")));
        return session.createQuery(criteriaQuery).list();
//        return PersistenceManager.getSession().createCriteria(AmpColorThreshold.class)
//                .addOrder(org.hibernate.criterion.Order.asc("thresholdStart")).list();
    }

    /*
     * Count agreements with the specified code.
     */
    public static Integer countAgreementsByCode(String agreementCode) {
        Session session =PersistenceManager.getRequestDBSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<AmpAgreement> root = criteriaQuery.from(AmpAgreement.class);
        Predicate restriction = builder.equal(
                builder.trim(root.get("code")), agreementCode.trim()
        );
        criteriaQuery.select(builder.count(root.get("id"))).where(restriction);
        return session.createQuery(criteriaQuery).uniqueResult().intValue();
//        return ((Long) PersistenceManager.getSession()
//                .createCriteria(AmpAgreement.class)
//                .setProjection(Projections.count("id"))
//                .add(Restrictions.sqlRestriction("trim({alias}.code) = ?", agreementCode, StringType.INSTANCE))
//                .uniqueResult()).intValue();
    }
    
    public static boolean hasDonorRole(Long id){
        Session session = null;
        Query query = null;
        boolean result = false;
        try {
            session = PersistenceManager.getRequestDBSession();         
            String queryString = "select count(*) from "    + AmpOrgRole.class.getName()
                    + " r where (r.organisation.id = :orgId) and r.role.roleCode = :code";
            query = session.createQuery(queryString);
            query.setParameter("orgId", id, LongType.INSTANCE);
            query.setParameter("code", Constants.FUNDING_AGENCY, StringType.INSTANCE);
            Long longValue = (Long) query.uniqueResult();
            int count = longValue.intValue();
            result = count > 0;
        } catch (Exception e) {
            logger.error("Exception from hasDonorRole()", e);
        }
        
        return result;  
    }
}
