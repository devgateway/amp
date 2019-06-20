package org.digijava.kernel.ampapi.endpoints.gpi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.gpi.reports.GPIReportConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.ampapi.endpoints.dto.Org;
import org.digijava.kernel.ampapi.endpoints.util.CalendarUtil;
import org.digijava.kernel.ampapi.postgis.util.QueryUtil;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.digijava.module.aim.dbentity.AmpGPINiAidOnBudget;
import org.digijava.module.aim.dbentity.AmpGPINiDonorNotes;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.Query;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.common.util.DateTimeUtil;
import org.digijava.module.translation.util.ContentTranslationUtil;

public class GPIUtils {
    private static Logger logger = Logger.getLogger(GPIUtils.class);

    public static AmpGPINiAidOnBudget getAidOnBudgetById(Long id) {
        return (AmpGPINiAidOnBudget) PersistenceManager.getSession().get(AmpGPINiAidOnBudget.class, id);
    }

    public static List<AmpGPINiAidOnBudget> getAidOnBudgetList(Integer offset, Integer count, String orderBy,
            String sort, Integer total) {
        Integer maxResults = count == null ? GPIEPConstants.DEFAULT_RECORDS_PER_PAGE : count;
        Integer startAt = (offset == null || offset > total) ? GPIEPConstants.DEFAULT_OFFSET : offset;
        String orderByColumn = (orderBy == null || isValidSortColumn(orderBy) == false) ? GPIEPConstants.DEFAULT_SORT_COLUMN
                : orderBy;
        String sortOrder = (sort == null || isValidSortOrder(sort) == false) ? GPIEPConstants.ORDER_DESC : sort;

        Session dbSession = PersistenceManager.getSession();
        String queryString = "select gpi from " + AmpGPINiAidOnBudget.class.getName() + " gpi order by "
                + GPIEPConstants.SORT_FIELDS.get(orderByColumn) + " " + sortOrder;
        Query query = dbSession.createQuery(queryString);
        query.setFirstResult(startAt);
        query.setMaxResults(maxResults);
        
        return query.list();
    }   

    public static Integer getAidOnBudgetCount() {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select count(*) from " + AmpGPINiAidOnBudget.class.getName();
        Query query = dbSession.createQuery(queryString);
        return (Integer) query.uniqueResult();
    }   

    public static AmpOrganisation getOrganisation(Long id) {
        return (AmpOrganisation) PersistenceManager.getSession().get(AmpOrganisation.class, id);
    }

