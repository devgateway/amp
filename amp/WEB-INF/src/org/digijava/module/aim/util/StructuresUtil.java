package org.digijava.module.aim.util;

/*
 * @author Fernando Ferreyra
 */
import java.util.Collection;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpStructureType;
import org.hibernate.Query;
import org.hibernate.Session;

public class StructuresUtil {

    private static Logger logger = Logger.getLogger(ComponentsUtil.class);

    @SuppressWarnings("unchecked")
    public static Collection<AmpStructureType> getAmpStructureTypes() {
        Collection<AmpStructureType> col = null;
        String queryString = null;
        Session session = null;
        Query qry = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            queryString = "select st from " + AmpStructureType.class.getName() + " st ";
            qry = session.createQuery(queryString);
            col = qry.list();
        } catch (Exception ex) {
            logger.error("Unable to get AmpStructureType  from database " + ex.getMessage());
        }
        return col;
    }
   
}
