/**
 * AdvancedReportUtil.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.aim.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpDesktopTabSelection;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpPages;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamPageFilters;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.hibernate.FlushMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.digijava.module.aim.dbentity.*;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;

import java.text.Collator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

/**
 * AdvancedReportUtil.java
 * TODO description here
 * @author mihai
 * @package org.digijava.module.aim.util
 * @since 11.09.2007
 */
public final class AdvancedReportUtil {
    
    private static Logger logger = Logger.getLogger(AdvancedReportUtil.class);
    
    private final static List<AmpColumns> CACHED_COLUMNS_LIST = Collections.unmodifiableList(buildColumnList());
    
	public static void saveReport(AmpReports ampReports,Long ampTeamId,Long ampMemberId,boolean teamLead)
	{
		Session session = null;
		try
		{
			session = PersistenceManager.getSession();
			session.saveOrUpdate(ampReports);
			
			if (ampReports.getMembers() == null){
				ampReports.setMembers(new HashSet<AmpTeamMember>());
			}
							
			AmpTeam ampTeam = (AmpTeam) session.get(AmpTeam.class, ampTeamId);
			
			String tempQry = "select teamRep from "
				+ AmpTeamReports.class.getName()
				+ " teamRep where (teamRep.team=:tId) and "
				+ " (teamRep.report=:rId)";
			Query tmpQry = session.createQuery(tempQry);
			tmpQry.setLong("tId", ampTeamId);
			tmpQry.setLong("rId", ampReports.getAmpReportId());
			if (tmpQry.list().isEmpty())
			{
				AmpTeamReports tr = new AmpTeamReports();
				tr.setTeam(ampTeam);
				tr.setReport(ampReports);
				tr.setTeamView(false);
				session.save(tr);
			}
			
			AmpTeamMember ampTeamMember = null;
			if (ampReports.getOwnerId() != null)
			{
				ampTeamMember=(AmpTeamMember) session.get(AmpTeamMember.class, ampReports.getOwnerId().getAmpTeamMemId());	
			}
			else
			{
				ampTeamMember = (AmpTeamMember) session.get(AmpTeamMember.class, ampMemberId);	
			}				
			
			ampReports.getMembers().add(ampTeamMember);
			session.saveOrUpdate(ampTeamMember);
		}
		catch (Exception ex) {
			logger.error("Exception from saveReport()  ", ex); 
		}
	}

	/**
	 * gets the list of the names of all the columns which exist in the system
	 * @return
	 */
    public static List<String> getColumnNamesList()
    {
    	List<String> res = new ArrayList<String>();
    	for(AmpColumns ac:getColumnList())
    		res.add(ac.getColumnName());
    	return res;
    }
    
    public static List<AmpColumns> getColumnList()
    {
    	return CACHED_COLUMNS_LIST;
    }
    
	private static List<AmpColumns> buildColumnList()
	{
		List<AmpColumns> res = new ArrayList<AmpColumns>();
		Session session = null;
		Query query = null;
		try
		{
			session = PersistenceManager.getSession();
			String sqlQuery = "select c from "+ AmpColumns.class.getName() + " c order by columnName asc";
			query = session.createQuery(sqlQuery);
			for(Object obj:query.list())
			{
				res.add((AmpColumns) obj);
			}
			return res;
		}
		catch(Exception e)
		{
			logger.error(e);
			throw new RuntimeException("cound not fetch columns list", e);
		}		
	}	

	/**
	 * fetches a column using its name
	 * @param name
	 * @return
	 */
	public static AmpColumns getColumnByName(String name){
		return (AmpColumns) PersistenceManager.getSession()
				.createQuery("SELECT c FROM " + AmpColumns.class.getName() + " c WHERE c.columnName=:name")
				.setString("name", name).uniqueResult();
	}
	
