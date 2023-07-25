package org.digijava.module.esrigis.helpers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpColorThreshold;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.DynLocationManagerUtil;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.OrganizationSkeleton;
import org.digijava.module.aim.util.ProgramUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.type.LongType;

public class DbUtil {
    private static Logger logger = Logger.getLogger(DbUtil.class);
    
    public static List<AmpOrganisation> getOrganisationByRole(String role) {
        Session session = null;
        Query q = null;
        
        List<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
        StringBuilder queryString = new StringBuilder("select distinct org from " + AmpOrgRole.class.getName() + " orgRole inner join orgRole.role role inner join orgRole.organisation org ");
        queryString.append(" where  role.roleCode='"+role+"' ");
        try {
            session = PersistenceManager.getRequestDBSession();
            q = session.createQuery(queryString.toString());

            organizations = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get organization from database ", ex);
        }
        Collections.sort(organizations);
        return organizations;
    }   
    
    /**
     * gets skeletons for all the organisations in the database
     * @return
     */
    public static List<OrganizationSkeleton> getOrganisationSkeletons() {
        return org.digijava.module.aim.util.DbUtil.getOrgSkeletonByGroupId(null);
    }   
    
    public static List<AmpOrganisation> getOrganisations() {
        Session session = null;
        Query q = null;
        
        List<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
        StringBuilder queryString = new StringBuilder("select distinct org from " + AmpOrganisation.class.getName() + " org ");
        try {
            session = PersistenceManager.getRequestDBSession();
            q = session.createQuery(queryString.toString());
            organizations = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get organization from database ", ex);
        }
        Collections.sort(organizations);
        return organizations;
    }   
    
    public static List<AmpSector> getParentSectorsFromConfig(Long configId) throws DgException{
            Session session = null;
            List<AmpSector> sectors =null;
            StringBuilder queryString = new StringBuilder();
            Query qry = null;
            try {
                session = PersistenceManager.getRequestDBSession();
                //queryString.append("select distinct sec from ");
                //queryString.append(AmpActivitySector.class.getName());
                //queryString.append(" actSec inner join actSec.classificationConfig cls inner join actSec.sectorId sec where  cls.id=:configId and sec.ampSecSchemeId=cls.classification and sec.parentSectorId is null order by sec.name");

                queryString.append("SELECT DISTINCT sect FROM ");
                queryString.append(AmpSector.class.getName());
                queryString.append(" sect WHERE (sect.deleted is null or sect.deleted = false) and (sect.ampSectorId IN ");

                queryString.append("(SELECT DISTINCT sec FROM ");   
                queryString.append(AmpActivitySector.class.getName());
                queryString.append(" actSec INNER JOIN actSec.classificationConfig cls INNER JOIN actSec.sectorId sec WHERE cls.id=:configId AND sec.ampSecSchemeId=cls.classification) ");

                queryString.append(" OR sect.ampSectorId IN ");

                queryString.append("(SELECT DISTINCT sec.parentSectorId FROM ");    
                queryString.append(AmpActivitySector.class.getName());
                queryString.append(" actSec INNER JOIN actSec.classificationConfig cls INNER JOIN actSec.sectorId sec WHERE cls.id=:configId AND sec.ampSecSchemeId=cls.classification) ");

                queryString.append(" OR sect.ampSectorId IN ");

                queryString.append("(SELECT DISTINCT sec.parentSectorId.parentSectorId FROM "); 
                queryString.append(AmpActivitySector.class.getName());
                queryString.append(" actSec INNER JOIN actSec.classificationConfig cls INNER JOIN actSec.sectorId sec WHERE cls.id=:configId AND sec.ampSecSchemeId=cls.classification)) ");

                queryString.append(" AND sect.parentSectorId IS NULL"); //ORDER BY " + AmpSector.hqlStringForName("sect") + " ");
                
                qry = session.createQuery(queryString.toString());
                qry.setLong("configId", configId);
                sectors=qry.list();
            } catch (Exception ex) {
                logger.error("Unable to get config from database ",ex);
                throw new DgException(ex);

            }
            return sectors;

    }
    
