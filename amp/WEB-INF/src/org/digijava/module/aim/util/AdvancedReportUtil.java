/**
 * AdvancedReportUtil.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpPages;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamPageFilters;
import org.digijava.module.aim.dbentity.AmpTeamReports;

/**
 * AdvancedReportUtil.java
 * TODO description here
 * @author mihai
 * @package org.digijava.module.aim.util
 * @since 11.09.2007
 */
public final class AdvancedReportUtil {
    
    private static Logger logger = Logger.getLogger(AdvancedReportUtil.class);
    
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
			tx = session.beginTransaction();
			session.save(ampReports);
			//ampReports.setDescription("/viewAdvancedReport.do?view=reset&ampReportId="+ampReports.getAmpReportId());
			session.update(ampReports);
			AmpTeam ampTeam = (AmpTeam) session.get(AmpTeam.class, ampTeamId);
			
			if(teamLead == true)
			{
				//logger.info(teamMember.getMemberName() + " is Team Leader ");
				AmpTeamReports ampTeamReports = new AmpTeamReports();
				ampTeamReports.setTeamView(true);
				
				ampTeamReports.setTeam(ampTeam);
				ampTeamReports.setReport(ampReports);
				session.save(ampTeamReports);
			}
			else
			{
				//logger.info(teamMember.getMemberName() + " is Team Memeber ");
				//Long lg = teamMember.getMemberId();
				AmpTeamMember ampTeamMember = (AmpTeamMember) session.get(AmpTeamMember.class, ampMemberId);
				Set reportSet = ampTeamMember.getReports();
				reportSet.add(ampReports);
				ampTeamMember.setReports(reportSet);
				session.save(ampTeamMember);
			}

			queryString = "select filters from " + AmpFilters.class.getName() + " filters ";
			//logger.info( " Filter Query...:: " + queryString);
			query = session.createQuery(queryString);
			if(query!=null)
			{
				iter = query.list().iterator();
				while(iter.hasNext())
				{
					AmpFilters filt = (AmpFilters) iter.next();
					if(filt.getFilterName().compareTo("Region") != 0 && 
						filt.getFilterName().compareTo("Start Date/Close Date") !=0	&& 
						filt.getFilterName().compareTo("Planned/Actual") != 0 )  
					{
						//logger.info("Insertd : " + filt.getFilterName());
						pageFilters.add(filt);
					}
				}
			}

			AmpPages ampPages = new AmpPages();
			ampPages.setFilters(pageFilters);
			ampPages.setPageName(ampReports.getName());
			//logger.info(" Page Name  : " + ampPages.getPageName());
				
			String pageCode = "" + ampReports.getName().trim().charAt(0);
			for(int j=0; j <ampReports.getName().length(); j++)
			{
				if(ampReports.getName().charAt(j) == ' ')
						pageCode = pageCode + ampReports.getName().charAt(j+1);
			}
			ampPages.setPageCode(pageCode);
			ampPages.setAmpTeamId(ampTeamId);
			session.save(ampPages);
			
			pageFilters = ampPages.getFilters();
			Iterator itr = pageFilters.iterator();
			while (itr.hasNext()) {
				AmpFilters filt = (AmpFilters) itr.next();
				AmpTeamPageFilters tpf = new AmpTeamPageFilters();
				tpf.setFilter(filt);
				tpf.setTeam(ampTeam);
				tpf.setPage(ampPages);
				session.save(tpf);
			}
			
			queryString = "select t from " + AmpTeam.class.getName() + " t " +
					"where t.accessType = 'Management'";
			query = session.createQuery(queryString);
			itr = query.list().iterator();
			while (itr.hasNext()) {
				AmpTeam t = (AmpTeam) itr.next();
				pageFilters = ampPages.getFilters();
				Iterator itr1 = pageFilters.iterator();
				while (itr1.hasNext()) {
					AmpFilters filt = (AmpFilters) itr1.next();
					AmpTeamPageFilters tpf = new AmpTeamPageFilters();
					tpf.setFilter(filt);
					tpf.setTeam(t);
					tpf.setPage(ampPages);
					session.save(tpf);
				}
			}
			
			tx.commit(); 

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

    
    
	public static Collection getColumnList()
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
			session = PersistenceManager.getSession();
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
			//System.out.println(" Error in getColumnList()  :  " + e);
		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				logger.error(e);
				e.printStackTrace();
			}
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
			//System.out.println(" Error in getMeasureList()  :  " + e);
		} finally {
			try {
				session.close();
			} catch (HibernateException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}
		return coll;
	}

	
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
}
