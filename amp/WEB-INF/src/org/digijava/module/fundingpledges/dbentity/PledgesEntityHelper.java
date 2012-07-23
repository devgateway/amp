package org.digijava.module.fundingpledges.dbentity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.exception.AimException;
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


/**
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
	            session = PersistenceManager.getSession();
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
	        }finally {
	        	try {
	        		if (session != null) {
	        			PersistenceManager.releaseSession(session);
	        		}
	        	} catch (Exception ex) {
	        		logger.error("releaseSession() failed");
	        	}
	        }
	        return AllPledges;
	}
	
	public static ArrayList<AmpFundingDetail> getFundingRelatedToPledges(FundingPledges pledge){
		 Session session = null;
	        Query q = null;
	        ArrayList<AmpFundingDetail> AllFunds = new ArrayList<AmpFundingDetail>();
	        try {
	            session = PersistenceManager.getSession();
	            String queryString = new String();
	            queryString = "select p from " + AmpFundingDetail.class.getName()
				+ " p where (p.pledgeid=:id)";
	            q = session.createQuery(queryString);
	            q.setParameter("id", pledge.getId(),Hibernate.LONG);
	            Iterator iter = q.list().iterator();
	            while (iter.hasNext()) {
	            	AllFunds.add((AmpFundingDetail)iter.next());
	            }

	        } catch (Exception ex) {
	        	logger.debug("Projects : Unable to get related fundings from database" + ex.getMessage());
	        }finally {
	        	try {
	        		if (session != null) {
	        			PersistenceManager.releaseSession(session);
	        		}
	        	} catch (Exception ex) {
	        		logger.error("releaseSession() failed");
	        	}
	        }
	        return AllFunds;
	}
	
	public static ArrayList<FundingPledges> getPledgesByDonorGroup(Long donorGrpId){
		 Session session = null;
	        Query q = null;
	        FundingPledges pledge = new FundingPledges();
	        ArrayList<FundingPledges> Pledges = new ArrayList<FundingPledges>();
	        List list = null;
	        try {
	            session = PersistenceManager.getSession();
	            String queryString = new String();
	            queryString = "select p from " + FundingPledges.class.getName()
				+ " p where (p.organizationGroup=:id)";
	            q = session.createQuery(queryString);
	            q.setParameter("id", donorGrpId,Hibernate.LONG);
	            Iterator iter = q.list().iterator();
	            while (iter.hasNext()) {
	            	pledge = (FundingPledges) iter.next();
	            	Pledges.add(pledge);
	            }
	        } catch (Exception ex) {
	        	logger.debug("Unable to get Pledges by organization Group from database" + ex.getMessage());
	        }finally {
	        	try {
	        		if (session != null) {
	        			PersistenceManager.releaseSession(session);
	        		}
	        	} catch (Exception ex) {
	        		logger.error("releaseSession() failed");
	        	}
	        }
	        return Pledges;
	}
	
	public static ArrayList<FundingPledges> getPledgesByDonor(Long donorid){
		 Session session = null;
	        Query q = null;
	        FundingPledges pledge = new FundingPledges();
	        ArrayList<FundingPledges> Pledges = new ArrayList<FundingPledges>();
	        List list = null;
	        try {
	            session = PersistenceManager.getSession();
	            String queryString = new String();
	            queryString = "select p from " + FundingPledges.class.getName()
				+ " p where (p.organization=:id)";
	            q = session.createQuery(queryString);
	            q.setParameter("id", donorid,Hibernate.LONG);
	            Iterator iter = q.list().iterator();
	            while (iter.hasNext()) {
	            	pledge = (FundingPledges) iter.next();
	            	Pledges.add(pledge);
	            }
	        } catch (Exception ex) {
	        	logger.debug("Projects : Unable to get Pledges names from database" + ex.getMessage());
	        }finally {
	        	try {
	        		if (session != null) {
	        			PersistenceManager.releaseSession(session);
	        		}
	        	} catch (Exception ex) {
	        		logger.error("releaseSession() failed");
	        	}
	        }
	        return Pledges;
	}
	
	public static ArrayList<FundingPledges> getPledgesByDonorAndTitle(Long donorid, String title){
		 Session session = null;
	        Query q = null;
	        FundingPledges pledge = new FundingPledges();
	        ArrayList<FundingPledges> Pledges = new ArrayList<FundingPledges>();
	        List list = null;
	        try {
	            session = PersistenceManager.getSession();
	            String queryString = new String();
	            queryString = "select p from " + FundingPledges.class.getName()
				+ " p where (p.organization=:id) and (p.title=:title)";
	            q = session.createQuery(queryString);
	            q.setParameter("id", donorid,Hibernate.LONG);
	            q.setParameter("title", title,Hibernate.STRING);
	            Iterator iter = q.list().iterator();
	            while (iter.hasNext()) {
	            	pledge = (FundingPledges) iter.next();
	            	Pledges.add(pledge);
	            }

	        } catch (Exception ex) {
	        	logger.debug("Projects : Unable to get Pledges names from database" + ex.getMessage());
	        }finally {
	        	try {
	        		if (session != null) {
	        			PersistenceManager.releaseSession(session);
	        		}
	        	} catch (Exception ex) {
	        		logger.error("releaseSession() failed");
	        	}
	        }
	        return Pledges;
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
	
	public static ArrayList<FundingPledgesDetails> getPledgesDetails(Long pledgeid){
		Session session = null;
		Query qry = null;
		ArrayList<FundingPledgesDetails> fundingpledgesdetails =  new ArrayList<FundingPledgesDetails>();
		FundingPledgesDetails fd = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select d from " + FundingPledgesDetails.class.getName()
					+ " d where (d.pledgeid=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", pledgeid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				fd = (FundingPledgesDetails) itr.next();
				fundingpledgesdetails.add(fd);
			}
		} catch (Exception e) {
			logger.error("Unable to get pledge details");
			logger.debug("Exception " + e);
		}finally {
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
	
	public static ArrayList<FundingPledgesLocation> getPledgesLocations(Long pledgeid){
		Session session = null;
		Query qry = null;
		ArrayList<FundingPledgesLocation> fundingpledgeloc = new ArrayList<FundingPledgesLocation>();
		FundingPledgesLocation pl = null;

		try {
			//session = PersistenceManager.getSession();
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select l from " + FundingPledgesLocation.class.getName()
					+ " l where (l.pledgeid=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", pledgeid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				pl = (FundingPledgesLocation) itr.next();
				fundingpledgeloc.add(pl);
			}
		} catch (Exception e) {
			logger.error("Unable to get pledge locations");
			logger.debug("Exception " + e);
		}finally {
        	try {
        		if (session != null) {
        			//PersistenceManager.releaseSession(session);
        		}
        	} catch (Exception ex) {
        		logger.error("releaseSession() failed");
        	}
        }
		return fundingpledgeloc;
	}
	
	public static ArrayList<FundingPledgesProgram> getPledgesPrograms(Long pledgeid){
		Session session = null;
		Query qry = null;
		ArrayList<FundingPledgesProgram> pledgeProgs = new ArrayList<FundingPledgesProgram>();
		FundingPledgesProgram fpp = null;

		try {
			//session = PersistenceManager.getSession();
			session = PersistenceManager.getRequestDBSession();
			String queryString = "select p from " + FundingPledgesProgram.class.getName()
					+ " p where (p.pledgeid=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", pledgeid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				fpp = (FundingPledgesProgram) itr.next();
				pledgeProgs.add(fpp);
			}
		} catch (Exception e) {
			logger.error("Unable to get pledge programs");
			logger.debug("Exception " + e);
		}finally {
        	try {
        		if (session != null) {
        			//PersistenceManager.releaseSession(session);
        		}
        	} catch (Exception ex) {
        		logger.error("releaseSession() failed");
        	}
        }
		return pledgeProgs;
	}
	
	public static ArrayList<FundingPledgesSector> getPledgesSectors(Long pledgeid){
		Session session = null;
		Query qry = null;
		ArrayList<FundingPledgesSector> fundingPledgesSector = new ArrayList<FundingPledgesSector>();
		FundingPledgesSector fs = null;

		try {
			session = PersistenceManager.getSession();
			String queryString = "select s from " + FundingPledgesSector.class.getName()
					+ " s where (s.pledgeid=:id)";
			qry = session.createQuery(queryString);
			qry.setParameter("id", pledgeid, Hibernate.LONG);
			Iterator itr = qry.list().iterator();
			while (itr.hasNext()) {
				fs = (FundingPledgesSector) itr.next();
				fundingPledgesSector.add(fs);
			}
		} catch (Exception e) {
			logger.error("Unable to get pledge sectors");
			logger.debug("Exception " + e);
		}finally {
        	try {
        		if (session != null) {
        			PersistenceManager.releaseSession(session);
        		}
        	} catch (Exception ex) {
        		logger.error("releaseSession() failed");
        	}
        } 
		return fundingPledgesSector;
	}
	public static void savePledge(FundingPledges pledge, Set<FundingPledgesSector> sectors,PledgeForm plf) throws DgException {
		
		Transaction tx=null;
		try {
			Session session = PersistenceManager.getSession();
//beginTransaction();
			session.save(pledge);
			
			for (Iterator iterator = sectors.iterator(); iterator.hasNext();) {
				FundingPledgesSector fundingPledgesSector = (FundingPledgesSector) iterator
						.next();
				fundingPledgesSector.setPledgeid(pledge);
				session.save(fundingPledgesSector);
			}
			if(plf.getFundingPledgesDetails()!=null && plf.getFundingPledgesDetails().size()>0){
				for (Iterator iterator = plf.getFundingPledgesDetails().iterator(); iterator.hasNext();) {
					FundingPledgesDetails FundingPledgesDetails = (FundingPledgesDetails) iterator.next();
					FundingPledgesDetails.setPledgeid(pledge);
					session.save(FundingPledgesDetails);
				}
			}
			
			if(plf.getSelectedLocs()!=null && plf.getSelectedLocs().size()>0){
				for (Iterator iterator = plf.getSelectedLocs().iterator(); iterator.hasNext();) {
					FundingPledgesLocation FundingPledgesloc = (FundingPledgesLocation) iterator.next();
					FundingPledgesloc.setPledgeid(pledge);
					session.save(FundingPledgesloc);
				}
			}
			
			if(plf.getSelectedProgs()!=null && plf.getSelectedProgs().size()>0){
				for (Iterator iterator = plf.getSelectedProgs().iterator(); iterator.hasNext();) {
					FundingPledgesProgram FundingPledgesProg = (FundingPledgesProgram) iterator.next();
					FundingPledgesProg.setPledgeid(pledge);
					session.save(FundingPledgesProg);
				}
			}
			
			//tx.commit();
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void removePledge(FundingPledges pledge) throws DgException {
		Transaction tx=null;
		try {
			Session session = PersistenceManager.getSession();
//beginTransaction();
			Collection<FundingPledgesSector> fpsl = PledgesEntityHelper.getPledgesSectors(pledge.getId());
			Collection<FundingPledgesLocation> fpll = PledgesEntityHelper.getPledgesLocations(pledge.getId());
			Collection<FundingPledgesProgram> fppl = PledgesEntityHelper.getPledgesPrograms(pledge.getId());
			Collection<FundingPledgesDetails> fpdl = PledgesEntityHelper.getPledgesDetails(pledge.getId());
			Collection<AmpFundingDetail> fprl = PledgesEntityHelper.getFundingRelatedToPledges(pledge);
			for (Iterator iterator = fpsl.iterator(); iterator.hasNext();) {
				FundingPledgesSector fundingPledgesSector = (FundingPledgesSector) iterator.next();
				session.delete(fundingPledgesSector);
			}
			for (Iterator iterator = fpdl.iterator(); iterator.hasNext();) {
				FundingPledgesDetails fundingPledgesDetails = (FundingPledgesDetails) iterator.next();
				session.delete(fundingPledgesDetails);
			}
			for (Iterator iterator = fpll.iterator(); iterator.hasNext();) {
				FundingPledgesLocation fundingPledgesloc = (FundingPledgesLocation) iterator.next();
				session.delete(fundingPledgesloc);
			}
			for (Iterator iterator = fppl.iterator(); iterator.hasNext();) {
				FundingPledgesProgram fundingPledgesProg = (FundingPledgesProgram) iterator.next();
				session.delete(fundingPledgesProg);
			}
			for (Iterator iterator = fprl.iterator(); iterator.hasNext();) {
				AmpFundingDetail fundingRelated = (AmpFundingDetail) iterator.next();
				fundingRelated.setPledgeid(null);
				session.update(fundingRelated);
			}
			session.delete(pledge);
			//tx.commit();
		} catch (HibernateException e) {
			logger.error("Error deleting pledge",e);
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception ex) {
					throw new DgException("Cannot rallback save pledge action",ex);
				}
				throw new DgException("Cannot delete Pledge!",e);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void updatePledge(FundingPledges pledge, Set<FundingPledgesSector> sectors,PledgeForm plf) throws DgException {
		
		Transaction tx=null;
		try {
			Session session = PersistenceManager.getSession();
//beginTransaction();
			session.update(pledge);
			Collection<FundingPledgesSector> fpsl = PledgesEntityHelper.getPledgesSectors(pledge.getId());
			Collection<FundingPledgesLocation> fpll = PledgesEntityHelper.getPledgesLocations(pledge.getId());
			Collection<FundingPledgesProgram> fppl = PledgesEntityHelper.getPledgesPrograms(pledge.getId());
			Collection<FundingPledgesDetails> fpdl = PledgesEntityHelper.getPledgesDetails(pledge.getId());
			
			if(sectors!=null && sectors.size()>0){
				for (Iterator iterator = sectors.iterator(); iterator.hasNext();) {
					FundingPledgesSector fundingPledgesSector = (FundingPledgesSector) iterator.next();
					if (fpsl.contains(fundingPledgesSector)) {
						fundingPledgesSector.setPledgeid(pledge);
						session.update(fundingPledgesSector);
					} else {
						fundingPledgesSector.setPledgeid(pledge);
						session.save(fundingPledgesSector);
						fpsl.add(fundingPledgesSector);
					}
				}
			} else {
				for (Iterator iterator = fpsl.iterator(); iterator.hasNext();) {
					FundingPledgesSector fundingPledgesSector = (FundingPledgesSector) iterator.next();
					session.delete(fundingPledgesSector);
				}
				fpsl = null;
			}
			
			if(fpsl!=null && fpsl.size()>0){
				for (Iterator iterator = fpsl.iterator(); iterator.hasNext();) {
					FundingPledgesSector fundingPledgesSector = (FundingPledgesSector) iterator.next();
					if (!sectors.contains(fundingPledgesSector)) {
						session.delete(fundingPledgesSector);
					}
				}
			}
			
			if(plf.getFundingPledgesDetails()!=null && plf.getFundingPledgesDetails().size()>0){
				for (Iterator iterator = plf.getFundingPledgesDetails().iterator(); iterator.hasNext();) {
					FundingPledgesDetails fundingPledgesDetails = (FundingPledgesDetails) iterator.next();
					if (fpdl.contains(fundingPledgesDetails)) {
						fundingPledgesDetails.setPledgeid(pledge);
						session.update(fundingPledgesDetails);
					} else {
						fundingPledgesDetails.setPledgeid(pledge);
						session.save(fundingPledgesDetails);
						fpdl.add(fundingPledgesDetails);
					}
				}
			} else {
				for (Iterator iterator = fpdl.iterator(); iterator.hasNext();) {
					FundingPledgesDetails fundingPledgesDetails = (FundingPledgesDetails) iterator.next();
					session.delete(fundingPledgesDetails);
				}
				fpdl = null;
			}
			
			if(fpdl!=null && fpdl.size()>0){
				for (Iterator iterator = fpdl.iterator(); iterator.hasNext();) {
					FundingPledgesDetails fundingPledgesDetails = (FundingPledgesDetails) iterator.next();
					if (!plf.getFundingPledgesDetails().contains(fundingPledgesDetails)) {
						session.delete(fundingPledgesDetails);
					}
				}
			}
			if(plf.getSelectedLocs()!=null && plf.getSelectedLocs().size()>0){
				for (Iterator iterator = plf.getSelectedLocs().iterator(); iterator.hasNext();) {
					FundingPledgesLocation fundingPledgesloc = (FundingPledgesLocation) iterator.next();
					if (fpll.contains(fundingPledgesloc)) {
						fundingPledgesloc.setPledgeid(pledge);
						session.update(fundingPledgesloc);
					} else {
						fundingPledgesloc.setPledgeid(pledge);
						session.save(fundingPledgesloc);
						fpll.add(fundingPledgesloc);
					}
				}
			} else {
				for (Iterator iterator = fpll.iterator(); iterator.hasNext();) {
					FundingPledgesLocation fundingPledgesloc = (FundingPledgesLocation) iterator.next();
					session.delete(fundingPledgesloc);
				}
				fpll = null;
			}
			
			if(fpll!=null && fpll.size()>0){
				for (Iterator iterator = fpll.iterator(); iterator.hasNext();) {
					FundingPledgesLocation fundingPledgesloc = (FundingPledgesLocation) iterator.next();
					if (!plf.getSelectedLocs().contains(fundingPledgesloc)) {
						session.delete(fundingPledgesloc);
					}
				}
			}
			
			if(plf.getSelectedProgs()!=null && plf.getSelectedProgs().size()>0){
				for (Iterator iterator = plf.getSelectedProgs().iterator(); iterator.hasNext();) {
					FundingPledgesProgram fundingPledgesProg = (FundingPledgesProgram) iterator.next();
					if (fppl.contains(fundingPledgesProg)) {
						fundingPledgesProg.setPledgeid(pledge);
						session.update(fundingPledgesProg);
					} else {
						fundingPledgesProg.setPledgeid(pledge);
						session.save(fundingPledgesProg);
						fppl.add(fundingPledgesProg);
					}
				}
			} else {
				for (Iterator iterator = fppl.iterator(); iterator.hasNext();) {
					FundingPledgesProgram fundingPledgesProg = (FundingPledgesProgram) iterator.next();
					session.delete(fundingPledgesProg);
				}
				fppl = null;
			}
			
			if(fppl!=null && fppl.size()>0){
				for (Iterator iterator = fppl.iterator(); iterator.hasNext();) {
					FundingPledgesProgram fundingPledgesProg = (FundingPledgesProgram) iterator.next();
					if (!plf.getSelectedProgs().contains(fundingPledgesProg)) {
						session.delete(fundingPledgesProg);
					}
				}
			}
			//tx.commit();
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static AmpOrganisation getOrganizationById(Long id) {
		Session session = null;
		AmpOrganisation ampOrg = null;
		try {
			//session = PersistenceManager.getSession();
			session = PersistenceManager.getSession();
			ampOrg = (AmpOrganisation) session.load(AmpOrganisation.class, id);
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return ampOrg;
	}
	
	public static AmpOrgGroup getOrgGroupById(Long id) {
		Session session = null;
		AmpOrgGroup ampOrgGrp = null;
		try {
			//session = PersistenceManager.getSession();
			session = PersistenceManager.getSession();
			ampOrgGrp = (AmpOrgGroup) session.load(AmpOrgGroup.class, id);
		}
		catch (Exception ex) {
			logger.error("Exception : " + ex.getMessage());
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				}
				catch (Exception rsf) {
					logger.error("Release session failed :" + rsf.getMessage());
				}
			}
		}
		return ampOrgGrp;
	}
	
	/**
	 * get pledge name for autocomplete box 
	 * @return
	 * @throws Exception
	 */
	public static String[] getPledgeNames() throws Exception {
		String[] retValue=null;
		Session session=null;
		String queryString =null;
		Query query=null;
		List pledgeNames=null;
		try{
			session=PersistenceManager.getSession();
			queryString="select distinct(pl.title) from " +FundingPledges.class.getName()+" pl " ;
			query=session.createQuery(queryString);
			pledgeNames=query.list();
		}catch (Exception e) {
			logger.error("...Failed to get pledge titles");
		}finally {
        	try {
        		if (session != null) {
        			PersistenceManager.releaseSession(session);
        		}
        	} catch (Exception ex) {
        		logger.error("releaseSession() failed");
        	}
        }
		
		if(pledgeNames!=null){
			retValue=new String[pledgeNames.size()];    		
			int i=0;
			for (Object row : pledgeNames) {
				String titleRow=(String)row;
				if(titleRow != null){
					titleRow = titleRow.replace('\n', ' ');
					titleRow = titleRow.replace('\r', ' ');
					titleRow = titleRow.replace("\\", "");
				}
				
				retValue[i]=new String(titleRow);					
				i++;
			}
		}
		return retValue;
	}

	public static void updateFundingPledgeDetail(FundingPledgesDetails fpd) throws DgException {
		Session session = null;
		Transaction tx=null;
		try {
			session = PersistenceManager.getSession();
//beginTransaction();
			session.update(fpd);
			//tx.commit();
		} catch (Exception e) {
			logger.error("Error saving pledge detail",e);
			if (tx!=null){
				try {
					tx.rollback();
				} catch (Exception ex) {
					throw new DgException("Cannot rallback save pledge action",ex);
				}
				throw new DgException("Cannot save Pledge detail!",e);
			}
		} finally {
        	try {
        		if (session != null) {
        			PersistenceManager.releaseSession(session);
        		}
        	} catch (Exception ex) {
        		logger.error("releaseSession() failed");
        	}
        }
	}
}
