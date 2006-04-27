package org.digijava.module.aim.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;

import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpMEIndicators;
import org.digijava.module.aim.dbentity.AmpMEIndicatorValue;
import org.digijava.module.aim.dbentity.AmpMECurrValHistory;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.helper.AmpMEIndicatorList;
import org.digijava.module.aim.helper.ActivityIndicator;
import org.digijava.module.aim.helper.AmpMEIndicatorList;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.aim.helper.MEIndicatorRisk;
import org.digijava.module.aim.helper.MEIndicatorValue;

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
		Collection col = new ArrayList();
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

	public static Collection searchForIndicators(String keyword) 
	{
		logger.info("Searching for indicators..........");
		Session session = null;
		Collection col = null;
		Query qry = null;
		
		try 
		{
			session = PersistenceManager.getSession();
			
			String queryString = "select ind from "
					+ AmpMEIndicators.class.getName() + " ind "
					+ "where (ind.name like '%" + keyword + "%' && ind.defaultInd=false)";
			
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
	
	public static Collection getPortfolioMEIndicatorValues(Collection actIds,
			Long indId) {
		
		Session session = null;
		Collection col = new ArrayList();
	
		try {
			session = PersistenceManager.getSession();
			String qryStr = null;
			Iterator itr = null;
			
			if (actIds != null && actIds.size() > 0) {
				if (actIds.size() == 1) {
					itr = actIds.iterator();
					Long actId = (Long) itr.next();
					if (indId.longValue() > 0) {
						qryStr = "select iv.base_val,iv.target_val,iv.revised_target_val," +
								"mi.name from amp_me_indicator_value iv inner join " +
								"amp_me_indicators mi on (iv.me_indicator_id=mi.amp_me_indicator_id)" +
								" where mi.default_ind = 1  and iv.activity_id=" + actId + " and " +
								"iv.me_indicator_id=" + indId + " order by iv.activity_id,mi.name";
					} else {
						qryStr = "select iv.base_val,iv.target_val,iv.revised_target_val,mi.name " +
								"from amp_me_indicator_value iv inner join amp_me_indicators mi " +
								"on (iv.me_indicator_id=mi.amp_me_indicator_id) where mi.default_ind = 1 " +
								"and iv.activity_id=" + actId + " order by iv.activity_id,mi.name";
					}
				} else {
					itr = actIds.iterator();
					String params = "";
					while (itr.hasNext()) {
						Long actId = (Long) itr.next();
						if (params.length() > 0) params += ",";
						params += actId;
					}
					if (indId.longValue() > 0) {
						qryStr = "select iv.base_val,iv.target_val,iv.revised_target_val,a.name from " +
								"amp_me_indicator_value iv inner join amp_me_indicators mi on " +
								"(iv.me_indicator_id=mi.amp_me_indicator_id) inner join amp_activity a on " +
								"(a.amp_activity_id=iv.activity_id) where mi.default_ind = 1  and " +
								"iv.activity_id in (" + params + ") and iv.me_indicator_id=" + indId + "" +
										" order by a.name";
					} else {
						qryStr = "select sum(iv.base_val),sum(iv.target_val),sum(iv.revised_target_val)," +
								"mi.name from amp_me_indicator_value iv inner join amp_me_indicators mi on" +
								" (iv.me_indicator_id=mi.amp_me_indicator_id) where mi.default_ind = 1 and " +
								"iv.activity_id in ( " + params + ") group by iv.me_indicator_id " +
										"order by iv.activity_id";
					}					
				}
				
				Connection con = session.connection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(qryStr);
				while (rs.next()) {
					double baseVal = rs.getDouble(1);
					double tarVal = rs.getDouble(2);
					double actVal = rs.getDouble(3);
					String key = rs.getString(4);
					
					double totIndVal = baseVal + tarVal + actVal;
					
					MEIndicatorValue baseIndVal = new MEIndicatorValue();
					baseIndVal.setIndicatorName(key);
					baseIndVal.setType(Constants.ME_IND_VAL_BASE_ID);
					if (totIndVal > 0) { 
						baseIndVal.setValue(baseVal * (100 / totIndVal));
					} else { 
						baseIndVal.setValue(0);
					}
					
					MEIndicatorValue targetIndVal = new MEIndicatorValue();
					targetIndVal.setIndicatorName(key);
					targetIndVal.setType(Constants.ME_IND_VAL_TARGET_ID);
					if (totIndVal > 0) { 
						targetIndVal.setValue(tarVal * (100 / totIndVal));
					} else { 
						targetIndVal.setValue(0);
					}
					
					MEIndicatorValue actIndVal = new MEIndicatorValue();
					actIndVal.setIndicatorName(key);
					actIndVal.setType(Constants.ME_IND_VAL_ACTUAL_ID);
					if (totIndVal > 0) {
						actIndVal.setValue(actVal * (100 / totIndVal));	
					} else {
						actIndVal.setValue(0);
					}
					
					col.add(baseIndVal);
					col.add(targetIndVal);
					col.add(actIndVal);
				}
			}
		} catch (Exception e) {
			logger.error("Exception from getPortfolioMEIndicatorValues() :" + e.getMessage());
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
	
	public static Collection getMEIndicatorValues(Long actId) {
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
				AmpMEIndicators meInd = meIndValue.getMeIndicatorId();
				
				double totIndVal = meIndValue.getBaseVal() + meIndValue.getTargetVal() + 
						meIndValue.getRevisedTargetVal();
				
				MEIndicatorValue baseIndVal = new MEIndicatorValue();
				baseIndVal.setIndicatorName(meInd.getName());
				baseIndVal.setType(Constants.ME_IND_VAL_BASE_ID);
				if (totIndVal > 0) { 
					baseIndVal.setValue(meIndValue.getBaseVal() * (100 / totIndVal));
				} else { 
					baseIndVal.setValue(0);
				}
				
				MEIndicatorValue targetIndVal = new MEIndicatorValue();
				targetIndVal.setIndicatorName(meInd.getName());
				targetIndVal.setType(Constants.ME_IND_VAL_TARGET_ID);
				if (totIndVal > 0) { 
					targetIndVal.setValue(meIndValue.getTargetVal() * (100 / totIndVal));
				} else { 
					targetIndVal.setValue(0);
				}
				
				MEIndicatorValue actIndVal = new MEIndicatorValue();
				actIndVal.setIndicatorName(meInd.getName());
				actIndVal.setType(Constants.ME_IND_VAL_ACTUAL_ID);
				if (totIndVal > 0) {
					actIndVal.setValue(meIndValue.getRevisedTargetVal() * (100 / totIndVal));	
				} else {
					actIndVal.setValue(0);
				}
				
				col.add(baseIndVal);
				col.add(targetIndVal);
				col.add(actIndVal);
			}
			
		} catch (Exception e) {
			logger.error("Exception from getMEIndicatorValues() :" + e.getMessage());
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
	
	public static Collection getPortfolioMEIndicatorRisks(Collection actIds) {
		Session session = null;
		Collection col = new ArrayList();
	
		try {
			session = PersistenceManager.getSession();
			String qryStr = null;
			Iterator itr = null;
			
			if (actIds != null && actIds.size() > 0) {
				itr = actIds.iterator();
				String params = "";
				while (itr.hasNext()) {
					Long id = (Long) itr.next();
					if (params.length() > 0) params += ",";
					params += id;
				}
					
				qryStr = "select count(*), r.rating_name from amp_me_indicator_value v " +
						"inner join amp_indicator_risk_ratings r on (r.amp_ind_risk_ratings_id=v.risk)" +
						" where v.activity_id in (" + params + ") group by v.risk";
				
				Connection con = session.connection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(qryStr);
				while (rs.next()) {
					int cnt = rs.getInt(1);
					String rName = rs.getString(2);

					MEIndicatorRisk meRisk = new MEIndicatorRisk();
					meRisk.setRisk(rName);
					meRisk.setRiskCount(cnt);
					col.add(meRisk);
				}
			}
		} catch (Exception e) {
			logger.error("Exception from getPortfolioMEIndicatorRisk() :" + e.getMessage());
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
	
	public static Collection getMEIndicatorRisks(Long actId) {
		Session session = null;
		Collection col = new ArrayList();
	
		try {
			session = PersistenceManager.getSession();
			String qryStr = "select count(*), indVal.risk from " + AmpMEIndicatorValue.class.getName() + "" +
					" indVal where (indVal.activityId=:actId)" +
							" group by indVal.risk";

			Query qry = session.createQuery(qryStr);

			qry.setParameter("actId",actId,Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				Object[] obj = (Object[]) itr.next();
				Integer riskCount = (Integer) obj[0];
				AmpIndicatorRiskRatings riskRating = (AmpIndicatorRiskRatings) obj[1];
				MEIndicatorRisk meRisk = new MEIndicatorRisk();
				meRisk.setRisk(riskRating.getRatingName());
				meRisk.setRiskCount(riskCount.intValue());
				col.add(meRisk);
			}
			
		} catch (Exception e) {
			logger.error("Exception from getMEIndicatorRisks() :" + e.getMessage());
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
	
	public static void saveMEIndicator(AmpMEIndicators newIndicator,Long actId,boolean defaultIndicator) {
		Session session = null;
		Transaction tx = null;
		
		try {
			session = PersistenceManager.getSession();

			tx = session.beginTransaction();
			session.save(newIndicator);
			if (defaultIndicator) {
				String qryStr = "select act from " + AmpActivity.class.getName() + " act";
				Query qry = session.createQuery(qryStr);
				Iterator itr = qry.list().iterator();
				while (itr.hasNext()) {
					AmpActivity act = (AmpActivity) itr.next();
					AmpMEIndicatorValue ampMEIndValnew = new AmpMEIndicatorValue();
					ampMEIndValnew.setActivityId(act);
					ampMEIndValnew.setMeIndicatorId(newIndicator);
					ampMEIndValnew.setBaseVal(0);
					ampMEIndValnew.setTargetVal(0);
					ampMEIndValnew.setRevisedTargetVal(0);
					ampMEIndValnew.setBaseValDate(null);
					ampMEIndValnew.setTargetValDate(null);
					ampMEIndValnew.setRevisedTargetValDate(null);
					ampMEIndValnew.setRisk(null);
					ampMEIndValnew.setComments(null);
					session.save(ampMEIndValnew);
				}
			} else {
				AmpActivity act = (AmpActivity) session.load(AmpActivity.class,actId);
				AmpMEIndicatorValue ampMEIndValnew = new AmpMEIndicatorValue();
				ampMEIndValnew.setActivityId(act);
				ampMEIndValnew.setMeIndicatorId(newIndicator);
				ampMEIndValnew.setBaseVal(0);
				ampMEIndValnew.setTargetVal(0);
				ampMEIndValnew.setRevisedTargetVal(0);
				ampMEIndValnew.setBaseValDate(null);
				ampMEIndValnew.setTargetValDate(null);
				ampMEIndValnew.setRevisedTargetValDate(null);
				ampMEIndValnew.setRisk(null);
				ampMEIndValnew.setComments(null);
				session.save(ampMEIndValnew);
			}
			tx.commit();							
		} catch (Exception e) {
			logger.error("Exception from saveMEIndicator() :" + e.getMessage());
			e.printStackTrace(System.out);
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Roll back failed");
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
