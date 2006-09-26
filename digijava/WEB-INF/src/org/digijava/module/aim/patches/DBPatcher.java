package org.digijava.module.aim.patches;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpTermsAssist;

public class DBPatcher {
	
	private static Logger logger = Logger.getLogger(DBPatcher.class);
	
	public void patchAMPDB() {
		logger.info("In PatchAMPDB()");
		Session session = null;
		String qryStr = null;
		Query qry = null;
		String newTypeOfAssistance = "Treasury";
		
		final int RATING_CRITICAL_KEY = -3;
		final String RATING_CRITICAL_VALUE = "Critical"; 
		final int RATING_VHIGH_KEY = -2;
		final String RATING_VHIGH_VALUE = "Very High";
		final int RATING_HIGH_KEY = -1;
		final String RATING_HIGH_VALUE = "High";
		final int RATING_MEDIUM_KEY = 1;
		final String RATING_MEDIUM_VALUE = "Medium";
		final int RATING_LOW_KEY = 2;
		final String RATING_LOW_VALUE = "Low";
		final int RATING_VLOW_KEY = 3;
		final String RATING_VLOW_VALUE = "Very Low";
		
		try {
			  
			 session = PersistenceManager.getSession();
			 qryStr = "select count(*) from " + AmpTermsAssist.class.getName() + " ta " +
			 		"where (ta.termsAssistName=:name)";
			 qry = session.createQuery(qryStr);
			 qry.setParameter("name",newTypeOfAssistance);
			 Iterator itr = qry.list().iterator();
			 if (itr.hasNext()) {
				 Integer count = (Integer) itr.next();
				 if (count.intValue() == 0) {
					 AmpTermsAssist newTa = new AmpTermsAssist();
					 newTa.setTermsAssistCode("4");
					 newTa.setTermsAssistName(newTypeOfAssistance);
					 session.save(newTa);
					 session.flush();
				 }
			 }
			 
			 qryStr = "select r from " + AmpIndicatorRiskRatings.class.getName() + " r";
			 qry = session.createQuery(qryStr);
			 itr = qry.list().iterator();
			 while (itr.hasNext()) {
				 AmpIndicatorRiskRatings rr = (AmpIndicatorRiskRatings) itr.next();
				 switch (rr.getRatingValue()) {
				case RATING_CRITICAL_KEY:
					rr.setRatingName(RATING_CRITICAL_VALUE);
					break;
				case RATING_VHIGH_KEY:
					rr.setRatingName(RATING_VHIGH_VALUE);
					break;
				case RATING_HIGH_KEY:
					rr.setRatingName(RATING_HIGH_VALUE);
					break;
				case RATING_MEDIUM_KEY:
					rr.setRatingName(RATING_MEDIUM_VALUE);
					break;
				case RATING_LOW_KEY:
					rr.setRatingName(RATING_LOW_VALUE);
					break;
				case RATING_VLOW_KEY:
					rr.setRatingName(RATING_VLOW_VALUE);
				}
				 logger.info("Updating " + rr.getAmpIndRiskRatingsId()+ " ," + rr.getRatingName());
				 session.update(rr);
				 session.flush();
				 
				 qryStr = "delete from dg_message where message_utf8 " +
				 		"like '%&nbsp%'";
				 Connection con = session.connection();
				 Statement stmt = con.createStatement();
				 stmt.executeUpdate(qryStr);
			 }
				
			 
		} catch (Exception e) {
			logger.error("Exception from patchAMPDB :" + e.getMessage());
			e.printStackTrace(System.out);
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("Release session failed!");
				}
			}
		}
	}
	
}