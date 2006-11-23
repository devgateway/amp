/*
 * ProgramUtil.java
 */

package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.AllPrgIndicators;
import org.digijava.module.aim.helper.AllThemes;
import org.digijava.module.aim.helper.AmpPrgIndicator;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.dbentity.AmpThemeIndicators;
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

	public static Collection getParentThemes() {
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

	public static Collection getAllThemeIndicators()
	{
		Session session = null;
		Query qry = null;
		Collection allTheme = new ArrayList();
		
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select tInd from " 
								+ AmpTheme.class.getName() + " tInd";
			qry = session.createQuery(queryString);
			Iterator themeItr = qry.list().iterator();
			while(themeItr.hasNext())
			{
				AmpTheme tempTheme = (AmpTheme) themeItr.next();
				AllThemes tempAllThemes = new AllThemes();
				tempAllThemes.setProgramId(tempTheme.getAmpThemeId());
				tempAllThemes.setProgramName(tempTheme.getName());
				Collection allInds = new ArrayList();
				Set tempSet = tempTheme.getIndicators();
				if(tempSet != null)
				{
					Iterator tempSetItr = tempSet.iterator();
					while(tempSetItr.hasNext())
					{
						AmpThemeIndicators tempThmInd = (AmpThemeIndicators) tempSetItr.next();
						AllPrgIndicators prgInd = new AllPrgIndicators();
						prgInd.setIndicatorId(tempThmInd.getAmpThemeIndId());
						prgInd.setName(tempThmInd.getName());
						prgInd.setCode(tempThmInd.getCode());
						prgInd.setType(tempThmInd.getType());
						prgInd.setCreationDate(DateConversion.ConvertDateToString(tempThmInd.getCreationDate()));
						prgInd.setValueType(tempThmInd.getValueType());
						prgInd.setCategory(tempThmInd.getCategory());
						prgInd.setNpIndicator(tempThmInd.isNpIndicator());
						allInds.add(prgInd);
					}
					tempAllThemes.setAllPrgIndicators(allInds);
				}
				else
				{
					tempAllThemes.setAllPrgIndicators(null);
				}
				allTheme.add(tempAllThemes);
			}
		}
		catch(Exception ex)
		{
			logger.error("Unable to get all Indicators of Themes");
			logger.debug("Exception " + ex);
		}
		finally
		{
			try 
			{
				if (session != null) 
				{
					PersistenceManager.releaseSession(session);
				}
			} 
			catch (Exception ex) 
			{
				logger.error("releaseSession() failed");
			}
		}
		return allTheme;
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
			Iterator itrIndSet = themeIndSet.iterator();
			while(itrIndSet.hasNext())
			{
				AmpThemeIndicators tempThemeInd = (AmpThemeIndicators) itrIndSet.next();
				AmpPrgIndicator tempPrgInd = new AmpPrgIndicator();
				tempPrgInd.setIndicatorId(tempThemeInd.getAmpThemeIndId());
				tempPrgInd.setName(tempThemeInd.getName());
				tempPrgInd.setCode(tempThemeInd.getCode());
				tempPrgInd.setCreationDate(DateConversion.ConvertDateToString(tempThemeInd.getCreationDate()));
				tempPrgInd.setType(tempThemeInd.getType());
				themeInd.add(tempPrgInd);
			}
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
	
	public static void saveThemeIndicators(AmpPrgIndicator tempPrgInd, Long ampThemeId)
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = PersistenceManager.getSession();
			AmpTheme tempAmpTheme = null;
			tempAmpTheme = (AmpTheme) session.load(AmpTheme.class,ampThemeId);
			AmpThemeIndicators ampThemeInd = new AmpThemeIndicators();
			ampThemeInd.setName(tempPrgInd.getName());
			ampThemeInd.setCode(tempPrgInd.getCode());
			ampThemeInd.setType(tempPrgInd.getType());
			ampThemeInd.setCreationDate(DateConversion.getDate(tempPrgInd.getCreationDate()));
			ampThemeInd.setValueType(tempPrgInd.getValueType());
			ampThemeInd.setCategory(tempPrgInd.getCategory());
			ampThemeInd.setNpIndicator(tempPrgInd.isNpIndicator());
			ampThemeInd.setDescription(tempPrgInd.getDescription());
			Set ampThemeSet = new HashSet();
			ampThemeSet.add(tempAmpTheme);
			ampThemeInd.setThemes(ampThemeSet);
			tx = session.beginTransaction();
			session.save(ampThemeInd);
			tempAmpTheme.getIndicators().add(ampThemeInd);
			session.saveOrUpdate(tempAmpTheme);
			tx.commit();
		}
		catch(Exception ex)
		{
			logger.error("Exception from saveThemeIndicators() : " + ex.getMessage());
			ex.printStackTrace(System.out);		
			if (tx != null) 
			{
				try 
				{
					tx.rollback();
				} 
				catch (Exception trbf) 
				{
					logger.error("Transaction roll back failed : "+trbf.getMessage());
					trbf.printStackTrace(System.out);
				}
			}
		}
		finally
		{
			if (session != null) 
			{
				try 
				{
					PersistenceManager.releaseSession(session);
				} 
				catch (Exception rsf) 
				{
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
	}
	
	public static void deleteTheme(Long themeId)
	{
		Collection colTheme = getRelatedThemes(themeId);
		Iterator colThemeItr = colTheme.iterator();
		while(colThemeItr.hasNext())
		{
			AmpTheme tempTheme = (AmpTheme) colThemeItr.next();
			DbUtil.delete(tempTheme);
		}
	}

	public static AllPrgIndicators getThemeIndicator(Long indId)
	{
		Session session = null;
		AllPrgIndicators tempPrgInd = new AllPrgIndicators();
		
		try
		{
			session = PersistenceManager.getSession();
			AmpThemeIndicators tempInd = (AmpThemeIndicators) session.load(AmpThemeIndicators.class,indId);
			tempPrgInd.setIndicatorId(tempInd.getAmpThemeIndId());
			tempPrgInd.setName(tempInd.getName());
			tempPrgInd.setCode(tempInd.getCode());
			tempPrgInd.setType(tempInd.getType());
			tempPrgInd.setCreationDate(DateConversion.ConvertDateToString(tempInd.getCreationDate()));
			tempPrgInd.setValueType(tempInd.getValueType());
			tempPrgInd.setCategory(tempInd.getCategory());
			tempPrgInd.setNpIndicator(tempInd.isNpIndicator());
			session.flush();
		}
		catch(Exception e)
		{
			logger.error("Unable to get the specified Indicator");
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
		return tempPrgInd;
	}
	
	public static void saveIndicator(AllPrgIndicators allPrgInd)
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = PersistenceManager.getSession();
			AmpThemeIndicators tempThemeInd = null;
			tempThemeInd = (AmpThemeIndicators) session.load(AmpThemeIndicators.class,allPrgInd.getIndicatorId());
			tempThemeInd.setName(allPrgInd.getName());
			tempThemeInd.setCode(allPrgInd.getCode());
			tempThemeInd.setType(allPrgInd.getType());
			tempThemeInd.setCreationDate(DateConversion.getDate(allPrgInd.getCreationDate()));
			tempThemeInd.setValueType(allPrgInd.getValueType());
			tempThemeInd.setCategory(allPrgInd.getCategory());
			tempThemeInd.setNpIndicator(allPrgInd.isNpIndicator());
			tx = session.beginTransaction();
			session.saveOrUpdate(tempThemeInd);
			tx.commit();
		}
		catch(Exception ex)
		{
			logger.error("Exception from saveIndicator() : " + ex.getMessage());
			ex.printStackTrace(System.out);		
			if (tx != null) 
			{
				try 
				{
					tx.rollback();
				} 
				catch (Exception trbf) 
				{
					logger.error("Transaction roll back failed : "+trbf.getMessage());
					trbf.printStackTrace(System.out);
				}
			}
		}
		finally
		{
			if (session != null) 
			{
				try 
				{
					PersistenceManager.releaseSession(session);
				} 
				catch (Exception rsf) 
				{
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
	}
	
	public static void deletePrgIndicator(Long indId)
	{
		Session session = null;
		Transaction tx = null;
		try
		{
			session = PersistenceManager.getSession();
			AmpThemeIndicators tempThemeInd = null;
			tempThemeInd = (AmpThemeIndicators) session.load(AmpThemeIndicators.class,indId);
			tx = session.beginTransaction();
			session.delete(tempThemeInd);
			tx.commit();
		}
		catch(Exception ex)
		{
			logger.error("Exception from deletePrgIndicator() : " + ex.getMessage());
			ex.printStackTrace(System.out);		
			if (tx != null) 
			{
				try 
				{
					tx.rollback();
				} 
				catch (Exception trbf) 
				{
					logger.error("Transaction roll back failed : "+trbf.getMessage());
					trbf.printStackTrace(System.out);
				}
			}
		}
		finally
		{
			if (session != null) 
			{
				try 
				{
					PersistenceManager.releaseSession(session);
				} 
				catch (Exception rsf) 
				{
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
	}
	
	static Collection tempPrg = new ArrayList();
	
	public static Collection getRelatedThemes(Long id)
	{
		AmpTheme ampTheme = new AmpTheme();
		ampTheme = ProgramUtil.getTheme(id);
		Collection themeCol = ProgramUtil.getSubThemes(id);
		tempPrg.add(ampTheme);
		if(!themeCol.isEmpty())
		{
			Iterator itr = themeCol.iterator();
			AmpTheme tempTheme = new AmpTheme();
			while(itr.hasNext())
			{
				tempTheme = (AmpTheme) itr.next();
				getRelatedThemes(tempTheme.getAmpThemeId());
			}
		}
		return tempPrg;
	}
}