    public static void saveAidOnBudget(AmpGPINiAidOnBudget aidOnBudget) {
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            session.saveOrUpdate(aidOnBudget);
        } catch (Exception e) {
            logger.error("Exception from saveAidOnBudget: " + e.getMessage());
        }
    }

    public static void deleteAidOnBudget(Long id) {
        AmpGPINiAidOnBudget aidOnBudget = GPIUtils.getAidOnBudgetById(id);
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            session.delete(aidOnBudget);
        } catch (Exception e) {
            logger.error("Exception from deleteAidOnBudget: " + e.getMessage());
        }
    }

    public static boolean checkAidOnBudgetExists(Long ampGPINiAidOnBudgetId, Long donorId, Date indicatorDate) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select gpi from " + AmpGPINiAidOnBudget.class.getName()
                + " gpi where gpi.donor.ampOrgId=:orgId and gpi.indicatorDate = :indicatorDate";
        if (ampGPINiAidOnBudgetId != null) {
            queryString += " and gpi.ampGPINiAidOnBudgetId != :ampGPINiAidOnBudgetId ";
        }

        Query query = dbSession.createQuery(queryString);
        query.setParameter("orgId", donorId);
        query.setParameter("indicatorDate", indicatorDate);
        if (ampGPINiAidOnBudgetId != null) {
            query.setParameter("ampGPINiAidOnBudgetId", ampGPINiAidOnBudgetId);
        }

        return query.list().size() > 0;
    }
    
    public static boolean checkDonorNotesExists(Long donorNotesId, Long donorId, Date notesDate, String indicatorCode) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select donorNotes from " + AmpGPINiDonorNotes.class.getName()
                + " donorNotes where donorNotes.donor.ampOrgId=:orgId and donorNotes.notesDate = :notesDate and indicatorCode = :indicatorCode";
        if (donorNotesId != null) {
            queryString += " and donorNotes.ampGPINiDonorNotesId != :donorNotesId ";
        }

        Query query = dbSession.createQuery(queryString);
        query.setParameter("orgId", donorId);
        query.setParameter("notesDate", notesDate);
        query.setParameter("indicatorCode", indicatorCode);
        if (donorNotesId != null) {
            query.setParameter("donorNotesId", donorNotesId);
        }

        return query.list().size() > 0;
    }
    
    public static AmpGPINiDonorNotes getDonorNotesById(Long id) {
        return (AmpGPINiDonorNotes) PersistenceManager.getSession().get(AmpGPINiDonorNotes.class, id);
    }
    
    public static void saveDonorNotes(AmpGPINiDonorNotes donorNotes) {
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            session.saveOrUpdate(donorNotes);
        } catch (Exception e) {
            logger.error("Exception from saveDonorNotes: " + e.getMessage());
        }
    }
    
    public static Integer getDonorNotesCount(String indicatorCode) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "select count(*) from " + AmpGPINiDonorNotes.class.getName() + " donorNotes where donorNotes.donor.ampOrgId in (:donorIds) and indicatorCode = :indicatorCode";
        Query query = dbSession.createQuery(queryString);
        query.setParameterList("donorIds", getVerifiedOrgsList());
        query.setString("indicatorCode", indicatorCode);
        return (Integer) query.uniqueResult();
    }
    
    public static List<AmpGPINiDonorNotes> getDonorNotesList(Integer offset, Integer count, String orderBy,
            String sort, Integer total, String indicatorCode) {
        Integer maxResults = count == null ? GPIEPConstants.DEFAULT_RECORDS_PER_PAGE : count;
        Integer startAt = (offset == null || offset > total) ? GPIEPConstants.DEFAULT_OFFSET : offset;
        String orderByColumn = (orderBy == null) ? GPIEPConstants.DEFAULT_DONOR_NOTES_SORT_COLUMN
                : orderBy;
        String sortOrder = (sort == null) ? GPIEPConstants.ORDER_DESC : sort;

        Session dbSession = PersistenceManager.getSession();
        String queryString = "select donorNotes from " + AmpGPINiDonorNotes.class.getName() + " donorNotes where donorNotes.donor.ampOrgId in (:donorIds) and indicatorCode = :indicatorCode order by "
                + GPIEPConstants.DONOR_NOTES_SORT_FIELDS.get(orderByColumn) + " " + sortOrder;
        Query query = dbSession.createQuery(queryString);
        query.setFirstResult(startAt);
        query.setMaxResults(maxResults);
        query.setParameterList("donorIds", getVerifiedOrgsList());
        query.setString("indicatorCode", indicatorCode);
        return query.list();
    }   

    public static List<AmpGPINiDonorNotes> getNotesByCode(String code) {
        Session dbSession = PersistenceManager.getSession();
        String queryString = "SELECT donorNotes FROM " + AmpGPINiDonorNotes.class.getName() + " donorNotes "
                + "WHERE indicatorCode = :indicatorCode ";
        Query query = dbSession.createQuery(queryString);
        query.setString("indicatorCode", code);
        return query.list();
    }
    
    /**
     * Filter a donorNotes by donor-type, donorId, from and to dates
     * 
     * @param donorNotes
     * @param donorIds
     * @param donorType
     * @param from
     * @param to
     * @return
     */
    public static List<AmpGPINiDonorNotes> filterNotes(List<AmpGPINiDonorNotes> donorNotes, List<Long> donorIds,
            String donorType, Long from, Long to) {

        List<AmpGPINiDonorNotes> filteredNotes = new ArrayList<>();

        Predicate<AmpGPINiDonorNotes> fromDatePredicate = note -> from == null || from == 0 ? true
                : DateTimeUtil.toJulianDayNumber(note.getNotesDate()) >= from;

        Predicate<AmpGPINiDonorNotes> toDatePredicate = note -> to == null || to == 0 ? true
                : DateTimeUtil.toJulianDayNumber(note.getNotesDate()) <= to;

        Predicate<AmpGPINiDonorNotes> donorPredicate = note -> donorIds == null || donorIds.isEmpty()
                || (donorIds.size() == 1 && donorIds.get(0) == null)
                        ? true
                        : donorType == null || GPIReportConstants.HIERARCHY_DONOR_AGENCY.equals(donorType)
                                ? donorIds.contains(note.getDonor().getAmpOrgId())
                                : GPIReportConstants.HIERARCHY_DONOR_GROUP.equals(donorType)
                                        ? donorIds.contains(note.getDonor().getOrgGrpId().getAmpOrgGrpId()) : false;

        filteredNotes = donorNotes.stream().filter(fromDatePredicate).filter(toDatePredicate).filter(donorPredicate)
                .sorted((n1, n2) -> n2.getNotesDate().compareTo(n1.getNotesDate())).collect(Collectors.toList());

        return filteredNotes;
    }

    public static Set<Long> getVerifiedOrgsList() {
        TeamMember tm = TeamUtil.getCurrentMember();
        AmpTeamMember atm = TeamMemberUtil.getAmpTeamMember(tm.getMemberId());
        Set<Long> orgs = new HashSet<>();
        
        for (AmpOrganisation verifiedOrg : atm.getUser().getAssignedOrgs()) {
            orgs.add(verifiedOrg.getAmpOrgId());
        }

        return orgs;
    }
    
    public static void deleteDonorNotes(Long id) {
        AmpGPINiDonorNotes donorNotes = GPIUtils.getDonorNotesById(id);
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            session.delete(donorNotes);
        } catch (Exception e) {
            logger.error("Exception from delete: " + e.getMessage());
        }
    }
    
    public static boolean isValidSortColumn(String columnName) {
        return GPIEPConstants.SORT_FIELDS.containsKey(columnName);
    }

    public static boolean isValidSortOrder(String sort) {
        return GPIEPConstants.ORDER_ASC.equals(sort) || GPIEPConstants.ORDER_DESC.equals(sort);
    }

    public static List<Org> getDonors() {
        final List<Org> donors = new ArrayList<>();
        PersistenceManager.getSession().doWork(new Work() {
            public void execute(Connection conn) throws SQLException {
                Map<Long, String> organisationsNames = QueryUtil.getTranslatedName(conn, "amp_organisation",
                        "amp_org_id", "name");
                String query = "SELECT (o.amp_org_id) orgId, o.name, o.acronym FROM  amp_organisation o "
                        + " WHERE o.amp_org_id IN (SELECT distinct o.amp_org_id "
                        + " FROM  amp_organisation o, amp_funding af, amp_activity_version v, amp_role r   "
                        + " WHERE  o.amp_org_id = af.amp_donor_org_id  "
                        + " AND v.amp_activity_id = af.amp_activity_id  AND (v.deleted is false) "
                        + " AND ((af.source_role_id IS NULL) "
                        + " OR af.source_role_id = r.amp_role_id and r.role_code = 'DN') "
                        + " AND (o.deleted IS NULL OR o.deleted = false))";
                query += " order by o.name";

                try (RsInfo rsi = SQLUtils.rawRunQuery(conn, query, null)) {
                    ResultSet rs = rsi.rs;

                    while (rs.next()) {
                        String name;
                        if (ContentTranslationUtil.multilingualIsEnabled()) {
                            name = organisationsNames.get(rs.getLong("orgId"));
                        } else {
                            name = rs.getString("name");
                        }
                        donors.add(new Org(rs.getLong("orgId"), name, rs.getString("acronym")));
                    }
                }

            }
        });
        return donors;
    }
    
    public static Date getYearStartDate(AmpFiscalCalendar calendar, int year) {
        return CalendarUtil.getStartOfYear(year, calendar.getStartMonthNum() - 1, calendar.getStartDayNum());
    }

    public static Date getYearEndDate(AmpFiscalCalendar calendar, int year) {
        return new Date(
                CalendarUtil.getStartOfYear(year + 1, calendar.getStartMonthNum() - 1, calendar.getStartDayNum())
                        .getTime() - GPIEPConstants.MILLISECONDS_IN_DAY);
    }
}
