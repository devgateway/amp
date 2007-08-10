/**
 * 
 */
package org.dgfoundation.amp;

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
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
