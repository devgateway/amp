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

import org.digijava.module.aim.dbentity.AmpMECurrValHistory;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.AmpMEIndicatorList;
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
			try {
				if (session != null) {
					PersistenceManager.releaseSession(session);
				}
			} catch (Exception ex) {
				logger.error("releaseSession() failed");
			}
		}
		return indicators;
	}
	
	public static Collection searchForIndicators(String keyword) 
	{
		Session session = null;
		Collection col = null;

		try 
		{
			session = PersistenceManager.getSession();
			String queryString = "select distinct ind from "
					+ AmpMEIndicators.class.getName() + " ind "
					+ "where name like '%"	+ keyword + "%'";
			Query qry = session.createQuery(queryString);
			col = qry.list();
			Iterator itr = col.iterator();
			while(itr.hasNext())
			{
				logger.info("dbutil search list :"+itr.next().toString());
			}
		} 
		catch (Exception ex) 
		{
			logger.debug("Unable to search " + ex);
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
	
	public static Collection getActivityIndicators(Long actId) {
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
	
	public static void saveMEIndicatorValues(ActivityIndicator actInd) {
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
