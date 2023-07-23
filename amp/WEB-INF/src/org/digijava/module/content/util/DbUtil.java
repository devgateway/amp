package org.digijava.module.content.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.content.dbentity.AmpContentItem;
import org.digijava.module.content.dbentity.AmpContentItemThumbnail;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.JDBCException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.StringType;

public class DbUtil {
    private static Logger logger = Logger.getLogger(DbUtil.class);

    public DbUtil() {
    }

    public static Collection<AmpContentItem> getAllContents() {
        Session session = null;
        Query qry = null;
        Collection<AmpContentItem> contents = new ArrayList<AmpContentItem>();

        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select ac from "
                    + AmpContentItem.class.getName() + " ac";
            qry = session.createQuery(queryString);
            contents = qry.list();
        } catch (Exception e) {
            logger.error("Unable to get all contents", e);
        }
        return contents;
    }

    public static AmpContentItem getContentItem(Long id) {
        Session session = null;
        AmpContentItem ampContentItem = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            ampContentItem = (AmpContentItem) session.load(
                    AmpContentItem.class, id);
            Hibernate.initialize(ampContentItem.getContentThumbnails());
        } catch (Exception e) {
            logger.error("Unable to get object of class "
                    + AmpContentItem.class.getName() + " width id=" + id
                    + ". Error was:" + e);
        }
        return ampContentItem;
    }

    public static AmpContentItem getContentItemByPageCode(String pageCode) {
        Session session = null;
        AmpContentItem ampContentItem = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "from " + AmpContentItem.class.getName() + " as aci where aci.pageCode = :pageCode";
            Query qry = session.createQuery(queryString);
            qry.setParameter("pageCode", pageCode, new StringType());
            Iterator<AmpContentItem> itr = qry.list().iterator();
            if (itr.hasNext()) {
                ampContentItem = (AmpContentItem) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get object of class "
                    + AmpContentItem.class.getName() + " width pageCode="
                    + pageCode + ". Error was:" + e);
        }
        return ampContentItem;
    }

    public static AmpContentItem getHomePage() {
        Session session = null;
        AmpContentItem ampContentItem = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            String queryString = "select aci.* from amp_content_item aci "
                    + "where aci.isHomepage = true";
            Query qry = session.createSQLQuery(queryString).addEntity(
                    AmpContentItem.class);
            Iterator<AmpContentItem> itr = qry.list().iterator();
            if (itr.hasNext()) {
                ampContentItem = (AmpContentItem) itr.next();
            }
        } catch (Exception e) {
            logger.error("Unable to get object of class "
                    + AmpContentItem.class.getName()
                    + " width homepage pageCode. Error was:" + e);
        }
        
        return ampContentItem;
    }

    public static void save(AmpContentItem contentItem) {
        Session session = null;
        Transaction tx = null;

        try {
            session = PersistenceManager.getRequestDBSession();
//beginTransaction();
            /*if (contentItem.getContentThumbnails() != null) {
                Iterator itr = contentItem.getContentThumbnails().iterator();
                while (itr.hasNext()) {
                    org.digijava.module.content.dbentity.AmpContentItemThumbnail thumb = (org.digijava.module.content.dbentity.AmpContentItemThumbnail) itr
                            .next();
                    thumb.setContentItem(contentItem);
                }
            }*/
            session.saveOrUpdate(contentItem);
            //tx.commit();
        } catch (Exception e) {
            logger.error("Unable to save content", e);
        }

    }

    public static void delete(AmpContentItem contentItem) {
        Session sess = null;
        Transaction tx = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
//beginTransaction();
            sess.delete(contentItem);
            //tx.commit();
        } catch (Exception e) {
            if (e instanceof JDBCException)
                throw (JDBCException) e;
            logger.error("Exception " + e.toString());
            try {
                tx.rollback();
            } catch (HibernateException ex) {
                logger.error("rollback() failed");
                logger.error(ex.toString());
            }
        } 
    }

    public static void removeThumbnails(AmpContentItem contentItem,
            Set<AmpContentItemThumbnail> contentThumbnailsRemoved) {
        Session sess = null;
        Transaction tx = null;

        try {
            sess = PersistenceManager.getRequestDBSession();
//beginTransaction();
            Iterator<AmpContentItemThumbnail> itr = contentThumbnailsRemoved
                    .iterator();
            while (itr.hasNext()) {
                AmpContentItemThumbnail delItem = itr.next();
                                
                if (delItem.getAmpContentItemThumbnailId() != null){
                    Query q = sess.createQuery("delete from " + AmpContentItemThumbnail.class.getName() + " where ampContentItemThumbnailId = " + String.valueOf(delItem.getAmpContentItemThumbnailId().longValue()));
                    q.executeUpdate();
                }
                //sess.delete(delItem);
            }
            //tx.commit();
        } catch (Exception e) {
            if (e instanceof JDBCException)
                throw (JDBCException) e;
            logger.error("Exception " + e.toString());
//          try {
//              tx.rollback();
//          } catch (HibernateException ex) {
//              logger.error("rollback() failed");
//              logger.error(ex.toString());
//          }
        } 

    }

    public static boolean pageCodeExists(String pageCode, Long contentId) {
        AmpContentItem contentItem = getContentItemByPageCode(pageCode);
        if (contentItem != null && contentId != null && contentItem.getAmpContentItemId() != contentId){
            return true;
        }
        return false;
    }

}
