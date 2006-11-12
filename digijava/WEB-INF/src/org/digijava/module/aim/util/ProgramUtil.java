/*
 * ProgramUtil.java
 */

package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.dbentity.AmpTheme;

public class ProgramUtil {

	private static Logger logger = Logger.getLogger(ProgramUtil.class);

	public static AmpTheme getTheme(String name) {
		Session session = null;
		AmpTheme theme = null;

		try {
			session = PersistenceManager.getSession();
			String qryStr = "select theme from " + AmpTheme.class.getName()
					+ " theme where (theme.name=:name)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("name", name, Hibernate.STRING);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				theme = (AmpTheme) itr.next();
			}
		} catch (Exception e) {
			logger.error("Exception from getTheme()");
			logger.error(e.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed");
				}
			}
		}
		return theme;
	}

	public static Collection getAllThemes() {
		Session session = null;
		Query qry = null;
		Collection themes = new ArrayList();

		try {
			session = PersistenceManager.getSession();
			String queryString = "select t from " + AmpTheme.class.getName()
					+ " t where t.parentThemeId is null";
			qry = session.createQuery(queryString);
			themes = qry.list();
		} catch (Exception e) {
			logger.error("Unable to get all themes");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return themes;
	}

	public static AmpTheme getTheme(Long ampThemeId) {
		Session session = null;
		Query qry = null;
		AmpTheme ampTheme = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select t from " + AmpTheme.class.getName()
					+ " t where (t.ampThemeId=:ampThemeId)";
			qry = session.createQuery(queryString);
			qry.setParameter("ampThemeId", ampThemeId, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				ampTheme = (AmpTheme) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get theme");
			logger.debug("Exceptiion " + e);
		} finally {
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return ampTheme;
	}
	
	public static Collection getThemeIndicators(Long ampThemeId)
	{
		Session session = null;
		AmpTheme tempAmpTheme = null;
		Collection themeInd = new ArrayList();
		
		try
		{
			session = PersistenceManager.getSession();
			tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,ampThemeId);
			Set themeIndSet = tempAmpTheme.getIndicators();
			logger.info("themeIndSet.size()... : "+themeIndSet.size());
			Iterator itrIndSet = themeIndSet.iterator();
			while(itrIndSet.hasNext())
			{
				AmpMEIndicators tempMEInd = (AmpMEIndicators) itrIndSet.next();
				AmpPrgIndicator tempPrgInd = new AmpPrgIndicator();
				tempPrgInd.setIndicatorId(tempMEInd.getAmpMEIndId());
				tempPrgInd.setCode(tempMEInd.getCode());
				tempPrgInd.setName(tempMEInd.getName());
				tempPrgInd.setCreationDate(tempMEInd.getCreationDate().toString());
				tempPrgInd.setType(tempMEInd.getType());
				themeInd.add(tempPrgInd);
			}
			session.flush();
			session.close();
		}
		catch(Exception ex)
		{
			logger.error("Exception from getThemeIndicators()  " + ex.getMessage());
			ex.printStackTrace(System.out);
		}
		finally
		{
			if (session != null) 
			{
				try 
				{
					PersistenceManager.releaseSession(session);
				} 
				catch (Exception e) 
				{
					logger.error("Release session faliled :" + e);
				}
			}
		}
		return themeInd;
	}
	
	public static Collection getSubThemes(Long parentThemeId)
	{
		Session session = null;
		Query qry = null;
		Collection subThemes = new ArrayList();
		
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select subT from " +AmpTheme.class.getName()
									+ " subT where (subT.parentThemeId=:parentThemeId)";
			qry = session.createQuery(queryString);
			qry.setParameter("parentThemeId",parentThemeId,Hibernate.LONG);
			subThemes = qry.list();
		}
		catch(Exception e)
		{
			logger.error("Unable to get all the Sub-Themes");
			logger.debug("Exception : "+e);
		}
		finally
		{
			try
			{
				if(session != null)
				{
					PersistenceManager.releaseSession(session);
				}
			}
			catch(Exception ex)
			{
				logger.error("releaseSession() failed");
			}
		}
		return subThemes;
	}
}