/**
 * 
 */
package org.digijava.module.aim.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author mihai
 *
 */
public class DBPersister {
	private static Logger logger = Logger.getLogger(DBPersister.class);
	
	public static boolean save(Identifiable o) {
		Session session=null;
		Transaction tx = null;
		try {
			session = PersistenceManager.getSession();
			tx=session.beginTransaction();
			session.saveOrUpdate(o);
			tx.commit();
		} catch (Exception e) {
			logger.error("Exception saving object of "+o.getClass() +" with identifier "+o.getIdentifier());
			e.printStackTrace();
			if (tx != null) {
				try {
					tx.rollback();
				} catch (Exception rbf) {
					logger.error("...Rollback failed");
					return false;
				}
			}
		} finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception rsf) {
					logger.error("...Release session failed");
					return false;
				}
			}
		}
		return true;
	}

}
