/**
 * 
 */
package org.digijava.module.aim.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.Session;

/**
 * @author mihai
 *
 */
public class DBPersister {
    private static Logger logger = Logger.getLogger(DBPersister.class);
    
    public static boolean save(Identifiable o) {
        Session session=null;
        try {
            session = PersistenceManager.getSession();
//beginTransaction();
            session.saveOrUpdate(o);
            //tx.commit();
        } catch (Exception e) {
            logger.error("Exception saving object of "+o.getClass() +" with identifier "+o.getIdentifier());
            e.printStackTrace();
        } 
        return true;
    }

}
