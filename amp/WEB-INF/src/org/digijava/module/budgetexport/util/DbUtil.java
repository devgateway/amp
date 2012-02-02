package org.digijava.module.budgetexport.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportProject;
import org.hibernate.Session;

/**
 * Created by IntelliJ IDEA.
 * User: flyer
 * Date: 2/1/12
 * Time: 6:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbUtil {
    private static Logger logger	= Logger.getLogger(DbUtil.class);
    public static void saveOrUpdateProject (AmpBudgetExportProject project) throws DgException {
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            sess.saveOrUpdate(project);
        } catch (DgException ex) {
            logger.debug("Unable to save or update Budget export project", ex);
            throw ex;
        }
    }
}
