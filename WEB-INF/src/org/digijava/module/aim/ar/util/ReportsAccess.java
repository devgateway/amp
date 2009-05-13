package org.digijava.module.aim.ar.util;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.action.reportsimpexp.ReportsExportAction;
import org.digijava.module.aim.dbentity.AmpReports;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ReportsAccess {
	private static Logger logger 		= Logger.getLogger(ReportsAccess.class);
	
	private Collection<Long> teamIds;
	private Collection<Long> userIds;
	private String tabsQuery 	= null;
	private String orderQuery	= null;
	
	private String generateFilter() {
		String query	= "";
		if ( teamIds != null && teamIds.size() > 0 ) {
			query 	+= " and r.ownerId.ampTeam in (" + Util.toCSString(teamIds) + ") ";
		}
		if ( userIds != null && userIds.size() > 0 ) {
			query 	+= " and r.ownerId.user in (" + Util.toCSString(userIds) + ") ";
		}
		
		return query;
	} 
	
	public ReportsAccess(boolean isTab, Collection<Long> teamIds, Collection<Long> userIds) {
		this.teamIds					= teamIds;
		this.userIds					= userIds;
		
		if ( isTab ) { 
			tabsQuery	= " and r.drilldownTab=true ";
		}
		else {
			tabsQuery	= " and (r.drilldownTab is null or r.drilldownTab=false) ";
		}
		
		this.orderQuery				= "";
	}
	
	public List<AmpReports> retrieveReports () {
		Session  session		= null;
		Transaction tx		= null;
		try {
			session 					= PersistenceManager.getRequestDBSession();
			tx							= session.beginTransaction();
			String qryString		= "select r from " + AmpReports.class.getName() + " r where 1=1 AND r.ownerId!=null " + this.tabsQuery  + this.generateFilter() + " order by " + orderQuery +  " r.name" ;
			System.out.println( "Query is: "  + qryString );
			Query query			= session.createQuery(qryString);
			List result				= query.list();
			tx.commit();
			return result;
		}
		catch (Exception e) {
			if ( tx !=  null ) {
				try {
					tx.rollback();
				}
				catch (Exception e1) {
					logger.error("Rollback failed");
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
			return null;
		}
	}
	
	
}
