package org.dgfoundation.amp.ar;

import java.sql.SQLException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * ARTester.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jul 6, 2006
 * 
 */
public class ARTester {

	SessionFactory sessionFactory;

	public static void main(String[] args) {

		ResourceStreamHandlerFactory.installIfNeeded();
		try {
			DigiConfigManager
					.initialize("/home/mihai/workspace/amp/repository");
			PersistenceManager.initialize(false);
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// get a Report:
		Session session;
		try {
			session = PersistenceManager.getSession();
			
			Transaction tx = session.beginTransaction();
			/*
			Query q = session
					.createQuery("SELECT r from AmpReports r WHERE r.ampReportId = :id");
			q.setInteger("id", 2);

			List result = q.list();

			*/
			AmpReports r=(AmpReports) session.get(AmpReports.class,new Long(91));
			
			
			AmpReportGenerator arg=new AmpReportGenerator(r,"select amp_activity_id from amp_activity");
			
			arg.generate();

			tx.commit();
			session.close();
			

		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
