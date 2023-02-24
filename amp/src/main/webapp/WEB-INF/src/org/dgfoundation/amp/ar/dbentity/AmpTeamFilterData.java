package org.dgfoundation.amp.ar.dbentity;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AmpTeamFilterData extends AmpFilterData {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(AmpTeamFilterData.class);
    
    public AmpTeamFilterData() {
        super();
    }
    
    public AmpTeamFilterData(FilterDataSetInterface filterRelObj,
            String propertyName, String propertyClassName,
            String elementClassName, String value) {
        super(filterRelObj, propertyName, propertyClassName, elementClassName, value);
    }

    public static void deleteOldFilterData ( Long ampReportId ) {
        Session sess    = null;
        Transaction tx  = null;
        try {
            sess    = PersistenceManager.openNewSession();
            tx      = sess.beginTransaction();
            String qryStr   = "select a from "
                + AmpTeamFilterData.class.getName() + " a "
                + "where (a.filterRelObj=:report)";
            Query query     = sess.createQuery(qryStr);
            query.setLong("report", ampReportId);
            List results    = query.list();
            
            if ( results != null ) {
                Iterator iter   = results.iterator();
                boolean cleared = false;
                while (iter.hasNext()) {
                    AmpTeamFilterData afd   = (AmpTeamFilterData)iter.next();
                    if ( !cleared ) {
                        cleared = true;
                        afd.getFilterRelObj().getFilterDataSet().clear();
                    } 
                    afd.setFilterRelObj(null);
                    sess.delete(afd);
                }
            }
            tx.commit();
            
        } catch (Exception e) {
            logger.error("Rolling back transaction of deleting exising filters before saving new ones", e);
            tx.rollback();
        }
        finally{
            PersistenceManager.closeSession(sess);
        }
        
    } 
    
}
