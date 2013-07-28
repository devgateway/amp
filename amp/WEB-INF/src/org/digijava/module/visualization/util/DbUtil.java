package org.digijava.module.visualization.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivityGroupCached;
import org.digijava.module.aim.dbentity.AmpActivityProgramSettings;
import org.digijava.module.aim.dbentity.AmpActivitySector;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpCategoryValueLocations;
import org.digijava.module.aim.dbentity.AmpContact;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpFundingMTEFProjection;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgRole;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.LocationUtil;
import org.digijava.module.aim.util.SectorUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryConstants.HardCodedCategoryValue;
import org.digijava.module.fundingpledges.dbentity.FundingPledgesDetails;
import org.digijava.module.visualization.dbentity.AmpDashboard;
import org.digijava.module.visualization.dbentity.AmpDashboardGraph;
import org.digijava.module.visualization.dbentity.AmpGraph;
import org.digijava.module.visualization.form.DashboardForm;
import org.digijava.module.visualization.helper.DashboardFilter;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DbUtil {
	private static Logger logger = Logger.getLogger(DbUtil.class);

	public static List<AmpOrgGroup> getOrganisationGroupsByRole(boolean publicView, DashboardFilter filter) {
        Session session = null;
        Query q = null;
        
        List<AmpOrgGroup> organizations = new ArrayList<AmpOrgGroup>();
        StringBuilder queryString = new StringBuilder("select distinct org.orgGrpId from " + AmpOrgRole.class.getName() + " orgRole inner join orgRole.role role inner join orgRole.organisation org ");
        if (publicView) {
            queryString.append(" inner join orgRole.activity act  inner join act.team tm ");
        }
        if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY)
        	queryString.append(" where  role.roleCode='EA' ");
        else if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY)
        	queryString.append(" where  role.roleCode='BA' ");
        else
			queryString.append(" where  role.roleCode='DN' ");

        if (publicView) {
            queryString.append(" and act.draft=false and act.approvalStatus ='approved' and tm.parentTeamId is not null ");
        }

