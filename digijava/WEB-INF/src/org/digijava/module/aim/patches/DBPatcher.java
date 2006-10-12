package org.digijava.module.aim.patches;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpIndicatorRiskRatings;
import org.digijava.module.aim.dbentity.AmpPages;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamPageFilters;
import org.digijava.module.aim.dbentity.AmpTermsAssist;
import org.digijava.module.aim.helper.Constants;

public class DBPatcher {
	
	private static Logger logger = Logger.getLogger(DBPatcher.class);
	
	public void patchAMPDB() {
		logger.info("In PatchAMPDB()");
		Session session = null;
		String qryStr = null;
		Query qry = null;
		String newTypeOfAssistance = "Treasury";
		String filterName = Constants.ACTIVITY_RISK_FILTER;
		
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
			 
			 Statement stmt = session.connection().createStatement();
			 
			 try {
				 qryStr = "ALTER TABLE AMP_ME_INDICATOR_VALUE " +
			 		"ADD base_val_comments VARCHAR(255)," +
			 		"ADD actual_val_comments VARCHAR(255)," +
			 		"ADD target_val_comments VARCHAR(255)," +
			 		"ADD revised_target_val_comments VARCHAR(255)";
				 stmt.executeUpdate(qryStr);
			 
				 qryStr = "ALTER TABLE AMP_ME_CURR_VAL_HISTORY " +
				 	"ADD comments VARCHAR(255)";
				 stmt.executeUpdate(qryStr);	 
			 } catch (SQLException sqle) {
				 logger.info("ME Tables already altered");
			 }
			 
			 try {
				 qryStr = "ALTER TABLE AMP_ME_INDICATORS " +
				 		"MODIFY ASCENDING_IND TINYINT(1)";
				 stmt.executeUpdate(qryStr);
			 
				 qryStr = "UPDATE AMP_ME_INDICATORS " +
				 		"SET ASCENDING_IND=1 WHERE ASCENDING_IND IS NULL";
				 stmt.executeUpdate(qryStr);	 
			 } catch (SQLException sqle) {
				 logger.info("ME Tables already altered");
			 }			 
			 
			 try {
				 qryStr = "ALTER TABLE AMP_FUNDING_DETAIL " +
				 		"MODIFY AMP_FUNDING_ID BIGINT(10) NULL";
				 stmt.executeUpdate(qryStr);
			 } catch (SQLException sqle) {
				 logger.info("FundingDetails Tables already altered");
			 }			 
			 
			 try {
				 qryStr = "UPDATE AMP_FUNDING_DETAIL " +
				 		"SET PERSPECTIVE_ID=2 " +
				 		"WHERE PERSPECTIVE_ID IS NULL";
				 stmt.executeUpdate(qryStr);
			 } catch (SQLException sqle) {
				 logger.info("Perspective not updated :" + sqle.getMessage());
			 }			 
			 
			 try {
				 qryStr = "SELECT COUNT(*) FROM AMP_FEATURE WHERE NAME LIKE 'Documents'";
				 ResultSet rs = stmt.executeQuery(qryStr);
				 if (rs.next()) {
					 int cnt = rs.getInt(1);
					 if (cnt == 0) {
						 qryStr = "INSERT INTO AMP_FEATURE (NAME,CODE,ACTIVE) VALUES ('Documents','DC',0)";
						 stmt.executeUpdate(qryStr);
					 }
				 }
				 qryStr = "SELECT COUNT(*) FROM AMP_FEATURE WHERE NAME LIKE 'Scenarios'";
				 rs = stmt.executeQuery(qryStr);
				 if (rs.next()) {
					 int cnt = rs.getInt(1);
					 if (cnt == 0) {
						 qryStr = "INSERT INTO AMP_FEATURE (NAME,CODE,ACTIVE) VALUES ('Scenarios','SC',0)";
						 stmt.executeUpdate(qryStr);
					 }
				 }
			 } catch (SQLException sqle) {
				 logger.info("Exception :" + sqle.getMessage());
			 }			 			 
			 
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
			 
			 qryStr = "select count(*) from " + AmpFilters.class.getName() + " f " +
			 		"where (f.filterName=:name)";
			 qry = session.createQuery(qryStr);
			 qry.setParameter("name",filterName);
			 itr = qry.list().iterator();
			 AmpFilters f = null;
			 if (itr.hasNext()) {
				 Integer count = (Integer) itr.next();
				 if (count.intValue() == 0) {
					 f = new AmpFilters();
					 f.setFilterName(filterName);
				 }
			 }
			 
			 if (f != null) {
				 qryStr = "select p from " + AmpPages.class.getName() + " p " +
				 		"where (p.pageCode!=:code)";
				 qry = session.createQuery(qryStr);
				 qry.setParameter("code","FP");
				 itr = qry.list().iterator();
				 Collection temp = new ArrayList();
				 AmpPages p = null;
				 while (itr.hasNext()) {
					 p = (AmpPages) itr.next();
					 temp.add(p);
					 if (f.getPages() == null) {
						 f.setPages(new HashSet());
					 }
					 f.getPages().add(p);
				 }
				 session.save(f);
				 
				 qryStr = "select t from " + AmpTeam.class.getName() + " t ";
				 qry = session.createQuery(qryStr);
				 itr = qry.list().iterator();
				 while (itr.hasNext()) {
					 AmpTeam team = (AmpTeam) itr.next();
					 
					 Iterator t1 = temp.iterator();
					 while (t1.hasNext()) {
						 AmpPages ps = (AmpPages) t1.next();
						 AmpTeamPageFilters tpf = new AmpTeamPageFilters();
						 tpf.setFilter(f);
						 tpf.setPage(ps);
						 tpf.setTeam(team);
						 session.save(tpf);
					 }
					 
				 }
				 session.flush();				 
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
				 session.update(rr);
				 session.flush();
				 
				 qryStr = "delete from dg_message where message_utf8 " +
				 		"like '%&nbsp%'";
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