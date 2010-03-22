package org.digijava.module.fundingpledges.dbentity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.fundingpledges.form.PledgeForm;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class PledgesEntityHelper {
	private static Logger logger = Logger.getLogger(PledgesEntityHelper.class);
	public static ArrayList<FundingPledges> getPledges(){
		 Session session = null;
	        Query q = null;
	        FundingPledges pledge = new FundingPledges();
	        ArrayList<FundingPledges> AllPledges = new ArrayList<FundingPledges>();
	        List list = null;
	        try {
	            session = PersistenceManager.getRequestDBSession();
	            String queryString = new String();
	            queryString = " select a from " + FundingPledges.class.getName() + " a ";
	            q = session.createQuery(queryString);
	            Iterator iter = q.list().iterator();
	            while (iter.hasNext()) {
	            	pledge = (FundingPledges) iter.next();
	            	AllPledges.add(pledge);
	            }

	        } catch (Exception ex) {
	        	logger.debug("Projects : Unable to get Pledges names from database" + ex.getMessage());
	        }
	        return AllPledges;
	}
	
	public static FundingPledges getPledgesById(Long id){
		Session session = null;
		Query qry = null;
		FundingPledges pledge = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select p from " + FundingPledges.class.getName()
					+ " p where (p.id=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", id, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				pledge = (FundingPledges) itr.next();
			}
		} catch (Exception e) {
			logger.error("Unable to get pledge");
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
		return pledge;
	}
	
	public static Collection<FundingPledgesDetails> getPledgesDetails(Long pledgeid){
		Session session = null;
		Query qry = null;
		Collection<FundingPledgesDetails> fundingpledgesdetails = null;
		FundingPledgesDetails fd = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select d from " + FundingPledgesDetails.class.getName()
					+ " d where (d.pledgeid=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", pledgeid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				fd = (FundingPledgesDetails) itr.next();
				fundingpledgesdetails.add(fd);
			}
		} catch (Exception e) {
			logger.error("Unable to get pledge");
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
		return fundingpledgesdetails;
	}
	
	public static Collection<FundingPledgesLocation> getPledgesLocations(Long pledgeid){
		Session session = null;
		Query qry = null;
		Collection<FundingPledgesLocation> fundingpledgeloc = null;
		FundingPledgesLocation pl = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select l from " + FundingPledgesLocation.class.getName()
					+ " l where (l.pledgeid=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", pledgeid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				pl = (FundingPledgesLocation) itr.next();
				fundingpledgeloc.add(pl);
			}
		} catch (Exception e) {
			logger.error("Unable to get pledge");
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
		return fundingpledgeloc;
	}
	public static Collection<FundingPledgesSector> getPledgesSectors(Long pledgeid){
		Session session = null;
		Query qry = null;
		Collection<FundingPledgesSector> FundingPledgesSector = null;
		FundingPledgesSector fs = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select s from " + FundingPledgesSector.class.getName()
					+ " s where (s.pledgeid=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", pledgeid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			if (itr.hasNext()) {
				fs = (FundingPledgesSector) itr.next();
				FundingPledgesSector.add(fs);
			}
		} catch (Exception e) {
			logger.error("Unable to get pledge");
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
		return FundingPledgesSector;
	}
	public static void savePledge(FundingPledges pledge, Set<FundingPledgesSector> sectors,PledgeForm plf) throws DgException {
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			session.save(pledge);
			for (Iterator iterator = sectors.iterator(); iterator.hasNext();) {
				FundingPledgesSector fundingPledgesSector = (FundingPledgesSector) iterator
						.next();
				fundingPledgesSector.setPledgeid(pledge);
				session.save(fundingPledgesSector);
			}
			for (Iterator iterator = plf.getFundingPledgesDetails().iterator(); iterator.hasNext();) {
				FundingPledgesDetails FundingPledgesDetails = (FundingPledgesDetails) iterator.next();
				FundingPledgesDetails.setPledgeid(pledge);
				session.save(FundingPledgesDetails);
			}
			for (Iterator iterator = plf.getSelectedLocs().iterator(); iterator.hasNext();) {
				FundingPledgesLocation FundingPledgesloc = (FundingPledgesLocation) iterator.next();
				FundingPledgesloc.setPledgeid(pledge);
				session.save(FundingPledgesloc);
			}
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