    public static List<AmpSector> getSubSectors(Long id){
        Session session = null;
        Query qry = null;
        List<AmpSector> col = new ArrayList<AmpSector>();
        try {   
            session = PersistenceManager.getSession();
            String queryString = "SELECT s.* FROM amp_sector s "
                    + "WHERE s.parent_sector_id =:id " ;
             qry = session.createSQLQuery(queryString).addEntity(AmpSector.class);
             qry.setParameter("id", id, LongType.INSTANCE);
             col = qry.list();
        } catch (Exception ex) {
            logger.error("Exception while getting sub-sectors : " + ex);
        } 
        return col;
    }
    
    public static String buildYearsInStatement(int startYear, int endYear) {
        if (startYear > endYear)
            return "-32768"; // avoid generating an "IN ()" substatement
        
        String years = "";
        boolean hasValues = false;
        for (int i = startYear; i <= endYear; i++) {
            years += "'" + i + "',";
            hasValues = true;
        }
        if(hasValues) {
            years = years.substring(0, years.length() - 1);
        }
        return years;
    }
    
    public static Set<AmpCategoryValueLocations> getSubRegions(Long id){
       // List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();

        if (id != null && id != -1) {
            AmpCategoryValueLocations region;
            try {
                region = LocationUtil.getAmpCategoryValueLocationById(id);
                if (region.getChildLocations() != null) {
                    return region.getChildLocations();
                }
            } catch (DgException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static List<AmpTheme> getPrograms(int programSetting){
        List<AmpTheme> programs = new ArrayList<AmpTheme>();
       
        try {
            String oql = "select prog from " + AmpTheme.class.getName() + " prog";
            Session session = PersistenceManager.getRequestDBSession();
            Query query = session.createQuery(oql);
            programs = query.list();
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        AmpActivityProgramSettings sett = null;
        switch (programSetting) {
        case 0:
            sett = getProgramSettingByName("National Plan Objective");
            break;
        case 1:
            sett = getProgramSettingByName("Primary Program");
            break;
        case 2:
            sett = getProgramSettingByName("Secondary Program");
            break;

        default:
            break;
        }
       
        List<AmpTheme> programs2 = new ArrayList<AmpTheme>();
        for (Iterator iterator = programs.iterator(); iterator.hasNext();) {
            AmpTheme ampTheme = (AmpTheme) iterator.next();
            if (ampTheme.getIndlevel()>0)
                if (sett.getDefaultHierarchyId()== ProgramUtil.getTopLevelProgram(ampTheme).getParentThemeId().getAmpThemeId())
                    programs2.add(ampTheme);
        }
        return programs2;
    }
    
    public static AmpActivityProgramSettings getProgramSettingByName(String name) {
        Session session = null;
        AmpActivityProgramSettings sett = null;
        Iterator itr = null;
        
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select aaps from " + AmpActivityProgramSettings.class.getName() 
            + " aaps where aaps.name='"+name+"'";
            Query qry = session.createQuery(queryString);
            qry = session.createQuery(queryString);
            itr = qry.list().iterator();
            if (itr.hasNext()) {
                sett = (AmpActivityProgramSettings) itr.next();
            }
        } catch (Exception ex) {
            logger.error("Unable to get Program Setting from database", ex);
        }
        return sett;
    }
    
    public static AmpTheme getAmpThemeById(Long id) {
        Session session = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            AmpTheme theme = (AmpTheme) session.get(AmpTheme.class, id);
            return theme;
        } catch (Exception ex) {
            logger.error("Unable to get Theme from DB:", ex);
        }
        return null;
    }

    /**
     * extracts a relevant Total from a FundingCalculationsHelper instance
     * @param cal
     * @param transactionType
     * @param adjustmentType
     * @return
     */
    public static DecimalWraper extractTotals(FundingCalculationsHelper cal, int transactionType, String adjustmentType)
    {       
        switch (transactionType) {
        
            case Constants.MTEFPROJECTION:
                return cal.getTotalMtef();
            
            case Constants.EXPENDITURE:
                if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
                    return cal.getTotActualExp();
                } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
                    return cal.getTotPlannedExp();
                } else {
                    return cal.getTotPipelineExp();
                }

            case Constants.DISBURSEMENT:
                if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
                    return cal.getTotActualDisb();
                } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
                    return cal.getTotPlanDisb();
                } else {
                    return cal.getTotPipelineDisb();
                }