//        queryString.append("order by org.orgGrpId.orgGrpName asc");
        try {
            session = PersistenceManager.getRequestDBSession();
            q = session.createQuery(queryString.toString());

            organizations = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get Amp organization group names from database ", ex);
        }
        Collections.sort(organizations);
        return organizations;
    }	
	
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
	
	public static List<AmpOrganisation> getDonorOrganisationByGroupId(
			Long orgGroupId, boolean publicView, DashboardFilter filter) {
        Session session = null;
        Query q = null;
        List<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
        StringBuilder queryString = new StringBuilder("select distinct org from " + AmpOrgRole.class.getName() + " orgRole inner join orgRole.role role inner join orgRole.organisation org ");
        if (publicView) {
            queryString.append(" inner join orgRole.activity act  inner join act.team tm ");
        }
        if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY)
        	queryString.append(" where  role.roleCode='EA' ");
        else if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY)
        	queryString.append(" where  role.roleCode='BA' ");
        else
			queryString.append(" where  role.roleCode='DN' ");
         if (orgGroupId != null&&orgGroupId !=-1) {
            queryString.append(" and org.orgGrpId=:orgGroupId ");
        }
        if (publicView) {
            queryString.append(" and act.draft=false and act.approvalStatus ='approved' and tm.parentTeamId is not null ");
        }

        queryString.append("order by org.name asc");
        try {
            session = PersistenceManager.getRequestDBSession();
            q = session.createQuery(queryString.toString());
            if (orgGroupId != null&&orgGroupId !=-1) {
                q.setLong("orgGroupId", orgGroupId);
            }
            organizations = q.list();
        } catch (Exception ex) {
            logger.error("Unable to get Amp organization names from database ", ex);
        }
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

	            queryString.append(" AND sect.parentSectorId IS NULL ORDER BY sect.name ");
	            
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
             qry.setParameter("id", id, Hibernate.LONG);
             col = qry.list();
		} catch (Exception ex) {
			logger.error("Exception while getting sub-sectors : " + ex);
		} 
        return col;
	}
	
	/**
     * Returns pledge amount in selected currency
     * for selected organization and year
     * @param orgID
     * @param year
     * @param currCode
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    @SuppressWarnings("unchecked")
    public static DecimalWraper getPledgesFunding(DashboardFilter filter,
            Date startDate, Date endDate,
            String currCode) throws DgException {
    	DecimalWraper totalPlannedPldges = new DecimalWraper();
        Long[] orgsGrpIds = filter.getOrgGroupIds();
		Long orgsGrpId = filter.getOrgGroupId();
		Long[] orgIds = filter.getSelOrgIds();
		Long[] orgGroupIds;
		if (orgsGrpIds == null || orgsGrpIds.length == 0 || orgsGrpIds[0] == -1) {
			Long[] temp = {orgsGrpId};
	        orgGroupIds = temp;
		} else {
	        orgGroupIds = orgsGrpIds;
		}	
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Integer startYear = Integer.valueOf(sdf.format(startDate));
        Integer endYear = Integer.valueOf(sdf.format(endDate));
        String years = "";
        for (int i = startYear; i <= endYear; i++) {
			if(!years.equals(""))
				years = years + ", ";
			years = years + "'" + i + "'";
		}
        
        String oql = "select fd ";
        oql += " from ";
        oql += FundingPledgesDetails.class.getName()
                + " fd inner join fd.pledgeid plg ";
        String where = " where  fd.fundingYear in (" + years + ")";
        if (orgIds == null || orgIds.length==0 || orgIds[0] == -1) {
            if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
                oql += " left outer join  plg.organization org  ";
            	oql += " inner join plg.organizationGroup orgGrp ";
            	where += " and (org.orgGrpId.ampOrgGrpId in (" + DashboardUtil.getInStatement(orgGroupIds) + ") ";
            	where += " or  orgGrp.ampOrgGrpId in (" + DashboardUtil.getInStatement(orgGroupIds) + ")) ";
            }
        } else {
            oql += " inner join  plg.organization org  ";
        	where += " and org.ampOrgId in (" + DashboardUtil.getInStatement(orgIds) + ") ";
        }
        oql += where;
        Session session = PersistenceManager.getRequestDBSession();
        List<FundingPledgesDetails> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            //query.setDate("startDate", startDate);
            //query.setDate("endDate", endDate);
            //if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1) && orgGroupId != -1) {
            //    query.setLong("orgGroupId", orgGroupId);
            //}
            fundingDets = query.list();
            Iterator<FundingPledgesDetails> fundDetIter = fundingDets.iterator();
            while (fundDetIter.hasNext()) {
                FundingPledgesDetails pledge = fundDetIter.next();
                //converting amounts
                java.sql.Date dt = new java.sql.Date(pledge.getFunding_date().getTime());
                double frmExRt = Util.getExchange(pledge.getCurrency().getCurrencyCode(), dt);
                double toExRt = Util.getExchange(currCode, dt);
                DecimalWraper amt = CurrencyWorker.convertWrapper(pledge.getAmount(), frmExRt, toExRt, dt);
                totalPlannedPldges.setValue(totalPlannedPldges.getValue().add(amt.getValue()));
            }



        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }


        return totalPlannedPldges;
    }
	public static Set<AmpCategoryValueLocations> getSubRegions(Long id){
        List<AmpCategoryValueLocations> zones = new ArrayList<AmpCategoryValueLocations>();

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
    
    public static List<AmpCategoryValueLocations> getRegions(DashboardFilter filter) throws DgException {
    	List<AmpCategoryValueLocations> locations = new ArrayList<AmpCategoryValueLocations>();
    	if (filter.getSelLocationIds()!=null && filter.getSelLocationIds().length > 0 && filter.getSelLocationIds()[0] != -1) {
			if (filter.getSelLocationIds().length == 1) {
				AmpCategoryValueLocations loc = LocationUtil.getAmpCategoryValueLocationById(filter.getSelLocationIds()[0]);
				locations.addAll(loc.getChildLocations());
				return locations;
			} else {
				for (int i = 0; i < filter.getSelLocationIds().length; i++) {
					AmpCategoryValueLocations loc = LocationUtil.getAmpCategoryValueLocationById(filter.getSelLocationIds()[i]);
					locations.add(loc);
				}
				return locations;
			}
		} else {
			
	        Long[] orgGroupIds = filter.getSelOrgGroupIds();
	        Long[] orgIds = filter.getSelOrgIds();
	        
	        int transactionType = filter.getTransactionType();
	        TeamMember tm = filter.getTeamMember();
	        // apply calendar filter
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
	        
	        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
	        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
	        Long[] sectorIds = filter.getSelSectorIds();
	        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
	        Long[] programIds = filter.getSelProgramIds();
	        boolean programCondition = programIds != null && programIds.length > 0 && !programIds[0].equals(-1l);
	        /*
	         * We are selecting regions which are funded
	         * In selected year by the selected organization
	         *
	         */
	        try {
	            String oql = "select distinct loc  ";
	            oql += getHQLQuery(filter, orgIds, orgGroupIds, true, sectorCondition, programCondition, null, sectorIds, programIds, null, null, tm, true);
	            oql+=" order by loc.parentCategoryValue";
	            
	            Session session = PersistenceManager.getRequestDBSession();
	            Query query = session.createQuery(oql);
	            query.setDate("startDate", startDate);
	            query.setDate("endDate", endDate);
	            if (sectorCondition)
	            	query.setLong("config", filter.getSelSectorConfigId());
	            if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
		            query.setLong("transactionType", transactionType);
		            query.setString("adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
	            }
                
	            locations = query.list();
	        }
	        catch (Exception e) {
	            logger.error(e);
	            throw new DgException("Cannot load regions from db", e);
	        }
	        return locations;
		}
     }
    
    public static List<AmpSector> getSectors(DashboardFilter filter) throws DgException {
    	List<AmpSector> sectors = new ArrayList<AmpSector>();
        if (filter.getSelSectorIds()!=null && filter.getSelSectorIds().length > 0 && filter.getSelSectorIds()[0] != -1) {
			if (filter.getSelSectorIds().length == 1) {
				List<AmpSector> sector = getSubSectors(filter.getSelSectorIds()[0]);
				AmpSector parentSector = SectorUtil.getAmpSector(filter.getSelSectorIds()[0]);
				sector.add(parentSector);
				sectors.addAll(sector);
				return sectors;
			} else {
				for (int i = 0; i < filter.getSelSectorIds().length; i++) {
					AmpSector sector = SectorUtil.getAmpSector(filter.getSelSectorIds()[i]);
					sectors.add(sector);
				}
				return sectors;
			}
		} else {
			//Get the selected Organization Groups and Organizations
			Long[] orgGroupIds = filter.getSelOrgGroupIds();
	        Long[] orgIds = filter.getSelOrgIds();
	        
	        //Get the funding transaction type
	        int transactionType = filter.getTransactionType();

	        //Get the user logged in to filter later
	        TeamMember tm = filter.getTeamMember();

	        //Get the Fiscal Calendar to determine Start/End Date
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
	        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
	        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
	        Long[] locationIds = filter.getSelLocationIds();
	        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
	        Long[] programIds = filter.getSelProgramIds();
	        boolean programCondition = programIds != null && programIds.length > 0 && !programIds[0].equals(-1l);
	        /*
	         * We are selecting sectors which are funded
	         * In selected year by the selected organization
	         *
	         */
	        try {
	            String oql = "select distinct sec ";
	            oql += getHQLQuery(filter, orgIds, orgGroupIds, locationCondition, true, programCondition, locationIds, null, programIds, null, null, tm, true);

	            Session session = PersistenceManager.getRequestDBSession();
	            Query query = session.createQuery(oql);
	            query.setDate("startDate", startDate);
	            query.setDate("endDate", endDate);
	            //if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1) && orgGroupId != -1) {
	            //    query.setLong("orgGroupId", orgGroupId);
	            //}
	            if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
		            query.setLong("transactionType", transactionType);
		            query.setString("adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
	            }
	            query.setLong("config", filter.getSelSectorConfigId());
	            sectors = query.list();
	        }
	        catch (Exception e) {
	            logger.error(e);
	            throw new DgException("Cannot load sectors from db", e);
	        }
	        return sectors;
    	}
     }
    
    public static List<AmpTheme> getPrograms(DashboardFilter filter, int programSetting) throws DgException {
    	List<AmpTheme> programs = new ArrayList<AmpTheme>();
       
		//Get the selected Organization Groups and Organizations
		Long[] orgGroupIds = filter.getSelOrgGroupIds();
        Long[] orgIds = filter.getSelOrgIds();
        
        //Get the funding transaction type
        int transactionType = filter.getTransactionType();

        //Get the user logged in to filter later
        TeamMember tm = filter.getTeamMember();

        //Get the Fiscal Calendar to determine Start/End Date
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
        Long[] locationIds = filter.getSelLocationIds();
        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
        Long[] sectorIds = filter.getSelSectorIds();
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
        /*
         * We are selecting sectors which are funded
         * In selected year by the selected organization
         *
         */
        try {
            String oql = "select distinct prog ";
            oql += getHQLQuery(filter, orgIds, orgGroupIds, locationCondition, sectorCondition, true, locationIds, sectorIds, null, null, null, tm, true);
            Session session = PersistenceManager.getRequestDBSession();
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            //if ((orgIds == null || orgIds.length==0 || orgIds[0] == -1) && orgGroupId != -1) {
            //    query.setLong("orgGroupId", orgGroupId);
            //}
            if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
	            query.setLong("transactionType", transactionType);
	            query.setString("adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
            }
            if (sectorCondition)
            	query.setLong("config", filter.getSelSectorConfigId());

            programs = query.list();
        }
        catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load sectors from db", e);
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
				if (sett.getDefaultHierarchyId()== DashboardUtil.getTopLevelProgram(ampTheme).getParentThemeId().getAmpThemeId())
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
    
    public static List getActivities(DashboardFilter filter, Date startDate,
            Date endDate, Long assistanceTypeId,
            Long financingInstrumentId,
            int transactionType,String adjustmentTypeActual) throws DgException {
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        List activities = null;
        Long[] orgIds= filter.getSelOrgIds();
        
        //int transactionType = filter.getTransactionType();
        TeamMember tm = filter.getTeamMember();
        // apply calendar filter
        Long fiscalCalendarId = filter.getFiscalCalendarId();
        
        //Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
        //Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
        Long[] locationIds = filter.getSelLocationIds();
        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
        Long[] sectorIds = filter.getSelSectorIds();
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
        Long[] programIds = filter.getSelProgramIds();
        boolean programCondition = programIds != null && programIds.length > 0 && !programIds[0].equals(-1l);

        //Sectors should include all the subsectors
        if(sectorCondition){
        	sectorIds = getAllDescendants(sectorIds, filter.getAllSectorList());
        }
        /*
         * We are selecting sectors which are funded
         * In selected year by the selected organization
         *
         */
        try {
        	String oql = "select act.ampActivityId, act.ampId, act.name ";
            oql += getHQLQuery(filter, orgIds, orgGroupIds, locationCondition, sectorCondition, programCondition, locationIds, sectorIds, programIds, assistanceTypeId, financingInstrumentId, tm, true);
            Session session = PersistenceManager.getRequestDBSession();
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (sectorCondition)
            	query.setLong("config", filter.getSelSectorConfigId());
            
            if (assistanceTypeId != null) {
                query.setLong("assistanceTypeId", assistanceTypeId);
            }
            if (financingInstrumentId != null) {
                query.setLong("financingInstrumentId", financingInstrumentId);
            }
            if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
	            query.setLong("transactionType", transactionType);
	            query.setString("adjustmentType",adjustmentTypeActual);
            }
            activities = query.list();
        }
        catch (Exception e) {
            logger.error(e);
            throw new DgException("Cannot load activities from db", e);
        }
        return activities;

     }
      
	public static List<AmpOrganisation> getAgencies(DashboardFilter filter) throws DgException {
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        List<AmpOrganisation> agencies = new ArrayList<AmpOrganisation>();
        Long[] orgIds= filter.getSelOrgIds();
        if (orgGroupIds!=null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
			if (orgGroupIds.length == 1) {
				List<AmpOrganisation> donorsByGrp = org.digijava.module.aim.util.DbUtil.getOrganisationByGroupId(orgGroupIds[0]);
				agencies.addAll(donorsByGrp);
				return agencies;
			} else {
				for (int i = 0; i < orgGroupIds.length; i++) {
					List<AmpOrganisation> donorsByGrp = org.digijava.module.aim.util.DbUtil.getOrganisationByGroupId(orgGroupIds[i]);
					agencies.addAll(donorsByGrp);
				}
				return agencies;
			}
		} else {
	        int transactionType = filter.getTransactionType();
	        TeamMember tm = filter.getTeamMember();
	        // apply calendar filter
	        Long fiscalCalendarId = filter.getFiscalCalendarId();
	        
	        Date startDate = DashboardUtil.getStartDate(fiscalCalendarId, filter.getStartYear().intValue());
	        Date endDate = DashboardUtil.getEndDate(fiscalCalendarId, filter.getEndYear().intValue());
	        Long[] locationIds = filter.getSelLocationIds();
	        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
	        Long[] sectorIds = filter.getSelSectorIds();
	        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
	        Long[] programIds = filter.getSelProgramIds();
	        boolean programCondition = programIds != null && programIds.length > 0 && !programIds[0].equals(-1l);
	        /*
	         * We are selecting sectors which are funded
	         * In selected year by the selected organization
	         *
	         */
	        try {
	            String oql = "select distinct agency ";
	            String specialInner = "";
	            String specialCondition = "";
	            if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY || filter.getAgencyType() == org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY)
	            	specialInner = " inner join act.orgrole orole inner join orole.role role ";
	            
	            switch (filter.getAgencyType()) {
	            case org.digijava.module.visualization.util.Constants.DONOR_AGENCY:
	            	specialInner += " inner join f.ampDonorOrgId agency ";
	    			break;
	            case org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY:
	            	specialInner += " inner join orole.organisation agency ";
	            	specialCondition = " and role.roleCode='EA' ";
	    			break;
	            case org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY:
	            	specialInner += " inner join orole.organisation agency ";
	            	specialCondition = " and role.roleCode='BA' ";
	    			break;
	            }
	        	oql += getHQLQuery(filter, orgIds, orgGroupIds, locationCondition, sectorCondition, programCondition, locationIds, sectorIds, programIds, null, null, tm, true, specialInner, specialCondition);
	            Session session = PersistenceManager.getRequestDBSession();
	            Query query = session.createQuery(oql);
	            query.setDate("startDate", startDate);
	            query.setDate("endDate", endDate);
	            if (sectorCondition)
	            	query.setLong("config", filter.getSelSectorConfigId());
	            if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
		            query.setLong("transactionType", transactionType);
		            query.setString("adjustmentType", CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey());
	            }
                
                agencies = query.list();
	        }
	        catch (Exception e) {
	            logger.error(e);
	            throw new DgException("Cannot load activities from db", e);
	        }
	        return agencies;
		}
     }
    
    /**
     * Returns funding amount
     * @param orgID
     * @param year
     * @param assistanceTypeId
     * @param currCode
     * @param transactionType
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    @SuppressWarnings("unchecked")
    public static DecimalWraper getFunding(DashboardFilter filter, Date startDate,
            Date endDate, Long assistanceTypeId,
            Long financingInstrumentId,
            int transactionType, String adjustmentTypeActual) throws DgException {
        DecimalWraper total = null;
        String oql = "";
        String currCode = "USD";
        if (filter.getCurrencyId()!=null) {
        	currCode = CurrencyUtil.getCurrency(filter.getCurrencyId()).getCurrencyCode();
		} 

        Long[] orgIds = filter.getSelOrgIds();
        Long[] orgsGrpIds = filter.getOrgGroupIds();
		Long orgsGrpId = filter.getOrgGroupId();
		Long[] orgGroupIds;
		if (orgsGrpIds == null || orgsGrpIds.length == 0 || orgsGrpIds[0] == -1) {
			Long[] temp = {orgsGrpId};
	        orgGroupIds = temp;
		} else {
	        orgGroupIds = orgsGrpIds;
		}	
        
        TeamMember tm = filter.getTeamMember();
        Long[] locationIds = filter.getSelLocationIds();
        Long[] sectorIds = filter.getSelSectorIds();
        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
        Long[] programIds = filter.getSelProgramIds();
        boolean programCondition = programIds != null && programIds.length > 0 && !programIds[0].equals(-1l);
        
        if (filter.getTransactionType()==Constants.MTEFPROJECTION){
        	oql = "select fp, act.ampId, act.name ";
        } else {
        	oql = "select fd, act.ampId, act.name ";
        }
        
        if (locationCondition)
        	oql += ", actloc.locationPercentage ";
        	
        if (sectorCondition)
        	oql += ", actsec.sectorPercentage ";
        
        if (programCondition)
        	oql += ", actProg.programPercentage ";

    	oql += getHQLQuery(filter, orgIds, orgGroupIds, locationCondition, sectorCondition, programCondition, locationIds, sectorIds, programIds, assistanceTypeId, financingInstrumentId, tm, true);
        Session session = PersistenceManager.getRequestDBSession();
        List fundings = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (sectorCondition) {
            	query.setLong("config", filter.getSelSectorConfigId());
            }
            //if ((orgIds == null || orgIds.length == 0 || orgIds[0] == -1) && orgGroupId != -1) {
            //    query.setLong("orgGroupId", orgGroupId);
            //}
            if (assistanceTypeId != null) {
                query.setLong("assistanceTypeId", assistanceTypeId);
            }
            if (financingInstrumentId != null) {
                query.setLong("financingInstrumentId", financingInstrumentId);
            }
            if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
	            query.setLong("transactionType", transactionType);
	            query.setString("adjustmentType",adjustmentTypeActual);
            }
            
            if (filter.getActivityId()!=null) {
                query.setLong("activityId", filter.getActivityId());
            }
            fundings = query.list();
            
            Iterator it = fundings.iterator();
            ArrayList<AmpFundingDetail> afda = new ArrayList<AmpFundingDetail>();
            while(it.hasNext()){
            	Object[] item = (Object[])it.next();
            	AmpFundingDetail currentFd = null;
            	if(filter.getTransactionType()==Constants.MTEFPROJECTION){
            		AmpFundingMTEFProjection fp = (AmpFundingMTEFProjection) item[0];
            		//Here use a tricky harcode to of transaction/adjustment type and it is set to "actual commitments", this is done to use FundingCalculationsHelper to do calculations for MTEF projections
            		currentFd = new AmpFundingDetail(Constants.COMMITMENT,null,fp.getAmount(),fp.getProjectionDate(),fp.getAmpCurrency(),0d);
            	} else {
            		AmpFundingDetail fd = (AmpFundingDetail) item[0];
            		currentFd = new AmpFundingDetail(fd.getTransactionType(),fd.getAdjustmentType(),fd.getAbsoluteTransactionAmount(),fd.getTransactionDate(),fd.getAmpCurrencyId(),fd.getFixedExchangeRate());
                }
            	if (item.length==4) 
            		currentFd.setTransactionAmount(currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100);
            	if (item.length==5) 
            		currentFd.setTransactionAmount((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100);
            	if (item.length==6) 
            		currentFd.setTransactionAmount(((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100)*(Float)item[5]/100);
            		
            	afda.add(currentFd);
            }
            
            
            /*the objects returned by query  and   selected currency
            are passed doCalculations  method*/
            FundingCalculationsHelper cal = new FundingCalculationsHelper();
            cal.doCalculations(afda, currCode);
            /*Depending on what is selected in the filter
            we should return either actual commitments
            or actual Disbursement or  */
            if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
            	total = cal.getTotActualComm(); //takes the actual commitments 
        	} else {
                switch (transactionType) {
                case Constants.EXPENDITURE:
                    if (adjustmentTypeActual.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
                        total = cal.getTotActualExp();
                    } else if (adjustmentTypeActual.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
                        total = cal.getTotPlannedExp();
                    } else {
                        total = cal.getTotPipelineExp();
                    }
                    break;
                case Constants.DISBURSEMENT:
                    if (adjustmentTypeActual.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
                        total = cal.getTotActualDisb();
                    } else if (adjustmentTypeActual.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
                        total = cal.getTotPlanDisb();
                    } else {
                        total = cal.getTotPipelineDisb();
                    }
                    break;
                default:
                    if (adjustmentTypeActual.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
                        total = cal.getTotActualComm();
                    } else if (adjustmentTypeActual.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
                        total = cal.getTotPlannedComm();
                    } else {
                        total = cal.getTotPipelineComm();
                    }
                }
            }

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load fundings from db", e);
        }


        return total;
    }

    private static String getHQLQuery(DashboardFilter filter, Long[] orgIds, Long[] orgGroupIds, boolean locationCondition, boolean sectorCondition, boolean programCondition, Long[] locationIds, Long[] sectorIds, Long[] programIds, Long assistanceTypeId, Long financingInstrumentId, TeamMember tm, boolean fundingTypeSpecified) {
		return getHQLQuery(filter, orgIds, orgGroupIds, locationCondition, sectorCondition, programCondition, locationIds, sectorIds, programIds, assistanceTypeId, financingInstrumentId, tm, fundingTypeSpecified, null, null);
    }
	private static String getHQLQuery(DashboardFilter filter, Long[] orgIds, Long[] orgGroupIds, boolean locationCondition, boolean sectorCondition, boolean programCondition, Long[] locationIds, Long[] sectorIds, Long[] programIds, Long assistanceTypeId, Long financingInstrumentId, TeamMember tm, boolean fundingTypeSpecified, String specialInner, String specialCondition) {
		
		
		String oql = "";
		boolean donorCondition = (filter.getDonorAgencyId()!=null && filter.getDonorAgencyId()!=-1)? true : false;
        boolean implementingCondition = (filter.getImplementingAgencyId()!=null && filter.getImplementingAgencyId()!=-1)? true : false;
        boolean beneficiaryCondition = (filter.getBeneficiaryAgencyId()!=null && filter.getBeneficiaryAgencyId()!=-1)? true : false;
        boolean peaceMarkerCondition = (filter.getPeacebuilderMarkerId()!=null && filter.getPeacebuilderMarkerId()!=-1)? true : false;
        boolean peacebuildingCondition = (filter.getPeacebuildingId()!=null && filter.getPeacebuildingId()!=-1)? true : false;
        
        if (filter.getTransactionType()==Constants.MTEFPROJECTION){
	        oql += " from ";
			oql += AmpFundingMTEFProjection.class.getName() + " as fp inner join fp.ampFunding f ";
	        oql += "   inner join f.ampActivityId act ";
        } else {
        	if(filter.getFromPublicView() !=null&& filter.getFromPublicView()){
        		oql += " from "+AmpActivityGroupCached.class.getName()+" grpLink inner join grpLink.ampActivityGroup as actGroup, ";
        	} else { 
        		oql += " from ";
        	}
			oql += AmpFundingDetail.class.getName() + " as fd inner join fd.ampFundingId f ";
	        oql += "   inner join f.ampActivityId act ";
        }
        //Join  for Organization/Organization Groups and their role
        if ((orgIds != null && orgIds.length != 0 && orgIds[0] != -1) || (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1))
    		if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY || filter.getAgencyType() == org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY)
    			oql += " inner join act.orgrole orole inner join orole.role role ";
 
        if (specialInner!=null && specialInner.length()>0)
        	oql += specialInner;

        //If it doesn't come from public view, then it joins to the regual amp_activity_group
        if(!(filter.getFromPublicView() !=null&& filter.getFromPublicView()))
        	oql += " inner join act.ampActivityGroup actGroup ";
        	
        //Join for locations filter
        if (locationCondition) {
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        }

        //Join for National/NNNN Programs
        if (programCondition) {
        	oql += " inner join act.actPrograms actProg ";
            oql += " inner join actProg.program prog ";
		}
        //Join  for Organization/Organization Groups and their role
        if (donorCondition || implementingCondition || beneficiaryCondition)
    		oql += " inner join act.orgrole orole inner join orole.role role ";
        
        //Join for Category Values
        if ((filter.getSelCVIds()!=null && filter.getSelCVIds().length>0) || (filter.getSelStatusIds()!=null && filter.getSelStatusIds().length>0) || peaceMarkerCondition || peacebuildingCondition ) {
        	oql += " inner join act.categories categ ";
		}

        //Join for the Sectors (actsec) and the sector scheme (config)
        if (sectorCondition) {
            oql += "  inner join act.sectors actsec ";
            oql += "  inner join actsec.classificationConfig config  ";
            oql += " inner join actsec.sectorId sec ";
        }

        // Get only for one adjustment/transaction type combo, like "actual" "disbursements". The "else" gets all the fundings for some calls that need all different adjustment/transaction types.
        if(fundingTypeSpecified && filter.getTransactionType()!=Constants.MTEFPROJECTION)
        	oql += " where fd.transactionType =:transactionType  and  fd.adjustmentType.value =:adjustmentType ";
        else
        	oql += " where 1 = 1 ";
        
        if (specialCondition!=null && specialCondition.length()>0)
        	oql += specialCondition;
        
        // Filter to get only the sector scheme (classification config) selected
        if (sectorCondition) {
        	oql += " and config.id=:config ";
        }

        // Filter for the selected programs
        if (programCondition && filter.getSelProgramIds()!=null && filter.getSelProgramIds().length>0) {
        	oql += " and prog.ampThemeId in ("+DashboardUtil.getInStatement(DashboardUtil.getProgramsDescendentsIds(filter.getSelProgramIds()))+") ";
		}

        // Filter for the Organizations/Organization Groups and their roles (Donor, Executing or Beneficiary)
        if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) { // If there are any individual organizations selected, goes through the else and adds them to the query. If not, checks if there are organization groups selected. If none of those are selected, then nothing is added.
            if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) {
                oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds,filter.getAgencyType());
            }
        } else {
            oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds,filter.getAgencyType());
        }
        
        // Filter for the Organizations and their roles (Donor, Implementing or Beneficiary)
        if (donorCondition) {
        	oql = " and role.roleCode='DN' and orole.organisation in (" + filter.getDonorAgencyId() + ") ";
        } 
        if (implementingCondition) {
        	oql = " and role.roleCode='IA' and orole.organisation in (" + filter.getImplementingAgencyId() + ") ";
        } 
        if (beneficiaryCondition) {
        	oql = " and role.roleCode='BA' and orole.organisation in (" + filter.getBeneficiaryAgencyId() + ") ";
        } 
        
        //Filter for Category Values
        if (peaceMarkerCondition) {
        	oql += " and categ.id in ("+filter.getPeacebuilderMarkerId()+") ";
		}
        if (peacebuildingCondition) {
        	oql += " and categ.id in ("+filter.getPeacebuildingId()+") ";
		}
        
        //Filter for locations. If there's a location selected, it adds that location as well as all child locations. Null is used for unallocated locations on the Activities.
        if (locationCondition && locationIds != null && locationIds.length > 0) {
        	if (locationIds[0].equals(0l)) {
        		oql += " and actloc is NULL "; //Unallocated condition
			} else {
				locationIds = getAllDescendantsLocation(locationIds, DbUtil.getAmpLocations());
	            oql += " and loc.id in ("+DashboardUtil.getInStatement(locationIds)+") ";
			}
        }

        //Filter for sectors. If there's a sector selected, it adds that sector as well as all children.
        if (sectorCondition && sectorIds != null && sectorIds.length > 0) {
        	sectorIds = getAllDescendants(sectorIds, filter.getAllSectorList());
            oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
        }

        //Filter for On/Off Budget
        if (filter.getSelCVIds()!=null && filter.getSelCVIds().length>0) {
        	if (filter.getSelCVIds()[0]==-1) {
        		oql += " and act.ampActivityId NOT in ( "+ getActivitiesIdByCategoryRelated(DashboardUtil.getInStatement(filter.getBudgetCVIds()))+" )";
			} else {
				oql += " and categ.id in ("+DashboardUtil.getInStatement(filter.getSelCVIds())+") ";
			}
		}

        //Filter for Activity Status
        if (filter.getSelStatusIds()!=null && filter.getSelStatusIds().length>0) {
			oql += " and categ.id in ("+DashboardUtil.getInStatement(filter.getSelStatusIds())+") ";
		}

        //Filter for individual activity information (used for the list of projects that appear when you click on a line/bar/pie slice.
        if (filter.getActivityId()!=null) {
            oql += " and act.ampActivityId =:activityId ";
        }

        //Filter for the Fiscal Year Start and End
        if(filter.getTransactionType()==Constants.MTEFPROJECTION)
        	oql += " and  (fp.projectionDate>=:startDate and fp.projectionDate<=:endDate)  ";
        else
        	oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";

        //Filter for the aid type (assistance type) usually Grant/Loan/In Kind. It varies with the AMP configuration
        if (assistanceTypeId != null) {
            oql += "  and f.typeOfAssistance=:assistanceTypeId ";
        }

        //Filter for the Aid Modality (Financing Instrument) usually Project support/Pool fund/etc.
        if (financingInstrumentId != null) {
            oql += "   and f.financingInstrument=:financingInstrumentId  ";
        }
        
        //If this comes from the public view, it gets the query for Management workspaces (since that's the information shown in Public View)
        //and links it to the cached version of amp_activity_group (since public view information should also come from cached views)
        //If it's not public view (the else) and links it to the non cached version of the amp_activity_group
        if(filter.getFromPublicView() !=null&& filter.getFromPublicView()){
            oql += DashboardUtil.getTeamQueryManagement();
            oql += " and grpLink.ampActivityLastVersion=act.ampActivityId "; 
        }
        else
        {
        	//Checks if the list of activities already filtered is available.
        	if (filter.getActivityComputedList() == null || filter.getActivityComputedList().size() == 0)
        		oql += DashboardUtil.getTeamQuery(tm);
        	oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";
        }

        //The getActivityComputedList holds the list of activities that the workspace has, it could come from a computed workspace that has a filter instead of children organizations.
        if (filter.getActivityComputedList() != null && filter.getActivityComputedList().size() > 0)
        	oql += " and act.ampActivityId IN (" + DashboardUtil.getInStatement(filter.getActivityComputedList()) + ")";
        else
        	oql += "  and act.team is not null ";
        	
        // This restricts to only show activities that are non draft, non deleted and validated
        oql += " and act.draft=false and act.approvalStatus IN (" + Util.toCSString(AmpARFilter.validatedActivityStatus) + ") ";
        oql += " and (act.deleted = false or act.deleted is null)";
        
		return oql;
	}
	
