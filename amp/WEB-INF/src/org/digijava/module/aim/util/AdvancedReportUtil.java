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

import java.text.Collator;

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
		Transaction tx = null;
		String queryString=null;
		Query query=null;
		Iterator iter=null;
		Set pageFilters = new HashSet();
		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			//session.save(ampReports);
			session.saveOrUpdate(ampReports);
			//ampReports.setDescription("/viewAdvancedReport.do?view=reset&ampReportId="+ampReports.getAmpReportId());
			//session.update(ampReports);
			
			if (ampReports.getMembers()==null){
				ampReports.setMembers(new HashSet());
			}
				
			
			AmpTeam ampTeam = (AmpTeam) session.get(AmpTeam.class, ampTeamId);
			
			String tempQry = "select teamRep from "
				+ AmpTeamReports.class.getName()
				+ " teamRep where (teamRep.team=:tId) and "
				+ " (teamRep.report=:rId)";
			Query tmpQry = session.createQuery(tempQry);
			tmpQry.setParameter("tId",ampTeamId,Hibernate.LONG);
			tmpQry.setParameter("rId",ampReports.getAmpReportId(),Hibernate.LONG);
			Iterator tmpItr = tmpQry.list().iterator();
			if (!tmpItr.hasNext()) {
				AmpTeamReports tr = new AmpTeamReports();
				tr.setTeam(ampTeam);
				tr.setReport(ampReports);
				tr.setTeamView(false);
				session.save(tr);
			}
			
			//amp-3304
			/*
			if(teamLead == true&&(ampReports.getOwnerId()==null||(ampReports.getOwnerId().getAmpTeam().getTeamLead()!=null && 
					ampReports.getOwnerId().getAmpTeamMemId().equals(ampReports.getOwnerId().getAmpTeam().getTeamLead().getAmpTeamMemId())))
					)
			{
				//logger.info(teamMember.getMemberName() + " is Team Leader ");

				//Check if this assignment was already done.
				AmpTeamReports teamReports = TeamUtil.getAmpTeamReport(ampTeam.getAmpTeamId(), ampReports.getAmpReportId());
				if(teamReports == null)
				{
					AmpTeamReports ampTeamReports = new AmpTeamReports();
					
					ampTeamReports.setTeamView(true);				
					ampTeamReports.setTeam(ampTeam);
					ampTeamReports.setReport(ampReports);
					session.save(ampTeamReports);
				}
			}
			*/
//			else
//			{
				//logger.info(teamMember.getMemberName() + " is Team Memeber ");
				//Long lg = teamMember.getMemberId();
				AmpTeamMember ampTeamMember =null;
				if(ampReports.getOwnerId()!=null){
					ampTeamMember=(AmpTeamMember) session.get(AmpTeamMember.class, ampReports.getOwnerId().getAmpTeamMemId());	
				}else {
					ampTeamMember = (AmpTeamMember) session.get(AmpTeamMember.class, ampMemberId);	
				}					
				Set reportSet = ampTeamMember.getReports();
				
				//reportSet.add(ampReports);  // Not needed because it is set from ampReports object
				
				
				ampReports.getMembers().add(ampTeamMember);
				session.saveOrUpdate(ampTeamMember);
				//session.save(ampTeamMember);
//			}