            case Constants.COMMITMENT:
                if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
                    return cal.getTotActualComm();
                } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
                    return cal.getTotPlannedComm();
                } else {
                    return cal.getTotPipelineComm();
                }
        }
        throw new RuntimeException("unknown / unsupported transaction type " + transactionType);
    }
    
    
    public static AmpOrganisation getOrganisation(Long id) {
        Session session = null;
        AmpOrganisation org = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from "
                + AmpOrganisation.class.getName() + " o "
                + "where (o.ampOrgId=:id) and (o.deleted is null or o.deleted = false) ";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                org = (AmpOrganisation) itr.next();
            }

        } catch (Exception ex) {
            logger.error("Unable to get organisation from database", ex);
        }
        logger.debug("Getting organisation successfully ");
        return org;
    }
    
    public static Collection<AmpOrganisation> getOrganisationsFromGroup(Long id) {
        Session session = null;
        Collection<AmpOrganisation> orgs = null;
        
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from "
                + AmpOrganisation.class.getName() + " o "
                + "where (o.orgGrpId=:id) and (o.deleted is null or o.deleted = false) ";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, LongType.INSTANCE);
            orgs = qry.list();
            
        } catch (Exception ex) {
            logger.error("Unable to get organisations by group from database", ex);
        }
        logger.debug("Getting organisations successfully ");
        return orgs;
    }
    
    public static AmpOrgGroup getOrgGroup(Long id) {
        Session session = null;
        AmpOrgGroup orgGrp = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select o from "
                + AmpOrgGroup.class.getName() + " o "
                + "where (o.ampOrgGrpId=:id)";
            Query qry = session.createQuery(queryString);
            qry.setParameter("id", id, LongType.INSTANCE);
            Iterator itr = qry.list().iterator();
            while (itr.hasNext()) {
                orgGrp = (AmpOrgGroup) itr.next();
            }

        } catch (Exception ex) {
            logger.error("Unable to get organisation group from database", ex);
        }
        logger.debug("Getting organisation successfully ");
        return orgGrp;
    }
    
    public static AmpContact getPrimaryContactForOrganization(Long orgId) throws DgException{
        AmpContact contact=null;
        Session session = null;
        StringBuilder queryString = new StringBuilder();
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString.append("select con from ");
            queryString.append(AmpOrganisation.class.getName());
            queryString.append(" org inner join org.organizationContacts orgContact  ");
            queryString.append(" inner join orgContact.contact con ");
            queryString.append(" where org.ampOrgId=:orgId and orgContact.primaryContact=true and (org.deleted is null or org.deleted = false) ");
            qry = session.createQuery(queryString.toString());
            qry.setLong("orgId", orgId);
            if (qry.uniqueResult()!=null){
                contact=(AmpContact)qry.uniqueResult();
            } else {
                queryString = new StringBuilder();
                queryString.append("select con from ");
                queryString.append(AmpOrganisation.class.getName());
                queryString.append(" org inner join org.organizationContacts orgContact  ");
                queryString.append(" inner join orgContact.contact con ");
                queryString.append(" where org.ampOrgId=:orgId and (org.deleted is null or org.deleted = false) ");
                qry = session.createQuery(queryString.toString());
                qry.setLong("orgId", orgId);
                if (qry.list()!=null && qry.list().size()>0)
                    contact=(AmpContact)qry.list().iterator().next();
            }
        } catch (Exception ex) {
            logger.error("Unable to get contact from database ",ex);
            throw new DgException(ex);

        }
        return contact;
    }
    public static void saveAdditionalInfo(Long id, String type, String background,String description, String keyAreas) throws DgException{
        Session sess = null;
        Transaction tx = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
            if(type.equals("Organization")){
                AmpOrganisation org = (AmpOrganisation) sess.get(AmpOrganisation.class, id);
                org.setOrgBackground(background);
                org.setOrgDescription(description);
                org.setOrgKeyAreas(keyAreas);
                sess.update(org);
            }
            else if (type.equals("OrganizationGroup")){
                AmpOrgGroup orgGrp = (AmpOrgGroup) sess.get(AmpOrgGroup.class, id);
                orgGrp.setOrgGrpBackground(background);
                orgGrp.setOrgGrpDescription(description);
                orgGrp.setOrgGrpKeyAreas(keyAreas);
                sess.update(orgGrp);
            }

        
        } catch (Exception e) {
            logger.error("Unable to update", e);
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException ex) {
                    logger.error("rollback() failed", ex);
                }
            }
              throw new DgException(e);
        }

    }

