package org.digijava.module.fundingpledges.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.fundingpledges.dbentity.FundingPledges;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PledgeUtil {

	private static Logger logger = Logger.getLogger(PledgeUtil.class);
	
	public static void savePledge(FundingPledges pledge) throws DgException {
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			session.saveOrUpdate(pledge);
			tx.commit();
		} catch (HibernateException e) {
			logger.error("Error saving pledge",e);
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception ex) {
					throw new DgException("Cannot rallback save pledge action",ex);
				}
				throw new DgException("Cannot save Pledge!",e);
			}
		}
	}
	
	public static AmpOrganisation getOrganizationById(Long id) {
		Session session = null;
		AmpOrganisation ampOrg = null;
		try {
			//session = PersistenceManager.getSession();
			session = PersistenceManager.getRequestDBSession();
			ampOrg = (AmpOrganisation) session.load(AmpOrganisation.class, id);
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					;//PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return ampOrg;
	}
}
