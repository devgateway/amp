package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpGPINiAidOnBudget;
import org.digijava.module.aim.dbentity.AmpGPINiDonorNotes;
import org.hibernate.Session;
import org.hibernate.Query;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;

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

    public static Set<Long> getVerifiedOrgsList(){
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
    
    
}