//  public static ArrayList<AmpSector> getAmpSectors() {
//      Session session = null;
//      Query q = null;
//      AmpSector ampSector = null;
//      ArrayList<AmpSector> sector = new ArrayList<AmpSector>();
//      String queryString = null;
//      Iterator<AmpSector> iter = null;
//
//      try {
//          session = PersistenceManager.getSession();
//          queryString = " select Sector from "
//                  + AmpSector.class.getName()
//                  + " Sector where Sector.parentSectorId is not null and (Sector.deleted is null or Sector.deleted = false) order by Sector.name";
//          q = session.createQuery(queryString);
//          iter = q.list().iterator();
//
//          while (iter.hasNext()) {
//              ampSector = (AmpSector) iter.next();
//              sector.add(ampSector);
//          }
//
//      } catch (Exception ex) {
//          logger.error("Unable to get Sector names  from database "
//                  + ex.getMessage());
//      }
//      return sector;
//  }
    public static List<AmpTeam> getAllChildComputedWorkspaces() {
        Session session = null;
        List<AmpTeam> teams = null;
        
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select team from "
                + AmpTeam.class.getName() + " team "
                + "where (team.parentTeamId is not null and team.computation=true)";
            Query qry = session.createQuery(queryString);
            teams = qry.list();
            
        } catch (Exception ex) {
            logger.error("Unable to get computed-child workspaces from database", ex);
        }
        logger.debug("Getting computed-child workspaces successfully ");
        return teams;
    }
    
    public static AmpCategoryValueLocations getLocationById(Long id) {
        Session session = null;
        try {
            session = PersistenceManager.getSession();
            String queryString = "select loc from "
            + AmpCategoryValueLocations.class.getName()
            + " loc where (loc.id=:id)" ;                   
            Query qry = session.createQuery(queryString);
            qry.setLong("id", id);
            AmpCategoryValueLocations returnLoc = (AmpCategoryValueLocations)qry.uniqueResult();
            return returnLoc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AmpTheme getProgramById(Long id) {
        Session session = null;
        AmpTheme prog = null;
        Iterator itr = null;
        
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select p from "
                + AmpTheme.class.getName() + " p where (p.ampThemeId=:id)";
            Query qry = session.createQuery(queryString);
            qry = session.createQuery(queryString);
            qry.setParameter("id", id, LongType.INSTANCE);
            itr = qry.list().iterator();
            if (itr.hasNext()) {
                prog = (AmpTheme) itr.next();
            }
        } catch (Exception ex) {
            logger.error("Unable to get Theme from database", ex);
        }
        return prog;
    }

    public static ArrayList<BigInteger> getInActivities(String query) throws Exception{
        Session session = PersistenceManager.getRequestDBSession();
        ArrayList<BigInteger> result = (ArrayList<BigInteger>) session.createSQLQuery("select amp_activity_id from amp_activity where " + query).list();
        return result;
    }
    
    /**
     * @return list of color thresholds, ordered by threshold ascending 
     */
    public static List<AmpColorThreshold> getColorThresholds() {
        return PersistenceManager.getSession().createCriteria(AmpColorThreshold.class)
                .addOrder(Order.asc("thresholdStart")).list();
    }
     
}
