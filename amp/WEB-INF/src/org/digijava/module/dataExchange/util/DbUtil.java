package org.digijava.module.dataExchange.util;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.dataExchange.dbentity.AmpDEUploadSession;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.dbentity.IatiCodeType;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 3/17/14
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbUtil {
    private static Logger logger = Logger.getLogger(DbUtil.class);

    public static void saveObjectSet (Set objs) {
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            for (Object o : objs) {
                sess.saveOrUpdate(o);
            }
        } catch (DgException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void saveObject (Object obj) {
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            sess.saveOrUpdate(obj);
        } catch (DgException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static Object getObject (Class clazz, Long id) {
        Object retVal = null;
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            retVal = sess.load(clazz, id);
        } catch (DgException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return retVal;
    }

    public static AmpDEUploadSession getAmpDEUploadSession(Long id) {
        return getAmpDEUploadSession(id, false);
    }

    public static AmpDEUploadSession getAmpDEUploadSession(Long id, boolean initFieldsCollection) {
        AmpDEUploadSession retVal = null;
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            Query q = sess.createQuery(new StringBuilder("from ").
                    append(AmpDEUploadSession.class.getName()).append(" us where us.id=:US_ID").toString());
            q.setLong("US_ID", id);
            retVal = (AmpDEUploadSession) q.uniqueResult();
            if (initFieldsCollection) {
                Hibernate.initialize(retVal.getLogItems());
            }
        } catch (DgException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return retVal;
    }

    public static List<AmpDEUploadSession> getAllAmpDEUploadSessions() {
        List<AmpDEUploadSession> retVal = null;
        try {
            Session sess = PersistenceManager.getRequestDBSession();
            Query q = sess.createQuery(new StringBuilder("from ").
                    append(AmpDEUploadSession.class.getName()).append(" us order by us.uploadDate").toString());
            retVal = (List<AmpDEUploadSession>) q.list();
        } catch (DgException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return retVal;
    }

    public static void deleteMappings (Set<Long> ids) {
        try {
            Session sess = PersistenceManager.getRequestDBSession();

            StringBuilder delStr = new StringBuilder("delete from ").
                append(DEMappingFields.class.getName()).append(" m where m.id in (").
                append(generateIds(ids)).append(")");


            StringBuilder getStr = new StringBuilder("from ").
                    append(DEMappingFields.class.getName()).append(" m where m.id in (").
                    append(generateIds(ids)).append(")");


            List<DEMappingFields> fromDb = sess.createQuery(getStr.toString()).list();

            for (DEMappingFields fld : fromDb) {
                for (AmpDEUploadSession us : fld.getUploadSessionsLinked()) {
                    us.getMappedFields().remove(fld);
                    saveObject(us);
//                    fld.getUploadSessionsLinked().remove(us);
                }
//                saveObject(fld);

            }

            sess.createQuery(delStr.toString()).executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static String generateIds (Set<Long> ids) {
        StringBuilder retVal = new StringBuilder();
        Iterator<Long> it = ids.iterator();
        while (it.hasNext()) {
            retVal.append(it.next());
            if (it.hasNext()) retVal.append(",");
        }
        return retVal.toString();
    }

    private static String generateNames (Set<String> names) {
        StringBuilder retVal = new StringBuilder();
        Iterator<String> it = names.iterator();
        while (it.hasNext()) {
            retVal.append("'").append(it.next()).append("'");
            if (it.hasNext()) retVal.append(",");
        }
        return retVal.toString();
    }

    public static List<IatiCodeType> getCodetypeListByNames(Set<String> names) {
        List<IatiCodeType> retVal = null;
        try {
            StringBuilder queryStr = new StringBuilder("from ").
                    append(IatiCodeType.class.getName()).
                    append(" ct where ct.name in (").append(generateNames(names)).
                    append(")");
            Session sess = PersistenceManager.getRequestDBSession();
            retVal = sess.createQuery(queryStr.toString()).list();
        } catch (DgException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return retVal;
    }

    public static List<IatiCodeType> getAllCodetypes() {
        List<IatiCodeType> retVal = null;
        try {
            StringBuilder queryStr = new StringBuilder("from ").
                    append(IatiCodeType.class.getName());
            Session sess = PersistenceManager.getRequestDBSession();
            retVal = sess.createQuery(queryStr.toString()).list();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return retVal;
    }


}
