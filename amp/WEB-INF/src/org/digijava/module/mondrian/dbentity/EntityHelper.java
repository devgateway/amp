package org.digijava.module.mondrian.dbentity;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.mondrian.action.MainReports;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class EntityHelper {
	private static Logger logger = Logger.getLogger(EntityHelper.class);

	/**
	 * Save a off line report into the table
	 * @param report
	 */
	public static void SaveReport(OffLineReports report) {
		Session session;
		try {
			session = PersistenceManager.getRequestDBSession();
			session.save(report);
//session.flush();
		} catch (DgException e) {
			logger.error(e);
		}

	}
	
	/**
	 * 
	 * @param report
	 */
	public static void UpdateReport (OffLineReports report) {
		Session session;
		try {
			session = PersistenceManager.getSession();
			session.update(report);
//session.flush();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param reportid
	 * @return
	 */
	public static OffLineReports LoadReport(Long reportid) {
		Session session;
		try {
			session = PersistenceManager.getSession();
			OffLineReports report  = (OffLineReports) session.get(OffLineReports.class, reportid);
			return report;
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * Delete a specific report
	 * @param report
	 */
	public static void DeleteReport(OffLineReports report) {
		Session session;
		try {
			session = PersistenceManager.getSession();
			session.delete(report);
//session.flush();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param TeamMember
	 * @return List
	 */
	public static List<OffLineReports> getReports(AmpTeamMember tm) {
		String queryString = null;
		Query qry = null;
		List<OffLineReports> result = null;
		try {
			Session session = PersistenceManager.getSession();
			queryString = " select r from " + OffLineReports.class.getName() + " r where r.ownerId=:powner or r.ownerId is NULL";
			qry = session.createQuery(queryString);
			qry.setParameter("powner",tm);
			result = qry.list();
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 
	 * @param TeamMember
	 * @return List
	 */
	public static List<OffLineReports> getPublicReports() {
		String queryString = null;
		Query qry = null;
		List<OffLineReports> result = null;

		try {
			Session session = PersistenceManager.getSession();
			queryString = " select r from "
					+ OffLineReports.class.getName() + " r where r.publicreport=true ";
			qry = session.createQuery(queryString);
			result = qry.list();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	public static boolean isDuplicated(String name) {
		String queryString = null;
		Query qry = null;
		List<OffLineReports> result = null;
		try {
			Session session = PersistenceManager.getSession();
			queryString = " select r from " + OffLineReports.class.getName() + " r where r.name=:pname";
			qry = session.createQuery(queryString);
			qry.setParameter("pname",name);
			result = qry.list();
			if (!result.isEmpty()){
				return false;
			}
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return true;
	}
}
