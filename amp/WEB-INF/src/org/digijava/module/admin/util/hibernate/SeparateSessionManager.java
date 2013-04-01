/**
 * 
 */
package org.digijava.module.admin.util.hibernate;

import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;

/**
 * @author alex
 *
 */
public class SeparateSessionManager {
	
	private Session session	= null;
	
	public void openSession () {
		if (session == null) {
			try{
				session 	= PersistenceManager.openNewSession();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void closeSession () {
		
		try {
			if ( session != null && session.isOpen() ) {
				session.flush();
				session.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Session getSession() {
		return session;
	}

	
	
	
}