private static String getHQLQueryForDD(DashboardFilter filter) {
		
		String oql = "";
		
		oql += " from ";
		oql += AmpFundingMTEFProjection.class.getName() + " as fp inner join fp.ampFunding f ";
        oql += "   inner join f.ampActivityId act ";
        boolean donorCondition = (filter.getDonorAgencyId()!=null && filter.getDonorAgencyId()!=-1)? true : false;
        boolean implementingCondition = (filter.getImplementingAgencyId()!=null && filter.getImplementingAgencyId()!=-1)? true : false;
        boolean beneficiaryCondition = (filter.getBeneficiaryAgencyId()!=null && filter.getBeneficiaryAgencyId()!=-1)? true : false;
        boolean regionCondition = (filter.getRegionId()!=null && filter.getRegionId()!=-1)? true : false;
        boolean sectorCondition = (filter.getSectorId()!=null && filter.getSectorId()!=-1)? true : false;
        boolean secSectorCondition = (filter.getSecondarySectorsId()!=null && filter.getSecondarySectorsId()!=-1)? true : false;
        boolean peaceMarkerCondition = (filter.getPeacebuilderMarkerId()!=null && filter.getPeacebuilderMarkerId()!=-1)? true : false;
        boolean peacebuildingCondition = (filter.getPeacebuildingId()!=null && filter.getPeacebuildingId()!=-1)? true : false;
        
        //Join  for Organization/Organization Groups and their role
        if (donorCondition || implementingCondition || beneficiaryCondition)
    		oql += " inner join act.orgrole orole inner join orole.role role ";
 
        //Join for locations filter
        if (regionCondition) {
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        }

        //Join for Category Values
        if (peaceMarkerCondition || peacebuildingCondition) {
        	oql += " inner join act.categories categ ";
		}

        //Join for the Sectors (actsec) and the sector scheme (config)
        if (sectorCondition || secSectorCondition) {
            oql += "  inner join act.sectors actsec ";
            oql += "  inner join actsec.classificationConfig config  ";
            oql += " inner join actsec.sectorId sec ";
        }
        
        oql += " where 1 = 1 ";

        // Filter for the Organizations and their roles (Donor, Implementing or Beneficiary)
        if (donorCondition) {
        	oql = " and role.roleCode='DN' and orole.organisation in (" + filter.getDonorAgencyId() + ") ";
        } 
        if (implementingCondition) {
        	oql = " and role.roleCode='IA' and orole.organisation in (" + filter.getImplementingAgencyId() + ") ";
        } 
        if (beneficiaryCondition) {
        	oql = " and role.roleCode='BA' and orole.organisation in (" + filter.getBeneficiaryAgencyId() + ") ";
        } 
        

        //Filter for regions. If there's a location selected, it adds that location as well as all child locations. Null is used for unallocated locations on the Activities.
        if (regionCondition) {
            Long[] locationIds = {filter.getRegionId()};
            locationIds = getAllDescendantsLocation(locationIds, DbUtil.getAmpLocations());
            oql += " and loc.id in ("+DashboardUtil.getInStatement(locationIds)+") ";
        }

        //Filter for sectors. If there's a sector selected, it adds that sector as well as all children.
        if (sectorCondition) {
        	Long[] sectorIds = {filter.getSectorId()};
            sectorIds = getAllDescendants(sectorIds, DbUtil.getAmpSectors());
            oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
        }

        if (secSectorCondition) {
        	Long[] secSectorIds = {filter.getSecondarySectorsId()};
            secSectorIds = getAllDescendants(secSectorIds, DbUtil.getAmpSectors());
            oql += " and sec.id in ("+DashboardUtil.getInStatement(secSectorIds)+") ";
        }

        //Filter for Category Values
        if (peaceMarkerCondition) {
        	oql += " and categ.id in ("+filter.getPeacebuilderMarkerId()+") ";
		}
        if (peacebuildingCondition) {
        	oql += " and categ.id in ("+filter.getPeacebuildingId()+") ";
		}

        //Filter for individual activity information (used for the list of projects that appear when you click on a line/bar/pie slice.
        if (filter.getActivityId()!=null) {
            oql += " and act.ampActivityId =:activityId ";
        }

        //Filter for the Fiscal Year Start and End
        oql += " and  (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";

        oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";

        //The getActivityComputedList holds the list of activities that the workspace has, it could come from a computed workspace that has a filter instead of children organizations.
        if (filter.getActivityComputedList() != null && filter.getActivityComputedList().size() > 0)
        	oql += " and act.ampActivityId IN (" + DashboardUtil.getInStatement(filter.getActivityComputedList()) + ")";
        else
        	oql += "  and act.team is not null ";
        	
        // This restricts to only show activities that are non draft, non deleted and validated
        oql += " and act.draft=false and act.approvalStatus IN (" + Util.toCSString(AmpARFilter.validatedActivityStatus) + ") ";
        oql += " and (act.deleted = false or act.deleted is null)";
        
		return oql;
	}
	
	private static String getActivitiesIdByCategoryRelated (String catList){
		String ret = "";
		Session session = null;
		Query q = null;
		String queryString = null;
		Iterator<AmpActivityVersion> iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select act from "
					+ AmpActivityVersion.class.getName()
					+ " act inner join act.categories categ where categ.id in ("+catList+")";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				AmpActivityVersion act = (AmpActivityVersion) iter.next();
				ret += act.getAmpActivityId().toString();
				if (iter.hasNext())
					ret += ",";
			}

		} catch (Exception ex) {
			logger.error("Unable to get activities from database "
					+ ex.getMessage());
		}
		return ret;
	}
	/**
     * Returns funding amount
     * @param orgID
     * @param year
     * @param assistanceTypeId
     * @param currCode
     * @param transactionType
     * @return
     * @throws org.digijava.kernel.exception.DgException
     */
    @SuppressWarnings("unchecked")
    public static List<AmpFundingDetail> getFundingDetails(DashboardFilter filter, Date startDate,
            Date endDate, Long assistanceTypeId,
            Long financingInstrumentId) throws DgException {
        
        String oql = "";
        Long[] orgIds = filter.getSelOrgIds();
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        
        TeamMember tm = filter.getTeamMember();
        Long[] locationIds = filter.getSelLocationIds();
        Long[] sectorIds = filter.getSelSectorIds();
        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);

        Long[] programIds = filter.getSelProgramIds();
        boolean programCondition = programIds != null && programIds.length > 0 && !programIds[0].equals(-1l);
        
        if (filter.getTransactionType()==Constants.MTEFPROJECTION){
        	oql = "select fp, act.ampId, act.name ";
        } else {
        	oql = "select fd, act.ampId, act.name ";
        }
        
        if (locationCondition)
        	oql += ", actloc.locationPercentage ";
        	
        if (sectorCondition)
        	oql += ", actsec.sectorPercentage ";
        
        if (programCondition)
        	oql += ", actProg.programPercentage ";

        oql += getHQLQuery(filter, orgIds, orgGroupIds, locationCondition, sectorCondition, programCondition, locationIds, sectorIds, programIds, assistanceTypeId, financingInstrumentId, tm, false);

        Session session = PersistenceManager.getRequestDBSession();
        List fundings = null;
        List<AmpFundingDetail> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if (sectorCondition) {
            	query.setLong("config", filter.getSelSectorConfigId());
            }
            //if ((orgIds == null || orgIds.length == 0 || orgIds[0] == -1) && orgGroupId != -1) {
            //    query.setLong("orgGroupId", orgGroupId);
            //}
            if (assistanceTypeId != null) {
                query.setLong("assistanceTypeId", assistanceTypeId);
            }
            if (financingInstrumentId != null) {
                query.setLong("financingInstrumentId", financingInstrumentId);
            }
            if (filter.getActivityId()!=null) {
                query.setLong("activityId", filter.getActivityId());
            }
            fundings = query.list();
            
            Iterator it = fundings.iterator();
            fundingDets = new ArrayList<AmpFundingDetail>();
            while(it.hasNext()){
            	Object[] item = (Object[])it.next();
            	AmpFundingDetail currentFd = null;
            	if(filter.getTransactionType()==Constants.MTEFPROJECTION){
            		AmpFundingMTEFProjection fp = (AmpFundingMTEFProjection) item[0];
            		//Here use a tricky harcode to of transaction/adjustment type and it is set to "actual commitments", this is done to use FundingCalculationsHelper to do calculations for MTEF projections
            		currentFd = new AmpFundingDetail(Constants.COMMITMENT,null,fp.getAmount(),fp.getProjectionDate(),fp.getAmpCurrency(),0d);
            	} else {
            		AmpFundingDetail fd = (AmpFundingDetail) item[0];
            		currentFd = new AmpFundingDetail(fd.getTransactionType(),fd.getAdjustmentType(),fd.getAbsoluteTransactionAmount(),fd.getTransactionDate(),fd.getAmpCurrencyId(),fd.getFixedExchangeRate());
                }
            	if (item.length==4) 
            		currentFd.setTransactionAmount(currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100);
            	if (item.length==5) 
            		currentFd.setTransactionAmount((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100);
            	if (item.length==6) 
            		currentFd.setTransactionAmount(((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100)*(Float)item[5]/100);
            		
            	fundingDets.add(currentFd);
            }
            
        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load fundings from db", e);
        }


        return fundingDets;
    }

    public static DecimalWraper calculateDetails(DashboardFilter filter, List<AmpFundingDetail> fundingDets,
            int transactionType,String adjustmentType){
        DecimalWraper total = null;
        String currCode = "USD";
        if (filter.getCurrencyId()!=null) {
        	currCode = CurrencyUtil.getCurrency(filter.getCurrencyId()).getCurrencyCode();
		} 
        FundingCalculationsHelper cal = new FundingCalculationsHelper();
        cal.doCalculations(fundingDets, currCode);
        switch (transactionType) {
	        case Constants.EXPENDITURE:
	            if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
	                total = cal.getTotActualExp();
	            } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
	                total = cal.getTotPlannedExp();
	            } else {
	                total = cal.getTotPipelineExp();
	            }
	            break;
	        case Constants.DISBURSEMENT:
	            if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
	                total = cal.getTotActualDisb();
	            } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
	                total = cal.getTotPlanDisb();
	            } else {
	                total = cal.getTotPipelineDisb();
	            }
	            break;
	        default:
	            if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
	                total = cal.getTotActualComm();
	            } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
	                total = cal.getTotPlannedComm();
	            } else {
	                total = cal.getTotPipelineComm();
	            }
        }
        return total;
    }
	@SuppressWarnings("unchecked")
    public static Map<AmpActivityVersion, BigDecimal> getFundingByActivityList(Collection<Long> actList, DashboardFilter filter, Date startDate,
            Date endDate, Long assistanceTypeId,
            Long financingInstrumentId,
            int transactionType,String adjustmentType,
            int decimalsToShow, BigDecimal divideByDenominator) throws DgException {
        
		Map<AmpActivityVersion, BigDecimal> map = new HashMap<AmpActivityVersion, BigDecimal>();
		Long[] orgIds = filter.getSelOrgIds();
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        
        TeamMember tm = filter.getTeamMember();
        Long[] locationIds = filter.getSelLocationIds();
        Long[] sectorIds = filter.getSelSectorIds();
        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
        Long[] programIds = filter.getSelProgramIds();
        boolean programCondition = programIds != null && programIds.length > 0 && !programIds[0].equals(-1l);

    	DecimalWraper total = null;
        String oql = "";
        //This determines if this query needs additional joins for role and role percentage
        Boolean organizationRoleQuery = false;
    	if ((orgIds != null && orgIds.length != 0 && orgIds[0] != -1) || (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1))
    		if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY || filter.getAgencyType() == org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY)
    			organizationRoleQuery = true;
    	
		 if (filter.getTransactionType()==Constants.MTEFPROJECTION){
		 	oql = "select fp, f.ampActivityId.ampActivityId, f.ampActivityId.name";
		 } else {
		 	oql = "select fd, f.ampActivityId.ampActivityId, f.ampActivityId.name";
		 }
        if (filter.getSelProgramIds()!=null && filter.getSelProgramIds().length>0) 
        	oql += ", actProg.programPercentage ";
        if (locationCondition)
        	oql += ", actloc.locationPercentage ";
        if (sectorCondition)
        	oql += ", actsec.sectorPercentage ";
        if (organizationRoleQuery)
        	oql += ", orole.percentage ";
        
        oql += getHQLQuery(filter, orgIds, orgGroupIds, locationCondition, sectorCondition, programCondition, locationIds, sectorIds, programIds, assistanceTypeId, financingInstrumentId, tm, true);
    	
        oql += " and f.ampActivityId in (" + DashboardUtil.getInStatement(actList.toArray()) + ")";

        Session session = PersistenceManager.getRequestDBSession();
        List fundings = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
	            query.setLong("transactionType", transactionType);
	            query.setString("adjustmentType",adjustmentType);
            }
            if (assistanceTypeId != null) {
                query.setLong("assistanceTypeId", assistanceTypeId);
            }
            if (financingInstrumentId != null) {
                query.setLong("financingInstrumentId", financingInstrumentId);
            }
            if (sectorCondition) {
            	query.setLong("config", filter.getSelSectorConfigId());
            }
            fundings = query.list();
            /*the objects returned by query  and   selected currency
            are passed doCalculations  method*/
            HashMap<Long, ArrayList<AmpFundingDetail>> hm = new HashMap<Long, ArrayList<AmpFundingDetail>>();
            HashMap<Long, String> hmName = new HashMap<Long, String>();
            Iterator it = fundings.iterator();
            while(it.hasNext()){
            	Object[] item = (Object[])it.next();
            	AmpFundingDetail currentFd = null;
            	if(filter.getTransactionType()==Constants.MTEFPROJECTION){
            		AmpFundingMTEFProjection fp = (AmpFundingMTEFProjection) item[0];
            		//Here use a tricky harcode to of transaction/adjustment type and it is set to "actual commitments", this is done to use FundingCalculationsHelper to do calculations for MTEF projections
            		currentFd = new AmpFundingDetail(Constants.COMMITMENT,null,fp.getAmount(),fp.getProjectionDate(),fp.getAmpCurrency(),0d);
            	} else {
            		AmpFundingDetail fd = (AmpFundingDetail) item[0];
            		currentFd = new AmpFundingDetail(fd.getTransactionType(),fd.getAdjustmentType(),fd.getAbsoluteTransactionAmount(),fd.getTransactionDate(),fd.getAmpCurrencyId(),fd.getFixedExchangeRate());
                }
            	if (item.length==4) 
            		currentFd.setTransactionAmount(currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100);
            	if (item.length==5) 
            		currentFd.setTransactionAmount((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100);
            	if (item.length==6) 
            		currentFd.setTransactionAmount(((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100)*(Float)item[5]/100);
            	Long id = (Long) item[1];
            	String name = (String) item[2];
            	if(hm.containsKey(id)){
            		ArrayList<AmpFundingDetail> afda = hm.get(id);
            		afda.add(currentFd);
            	}
            	else
            	{
            		ArrayList<AmpFundingDetail> afda = new ArrayList<AmpFundingDetail>();
            		afda.add(currentFd);
            		hmName.put(id, name);
            		hm.put(id, afda);
            	}
            }

            Iterator<Long> it2 = hm.keySet().iterator();
            String currCode = filter.getCurrencyCode();
            while(it2.hasNext()){
            	Long activityId = it2.next();
            	ArrayList<AmpFundingDetail> afda = hm.get(activityId);
                FundingCalculationsHelper cal = new FundingCalculationsHelper();
                cal.doCalculations(afda, currCode);
                /*Depending on what is selected in the filter
                we should return either actual commitments
                or actual Disbursement or  */
                if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
                	total = cal.getTotActualComm(); //takes the actual commitments 
            	} else {
	                switch (transactionType) {
	                case Constants.EXPENDITURE:
	                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
	                        total = cal.getTotActualExp();
	                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
	                        total = cal.getTotPlannedExp();
	                    } else {
	                        total = cal.getTotPipelineExp();
	                    }
	                    break;
	                case Constants.DISBURSEMENT:
	                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
	                        total = cal.getTotActualDisb();
	                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
	                        total = cal.getTotPlanDisb();
	                    } else {
	                        total = cal.getTotPipelineDisb();
	                    }
	                    break;
	                default:
	                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
	                        total = cal.getTotActualComm();
	                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
	                        total = cal.getTotPlannedComm();
	                    } else {
	                        total = cal.getTotPipelineComm();
	                    }
	                }
                }
                AmpActivityVersion aav = new AmpActivityVersion(activityId, hmName.get(activityId), "");
                map.put(aav, total.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(decimalsToShow, RoundingMode.HALF_UP));
            }

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load fundings from db", e);
        }


        return map;
    }
	public static Map<AmpOrganisation, BigDecimal> getFundingByAgencyList(Collection<Long> orgList, String currCode,  Date startDate,
            Date endDate, int transactionType,String adjustmentType, int decimalsToShow, BigDecimal divideByDenominator, DashboardFilter filter) throws DgException {
        
		Map<AmpOrganisation, BigDecimal> map = new HashMap<AmpOrganisation, BigDecimal>();
		Long[] orgIds = new Long[orgList.size()];
		int i = 0;
		for (Iterator iterator = orgList.iterator(); iterator.hasNext();) {
			Long long1 = (Long) iterator.next();
			orgIds[i++] = long1;
		}
		//Long[] orgIds = filter.getSelOrgIds();
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        
        TeamMember tm = filter.getTeamMember();
        Long[] locationIds = filter.getSelLocationIds();
        Long[] sectorIds = filter.getSelSectorIds();
        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);
        Long[] programIds = filter.getSelProgramIds();
        boolean programCondition = programIds != null && programIds.length > 0 && !programIds[0].equals(-1l);

    	DecimalWraper total = null;
        String oql = "";
        if (filter.getTransactionType()==Constants.MTEFPROJECTION){
        	if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY || filter.getAgencyType() == org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY)
            	oql = "select fp, roleOrg.ampOrgId, roleOrg.name";
            else 
            	oql = "select fp, f.ampDonorOrgId.ampOrgId, f.ampDonorOrgId.name";
		} else {
			if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY || filter.getAgencyType() == org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY)
	        	oql = "select fd, roleOrg.ampOrgId, roleOrg.name";
	        else 
	        	oql = "select fd, f.ampDonorOrgId.ampOrgId, f.ampDonorOrgId.name";
		}
		if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY || filter.getAgencyType() == org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY)
			oql += ", orole.percentage ";
		if (locationCondition)
        	oql += ", actloc.locationPercentage ";
        if (sectorCondition)
        	oql += ", actsec.sectorPercentage ";
        
        String specialInner = null;
    	if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY || filter.getAgencyType() == org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY)
    		specialInner = " inner join act.orgrole orole inner join orole.role role inner join orole.organisation roleOrg ";
    
    	oql += getHQLQuery(filter, orgIds, orgGroupIds, locationCondition, sectorCondition, programCondition, locationIds, sectorIds, programIds, null, null, tm, true, specialInner, null);
    	

        Session session = PersistenceManager.getRequestDBSession();
        List<AmpFundingDetail> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
	            query.setLong("transactionType", transactionType);
	            query.setString("adjustmentType",adjustmentType);
            }
            if (sectorCondition) {
            	query.setLong("config", filter.getSelSectorConfigId());
            }
            fundingDets = query.list();
            /*the objects returned by query  and   selected currency
            are passed doCalculations  method*/
            HashMap<Long, ArrayList<AmpFundingDetail>> hm = new HashMap<Long, ArrayList<AmpFundingDetail>>();
            HashMap<Long, String> hmName = new HashMap<Long, String>();
            Iterator it = fundingDets.iterator();
            while(it.hasNext()){
            	Object[] item = (Object[])it.next();
            	
            	AmpFundingDetail currentFd = null;
            	if(filter.getTransactionType()==Constants.MTEFPROJECTION){
            		AmpFundingMTEFProjection fp = (AmpFundingMTEFProjection) item[0];
            		//Here use a tricky harcode to of transaction/adjustment type and it is set to "actual commitments", this is done to use FundingCalculationsHelper to do calculations for MTEF projections
            		currentFd = new AmpFundingDetail(Constants.COMMITMENT,null,fp.getAmount(),fp.getProjectionDate(),fp.getAmpCurrency(),0d);
            	} else {
            		AmpFundingDetail fd = (AmpFundingDetail) item[0];
            		currentFd = new AmpFundingDetail(fd.getTransactionType(),fd.getAdjustmentType(),fd.getAbsoluteTransactionAmount(),fd.getTransactionDate(),fd.getAmpCurrencyId(),fd.getFixedExchangeRate());
                }
            	if (item.length==4 && item[3] != null) 
            		currentFd.setTransactionAmount(currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100);
            	if (item.length==5) 
            		currentFd.setTransactionAmount((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100);
            	if (item.length==6) 
            		currentFd.setTransactionAmount(((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100)*(Float)item[5]/100);
            	Long id = (Long) item[1];
            	String name = (String) item[2];
            	if(hm.containsKey(id)){
            		ArrayList<AmpFundingDetail> afda = hm.get(id);
            		afda.add(currentFd);
            	}
            	else
            	{
            		ArrayList<AmpFundingDetail> afda = new ArrayList<AmpFundingDetail>();
            		afda.add(currentFd);
            		hmName.put(id, name);
            		hm.put(id, afda);
            	}
            }

            Iterator<Long> it2 = hm.keySet().iterator();
            while(it2.hasNext()){
            	Long orgId = it2.next();
            	ArrayList<AmpFundingDetail> afda = hm.get(orgId);
                FundingCalculationsHelper cal = new FundingCalculationsHelper();
                cal.doCalculations(afda, currCode);
                /*Depending on what is selected in the filter
                we should return either actual commitments
                or actual Disbursement or  */
                switch (transactionType) {
	                case Constants.EXPENDITURE:
	                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
	                        total = cal.getTotActualExp();
	                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
	                        total = cal.getTotPlannedExp();
	                    } else {
	                        total = cal.getTotPipelineExp();
	                    }
	                    break;
	                case Constants.DISBURSEMENT:
	                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
	                        total = cal.getTotActualDisb();
	                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
	                        total = cal.getTotPlanDisb();
	                    } else {
	                        total = cal.getTotPipelineDisb();
	                    }
	                    break;
	                default:
	                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
	                        total = cal.getTotActualComm();
	                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
	                        total = cal.getTotPlannedComm();
	                    } else {
	                        total = cal.getTotPipelineComm();
	                    }
                }
                AmpOrganisation aorg = new AmpOrganisation();
                aorg.setAmpOrgId(orgId);
                aorg.setName(hmName.get(orgId));
                map.put(aorg, total.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(decimalsToShow, RoundingMode.HALF_UP));
            }

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load fundings from db", e);
        }
        return map;
    }
	
	public static Map<AmpSector, BigDecimal> getFundingBySectorList(Collection<AmpSector> secListChildren, Collection<AmpSector> secListParent, String currCode,  Date startDate,
            Date endDate, int transactionType,String adjustmentType, int decimalsToShow, BigDecimal divideByDenominator, DashboardFilter filter) throws DgException {
        
		Map<AmpSector, BigDecimal> map = new HashMap<AmpSector, BigDecimal>();
		Long[] orgIds = filter.getSelOrgIds();
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        
        TeamMember tm = filter.getTeamMember();
        Long[] locationIds = filter.getSelLocationIds();
        Long[] sectorIds = filter.getSelSectorIds();
        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);

    	DecimalWraper total = null;
        String oql = "";

        if (filter.getTransactionType()==Constants.MTEFPROJECTION){
        	oql = "select fp, sec.ampSectorId, sec.name ";
        } else {
        	oql = "select fd, sec.ampSectorId, sec.name ";
        }
        
        if (locationCondition)
        	oql += ", actloc.locationPercentage ";
        	
        oql += ", actsec.sectorPercentage ";

    	boolean donorCondition = (filter.getDonorAgencyId()!=null && filter.getDonorAgencyId()!=-1)? true : false;
        boolean implementingCondition = (filter.getImplementingAgencyId()!=null && filter.getImplementingAgencyId()!=-1)? true : false;
        boolean beneficiaryCondition = (filter.getBeneficiaryAgencyId()!=null && filter.getBeneficiaryAgencyId()!=-1)? true : false;
        boolean peaceMarkerCondition = (filter.getPeacebuilderMarkerId()!=null && filter.getPeacebuilderMarkerId()!=-1)? true : false;
        boolean peacebuildingCondition = (filter.getPeacebuildingId()!=null && filter.getPeacebuildingId()!=-1)? true : false;
        
        if (filter.getTransactionType()==Constants.MTEFPROJECTION){
	        oql += " from ";
			oql += AmpFundingMTEFProjection.class.getName() + " as fp inner join fp.ampFunding f ";
	        oql += "   inner join f.ampActivityId act ";
        } else {
        	if(filter.getFromPublicView() !=null&& filter.getFromPublicView()){
        		oql += " from "+AmpActivityGroupCached.class.getName()+" grpLink inner join grpLink.ampActivityGroup as actGroup, ";
        	} else { 
        		oql += " from ";
        	}
			oql += AmpFundingDetail.class.getName() + " as fd inner join fd.ampFundingId f ";
	        oql += "   inner join f.ampActivityId act ";
        }
    	
    	if(!(filter.getFromPublicView() !=null&& filter.getFromPublicView()))
        	oql += " inner join act.ampActivityGroup actGroup ";
    	
    	if (donorCondition || implementingCondition || beneficiaryCondition)
    		oql += " inner join act.orgrole orole inner join orole.role role ";
        
        //Join for Category Values
        if ((filter.getSelCVIds()!=null && filter.getSelCVIds().length>0) || (filter.getSelStatusIds()!=null && filter.getSelStatusIds().length>0) || peaceMarkerCondition || peacebuildingCondition ) {
        	oql += " inner join act.categories categ ";
		}
    	if ((orgIds != null && orgIds.length != 0 && orgIds[0] != -1) || (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1))
    		if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY || filter.getAgencyType() == org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY)
    			oql += " inner join act.orgrole orole inner join orole.role role ";
        if (locationCondition) 
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        
        oql += "  inner join act.sectors actsec ";
        oql += "  inner join actsec.classificationConfig config  ";
        oql += " inner join actsec.sectorId sec ";
        
    	oql += " where config.id=:config  ";
    	
    	//Filter for the Fiscal Year Start and End
        if(filter.getTransactionType()==Constants.MTEFPROJECTION){
        	oql += " and  (fp.projectionDate>=:startDate and fp.projectionDate<=:endDate)  ";
        } else {
        	oql += " and fd.transactionType =:transactionType  and  fd.adjustmentType.value =:adjustmentType ";
            oql += " and (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
        }
        oql += " and sec.ampSectorId in (" + DashboardUtil.getInStatement(secListChildren) + ")";
        
        //Mapping the subsectors with their parents
        HashMap<Long, Long> sectorMap = new HashMap<Long, Long>();
        for(AmpSector currentSector : secListChildren ){
        	sectorMap.put(currentSector.getAmpSectorId(), DashboardUtil.getTopLevelParent(currentSector).getAmpSectorId());
        }
        
        // Filter for the Organizations and their roles (Donor, Implementing or Beneficiary)
        if (donorCondition) {
        	oql = " and role.roleCode='DN' and orole.organisation in (" + filter.getDonorAgencyId() + ") ";
        } 
        if (implementingCondition) {
        	oql = " and role.roleCode='IA' and orole.organisation in (" + filter.getImplementingAgencyId() + ") ";
        } 
        if (beneficiaryCondition) {
        	oql = " and role.roleCode='BA' and orole.organisation in (" + filter.getBeneficiaryAgencyId() + ") ";
        } 
       
        //Filter for Category Values
        if (peaceMarkerCondition) {
        	oql += " and categ.id in ("+filter.getPeacebuilderMarkerId()+") ";
		}
        if (peacebuildingCondition) {
        	oql += " and categ.id in ("+filter.getPeacebuildingId()+") ";
		}
        
        if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
            if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) 
                oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds, filter.getAgencyType());
        } else 
            oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds, filter.getAgencyType());
        if (locationCondition) {
        	if (locationIds[0].equals(0l)) {
        		oql += " and actloc is NULL "; //Unallocated condition
			} else {
				locationIds = getAllDescendantsLocation(locationIds, DbUtil.getAmpLocations());
	            oql += " and loc.id in ("+DashboardUtil.getInStatement(locationIds)+") ";
			}
        }
        if (sectorCondition) {
        	sectorIds = getAllDescendants(sectorIds, filter.getAllSectorList());
            oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
        }
        //Filter for Activity Status
        if (filter.getSelStatusIds()!=null && filter.getSelStatusIds().length>0) {
			oql += " and categ.id in ("+DashboardUtil.getInStatement(filter.getSelStatusIds())+") ";
		}
        if(filter.getFromPublicView() !=null&& filter.getFromPublicView()){
            oql += DashboardUtil.getTeamQueryManagement();
            oql += " and grpLink.ampActivityLastVersion=act.ampActivityId "; 
        }
        else
        {
        	if (filter.getActivityComputedList() == null || filter.getActivityComputedList().size() == 0)
        		oql += DashboardUtil.getTeamQuery(tm);
        	oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";
        }

        if (filter.getActivityComputedList() != null && filter.getActivityComputedList().size() > 0)
        	oql += " and act.ampActivityId IN (" + DashboardUtil.getInStatement(filter.getActivityComputedList()) + ")";
        else
        	oql += "  and act.team is not null ";
        	
        oql += " and act.draft=false and act.approvalStatus IN (" + Util.toCSString(AmpARFilter.validatedActivityStatus) + ") ";
        oql += " and (act.deleted = false or act.deleted is null)";

        Session session = PersistenceManager.getRequestDBSession();
        List fundings = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
	            query.setLong("transactionType", transactionType);
	            query.setString("adjustmentType",adjustmentType);
            }
            query.setLong("config", filter.getSelSectorConfigId());
            fundings = query.list();

            HashMap<Long, AmpSector> sectorParentList = new HashMap<Long, AmpSector>();
            Iterator iter = secListParent.iterator();
            while (iter.hasNext()) {
            	AmpSector sec = (AmpSector)iter.next();
            	AmpSector sec2 = DashboardUtil.getTopLevelParent(sec);
            	sectorParentList.put(sec.getAmpSectorId(), sec2);
            }
            
            HashMap<Long, ArrayList<AmpFundingDetail>> hm = new HashMap<Long, ArrayList<AmpFundingDetail>>();
            HashMap<Long, String> hmName = new HashMap<Long, String>();
            Iterator it = fundings.iterator();
            while(it.hasNext()){
            	Object[] item = (Object[])it.next();
            	AmpFundingDetail currentFd = null;
            	if(filter.getTransactionType()==Constants.MTEFPROJECTION){
            		AmpFundingMTEFProjection fp = (AmpFundingMTEFProjection) item[0];
            		//Here use a tricky harcode to of transaction/adjustment type and it is set to "actual commitments", this is done to use FundingCalculationsHelper to do calculations for MTEF projections
            		currentFd = new AmpFundingDetail(Constants.COMMITMENT,null,fp.getAmount(),fp.getProjectionDate(),fp.getAmpCurrency(),0d);
            	} else {
            		AmpFundingDetail fd = (AmpFundingDetail) item[0];
            		currentFd = new AmpFundingDetail(fd.getTransactionType(),fd.getAdjustmentType(),fd.getAbsoluteTransactionAmount(),fd.getTransactionDate(),fd.getAmpCurrencyId(),fd.getFixedExchangeRate());
                }
            	if (item.length==4) 
            		currentFd.setTransactionAmount(currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100);
            	if (item.length==5) 
            		currentFd.setTransactionAmount((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100);
            	if (item.length==6) 
            		currentFd.setTransactionAmount(((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100)*(Float)item[5]/100);
            	Long id = (Long) item[1];
            	String name = (String) item[2];
            	if (!sectorCondition) {
            		if(sectorParentList.get(id) == null){
            			id = sectorMap.get(id);
            		}
            		name = sectorParentList.get(id).getName();
                	id = sectorParentList.get(id).getAmpSectorId();
            	}
            	if(hm.containsKey(id)){
            		ArrayList<AmpFundingDetail> afda = hm.get(id);
            		afda.add(currentFd);
            	}
            	else
            	{
            		ArrayList<AmpFundingDetail> afda = new ArrayList<AmpFundingDetail>();
            		afda.add(currentFd);
            		hmName.put(id, name);
            		hm.put(id, afda);
            	}
            }

            Iterator<Long> it2 = hm.keySet().iterator();
            while(it2.hasNext()){
            	Long secId = it2.next();
            	ArrayList<AmpFundingDetail> afda = hm.get(secId);
                FundingCalculationsHelper cal = new FundingCalculationsHelper();
                cal.doCalculations(afda, currCode);
                /*Depending on what is selected in the filter
                we should return either actual commitments
                or actual Disbursement or  */
                if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
                	total = cal.getTotActualComm(); //takes the actual commitments 
            	} else {
	                switch (transactionType) {
		                case Constants.EXPENDITURE:
		                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
		                        total = cal.getTotActualExp();
		                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
		                        total = cal.getTotPlannedExp();
		                    } else {
		                        total = cal.getTotPipelineExp();
		                    }
		                    break;
		                case Constants.DISBURSEMENT:
		                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
		                        total = cal.getTotActualDisb();
		                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
		                        total = cal.getTotPlanDisb();
		                    } else {
		                        total = cal.getTotPipelineDisb();
		                    }
		                    break;
		                default:
		                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
		                        total = cal.getTotActualComm();
		                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
		                        total = cal.getTotPlannedComm();
		                    } else {
		                        total = cal.getTotPipelineComm();
		                    }
	                }
            	}
                AmpSector asec = new AmpSector();
                asec.setAmpSectorId(secId);
                asec.setName(hmName.get(secId));
                map.put(asec, total.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(decimalsToShow, RoundingMode.HALF_UP));
            }

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load fundings from db", e);
        }
        return map;
    }
	
	public static Map<AmpCategoryValueLocations, BigDecimal> getFundingByRegionList(Collection<AmpCategoryValueLocations> regListChildren, Collection<AmpCategoryValueLocations> regListParent, AmpCategoryValueLocations natLoc, String currCode,  Date startDate,
            Date endDate, int transactionType,String adjustmentType, int decimalsToShow, BigDecimal divideByDenominator, DashboardFilter filter, HttpServletRequest request) throws DgException {
        
		Map<AmpCategoryValueLocations, BigDecimal> map = new HashMap<AmpCategoryValueLocations, BigDecimal>();
		Long[] orgIds = filter.getSelOrgIds();
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        
        TeamMember tm = filter.getTeamMember();
        Long[] locationIds = filter.getSelLocationIds();
        Long[] sectorIds = filter.getSelSectorIds();
        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);

    	DecimalWraper total = null;
        String oql = "";

        if (filter.getTransactionType()==Constants.MTEFPROJECTION){
        	oql = "select fp, loc.id, loc.name ";
        } else {
        	oql = "select fd, loc.id, loc.name ";
        }
        oql += ", actloc.locationPercentage ";
        if (sectorCondition)        	
        	oql += ", actsec.sectorPercentage ";
        
        boolean donorCondition = (filter.getDonorAgencyId()!=null && filter.getDonorAgencyId()!=-1)? true : false;
        boolean implementingCondition = (filter.getImplementingAgencyId()!=null && filter.getImplementingAgencyId()!=-1)? true : false;
        boolean beneficiaryCondition = (filter.getBeneficiaryAgencyId()!=null && filter.getBeneficiaryAgencyId()!=-1)? true : false;
        boolean peaceMarkerCondition = (filter.getPeacebuilderMarkerId()!=null && filter.getPeacebuilderMarkerId()!=-1)? true : false;
        boolean peacebuildingCondition = (filter.getPeacebuildingId()!=null && filter.getPeacebuildingId()!=-1)? true : false;
        
        if (filter.getTransactionType()==Constants.MTEFPROJECTION){
	        oql += " from ";
			oql += AmpFundingMTEFProjection.class.getName() + " as fp inner join fp.ampFunding f ";
	        oql += "   inner join f.ampActivityId act ";
        } else {
        	if(filter.getFromPublicView() !=null&& filter.getFromPublicView()){
        		oql += " from "+AmpActivityGroupCached.class.getName()+" grpLink inner join grpLink.ampActivityGroup as actGroup, ";
        	} else { 
        		oql += " from ";
        	}
			oql += AmpFundingDetail.class.getName() + " as fd inner join fd.ampFundingId f ";
	        oql += "   inner join f.ampActivityId act ";
        }
        
    	if(!(filter.getFromPublicView() !=null&& filter.getFromPublicView()))
        	oql += " inner join act.ampActivityGroup actGroup ";
    	if ((orgIds != null && orgIds.length != 0 && orgIds[0] != -1) || (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1))
    		if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY || filter.getAgencyType() == org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY)
    			oql += " inner join act.orgrole orole inner join orole.role role ";
    	
    	if (donorCondition || implementingCondition || beneficiaryCondition)
    		oql += " inner join act.orgrole orole inner join orole.role role ";
        
        //Join for Category Values
        if ((filter.getSelCVIds()!=null && filter.getSelCVIds().length>0) || (filter.getSelStatusIds()!=null && filter.getSelStatusIds().length>0) || peaceMarkerCondition || peacebuildingCondition ) {
        	oql += " inner join act.categories categ ";
		}
    	
    	//if (locationCondition) 
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        if (sectorCondition) {
            oql += "  inner join act.sectors actsec ";
            oql += "  inner join actsec.classificationConfig config  ";
            oql += " inner join actsec.sectorId sec ";
        }
        if (sectorCondition) 
        	oql += " where config.id=:config  ";
        else
        	oql += " where 1=1 ";
        
      //Filter for the Fiscal Year Start and End
        if(filter.getTransactionType()==Constants.MTEFPROJECTION){
        	oql += " and  (fp.projectionDate>=:startDate and fp.projectionDate<=:endDate)  ";
        } else {
        	oql += " and fd.transactionType =:transactionType  and  fd.adjustmentType.value =:adjustmentType ";
            oql += " and (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
        }
        oql += " and loc.id in (" + DashboardUtil.getInStatement(regListChildren) + ")";
        
        //Mapping the locations with their parents
        HashMap<Long, Long> locationMap = new HashMap<Long, Long>();
        for(AmpCategoryValueLocations currentLocation : regListChildren ){
        	locationMap.put(currentLocation.getId(), DashboardUtil.getTopLevelLocation(currentLocation).getId());
        }

        // Filter for the Organizations and their roles (Donor, Implementing or Beneficiary)
        if (donorCondition) {
        	oql = " and role.roleCode='DN' and orole.organisation in (" + filter.getDonorAgencyId() + ") ";
        } 
        if (implementingCondition) {
        	oql = " and role.roleCode='IA' and orole.organisation in (" + filter.getImplementingAgencyId() + ") ";
        } 
        if (beneficiaryCondition) {
        	oql = " and role.roleCode='BA' and orole.organisation in (" + filter.getBeneficiaryAgencyId() + ") ";
        } 
       
        //Filter for Category Values
        if (peaceMarkerCondition) {
        	oql += " and categ.id in ("+filter.getPeacebuilderMarkerId()+") ";
		}
        if (peacebuildingCondition) {
        	oql += " and categ.id in ("+filter.getPeacebuildingId()+") ";
		}
        
        if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
            if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) 
                oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds, filter.getAgencyType());
        } else 
            oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds, filter.getAgencyType());
        if (locationCondition) {
        	if (locationIds[0].equals(0l)) {
        		oql += " and actloc is NULL "; //Unallocated condition
			} else {
				locationIds = getAllDescendantsLocation(locationIds, DbUtil.getAmpLocations());
	            oql += " and loc.id in ("+DashboardUtil.getInStatement(locationIds)+") ";
			}
        }
        if (sectorCondition) {
        	sectorIds = getAllDescendants(sectorIds, filter.getAllSectorList());
            oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
        }
        //Filter for Activity Status
        if (filter.getSelStatusIds()!=null && filter.getSelStatusIds().length>0) {
			oql += " and categ.id in ("+DashboardUtil.getInStatement(filter.getSelStatusIds())+") ";
		}
        if(filter.getFromPublicView() !=null&& filter.getFromPublicView()){
            oql += DashboardUtil.getTeamQueryManagement();
            oql += " and grpLink.ampActivityLastVersion=act.ampActivityId "; 
        }
        else
        {
        	if (filter.getActivityComputedList() == null || filter.getActivityComputedList().size() == 0)
        		oql += DashboardUtil.getTeamQuery(tm);
        	oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";
        }

        if (filter.getActivityComputedList() != null && filter.getActivityComputedList().size() > 0)
        	oql += " and act.ampActivityId IN (" + DashboardUtil.getInStatement(filter.getActivityComputedList()) + ")";
        else
        	oql += "  and act.team is not null ";
        	
        oql += " and act.draft=false and act.approvalStatus IN (" + Util.toCSString(AmpARFilter.validatedActivityStatus) + ") ";
        oql += " and (act.deleted = false or act.deleted is null)";

        Session session = PersistenceManager.getRequestDBSession();
        List fundings = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
	            query.setLong("transactionType", transactionType);
	            query.setString("adjustmentType",adjustmentType);
            }
            if (sectorCondition) {
            	query.setLong("config", filter.getSelSectorConfigId());
            }
            fundings = query.list();

            HashMap<Long, AmpCategoryValueLocations> locationParentList = new HashMap<Long, AmpCategoryValueLocations>();
            Iterator iter = regListParent.iterator();
            while (iter.hasNext()) {
            	AmpCategoryValueLocations loc = (AmpCategoryValueLocations)iter.next();
            	if (loc.getId()!=natLoc.getId()){
            		AmpCategoryValueLocations loc2 = DashboardUtil.getTopLevelLocation(loc);
            		locationParentList.put(loc.getId(), loc2);
            	}
            }
            
            HashMap<Long, ArrayList<AmpFundingDetail>> hm = new HashMap<Long, ArrayList<AmpFundingDetail>>();
            HashMap<Long, String> hmName = new HashMap<Long, String>();
            Iterator it = fundings.iterator();
            while(it.hasNext()){
            	Object[] item = (Object[])it.next();
            	AmpFundingDetail currentFd = null;
            	if(filter.getTransactionType()==Constants.MTEFPROJECTION){
            		AmpFundingMTEFProjection fp = (AmpFundingMTEFProjection) item[0];
            		//Here use a tricky harcode to of transaction/adjustment type and it is set to "actual commitments", this is done to use FundingCalculationsHelper to do calculations for MTEF projections
            		currentFd = new AmpFundingDetail(Constants.COMMITMENT,null,fp.getAmount(),fp.getProjectionDate(),fp.getAmpCurrency(),0d);
            	} else {
            		AmpFundingDetail fd = (AmpFundingDetail) item[0];
            		currentFd = new AmpFundingDetail(fd.getTransactionType(),fd.getAdjustmentType(),fd.getAbsoluteTransactionAmount(),fd.getTransactionDate(),fd.getAmpCurrencyId(),fd.getFixedExchangeRate());
                }
            	if (item.length==4 && item[3] != null) 
            		currentFd.setTransactionAmount(currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100);
            	if (item.length==5 && item[3] != null && item[4] != null) 
            		currentFd.setTransactionAmount((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100);
            	if (item.length==6 && item[3] != null && item[4] != null && item[5] != null ) 
            		currentFd.setTransactionAmount(((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100)*(Float)item[5]/100);
            	Long id = (Long) item[1];
            	String name = (String) item[2];

            	if (natLoc != null && id.equals(new Long(natLoc.getId()))){
            		name = natLoc.getName();
            	} else if (!locationCondition) {
            		if(locationParentList.get(id) == null){
            			id = locationMap.get(id);
            		}
            		name = locationParentList.get(id).getName();
            		id = locationParentList.get(id).getId();
            	}
            	if(hm.containsKey(id)){
            		ArrayList<AmpFundingDetail> afda = hm.get(id);
            		afda.add(currentFd);
            	}
            	else
            	{
            		ArrayList<AmpFundingDetail> afda = new ArrayList<AmpFundingDetail>();
            		afda.add(currentFd);
            		hmName.put(id, name);
            		hm.put(id, afda);
            	}
            }

            Iterator<Long> it2 = hm.keySet().iterator();
            while(it2.hasNext()){
            	Long locId = it2.next();
            	ArrayList<AmpFundingDetail> afda = hm.get(locId);
                FundingCalculationsHelper cal = new FundingCalculationsHelper();
                cal.doCalculations(afda, currCode);
                /*Depending on what is selected in the filter
                we should return either actual commitments
                or actual Disbursement or  */
                if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
                	total = cal.getTotActualComm(); //takes the actual commitments 
            	} else {
	                switch (transactionType) {
		                case Constants.EXPENDITURE:
		                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
		                        total = cal.getTotActualExp();
		                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
		                        total = cal.getTotPlannedExp();
		                    } else {
		                        total = cal.getTotPipelineExp();
		                    }
		                    break;
		                case Constants.DISBURSEMENT:
		                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
		                        total = cal.getTotActualDisb();
		                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
		                        total = cal.getTotPlanDisb();
		                    } else {
		                        total = cal.getTotPipelineDisb();
		                    }
		                    break;
		                default:
		                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
		                        total = cal.getTotActualComm();
		                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
		                        total = cal.getTotPlannedComm();
		                    } else {
		                        total = cal.getTotPipelineComm();
		                    }
	                }
            	}
                AmpCategoryValueLocations aloc = new AmpCategoryValueLocations();
                aloc.setId(locId);
                aloc.setName(hmName.get(locId));
                map.put(aloc, total.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(decimalsToShow, RoundingMode.HALF_UP));
            }

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load fundings from db", e);
        }
        return map;
    }
	
	public static Map<AmpTheme, BigDecimal> getFundingByProgramList(Collection<AmpTheme> progList, String currCode,  Date startDate,
            Date endDate, int transactionType, String adjustmentType, int decimalsToShow, BigDecimal divideByDenominator, DashboardFilter filter) throws DgException {
        
		Map<AmpTheme, BigDecimal> map = new HashMap<AmpTheme, BigDecimal>();
		Long[] orgIds = filter.getSelOrgIds();
        Long[] orgGroupIds = filter.getSelOrgGroupIds();
        
        TeamMember tm = filter.getTeamMember();
        Long[] locationIds = filter.getSelLocationIds();
        Long[] sectorIds = filter.getSelSectorIds();
        boolean locationCondition = locationIds != null && locationIds.length > 0 && !locationIds[0].equals(-1l);
        boolean sectorCondition = sectorIds != null && sectorIds.length > 0 && !sectorIds[0].equals(-1l);

    	DecimalWraper total = null;
        String oql = "";

        if (filter.getTransactionType()==Constants.MTEFPROJECTION){
        	oql = "select fp, prog.ampThemeId, prog.name ";
        } else {
        	oql = "select fd, prog.ampThemeId, prog.name ";
        }
        oql += ", actProg.programPercentage ";
        if (locationCondition)
        	oql += ", actloc.locationPercentage ";
        if (sectorCondition)
        	oql += ", actsec.sectorPercentage ";
        
        boolean donorCondition = (filter.getDonorAgencyId()!=null && filter.getDonorAgencyId()!=-1)? true : false;
        boolean implementingCondition = (filter.getImplementingAgencyId()!=null && filter.getImplementingAgencyId()!=-1)? true : false;
        boolean beneficiaryCondition = (filter.getBeneficiaryAgencyId()!=null && filter.getBeneficiaryAgencyId()!=-1)? true : false;
        boolean peaceMarkerCondition = (filter.getPeacebuilderMarkerId()!=null && filter.getPeacebuilderMarkerId()!=-1)? true : false;
        boolean peacebuildingCondition = (filter.getPeacebuildingId()!=null && filter.getPeacebuildingId()!=-1)? true : false;
        
        if (filter.getTransactionType()==Constants.MTEFPROJECTION){
	        oql += " from ";
			oql += AmpFundingMTEFProjection.class.getName() + " as fp inner join fp.ampFunding f ";
	        oql += "   inner join f.ampActivityId act ";
        } else {
        	if(filter.getFromPublicView() !=null&& filter.getFromPublicView()){
        		oql += " from "+AmpActivityGroupCached.class.getName()+" grpLink inner join grpLink.ampActivityGroup as actGroup, ";
        	} else { 
        		oql += " from ";
        	}
			oql += AmpFundingDetail.class.getName() + " as fd inner join fd.ampFundingId f ";
	        oql += "   inner join f.ampActivityId act ";
        }
        
        oql += " inner join act.actPrograms actProg ";
        oql += " inner join actProg.program prog ";
        
    	if(!(filter.getFromPublicView() !=null&& filter.getFromPublicView()))
        	oql += " inner join act.ampActivityGroup actGroup ";
        if (locationCondition) 
            oql += " inner join act.locations actloc inner join actloc.location amploc inner join amploc.location loc ";
        if (sectorCondition) {
            oql += "  inner join act.sectors actsec ";
            oql += "  inner join actsec.classificationConfig config  ";
            oql += " inner join actsec.sectorId sec ";
        }
        if (donorCondition || implementingCondition || beneficiaryCondition)
    		oql += " inner join act.orgrole orole inner join orole.role role ";
        
        //Join for Category Values
        if ((filter.getSelCVIds()!=null && filter.getSelCVIds().length>0) || (filter.getSelStatusIds()!=null && filter.getSelStatusIds().length>0) || peaceMarkerCondition || peacebuildingCondition ) {
        	oql += " inner join act.categories categ ";
		}
    	
        if ((orgIds != null && orgIds.length != 0 && orgIds[0] != -1) || (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1))
    		if (filter.getAgencyType() == org.digijava.module.visualization.util.Constants.EXECUTING_AGENCY || filter.getAgencyType() == org.digijava.module.visualization.util.Constants.BENEFICIARY_AGENCY)
    			oql += " inner join act.orgrole orole inner join orole.role role ";
        if (sectorCondition) 
        	oql += " where config.id=:config  ";
        else
        	oql += " where 1=1 ";
        
      //Filter for the Fiscal Year Start and End
        if(filter.getTransactionType()==Constants.MTEFPROJECTION){
        	oql += " and  (fp.projectionDate>=:startDate and fp.projectionDate<=:endDate)  ";
        } else {
        	oql += " and fd.transactionType =:transactionType  and  fd.adjustmentType.value =:adjustmentType ";
            oql += " and (fd.transactionDate>=:startDate and fd.transactionDate<=:endDate)  ";
        }
        oql += " and prog.ampThemeId in (" + DashboardUtil.getInStatement(progList) + ")";

        // Filter for the Organizations and their roles (Donor, Implementing or Beneficiary)
        if (donorCondition) {
        	oql = " and role.roleCode='DN' and orole.organisation in (" + filter.getDonorAgencyId() + ") ";
        } 
        if (implementingCondition) {
        	oql = " and role.roleCode='IA' and orole.organisation in (" + filter.getImplementingAgencyId() + ") ";
        } 
        if (beneficiaryCondition) {
        	oql = " and role.roleCode='BA' and orole.organisation in (" + filter.getBeneficiaryAgencyId() + ") ";
        } 

        //Filter for Category Values
        if (peaceMarkerCondition) {
        	oql += " and categ.id in ("+filter.getPeacebuilderMarkerId()+") ";
		}
        if (peacebuildingCondition) {
        	oql += " and categ.id in ("+filter.getPeacebuildingId()+") ";
		}
        
        if (filter.getSelProgramIds()!=null && filter.getSelProgramIds().length>0) {
        	oql += " and prog.ampThemeId in ("+DashboardUtil.getInStatement(DashboardUtil.getProgramsDescendentsIds(filter.getSelProgramIds()))+") ";
		}
        if (orgIds == null || orgIds.length == 0 || orgIds[0] == -1) {
            if (orgGroupIds != null && orgGroupIds.length > 0 && orgGroupIds[0] != -1) 
                oql += DashboardUtil.getOrganizationQuery(true, orgIds, orgGroupIds, filter.getAgencyType());
        } else 
            oql += DashboardUtil.getOrganizationQuery(false, orgIds, orgGroupIds, filter.getAgencyType());
        if (locationCondition) {
        	if (locationIds[0].equals(0l)) {
        		oql += " and actloc is NULL "; //Unallocated condition
			} else {
				locationIds = getAllDescendantsLocation(locationIds, DbUtil.getAmpLocations());
	            oql += " and loc.id in ("+DashboardUtil.getInStatement(locationIds)+") ";
			}
        }
        if (sectorCondition) {
        	sectorIds = getAllDescendants(sectorIds, filter.getAllSectorList());
            oql += " and sec.id in ("+DashboardUtil.getInStatement(sectorIds)+") ";
        }
        //Filter for Activity Status
        if (filter.getSelStatusIds()!=null && filter.getSelStatusIds().length>0) {
			oql += " and categ.id in ("+DashboardUtil.getInStatement(filter.getSelStatusIds())+") ";
		}
        if(filter.getFromPublicView() !=null&& filter.getFromPublicView()){
            oql += DashboardUtil.getTeamQueryManagement();
            oql += " and grpLink.ampActivityLastVersion=act.ampActivityId "; 
        }
        else
        {
        	if (filter.getActivityComputedList() == null || filter.getActivityComputedList().size() == 0)
        		oql += DashboardUtil.getTeamQuery(tm);
        	oql += " and act.ampActivityId = actGroup.ampActivityLastVersion";
        }

        if (filter.getActivityComputedList() != null && filter.getActivityComputedList().size() > 0)
        	oql += " and act.ampActivityId IN (" + DashboardUtil.getInStatement(filter.getActivityComputedList()) + ")";
        else
        	oql += "  and act.team is not null ";
        	
        oql += " and act.draft=false and act.approvalStatus IN (" + Util.toCSString(AmpARFilter.validatedActivityStatus) + ") ";
        oql += " and (act.deleted = false or act.deleted is null)";
        
        Session session = PersistenceManager.getRequestDBSession();
        List fundings = null;
        try {
            Query query = session.createQuery(oql);
            query.setDate("startDate", startDate);
            query.setDate("endDate", endDate);
            if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
	            query.setLong("transactionType", transactionType);
	            query.setString("adjustmentType",adjustmentType);
            }
            if (sectorCondition) {
            	query.setLong("config", filter.getSelSectorConfigId());
            }
            fundings = query.list();

            HashMap<Long, AmpTheme> programParentList = new HashMap<Long, AmpTheme>();
            Iterator iter = progList.iterator();
            while (iter.hasNext()) {
            	AmpTheme prog = (AmpTheme)iter.next();
            	AmpTheme prog2 = DashboardUtil.getTopLevelProgram(prog);
            	programParentList.put(prog.getAmpThemeId(), prog2);
            }
            
            HashMap<Long, ArrayList<AmpFundingDetail>> hm = new HashMap<Long, ArrayList<AmpFundingDetail>>();
            HashMap<Long, String> hmName = new HashMap<Long, String>();
            Iterator it = fundings.iterator();
            while(it.hasNext()){
            	Object[] item = (Object[])it.next();
            	AmpFundingDetail currentFd = null;
            	if(filter.getTransactionType()==Constants.MTEFPROJECTION){
            		AmpFundingMTEFProjection fp = (AmpFundingMTEFProjection) item[0];
            		//Here use a tricky harcode to of transaction/adjustment type and it is set to "actual commitments", this is done to use FundingCalculationsHelper to do calculations for MTEF projections
            		currentFd = new AmpFundingDetail(Constants.COMMITMENT,null,fp.getAmount(),fp.getProjectionDate(),fp.getAmpCurrency(),0d);
            	} else {
            		AmpFundingDetail fd = (AmpFundingDetail) item[0];
            		currentFd = new AmpFundingDetail(fd.getTransactionType(),fd.getAdjustmentType(),fd.getAbsoluteTransactionAmount(),fd.getTransactionDate(),fd.getAmpCurrencyId(),fd.getFixedExchangeRate());
                }
            	if (item.length==4) 
            		currentFd.setTransactionAmount(currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100);
            	if (item.length==5) 
            		currentFd.setTransactionAmount((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100);
            	if (item.length==6) 
            		currentFd.setTransactionAmount(((currentFd.getAbsoluteTransactionAmount()*(Float)item[3]/100)*(Float)item[4]/100)*(Float)item[5]/100);
            	Long id = (Long) item[1];
            	String name = programParentList.get(id).getName();
            	id = programParentList.get(id).getAmpThemeId();
            	if(hm.containsKey(id)){
            		ArrayList<AmpFundingDetail> afda = hm.get(id);
            		afda.add(currentFd);
            	}
            	else
            	{
            		ArrayList<AmpFundingDetail> afda = new ArrayList<AmpFundingDetail>();
            		afda.add(currentFd);
            		hmName.put(id, name);
            		hm.put(id, afda);
            	}
            }

            Iterator<Long> it2 = hm.keySet().iterator();
            while(it2.hasNext()){
            	Long progId = it2.next();
            	ArrayList<AmpFundingDetail> afda = hm.get(progId);
                FundingCalculationsHelper cal = new FundingCalculationsHelper();
                cal.doCalculations(afda, currCode);
                /*Depending on what is selected in the filter
                we should return either actual commitments
                or actual Disbursement or  */
                if(filter.getTransactionType()!=Constants.MTEFPROJECTION){
                	total = cal.getTotActualComm(); //takes the actual commitments 
            	} else {
	                switch (transactionType) {
	                case Constants.EXPENDITURE:
	                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
	                        total = cal.getTotActualExp();
	                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
	                        total = cal.getTotPlannedExp();
	                    } else {
	                        total = cal.getTotPipelineExp();
	                    }
	                    break;
	                case Constants.DISBURSEMENT:
	                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
	                        total = cal.getTotActualDisb();
	                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
	                        total = cal.getTotPlanDisb();
	                    } else {
	                        total = cal.getTotPipelineDisb();
	                    }
	                    break;
	                default:
	                    if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey())) {
	                        total = cal.getTotActualComm();
	                    } else if (adjustmentType.equals(CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey())) {
	                        total = cal.getTotPlannedComm();
	                    } else {
	                        total = cal.getTotPipelineComm();
	                    }
	                }
                }
                AmpTheme aprog = new AmpTheme();
                aprog.setAmpThemeId(progId);
                aprog.setName(hmName.get(progId));
                map.put(aprog, total.getValue().divide(divideByDenominator, RoundingMode.HALF_UP).setScale(decimalsToShow, RoundingMode.HALF_UP));
            }

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load fundings from db", e);
        }
        return map;
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
            qry.setParameter("id", id, Hibernate.LONG);
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
            qry.setParameter("id", id, Hibernate.LONG);
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
            qry.setParameter("id", id, Hibernate.LONG);
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
			contact=(AmpContact)qry.uniqueResult();
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

	public static ArrayList<AmpSector> getAmpSectors() {
		Session session = null;
		Query q = null;
		AmpSector ampSector = null;
		ArrayList<AmpSector> sector = new ArrayList<AmpSector>();
		String queryString = null;
		Iterator<AmpSector> iter = null;

		try {
			session = PersistenceManager.getSession();
			queryString = " select Sector from "
					+ AmpSector.class.getName()
					+ " Sector where Sector.parentSectorId is not null and (Sector.deleted is null or Sector.deleted = false) order by Sector.name";
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				ampSector = (AmpSector) iter.next();
				sector.add(ampSector);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Sector names  from database "
					+ ex.getMessage());
		}
		return sector;
	}
    private static Long[] getAllDescendants(Long[] sectorIds,
			ArrayList<AmpSector> allSectorList) {
    	//Go through the list to determine the children
    	List<Long> tempSectorIds = new ArrayList<Long>();
		for(AmpSector as : allSectorList){
			for(Long i : sectorIds){
		    	if(!tempSectorIds.contains(i)) tempSectorIds.add(i);
    			if(as.getParentSectorId() != null && as.getParentSectorId().getAmpSectorId().equals(i)){
    	    		tempSectorIds.add(as.getAmpSectorId());
    			} else if(as.getParentSectorId() != null && as.getParentSectorId().getParentSectorId() != null && as.getParentSectorId().getParentSectorId().getAmpSectorId().equals(i)){
    	    		tempSectorIds.add(as.getAmpSectorId());
    			}
    		}
    	}
		return (Long[]) tempSectorIds.toArray(new Long[0]);
	}

	public static ArrayList<AmpCategoryValueLocations> getAmpLocations() {
		Session session = null;
		Query q = null;
		AmpCategoryValueLocations location = null;
		ArrayList<AmpCategoryValueLocations> locations = new ArrayList<AmpCategoryValueLocations>();
		Iterator<AmpCategoryValueLocations> iter = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = " from " + AmpCategoryValueLocations.class.getName()+
            " vl where vl.parentLocation  is not null " ;
			q = session.createQuery(queryString);
			iter = q.list().iterator();

			while (iter.hasNext()) {
				location = (AmpCategoryValueLocations) iter.next();
				locations.add(location);
			}

		} catch (Exception ex) {
			logger.error("Unable to get Sector names  from database "
					+ ex.getMessage());
		}
		return locations;
	}

	private static Long[] getAllDescendantsLocation(Long[] locationIds,
			ArrayList<AmpCategoryValueLocations> allLocationsList) {
    	List<Long> tempLocationsIds = new ArrayList<Long>();
    	if (locationIds.length == 1){
    		AmpCategoryValueLocations loc = getLocationById(locationIds[0]);
    		if (loc.getParentLocation()==null) {
    			Long[] ids = {loc.getId()};
				return ids;
			}
    	}
		for(AmpCategoryValueLocations as : allLocationsList){
			for(Long i : locationIds){
		    	if(!tempLocationsIds.contains(i)) tempLocationsIds.add(i);
    			if(as.getParentLocation() != null && as.getParentLocation().getId().equals(i)){
    				tempLocationsIds.add(as.getId());
    			} else if(as.getParentLocation() != null && as.getParentLocation().getParentLocation() != null && as.getParentLocation().getParentLocation().getId().equals(i)){
    				tempLocationsIds.add(as.getId());
    			}
    		}
    	}
		return (Long[]) tempLocationsIds.toArray(new Long[0]);
	}

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

	public static List<AmpDashboard> getAllDashboards() {
        Session session = null;
        List<AmpDashboard> dsb = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select d from "
                + AmpDashboard.class.getName() + " d ";
            Query qry = session.createQuery(queryString);
            dsb = qry.list();

        } catch (Exception ex) {
            logger.error("Unable to get dashboards from database", ex);
        }
        logger.debug("Getting dashboards successfully ");
        return dsb;
    }
	
	public static AmpDashboard getDashboardById(Long id) {
        Session session = null;
        AmpDashboard dash = null;
        Iterator itr = null;
		
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select d from "
                + AmpDashboard.class.getName() + " d where (d.id=:id)";
            Query qry = session.createQuery(queryString);
            qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			itr = qry.list().iterator();
			if (itr.hasNext()) {
				dash = (AmpDashboard) itr.next();
			}
        } catch (Exception ex) {
            logger.error("Unable to get dashboard from database", ex);
        }
        logger.debug("Getting dashboard successfully ");
        return dash;
    }

    
	public static List<AmpGraph> getAllGraphs() {
        Session session = null;
        List<AmpGraph> graphs = null;

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select g from "
                + AmpGraph.class.getName() + " g ";
            Query qry = session.createQuery(queryString);
            graphs = qry.list();

        } catch (Exception ex) {
            logger.error("Unable to get graphs from database", ex);
        }
        logger.debug("Getting graphs successfully ");
        return graphs;
    }
	
	public static AmpGraph getGraphById(Long id) {
        Session session = null;
        AmpGraph graph = null;
        Iterator itr = null;
		
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select g from "
                + AmpGraph.class.getName() + " g where (g.id=:id)";
            Query qry = session.createQuery(queryString);
            qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			itr = qry.list().iterator();
			if (itr.hasNext()) {
				graph = (AmpGraph) itr.next();
			}
        } catch (Exception ex) {
            logger.error("Unable to get graphs from database", ex);
        }
        logger.debug("Getting graphs successfully ");
        return graph;
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
			qry.setParameter("id", id, Hibernate.LONG);
			itr = qry.list().iterator();
			if (itr.hasNext()) {
				prog = (AmpTheme) itr.next();
			}
        } catch (Exception ex) {
            logger.error("Unable to get Theme from database", ex);
        }
        return prog;
    }

	public static List<AmpDashboardGraph> getDashboardGraphByDashboard(Long id) {
        Session session = null;
        List<AmpDashboardGraph> dashGraphs = null;
        Iterator itr = null;
		
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select dg from "
                + AmpDashboardGraph.class.getName() + " dg where (dg.dashboard=:id)";
            Query qry = session.createQuery(queryString);
            qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			dashGraphs = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get dashboardGraphs from database", ex);
        }
        logger.debug("Getting dashboardGraphs successfully ");
        return dashGraphs;
    }

	public static AmpDashboardGraph getDashboardGraphById(Long id) {
        Session session = null;
        AmpDashboardGraph dashGraph = null;
        Iterator itr = null;
		
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select d from "
                + AmpDashboardGraph.class.getName() + " d where (d.id=:id)";
            Query qry = session.createQuery(queryString);
            qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			itr = qry.list().iterator();
			if (itr.hasNext()) {
				dashGraph = (AmpDashboardGraph) itr.next();
			}
        } catch (Exception ex) {
            logger.error("Unable to get dashboard graph from database", ex);
        }
        logger.debug("Getting dashboard graph successfully ");
        return dashGraph;
    }

	public static void saveDashboard(AmpDashboard dashboard, DashboardForm form) throws DgException {
		
		Transaction tx=null;
		try {
			Session session = PersistenceManager.getSession();
			session.save(dashboard);
			
			if(form.getDashGraphList()!=null && form.getDashGraphList().size()>0){
				for (Iterator iterator = form.getDashGraphList().iterator(); iterator.hasNext();) {
					AmpDashboardGraph dashboardGraph = (AmpDashboardGraph) iterator.next();
					dashboardGraph.setDashboard(dashboard);
					session.save(dashboardGraph);
				}
			}
		} catch (HibernateException e) {
			logger.error("Error saving dashboard",e);
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception ex) {
					throw new DgException("Cannot rallback save dashboard action",ex);
				}
				throw new DgException("Cannot save dashboard!",e);
			}
		}
	}
	
	public static void updateDashboard(AmpDashboard dashboard, DashboardForm form) throws DgException {
		
		try {
			Session session = PersistenceManager.getSession();
			session.update(dashboard);
			Collection<AmpDashboardGraph> dashGraphs = getDashboardGraphByDashboard(dashboard.getId());

			if(form.getDashGraphList()!=null && form.getDashGraphList().size()>0){
				for (Iterator iterator = form.getDashGraphList().iterator(); iterator.hasNext();) {
					AmpDashboardGraph ampDashboardGraph = (AmpDashboardGraph) iterator.next();
					if (dashGraphs.contains(ampDashboardGraph)) {
						ampDashboardGraph.setDashboard(dashboard);
						session.update(ampDashboardGraph);
					} else {
						ampDashboardGraph.setDashboard(dashboard);
						session.save(ampDashboardGraph);
						dashGraphs.add(ampDashboardGraph);
					}
				}
			} else {
				for (Iterator iterator = dashGraphs.iterator(); iterator.hasNext();) {
					AmpDashboardGraph ampDashboardGraph = (AmpDashboardGraph) iterator.next();
					session.delete(ampDashboardGraph);
				}
				dashGraphs = null;
			}
			
			if(dashGraphs!=null && dashGraphs.size()>0){
				for (Iterator iterator = dashGraphs.iterator(); iterator.hasNext();) {
					AmpDashboardGraph ampDashboardGraph = (AmpDashboardGraph) iterator.next();
					if (!form.getDashGraphList().contains(ampDashboardGraph)) {
						session.delete(ampDashboardGraph);
					}
				}
			}
		} catch (HibernateException e) {
			logger.error("Error saving dashboard",e);
		}
	}
	
	public static void removeDashboard(AmpDashboard dashboard) throws DgException {
		try {
			Session session = PersistenceManager.getSession();

			Collection<AmpDashboardGraph> dashGraphs = getDashboardGraphByDashboard(dashboard.getId());
			
			for (Iterator iterator = dashGraphs.iterator(); iterator.hasNext();) {
				AmpDashboardGraph ampDashboardGraph = (AmpDashboardGraph) iterator.next();
				session.delete(ampDashboardGraph);
			}
			session.delete(dashboard);
		} catch (HibernateException e) {
			logger.error("Error deleting dashboard",e);
		}
	}
	
	public static List<AmpDashboard> getDashboardsToShowInMenu() {
        Session session = null;
        List<AmpDashboard> dashs = null;
        Iterator itr = null;
		
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select d from "
                + AmpDashboard.class.getName() + " d where (d.showInMenu='true')";
            Query qry = session.createQuery(queryString);
            qry = session.createQuery(queryString);
			dashs = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get dashboards from database", ex);
        }
        
        return dashs;
    }
	public static ArrayList<BigInteger> getInActivities(String query) throws Exception{
		Session session = PersistenceManager.getRequestDBSession();
		ArrayList<BigInteger> result = (ArrayList<BigInteger>) session.createSQLQuery("select amp_activity_id from amp_activity where " + query).list();
		return result;
	}
}
