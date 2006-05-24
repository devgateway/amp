package org.digijava.module.aim.util;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFeature;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

public class FeaturesUtil {
	
	private static Logger logger = Logger.getLogger(FeaturesUtil.class);
	
	public static Collection getAMPFeatures() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + AmpFeature.class.getName() + " f";
			qry = session.createQuery(qryStr);
			col = qry.list();
				
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return col;
	}	
	
	/**
	 * Used to get the features which are currently active for AMP 
	 * @return The collection of org.digijava.module.aim.dbentity.AmpFeature objects
	 */
	public static Collection getActiveFeatures() {
		Session session = null;
		Collection col = new ArrayList();
		String qryStr = null;
		Query qry = null;
		
		try {
			session = PersistenceManager.getSession();
			qryStr = "select f from " + AmpFeature.class.getName() + " f" +
					" where f.active = true";
			qry = session.createQuery(qryStr);
			col = qry.list();
				
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return col;
	}
	
	public static AmpFeature toggleFeature(Integer featureId) {
		Session session = null;
		Transaction tx = null;
		AmpFeature feature = null;
		
		try {
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();
			feature = (AmpFeature) session.load(AmpFeature.class,
					featureId);
			feature.setActive(!feature.isActive());
			session.update(feature);
			tx.commit();
		} catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("Transaction rollback failed :" + rbf.getMessage());
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return feature;
	}
}