	public static List<AmpMeasures> getMeasureList()
	{
		Session session = null;
		try
		{
			session = PersistenceManager.getSession();
			String sqlQuery = "select c from "+ AmpMeasures.class.getName() + " c";
			Query query = session.createQuery(sqlQuery);
			return new ArrayList<AmpMeasures>(query.list());
		}
		catch(Exception e)
		{
			throw new RuntimeException(e); 
		}
	}
	
	
	public static List<AmpMeasures> getMeasureListbyType(String type)
	{
		Session session = null;
		Query query = null;
		try
		{
			session = PersistenceManager.getSession();
			String sqlQuery = "select c from "+ AmpMeasures.class.getName() + " c where c.type=:type";
			query = session.createQuery(sqlQuery);
			query.setString("type", type);
			return new ArrayList<AmpMeasures>(query.list());
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	        
	public static boolean checkDuplicateReportName(String reportTitle, Long ownerId, Long dbReportId, Boolean drilldownTab) throws Exception
	{
		Session session = null;
		Query query = null;
		//Iterator iter=null;
		String queryString;
		try {
			session = PersistenceManager.getRequestDBSession();
			String reportName = AmpReports.hqlStringForName("report");
			queryString = "select report.ownerId from " + AmpReports.class.getName() 
                            + " report where " + reportName + "=:name and report.ownerId=:ownerId and report.drilldownTab=:drilldownTab";
			if(dbReportId!=null){
				queryString+=" and report.ampReportId!=:dbReportId";
			}
			query = session.createQuery(queryString);
			query.setLong("ownerId", ownerId);
			query.setString("name", reportTitle.trim());
			query.setBoolean("drilldownTab", drilldownTab);
			if (dbReportId != null)
			{
				query.setLong("dbReportId", dbReportId);
			}
			boolean exists = !query.list().isEmpty();
			return exists;
		} 
		catch (Exception ex)
		{
			logger.error("Unable to get checkDupilcateReportName()", ex);
			throw ex;
		}
	}
        
	/**
	 * compares AmpReports with primary key
	 * @author dare
	 *
	 */
	public static class AmpReportIdComparator implements Comparator<AmpReports>{

		public int compare(AmpReports r1, AmpReports r2) {			
			return r1.getAmpReportId().compareTo(r2.getAmpReportId());
		}
		
	}
    public static enum SortOrder{
        ASC,DESC;
    }
    
	public static class AmpReportTitleComparator implements Comparator<AmpReports>{
        private SortOrder sortOrder;
        private Collator collator;
        public AmpReportTitleComparator(SortOrder sort,Collator collator){
            this.sortOrder=sort;
            this.collator=collator;
        }
        @Override
		public int compare(AmpReports r1, AmpReports r2) {
            int compare=collator.compare(r1.getName(),r2.getName());
            if(sortOrder.equals(SortOrder.DESC)){
               compare=-compare;
            }
            return compare;
		}
	}
	
	public static class AmpReportOwnerComparator implements Comparator<AmpReports>{
        private SortOrder sortOrder;
        private Collator collator;
        public AmpReportOwnerComparator(SortOrder sort,Collator collator){
            this.sortOrder=sort;
            this.collator=collator;
        }
        @Override
		public int compare(AmpReports r1, AmpReports r2) {
            int compare=collator.compare(r1.getOwnerId().getUser().getName(),r2.getOwnerId().getUser().getName());
            if(sortOrder.equals(SortOrder.DESC)){
               compare=-compare;
            }
            return compare;
		}
	}

    public static class AmpReportCreationDateComparator implements Comparator<AmpReports>{
        SortOrder sortOrder;
        public AmpReportCreationDateComparator(SortOrder sort){
            this.sortOrder=sort;
        }
        @Override
		public int compare(AmpReports r1, AmpReports r2) {
            int compare=r1.getUpdatedDate().compareTo(r2.getUpdatedDate());
            if(sortOrder.equals(SortOrder.DESC)){
               compare=-compare;
            }
            return compare;
		}
	}
	/*
	 * this is to delete a report completely by a team lead
	 */
	public static boolean deleteReportsCompletely(Long qid) {
	    Session session = null;
	    String queryString = null;
	    Query qry = null;
	    try {
	        session = PersistenceManager.getRequestDBSession();
	        AmpReports ampReports = null;
	        // loading the 3 tables from where the deletion has to be done
           // logger.info(" this is the utils's qid " + qid);
            ampReports = (AmpReports) session.load(AmpReports.class, qid);

            queryString = "select tr from " + AmpTeamReports.class.getName() + " tr " + "where tr.report=:qid ";
            qry = session.createQuery(queryString);
            qry.setLong("qid", qid);
            for(AmpTeamReports ampTeamReports: (List<AmpTeamReports>) qry.list())
            {
                ampTeamReports.setReport(null);
                session.delete(ampTeamReports);
            }
	            
           /**
             * Removing link between reports and AmpTeamMember entity
             */
            if ( ampReports.getMembers() != null )
            {
	            for (AmpTeamMember atm: ampReports.getMembers() )
	            {
	            	if ( atm.getReports() != null ) {
		            	Iterator<AmpReports> itr2	= atm.getReports().iterator();
		            	while (itr2.hasNext()) {
		            		if ( qid.equals( itr2.next().getAmpReportId() ) ){
		            			////System.out.println("removing relation to ampTeamMember");
		            			itr2.remove();
		            		}
						}
	            	}
	            }
            }
            ampReports.getMembers().clear();
            
            for (AmpDesktopTabSelection adts : ampReports.getDesktopTabSelections()) {
            	adts.getOwner().getDesktopTabSelections().remove(adts);
            	adts.setOwner(null);
            	adts.setReport(null);
			}
            ampReports.getDesktopTabSelections().clear();
            session.delete(ampReports);

            //logger.info("SESSION HAS BEEN FLUSHED OUT !!!!!!!!!!!!!!!!!!");
            return true;
	    } catch (Exception e) {
	        logger.error("Exception from deleteQuestion() :", e);
	        return false;
	    }
	}

	
	/**
	 * Return true if the column colName is already in the columns list.
	 * @param columns
	 * @param colName
	 * @return
	 */
	public static boolean isColumnAdded(Set<AmpReportColumn> columns, String colName){
		
		for ( AmpReportColumn tempCol: columns ) {
			if ( colName.equals(tempCol.getColumn().getColumnName()) ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes the column colName from the hierarchies list.
	 * @param columns
	 * @param colName
	 */
	public static void removeColumnFromHierarchies (Set <AmpReportHierarchy> columns, String colName){
		AmpReportHierarchy hierarchy = null;
		for ( AmpReportHierarchy tempCol: columns ) {
			if ( colName.equals(tempCol.getColumn().getColumnName()) ) {
				hierarchy = tempCol;
				break;
			}
		}
		if (hierarchy!=null) {
			columns.remove(hierarchy);
		}
	}
	
	/**
	 * Find and removes duplicate columns from a AmpReport
	 * @param ampReport
	 */
	public static void removeDuplicatedColumns(AmpReports ampReport){
		Set<AmpReportColumn> cols1 = ampReport.getColumns();
		Set<AmpReportColumn> cols2 = ampReport.getColumns();
		for (Iterator<AmpReportColumn> iterator = cols1.iterator(); iterator.hasNext();) {
			AmpReportColumn ampReportColumn1 = iterator.next();
			int cont = 0;
			for (AmpReportColumn ampReportColumn2:cols2)
			{
				if (ampReportColumn1.getColumn().getColumnName().equals(ampReportColumn2.getColumn().getColumnName())) {
					cont++;
				}
			}
			if (cont > 1) {
				iterator.remove();
			}
		}
		ampReport.setColumns(cols1);
	}
	public static String getAidEffectivenesForExport(ServletContext ampContext,
			AmpActivityVersion activity,HttpSession session) {
		String aidEffectivenesToAdd = "";

		if (FeaturesUtil
				.isVisibleModule(
						"/Activity Form/Aid Effectivenes/Project uses parallel project implementation unit",
						ampContext,session)) {
			aidEffectivenesToAdd += TranslatorWorker
					.translateText("Project uses parallel project implementation unit")
					+ ":\n";
			if(activity.getProjectImplementationUnit()!=null){
				aidEffectivenesToAdd += activity.getProjectImplementationUnit();
			}	
			aidEffectivenesToAdd +=" \n";
		}
		AmpCategoryValue ampCategoryValue = CategoryManagerUtil
				.getAmpCategoryValueFromListByKey(
						CategoryConstants.PROJECT_IMPLEMENTATION_MODE_KEY,
						activity.getCategories());

		if (FeaturesUtil.isVisibleModule(
				"/Activity Form/Aid Effectivenes/Project Implementation Mode",
				ampContext,session)
				&& ampCategoryValue != null) {
			aidEffectivenesToAdd += TranslatorWorker
					.translateText("Project Implementation Mode") + ":\n";
			aidEffectivenesToAdd += ampCategoryValue.getValue() + "\n";

		}
		if (FeaturesUtil
				.isVisibleModule(
						"/Activity Form/Aid Effectivenes/Project has been approved by IMAC",
						ampContext,session)) {
			aidEffectivenesToAdd += TranslatorWorker
					.translateText("Project has been approved by IMAC") + ":\n";
			if(activity.getImacApproved()!=null){
				aidEffectivenesToAdd += activity.getImacApproved() + "\n";
			}
			aidEffectivenesToAdd +=" \n";
		}
		if (FeaturesUtil
				.isVisibleModule(
						"/Activity Form/Aid Effectivenes/Government is meber of project steering committee",
						ampContext,session)) {
			aidEffectivenesToAdd += TranslatorWorker
					.translateText("Government is meber of project steering committee")
					+ ":\n";
			if(activity.getNationalOversight()!=null){
				aidEffectivenesToAdd += activity.getNationalOversight() ; 
			}
			aidEffectivenesToAdd +=" \n";
		}
		if (FeaturesUtil
				.isVisibleModule(
						"/Activity Form/Aid Effectivenes/Project is on budget",
						ampContext,session)) {
			aidEffectivenesToAdd += TranslatorWorker
					.translateText("Project is on budget") + ":\n";
			if(activity.getOnBudget()!=null){
				aidEffectivenesToAdd += activity.getOnBudget();	
			}
			aidEffectivenesToAdd += "\n";
		}
		if (FeaturesUtil.isVisibleModule(
				"/Activity Form/Aid Effectivenes/Project is on parliament",
				ampContext,session)) {
			aidEffectivenesToAdd += TranslatorWorker
					.translateText("Project is on parliament") + ":\n";
			if(activity.getOnParliament()!=null){
				aidEffectivenesToAdd += activity.getOnParliament() ;
			}
			aidEffectivenesToAdd += "\n";
		}
		if (FeaturesUtil
				.isVisibleModule(
						"/Activity Form/Aid Effectivenes/Project disburses directly into the Goverment single treasury account",
						ampContext,session)) {
			aidEffectivenesToAdd += TranslatorWorker
					.translateText("Project disburses directly into the Goverment single treasury account")
					+ ":\n";
			if(activity.getOnTreasury()!=null){
				aidEffectivenesToAdd += activity.getOnTreasury();	
			}
			aidEffectivenesToAdd +=  "\n";
		}
		if (FeaturesUtil
				.isVisibleModule(
						"/Activity Form/Aid Effectivenes/Project uses national financial management systems",
						ampContext,session)) {
			aidEffectivenesToAdd += TranslatorWorker
					.translateText("Project uses national financial management systems")
					+ ":\n";
			if(activity.getNationalFinancialManagement()!=null){
				aidEffectivenesToAdd += activity.getNationalFinancialManagement();	
			}
			
			aidEffectivenesToAdd += "\n";
		}
		if (FeaturesUtil
				.isVisibleModule(
						"/Activity Form/Aid Effectivenes/Project uses national procurement systems",
						ampContext,session)) {
			aidEffectivenesToAdd += TranslatorWorker
					.translateText("Project uses national procurement systems")
					+ ":\n";
			if(activity.getNationalProcurement()!=null){
				aidEffectivenesToAdd += activity.getNationalProcurement();
			}
			aidEffectivenesToAdd +=  "\n";
		}
		if (FeaturesUtil
				.isVisibleModule(
						"/Activity Form/Aid Effectivenes/Project uses national audit systems",
						ampContext,session)) {
			aidEffectivenesToAdd += TranslatorWorker
					.translateText("Project uses national audit systems")
					+ ":\n";
			if(activity.getNationalAudit()!=null){
				aidEffectivenesToAdd += activity.getNationalAudit();
			}
			aidEffectivenesToAdd +=  "\n";

		}
		return aidEffectivenesToAdd;
	}
	
}
