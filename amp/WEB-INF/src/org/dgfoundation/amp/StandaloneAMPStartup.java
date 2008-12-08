/**
 * 
 */
package org.dgfoundation.amp;

import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.util.DigiConfigManager;
import org.digijava.kernel.util.resource.ResourceStreamHandlerFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * @author mihai
 * This is a standalone initializer for digi/hibernate, that can be use to patch/inspect/query
 * the amp database without jboss/tomcat running.
 */
public class StandaloneAMPStartup {
	private static Logger logger = Logger.getLogger(StandaloneAMPStartup.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResourceStreamHandlerFactory.installIfNeeded();
		try {
			
			StandaloneJndiAMPInitializer.initAMPUnifiedJndiAlias();
			
			DigiConfigManager.initialize("repository");
			PersistenceManager.initialize(false);
			
			
			//BELOW THIS LINE, HIBERNATE IS AVAILABLE, ADD YOUR SCRIPT INVOCATION HERE			
			try {
				//EXAMPLE OF A WORKING HIBERNATE SESSION OBJECT:
				Session session = PersistenceManager.getSession();
				PersistenceManager.releaseSession(session);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e);
			}
			
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}

	}

}
