package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;

import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpMECurrValHistory;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.helper.AmpMEIndicatorList;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.DateConversion;

public class MEIndicatorsUtil 
{
	private static Logger logger = Logger.getLogger(MEIndicatorsUtil.class);
	public static Collection getAllIndicators() 
	{
		Session session = null;
		Query qry = null;
		Collection indicators = new ArrayList();
		Collection col;
		AmpMEIndicatorList indList = null;

		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select i from " + AmpMEIndicators.class.getName() + " i";
			qry = session.createQuery(queryString);
			col = qry.list();
			Iterator itr = col.iterator();
			while(itr.hasNext())
			{
				indList = new AmpMEIndicatorList();
				AmpMEIndicators meindicators = (AmpMEIndicators) itr.next();
				indList.setAmpMEIndId(meindicators.getAmpMEIndId());
				indList.setName(meindicators.getName());
				indList.setCode(meindicators.getCode());
				indList.setDescription(meindicators.getDescription());
				indList.setDefaultInd(meindicators.isDefaultInd());
				indList.setIndicatorValues(null);
				indicators.add(indList);
			}
		} catch (Exception e) {
			logger.error("Unable to get indicators");
			logger.debug("Exception : " + e);
		} finally {
			try 
			{
				if (session != null) 
				{
					PersistenceManager.releaseSession(session);
				}
			} 
			catch (Exception ex) 
			{
				logger.debug("releaseSession() FAILED", ex);
			}
		}
		return indicators;
	}
	
	public static Collection getAllDefaultIndicators()
	{
		Session session = null;
		Collection col = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select defInd from "
								+ AmpMEIndicators.class.getName()
								+ " defInd where defInd.defaultInd = 1";
			qry = session.createQuery(queryString);
			col = qry.list();
		}	
		catch(Exception e)
		{
			logger.error("Unable to get non-default indicators");
			logger.debug("Exception : " + e);
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
				logger.debug("releaseSession() FAILED", ex);
			}
		}
		return col;
	}
	
	public static Collection getAllNonDefaultIndicators()
	{
		Session session = null;
		Collection col = null;
		Query qry = null;
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select nondefInd from "
								+ AmpMEIndicators.class.getName()
								+ " nondefInd where nondefInd.defaultInd = 0";
			qry = session.createQuery(queryString);
			col = qry.list();
		}	
		catch(Exception e)
		{
			logger.error("Unable to get non-default indicators");
			logger.debug("Exception : " + e);
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
				logger.debug("releaseSession() FAILED", ex);
			}
		}
		return col;
	}
	
	public static Collection getMeIndValIds(Long meIndicatorId)
	{
		Session session = null;
		Collection col = null;
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select ampMeIndValId from "
				+ AmpMEIndicatorValue.class.getName() 
				+ " ampMeIndValId where (ampMeIndValId.meIndicatorId=:meIndicatorId)";
			Query qry = session.createQuery(queryString);
			qry.setParameter("meIndicatorId",meIndicatorId,Hibernate.LONG);
			col = qry.list();
		}
		catch(Exception exp)
		{
			logger.debug("UNABLE to find meIndicatorValueIds for given meIndicatorId.", exp);			
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch(Exception e)
			{
				logger.debug("releaseSession() FAILED", e);
			}
		}
		return col;
	}

	public static AmpMEIndicators findIndicatorId(String name,String code)
	{
		Session session = null;
		Query qry = null;
		Collection col = null;
		AmpMEIndicators meind = null;
		
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select ind from "
								+ AmpMEIndicators.class.getName() + " ind " 
								+ "where (name like '" + name + "' && code like '" + code + "')";
			qry = session.createQuery(queryString);
			col = qry.list();
			Iterator itr = col.iterator();
			while(itr.hasNext())
				meind = (AmpMEIndicators) itr.next();
		}
		catch(Exception e)
		{
			logger.debug("UNABLE to find Indicators with the name,code.", e);
		}
		finally
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			} 
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() FAILED", ex2);
			}
		}
		return meind;
	}
	
	public static boolean checkDuplicateNameCode(String name,String code)
	{
		Session session = null;
		Query qry = null;
		boolean duplicatesExist = false;
		
		try
		{
			session = PersistenceManager.getSession();
			String queryString = "select count(*) from "
								+ AmpMEIndicators.class.getName() + " meind " 
								+ "where name like '" + name + "'"
								+ "or code like '" + code + "'" ;
			qry = session.createQuery(queryString);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				Integer cnt = (Integer) itr.next();
				if (cnt.intValue() > 0)
					duplicatesExist = true;
			}
			
		}
		catch (Exception ex) 
		{
			logger.debug("UNABLE to find Indicators with duplicate name.", ex);
		} 
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			} 
			catch (Exception ex2) 
			{
				logger.debug("releaseSession() FAILED", ex2);
			}
		}
		return duplicatesExist;
	}

	public static Collection getActivityList()
	{
		Session session = null;
		Collection col = null;

		try
		{
			session = PersistenceManager.getSession();
	
			String queryString = "select ampActivityId from "
								+ AmpActivity.class.getName() + " ampActivityId";
			Query qry = session.createQuery(queryString);
			col = qry.list();
		}
		catch(Exception ex)
		{
			logger.debug("UNABLE to find activity ids from AmpActivity.");
		}
		finally
		{
			try
			{
				PersistenceManager.releaseSession(session);
			}
			catch(Exception exp)
			{
				logger.debug("releaseSession() FAILED", exp);
			}
		}
		return col;
	}
	
	public static Collection searchForIndicators(String keyword) 
	{
		Session session = null;
		Collection col = null;
		Query qry = null;
		
		try 
		{
			session = PersistenceManager.getSession();
			
			String queryString = "select ind from "
					+ AmpMEIndicators.class.getName() + " ind "
					+ "where (name like '%" + keyword + "%' && default_ind = 0)";
			
			qry = session.createQuery(queryString);
			col = qry.list();
		} 
		catch (Exception ex)
		{
			logger.debug("Unable to SEARCH for Indicators", ex);
		} 
		finally 
		{
			try 
			{
				PersistenceManager.releaseSession(session);
			} catch (Exception ex2) {
				logger.debug("releaseSession() failed", ex2);
			}
		}
		return col;
	}
	
	public static Collection getActivityIndicatorsList(Long actId) 
	{
		Session session = null;
		Collection col = new ArrayList();
		try 
		{
			session = PersistenceManager.getSession();
			String qryStr = "select indVal from " + AmpMEIndicatorValue.class.getName() + "" +
					" indVal where (indVal.activityId=:actId)" ;
			Query qry = session.createQuery(qryStr);
			qry.setParameter("actId",actId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) 
			{
				AmpMEIndicatorValue meIndValue = (AmpMEIndicatorValue) itr.next();
				ActivityIndicator actInd = new ActivityIndicator();
				actInd.setIndicatorId(meIndValue.getMeIndicatorId().getAmpMEIndId());
				col.add(actInd);
			}
		} 
		catch (Exception e) 
		{
			logger.error("Exception from getActivityIndicatorsList() :" + e.getMessage());
			e.printStackTrace(System.out);
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
		return col;
	}

	public static Collection getActivityIndicators(Long actId) 
	{
		Session session = null;
		Collection col = new ArrayList();
	
		try {
			session = PersistenceManager.getSession();
			String qryStr = "select indVal from " + AmpMEIndicatorValue.class.getName() + "" +
					" indVal where (indVal.activityId=:actId)" ;
			Query qry = session.createQuery(qryStr);
			qry.setParameter("actId",actId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				AmpMEIndicatorValue meIndValue = (AmpMEIndicatorValue) itr.next();
				ActivityIndicator actInd = new ActivityIndicator();
				actInd.setIndicatorName(meIndValue.getMeIndicatorId().getName());
				actInd.setIndicatorCode(meIndValue.getMeIndicatorId().getCode());
				actInd.setBaseVal(meIndValue.getBaseVal());
				if (meIndValue.getBaseValDate() != null) {
					actInd.setBaseValDate(DateConversion.
							ConvertDateToString(meIndValue.getBaseValDate()));					
				}
				actInd.setIndicatorId(meIndValue.getMeIndicatorId().getAmpMEIndId());
				actInd.setIndicatorValId(meIndValue.getAmpMeIndValId());
				actInd.setRevTargetVal(meIndValue.getRevisedTargetVal());
				if (meIndValue.getRevisedTargetValDate() != null) {
					actInd.setRevTargetValDate(DateConversion.
							ConvertDateToString(meIndValue.getRevisedTargetValDate()));
				}
				actInd.setTargetVal(meIndValue.getTargetVal());
				if (meIndValue.getTargetValDate() != null) {
					actInd.setTargetValDate(DateConversion.
							ConvertDateToString(meIndValue.getTargetValDate()));					
				}
				col.add(actInd);
			}
			
		} catch (Exception e) {
			logger.error("Exception from getActivityIndicators() :" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}
		}
		return col;
	}
	
	public static void saveMEIndicatorValues(ActivityIndicator actInd) 
	{
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getSession();
			AmpMEIndicatorValue meIndVal = (AmpMEIndicatorValue) session.load(
					AmpMEIndicatorValue.class,actInd.getIndicatorValId());
			meIndVal.setBaseVal(actInd.getBaseVal());
			meIndVal.setTargetVal(actInd.getTargetVal());
			meIndVal.setRevisedTargetVal(actInd.getRevTargetVal());
			meIndVal.setBaseValDate(DateConversion.getDate(actInd.getBaseValDate()));
			meIndVal.setTargetValDate(DateConversion.getDate(actInd.getTargetValDate()));
			meIndVal.setRevisedTargetValDate(DateConversion.getDate(actInd.getRevTargetValDate()));
			tx = session.beginTransaction();
			session.update(meIndVal);
			tx.commit();
			
		} catch (Exception e) {
			logger.error("Exception from saveMEIndicatorValues() :" + e.getMessage());
			e.printStackTrace(System.out);		
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception trbf) {
					logger.error("Transaction roll back failed ");
					e.printStackTrace(System.out);
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}			
		}
	}
	
	public static void deleteMEIndicatorValues(Long indValId) {
		Session session = null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getSession();
			AmpMEIndicatorValue meIndVal = (AmpMEIndicatorValue) session.load(
					AmpMEIndicatorValue.class,indValId);
			
			String qryStr = "select meCh from " + AmpMECurrValHistory.class.getName() + "" +
					" meCh where (meCh.meIndValue=:indVal)";
			Query qry = session.createQuery(qryStr);
			qry.setParameter("indVal",indValId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			tx = session.beginTransaction();
			while (itr.hasNext()) {
				AmpMECurrValHistory currValHist = (AmpMECurrValHistory) itr.next();
				session.delete(currValHist);
			}
			session.delete(meIndVal);
			tx.commit();
			
		} catch (Exception e) {
			logger.error("Exception from saveMEIndicatorValues() :" + e.getMessage());
			e.printStackTrace(System.out);		
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception trbf) {
					logger.error("Transaction roll back failed ");
					e.printStackTrace(System.out);
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Failed to release session :" + rsf.getMessage());
				}
			}			
		}
	}	
}
