package org.digijava.module.fundingpledges.dbentity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.print.DocFlavor.STRING;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.helper.Pledge;
import org.digijava.module.aim.util.DbUtil;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;


/**
 * 
 * @author Diego Dimunzio
 * 
 */
public class PledgesEntityHelper {
	private static Logger logger = Logger.getLogger(PledgesEntityHelper.class);
	
	public static Integer PLEDGE_TYPE_NEW_FOUNDS = 1;
	public static Integer PLEDGE_TYPE_REPROGRAMMED_FOUNDS = 2;
	
	public static String PLEDGE_TYPE_NEW_FOUNDS_TEXT = "New funds";
	public static String PLEDGE_TYPE_REPROGRAMMED_FOUNDS_TEXT = "Reprogrammed funds";
	
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
}
