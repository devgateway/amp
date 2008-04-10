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

/**
 * ActivityEraser.java
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
public class ActivityEraser {

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
			

			session.delete("select from AmpActivity");
	
				
			tx.commit();
			
			
			tx = session.beginTransaction();
			

			session.delete("select from AmpReports");
	

			tx.commit();

			
			PersistenceManager.releaseSession(session);
			

		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
