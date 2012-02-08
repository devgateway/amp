package org.digijava.module.budgetexport.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.budgetexport.adapter.MappingEntityAdapterUtil;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportCSVItem;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapItem;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportMapRule;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportProject;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
            logger.debug("Unable to save or update Budget export project in to DB", ex);
            throw ex;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<AmpBudgetExportProject> getAllProjects() throws DgException {
        List<AmpBudgetExportProject> retVal = null;
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("from ");
            queryStr.append(AmpBudgetExportProject.class.getName());
            Query q = sess.createQuery(queryStr.toString());
            retVal = (List<AmpBudgetExportProject>) q.list();
        } catch (DgException ex) {
            logger.debug("Unable to get Budget export projects from DB", ex);
            throw ex;
        }
        
        return retVal;
    }

    @SuppressWarnings("unchecked")
    public static AmpBudgetExportProject getProjectById (Long id) throws DgException {
        AmpBudgetExportProject retVal = null;
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            retVal = (AmpBudgetExportProject) sess.load(AmpBudgetExportProject.class, id);

        } catch (DgException ex) {
            logger.debug("Unable to get Budget export project from DB", ex);
            throw ex;
        }


        return retVal;
    }
    
    public static void deleteProjectById(Long id)  throws DgException {
        Session sess = PersistenceManager.getRequestDBSession();
        sess.delete(getProjectById(id));
    }

    @SuppressWarnings("unchecked")
    public static List<AmpBudgetExportMapRule> getProjectMappingRules(Long projectId) throws DgException {
        List<AmpBudgetExportMapRule> retVal = null;
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("from ");
            queryStr.append(AmpBudgetExportMapRule.class.getName());
            queryStr.append(" rule where rule.project.id=:PRJ_ID");
            
            Query q = sess.createQuery(queryStr.toString());
            q.setLong("PRJ_ID", projectId);
            retVal = q.list();


        } catch (DgException ex) {
            logger.debug("Unable to get Budget export rules from DB", ex);
            throw ex;
        }

        return retVal;
    }

    @SuppressWarnings("unchecked")
    public static List<AmpColumns> getAvailableColumns() throws DgException {
        List<AmpColumns> retVal = null;
        Set <String> adapterKeys = MappingEntityAdapterUtil.getAvailEntityAdapterKeys();
        StringBuilder viewsWhereclause = new StringBuilder();

        Iterator<String> adapterKeyIt = adapterKeys.iterator();
        while (adapterKeyIt.hasNext()) {
            String adapterKey = adapterKeyIt.next();
            viewsWhereclause.append("'");
            viewsWhereclause.append(adapterKey);
            viewsWhereclause.append("'");
            if (adapterKeyIt.hasNext()) {
                viewsWhereclause.append(",");
            }
        }



        try {
            Session sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("from ");
            queryStr.append(AmpColumns.class.getName());
            queryStr.append(" col where col.extractorView != null");
            queryStr.append(" and col.extractorView in (");
            queryStr.append(viewsWhereclause);
            queryStr.append(")");
            Query q = sess.createQuery(queryStr.toString());
            retVal = q.list();
        } catch (DgException ex) {
            logger.debug("Unable to get AMP columns from DB", ex);
            throw ex;
        }
        return retVal;
    }

    @SuppressWarnings("unchecked")
    public static AmpColumns getAmpColumnById (Long id) throws DgException {
        AmpColumns retVal = null;
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            retVal = (AmpColumns) sess.load(AmpColumns.class, id);

        } catch (DgException ex) {
            logger.debug("Unable to get AMP column from DB", ex);
            throw ex;
        }
        return retVal;
    }

    public static AmpBudgetExportMapRule getMapRuleById (Long id) throws DgException {
        return getMapRuleById (id, false);
    }

    @SuppressWarnings("unchecked")
    public static AmpBudgetExportMapRule getMapRuleById (Long id, boolean initLazyCollections) throws DgException {
        AmpBudgetExportMapRule retVal = null;
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            retVal = (AmpBudgetExportMapRule) sess.load(AmpBudgetExportMapRule.class, id);

        } catch (DgException ex) {
            logger.debug("Unable to get Budget export map rule from DB", ex);
            throw ex;
        }

        if (initLazyCollections) {
            Hibernate.initialize(retVal.getItems());
            Hibernate.initialize(retVal.getCsvItems());
        }

        return retVal;
    }

    public static void saveOrUpdateMapRule (AmpBudgetExportMapRule rule) throws DgException {
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            sess.saveOrUpdate(rule);
        } catch (DgException ex) {
            logger.debug("Unable to save or update Budget export map rule in to DB", ex);
            throw ex;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<AmpBudgetExportMapItem> getMappingRuleItems(Long ruleId) throws DgException {
        List<AmpBudgetExportMapItem> retVal = null;
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            StringBuilder queryStr = new StringBuilder("from ");
            queryStr.append(AmpBudgetExportMapItem.class.getName());
            queryStr.append(" ruleItem where ruleItem.rule.id=:RULE_ID");

            Query q = sess.createQuery(queryStr.toString());
            q.setLong("RULE_ID", ruleId);
            retVal = q.list();


        } catch (DgException ex) {
            logger.debug("Unable to get Budget export rule items from DB", ex);
            throw ex;
        }

        return retVal;
    }

    @SuppressWarnings("unchecked")
    public static AmpBudgetExportMapItem getMapItemById (Long id) throws DgException {
        AmpBudgetExportMapItem retVal = null;
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            retVal = (AmpBudgetExportMapItem) sess.load(AmpBudgetExportMapItem.class, id);

        } catch (DgException ex) {
            logger.debug("Unable to get Budget export map item from DB", ex);
            throw ex;
        }


        return retVal;
    }

    @SuppressWarnings("unchecked")
        public static List<AmpBudgetExportCSVItem> getRuleCSVItems(Long ruleId) throws DgException {
            List<AmpBudgetExportCSVItem> retVal = null;
            try {
                Session sess = PersistenceManager.getRequestDBSession();
                StringBuilder queryStr = new StringBuilder("from ");
                queryStr.append(AmpBudgetExportCSVItem.class.getName());
                queryStr.append(" csvItem where csvItem.rule.id=:RULE_ID");

                Query q = sess.createQuery(queryStr.toString());
                q.setLong("RULE_ID", ruleId);
                retVal = q.list();


            } catch (DgException ex) {
                logger.debug("Unable to get Budget export rule CSV items from DB", ex);
                throw ex;
            }

            return retVal;
        }
}
