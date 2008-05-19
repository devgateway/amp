package org.digijava.module.gis.util;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;
import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.gis.dbentity.GisMap;
import org.digijava.module.forum.dbentity.ForumUserSettings;
import net.sf.hibernate.Query;
import net.sf.hibernate.Hibernate;
import java.util.Iterator;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DbUtil {
    private static Logger logger = Logger.getLogger(DbUtil.class);

    public DbUtil() {
    }

    public static void saveMap(GisMap map) throws DgException {
        Session sess = null;
        Transaction tx = null;
        try {
            sess = PersistenceManager.getSession();
            tx = sess.beginTransaction();
            sess.save(map);
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (HibernateException ex1) {
                    logger.warn("rollback() failed", ex1);
                }
            }
            logger.debug("Unable to save the map", ex);
            throw new DgException(
                    "Unable to save the map", ex);
        } finally {
            if (sess != null) {
                try {
                    PersistenceManager.releaseSession(sess);
                } catch (Exception ex1) {
                    logger.warn("releaseSession() failed", ex1);
                }
            }
        }
    }

    public static GisMap getMap(long id) {
        Session session = null;
        GisMap gisMap = null;
        try {
            session = PersistenceManager.getSession();
            gisMap = (GisMap) session.load(
                    GisMap.class,
                    new Long(id));
        } catch (Exception ex) {
            String gg = null;
        } finally {
            try {
                PersistenceManager.releaseSession(session);
            } catch (Exception ex2) {
            }
        }
        return gisMap;
    }

    public static GisMap getMapByCode(String code) {
        Session session = null;
        GisMap map = null;
        try {
            session = PersistenceManager.getRequestDBSession();
            Query q = session.createQuery("from rs in class " +
                                          GisMap.class.getName() +
                                          " where (rs.mapCode=:mapCode)");
            q.setParameter("mapCode", code, Hibernate.STRING);
            Iterator it = q.iterate();
            if (it.hasNext()) {
                map = (GisMap) it.next();
            } else {
                logger.debug("Unable to get map from DB");
            }
        } catch (Exception ex) {
            logger.debug("Unable to get map from DB", ex);
        }
        return map;

    }

}
