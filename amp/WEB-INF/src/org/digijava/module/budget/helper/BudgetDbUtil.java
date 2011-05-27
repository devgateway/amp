package org.digijava.module.budget.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpModulesVisibility;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpTheme;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.budget.dbentity.AmpDepartments;
import org.digijava.module.budget.dbentity.AmpBudgetSector;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BudgetDbUtil {
	private static Logger logger = Logger.getLogger(DbUtil.class);
	
	/**
	 * 
	 * @return ArrayList of Departments
	 */
	public static ArrayList<AmpDepartments> getDepartments(){
		ArrayList<AmpDepartments> departments = new ArrayList<AmpDepartments>();
		AmpDepartments dep = null;
        Session session = null;
        Query q = null;
        
        try {
			session = PersistenceManager.getRequestDBSession();
			  String queryString = new String();
		      queryString = "select d from " + AmpDepartments.class.getName()
		      + " d order by d.name";
		      q = session.createQuery(queryString);
		      Iterator iter = q.list().iterator();
		      while (iter.hasNext()) {
		    	  dep  = (AmpDepartments) iter.next();
		    	  departments.add(dep);
	            }
			 
		} catch (DgException e) {
			logger.error("Unable to get Amp Departments from database "
                    + e.getMessage());
		}
		Collections.sort(departments);
		return departments;
		
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static AmpBudgetSector getBudgetSectorById(Long id){
		AmpBudgetSector sector = new AmpBudgetSector();
		Session session = null;
        Query q = null;
        
        try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select s from " + AmpBudgetSector.class.getName()
			+ " s where s.idsector=:id";
			q = session.createQuery(queryString);
			q.setLong("id", id);
			Iterator iter = q.list().iterator();
			while (iter.hasNext()) {
	    	  sector = (AmpBudgetSector) iter.next();
	    	}
        } catch (DgException e) {
        	logger.error("Unable to get Amp budget sectors from database "
                    + e.getMessage());
		}
        
		return sector;
	}
	
	public static boolean existsBudgetSector(String name ,String code , Long id){
		Session session = null;
        Query q = null;
        boolean retVal=true;
        try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select s from " + AmpBudgetSector.class.getName() +" s where (s.sectorname=:name or s.code=:code) " ;
			if(id!=null){
				queryString+=" and s.idsector!=:id" ;
			}
			q = session.createQuery(queryString);
			if(id!=null){
				q.setLong("id", id);
			}			
			q.setParameter("name", name);
			q.setParameter("code", code);
			Collection col = q.list();
			if (col ==null || col.size()==0) {
				retVal = false;
	    	}
        } catch (DgException e) {
        	logger.error("Unable to get Amp budget sectors from database " + e.getMessage());
		}
        
		return retVal;
	}
	
	public static AmpDepartments getBudgetDepartmentById(Long id){
		AmpDepartments dep = new AmpDepartments();
		Session session = null;
        Query q = null;
        
        try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select d from " + AmpDepartments.class.getName()
			+ " d where d.id=:id";
			q = session.createQuery(queryString);
			q.setLong("id", id);
			Iterator iter = q.list().iterator();
			while (iter.hasNext()) {
	    	  dep = (AmpDepartments) iter.next();
	    	}
        } catch (DgException e) {
        	logger.error("Unable to get Amp budget sectors from database "
                    + e.getMessage());
		}
        
		return dep;
	}
	
	/**
	 * 
	 * @return ArrayList of budget sectors
	 */
	public static ArrayList<AmpBudgetSector> getBudgetSectors(){
		ArrayList<AmpBudgetSector> sectors = new ArrayList<AmpBudgetSector>();
		AmpBudgetSector sector = null;
		Session session = null;
        Query q = null;
        
        try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = new String();
			queryString = "select s from " + AmpBudgetSector.class.getName()
			+ " s order by s.sectorname";
			q = session.createQuery(queryString);
			Iterator iter = q.list().iterator();
			while (iter.hasNext()) {
	    	  sector = (AmpBudgetSector) iter.next();
	    	  sectors.add(sector);
			}
        } catch (DgException e) {
        	logger.error("Unable to get Amp budget sectors from database "
                    + e.getMessage());
		}
        Collections.sort(sectors);
		return sectors;
		
	}
	
	/**
	 * 
	 * @param orgid
	 * @return AmpDepartments ArrayList
	 */
	public static Collection<AmpDepartments> getDepartmentsbyOrg(Long orgid){
		ArrayList<AmpDepartments> departments = new ArrayList<AmpDepartments>();
		AmpDepartments dep= null;
		Session session = null;
		Query q = null;
		
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select dep from " + AmpDepartments.class.getName() +
			" dep inner join dep.organisations org"+
			" where (org.ampOrgId=:orgid)";
	        
	        q = session.createQuery(queryString);
	        q.setLong("orgid", orgid);
	    	Iterator iter = q.list().iterator();
	        while (iter.hasNext()) {
				dep = (AmpDepartments) iter.next();
				departments.add(dep);
			}
		} catch (DgException e) {
			e.printStackTrace();
		}
		Collections.sort(departments);
        return departments;
	}
	
	/**
	 * 
	 * @param budgedsectorid
	 * @return organizations ArrayList
	 */
	public static Collection<AmpOrganisation> getOrganizationsBySector(Long budgedsectorid){
		ArrayList<AmpOrganisation> organizations = new ArrayList<AmpOrganisation>();
		AmpOrganisation org= null;
		Session session = null;
		Query q = null;
		
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select org from " + AmpOrganisation.class.getName() +
			" org inner join org.budgetsectors s"+
			" where (s.idsector=:budgedsectorid)";
	        
	        q = session.createQuery(queryString);
	        q.setLong("budgedsectorid", budgedsectorid);
	    	Iterator iter = q.list().iterator();
	        while (iter.hasNext()) {
				org = (AmpOrganisation) iter.next();
				organizations.add(org);
			}
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(organizations);
        return organizations;
	}
	/**
	 * 
	 * @param sector
	 */
	public static void SaveBudgetSector(AmpBudgetSector sector){
		Session session;
		try {
			session = PersistenceManager.getRequestDBSession();
			session.save(sector);
		} catch (DgException e) {
			logger.error("Can not save sector" + e.getMessage());
		};
		
	}
	
	/**
	 * 
	 * @param sector
	 */
	public static void UpdateBudgetSector(AmpBudgetSector sector){
		Session session;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();
				session.update(sector);
			tx.commit();
		} catch (DgException e) {
			logger.error("Can not Update sector" + e.getMessage());
		};
		
	}
	
	/**
	 * 
	 * @param id
	 */
	
	public static void DeleteSector(Long id){
		Session session;
		AmpBudgetSector s= null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			s = (AmpBudgetSector) session.load(AmpBudgetSector.class, id);
			tx = session.beginTransaction();
			session.delete(s);
			tx.commit();
		} catch (DgException e) {
			logger.error("Can not delete sector" + e.getMessage());
		};
	}
	
	/**
	 * 
	 * @return
	 */
	public static ArrayList<AmpTheme> getBudgetPrograms(){
		ArrayList<AmpTheme> programs = new ArrayList<AmpTheme>();
		AmpTheme program= null;
		Session session = null;
		Query q = null;
		
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select p from " + AmpTheme.class.getName() +
			" p where p.parentThemeId IN(select pp.ampThemeId from " + AmpTheme.class.getName()+
			" pp where pp.indlevel=0 and pp.isbudgetprogram=1)";
	        q = session.createQuery(queryString);
	    	Iterator iter = q.list().iterator();
	        while (iter.hasNext()) {
				program = (AmpTheme) iter.next();
				programs.add(program);
			}
		} catch (DgException e) {
			e.printStackTrace();
		}
        return programs;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public static ArrayList<AmpTheme> getBudgetProgramsLevel_0(){
		ArrayList<AmpTheme> programs = new ArrayList<AmpTheme>();
		AmpTheme program= null;
		Session session = null;
		Query q = null;
		
		try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select p from " + AmpTheme.class.getName() +
			" p where p.indlevel=0";
	        q = session.createQuery(queryString);
	    	Iterator iter = q.list().iterator();
	        while (iter.hasNext()) {
				program = (AmpTheme) iter.next();
				programs.add(program);
			}
		} catch (DgException e) {
			e.printStackTrace();
		}
        return programs;
		
	}
	
	/**
	 * 
	 * @param dep
	 */
	public static void saveDepartment(AmpDepartments dep) throws DgException{
		Session session;
                Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
                        tx = session.beginTransaction();
			session.save(dep);
			tx.commit();

		} catch (DgException e) {
			logger.error("Can not save department" + e.getMessage());
                        if(tx!=null) {
				try {
					tx.rollback();					
				}catch(Exception ex ) {
					logger.error("...Rollback failed");
					throw new DgException("Can't rollback", ex);
				}			
			}
                        throw new DgException("Can't rollback", e);
		};
		
	}
	
	/**
	 * 
	 * @param id
	 */
	public static void DeleteDepartment(Long id){
		Session session;
		AmpDepartments s= null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			s = (AmpDepartments) session.load(AmpDepartments.class, id);
			tx = session.beginTransaction();
			session.delete(s);
			tx.commit();
		} catch (DgException e) {
			logger.error("Can not Delete Department" + e.getMessage());
		};
	}
	
	public static void UpdateBudgetDepartment(AmpDepartments dep){
		Session session;
		Transaction tx = null;
		try {
			session = PersistenceManager.getRequestDBSession();
			tx = session.beginTransaction();
				session.update(dep);
			tx.commit();
		} catch (DgException e) {
			logger.error("Can not Update Department" + e.getMessage());
		};
	}	
	
	public static boolean existsDepartment(String name ,String code , Long id){
		Session session = null;
        Query q = null;
        boolean retVal=true;
        try {
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select s from " + AmpDepartments.class.getName() +" s where (s.name=:name or s.code=:code) " ;
			if(id!=null){
				queryString+=" and s.id!=:id" ;
			}
			q = session.createQuery(queryString);
			if(id!=null){
				q.setLong("id", id);
			}			
			q.setParameter("name", name);
			q.setParameter("code", code);
			Collection col = q.list();
			if (col ==null || col.size()==0) {
				retVal = false;
	    	}
        } catch (DgException e) {
        	logger.error("Unable to get Departments from database " + e.getMessage());
		}
        
		return retVal;
	}
}
