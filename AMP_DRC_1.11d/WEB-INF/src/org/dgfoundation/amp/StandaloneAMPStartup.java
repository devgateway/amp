/**
 * 
 */
package org.dgfoundation.amp;

import java.sql.SQLException;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.dgfoundation.amp.importers.ImporterWorker;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;

/**
 * @author mihai
 * This is a standalone initializer for digi/hibernate, that can be use to patch/inspect/query
 * the amp database without jboss/tomcat running.
 */
public class StandaloneAMPStartup {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResourceStreamHandlerFactory.installIfNeeded();
		try {
			DigiConfigManager
					.initialize("repository");
			PersistenceManager.initialize(false);
			
			
			//BELOW THIS LINE, HIBERNATE IS AVAILABLE, ADD YOUR SCRIPT INVOCATION HERE			
			try {
				//EXAMPLE OF A WORKING HIBERNATE SESSION OBJECT:
				Session session = PersistenceManager.getSession();
				PersistenceManager.releaseSession(session);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//we import stuff using csv importer
			//ImporterWorker iw=new ImporterWorker("/home/mihai/workspace/amp-testing/importers/");
			//iw.start();
			
			
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