//			queryString = "select filters from " + AmpFilters.class.getName() + " filters ";
//			//logger.info( " Filter Query...:: " + queryString);
//			query = session.createQuery(queryString);
//			if(query!=null)
//			{
//				iter = query.list().iterator();
//				while(iter.hasNext())
//				{
//					AmpFilters filt = (AmpFilters) iter.next();
//					if(filt.getFilterName().compareTo("Region") != 0 && 
//						filt.getFilterName().compareTo("Start Date/Close Date") !=0	&& 
//						filt.getFilterName().compareTo("Planned/Actual") != 0 )  
//					{
//						//logger.info("Insertd : " + filt.getFilterName());
//						pageFilters.add(filt);
//					}
//				}
//			}
//
//			AmpPages ampPages = new AmpPages();
//			ampPages.setFilters(pageFilters);
//			ampPages.setPageName(ampReports.getName());
//			//logger.info(" Page Name  : " + ampPages.getPageName());
//				
//			String pageCode = "" + ampReports.getName().trim().charAt(0);
//			for(int j=0; j <ampReports.getName().length(); j++)
//			{
//				if(ampReports.getName().charAt(j) == ' ')
//						pageCode = pageCode + ampReports.getName().charAt(j+1);
//			}
//			ampPages.setPageCode(pageCode);
//			ampPages.setAmpTeamId(ampTeamId);
//			session.save(ampPages);
//			
//			
//			pageFilters = ampPages.getFilters();
//			Iterator itr = pageFilters.iterator();
//			while (itr.hasNext()) {
//				AmpFilters filt = (AmpFilters) itr.next();
//				AmpTeamPageFilters tpf = new AmpTeamPageFilters();
//				tpf.setFilter(filt);
//				tpf.setTeam(ampTeam);
//				tpf.setPage(ampPages);
//				session.save(tpf);
//			
//			}
//			
//			ampReports.setAmpPage(ampPages);
//			
//			queryString = "select t from " + AmpTeam.class.getName() + " t " +
//					"where t.accessType = 'Management'";
//			query = session.createQuery(queryString);
//			itr = query.list().iterator();
//			while (itr.hasNext()) {
//				AmpTeam t = (AmpTeam) itr.next();
//				pageFilters = ampPages.getFilters();
//				Iterator itr1 = pageFilters.iterator();
//				while (itr1.hasNext()) {
//					AmpFilters filt = (AmpFilters) itr1.next();
//					AmpTeamPageFilters tpf = new AmpTeamPageFilters();
//					tpf.setFilter(filt);
//					tpf.setTeam(t);
//					tpf.setPage(ampPages);
//					session.save(tpf);
//			
//				}
//			}
			
			//tx.commit(); 

		}
		catch (Exception ex) {
			logger.error("Exception from saveReport()  " + ex.getMessage()); 
			ex.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
					logger.debug("Transaction Rollbacked");
				}
				catch (HibernateException e) {
					logger.error("Rollback failed :" + e);
				}
			}			
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception e) {
					logger.error("Release session faliled :" + e);
				}
			}
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
	
	public static Collection getColumnListWithDbSession()
	{
		Session session = null;
		String sqlQuery = "";
		boolean flag =false;
		Iterator iter = null;
		Collection coll = new ArrayList();
		Query query = null;
		AmpColumns ampColumns = new AmpColumns();
		try
		{
			session = PersistenceManager.getRequestDBSession();
			sqlQuery = "select c from "+ AmpColumns.class.getName() + " c order by columnName asc";
			query = session.createQuery(sqlQuery);
			if (query != null) 
			{
				iter = query.list().iterator();
				while (iter.hasNext()) 
				{
					ampColumns = (AmpColumns) iter.next();
					coll.add(ampColumns);
				}
				flag = true;
			}
			return coll;
		}
		catch(Exception e)
		{
			logger.error(e);
			////System.out.println(" Error in getColumnList()  :  " + e);
		}
		return coll;
			
	}

	public static Collection getMeasureList()
	{
		Session session = null;
		String sqlQuery = "";
		boolean flag =false;
		Iterator iter = null;
		Collection coll = new ArrayList();
		Query query = null;
		AmpMeasures ampMeasures = new AmpMeasures();
		try
		{
			session = PersistenceManager.getSession();
			sqlQuery = "select c from "+ AmpMeasures.class.getName() + " c";
			query = session.createQuery(sqlQuery);
			if (query != null) 
			{
				iter = query.list().iterator();
				while (iter.hasNext()) 
				{
					ampMeasures = (AmpMeasures) iter.next();
					coll.add(ampMeasures);
				}
				flag = true;
			}
			return coll;
		}
		catch(Exception e)
		{
			logger.error(e);
			////System.out.println(" Error in getMeasureList()  :  " + e);
		} finally {
			PersistenceManager.releaseSession(session);			
		}
		return coll;
	}
	
	
	public static Collection getMeasureListbyType(String type)
	{
		Session session = null;
		String sqlQuery = "";
		boolean flag =false;
		Iterator iter = null;
		Collection coll = new ArrayList();
		Query query = null;
		AmpMeasures ampMeasures = new AmpMeasures();
		try
		{
			session = PersistenceManager.getSession();
			sqlQuery = "select c from "+ AmpMeasures.class.getName() + " c where c.type=:type";
			query = session.createQuery(sqlQuery);
			query.setString("type", type);
			if (query != null) 
			{
				iter = query.list().iterator();
				while (iter.hasNext()) 
				{
					ampMeasures = (AmpMeasures) iter.next();
					coll.add(ampMeasures);
				}
				flag = true;
			}
			return coll;
		}
		catch(Exception e)
		{
			logger.error(e);
			////System.out.println(" Error in getMeasureList()  :  " + e);
		} finally {
			PersistenceManager.releaseSession(session);
		}
		return coll;
	}
	@Deprecated
	public static AmpTeamMember checkDuplicateReportName(String reportTitle){
		AmpTeamMember teamMember=null;
		Session session = null;
		Query query = null;
		Iterator iter=null;
		String queryString;
		try {
			session = PersistenceManager.getSession();
			queryString = "select report from " + AmpReports.class.getName() + " report ";
			//logger.info( " Query :" + queryString);
			query = session.createQuery(queryString);
	//		iter = query.list().iterator();
			
			if(query!=null)
			{
				iter = query.list().iterator();
				//logger.info("............Query is not null............");
				while(iter.hasNext())
				{
					AmpReports r = (AmpReports) iter.next();
					if( reportTitle.trim().equals(r.getName()) )
					{
						teamMember=r.getOwnerId();
						break;
					}
						
				}
			}

		} catch (Exception ex) {
			logger.error("Unable to get checkDupilcateReportName()", ex);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.debug("releaseSession() failed");
			}
		}
		return teamMember;
	}
        
        public static boolean checkDuplicateReportName(String reportTitle, Long ownerId, Long dbReportId, Boolean drilldownTab) throws Exception{
		boolean exist=false;
		Session session = null;
		Query query = null;
		Iterator iter=null;
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
                         if(dbReportId!=null){
                            query.setLong("dbReportId", dbReportId);
                        }
                       if(query.list()!=null&&query.list().size()>0){
                           exist=true;
                       }			

		} catch (Exception ex) {
			logger.error("Unable to get checkDupilcateReportName()", ex);
                        throw ex;
		} 
		
		return exist;
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
	        try {
	            logger.info(" this is the utils's qid " + qid);
	            ampReports = (AmpReports) session.load(AmpReports.class, qid);
	            AmpTeamReports ampTeamReports = null;
	            queryString = "select tr from " + AmpTeamReports.class.getName() + " tr " + "where tr.report=:qid ";
	            qry = session.createQuery(queryString);
	            qry.setParameter("qid", qid, Hibernate.LONG);
	            List lst	= qry.list();
	            Iterator itr = lst.iterator();
	            Collection col = new ArrayList(); 
	            while (itr.hasNext()) {
	                ampTeamReports = (AmpTeamReports) itr.next();
	                ampTeamReports.setReport(null);
	                session.delete(ampTeamReports);
	            }
	            
	           /**
	             * Removing link between reports and AmpTeamMember entity
	             */
	            if ( ampReports.getMembers() != null ) {
		            for ( Object tm: ampReports.getMembers() ) {
		            	AmpTeamMember atm		= (AmpTeamMember) tm;
		            	if ( atm.getReports() != null ) {
			            	Iterator<AmpReports> itr2	= atm.getReports().iterator();
			            	while (itr2.hasNext()) {
			            		if ( qid.equals( itr2.next().getAmpReportId() ) ){
			            			//System.out.println("removing relation to ampTeamMember");
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
	        } catch (org.hibernate.ObjectNotFoundException onfe) {
	            logger.error("Exception from deleteQuestion() :", onfe);
	        }
	    } catch (Exception e) {
	        logger.error("Exception from deleteQuestion() :", e);
	    }
	    return false;
	}



	public static boolean deleteReportsForOwner(Long qid) {
	    Session session = null;
	    Transaction tx = null;
	    String queryString = null;
	    Query qry = null;
	    Collection col = null;
	    try {
	        session = PersistenceManager.getRequestDBSession();
	        try {
	            queryString = "select rep from " + AmpReports.class.getName() + " rep " +
	                "where rep.ownerId=:oId ";
	            qry = session.createQuery(queryString);
	            qry.setParameter("oId", qid, Hibernate.LONG);
	            Iterator itr = qry.list().iterator();
	            col = new ArrayList();
	            while (itr.hasNext()) {
	                AmpReports rep = (AmpReports) itr.next();
	                session.delete(rep);
	            }
	            return true;
	        } catch (org.hibernate.ObjectNotFoundException onfe) {
	            logger.error("Exception from deleteQuestion() :", onfe);
	        }
	    } catch (Exception e) {
	        logger.error("Exception from deleteQuestion() :", e);
	    }
	    return false;
	}
	
	/**
	 * Return true if the column colName is already in the columns list.
	 * @param columns
	 * @param colName
	 * @return
	 */
	public static boolean isColumnAdded (Set <AmpReportColumn> columns, String colName){
		
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
		for (Iterator iterator = cols1.iterator(); iterator.hasNext();) {
			AmpReportColumn ampReportColumn1 = (AmpReportColumn) iterator.next();
			int cont = 0;
			for (Iterator iterator2 = cols2.iterator(); iterator2.hasNext();) {
				AmpReportColumn ampReportColumn2 = (AmpReportColumn) iterator2.next();
				if (ampReportColumn1.getColumn().getColumnName().equals(ampReportColumn2.getColumn().getColumnName())) {
					cont++;
				}
			}
			if (cont>1) {
				iterator.remove();
			}
		}
		ampReport.setColumns(cols1);
	}
	
}
