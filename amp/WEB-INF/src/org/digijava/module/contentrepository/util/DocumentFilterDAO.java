package org.digijava.module.contentrepository.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.contentrepository.dbentity.filter.DocumentFilter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DocumentFilterDAO {
    private static Logger logger    = Logger.getLogger(DocumentFilterDAO.class);
    public void saveObject(DocumentFilter obj) {
        Session hbSession;
        Transaction tx=null;
        try{
            hbSession   = PersistenceManager.getRequestDBSession();
//beginTransaction();
            hbSession.saveOrUpdate(obj);
            //tx.commit();
        }
        catch (Exception e) {
            if(tx!=null) {
                try {
                    tx.rollback();                  
                }catch(Exception ex ) {
                    logger.error("...Rollback failed");
                    throw new RuntimeException("Can't rollback", e);
                }           
            }
            e.printStackTrace();
        }
    }
    public List<DocumentFilter> getAll() {
        Session hbSession;
        try{
            hbSession   = PersistenceManager.getRequestDBSession();
            String queryString  = "select df from " + DocumentFilter.class.getName() + " df ";
            Query query         = hbSession.createQuery(queryString);
            List<DocumentFilter> resultList =  query.list();
            return resultList;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public DocumentFilter getDocumentFilter(Long id) {
        Session hbSession;
        try{
            hbSession           = PersistenceManager.getRequestDBSession();
            DocumentFilter  df  = (DocumentFilter)hbSession.load(DocumentFilter.class, id);
            
            return df;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void deleteDocumentFilter(Long id) {
        Session hbSession;
        try{
            hbSession           = PersistenceManager.getRequestDBSession();
//beginTransaction();
            DocumentFilter  df  = (DocumentFilter)hbSession.load(DocumentFilter.class, id);
            
            hbSession.delete(df);
            //tx.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
