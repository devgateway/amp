package org.digijava.module.dataExchange.util;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dgfoundation.amp.error.AMPException;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.dataExchange.dbentity.AmpDEUploadSession;
import org.digijava.module.dataExchange.dbentity.DEMappingFields;
import org.digijava.module.dataExchange.dbentity.IatiCodeItem;
import org.digijava.module.dataExchange.dbentity.IatiCodeType;
import org.digijava.module.dataExchange.util.DataExchangeConstants.IatiCodeTypeEnum;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Created with IntelliJ IDEA.
 * User: flyer
 * Date: 3/17/14
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class DbUtil {
    private static Logger logger = Logger.getLogger(DbUtil.class);

    public static void saveObjectSet(Set<?> objs) {
    	Session sess = PersistenceManager.getSession();
    	for (Object o : objs) {
    		sess.saveOrUpdate(o);
    	}
    }

    public static void saveObject(Object obj) {
    	PersistenceManager.getSession().saveOrUpdate(obj);
    }

    public static Object getObject(Class clazz, Long id) {
    	return PersistenceManager.getSession().load(clazz, id);
    }

    public static AmpDEUploadSession getAmpDEUploadSession(Long id) {
        return getAmpDEUploadSession(id, false);
    }

    public static AmpDEUploadSession getAmpDEUploadSession(Long id, boolean initFieldsCollection) {
        AmpDEUploadSession retVal = null;
        Session sess = PersistenceManager.getRequestDBSession();
        Query q = sess.createQuery(new StringBuilder("from ")
        	.append(AmpDEUploadSession.class.getName())
        	.append(" us where us.id=:US_ID").toString());
        q.setLong("US_ID", id);
        retVal = (AmpDEUploadSession) q.uniqueResult();
        if (initFieldsCollection) {
        	Hibernate.initialize(retVal.getLogItems());
        }
        return retVal;
    }

    public static List<AmpDEUploadSession> getAllAmpDEUploadSessions() {
    	return PersistenceManager.getSession().createQuery(new StringBuilder("from ").
    			append(AmpDEUploadSession.class.getName()).append(" us order by us.uploadDate").toString())
    			.list();
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
        StringBuilder queryStr = new StringBuilder("from ").
        		append(IatiCodeType.class.getName()).
        		append(" ct where ct.name in (").append(generateNames(names)).
        		append(")");
        return PersistenceManager.getSession().createQuery(queryStr.toString()).list();
    }

    public static List<IatiCodeType> getAllCodetypes() {
        List<IatiCodeType> retVal = null;
        try {
            StringBuilder queryStr = new StringBuilder("from ").
                    append(IatiCodeType.class.getName()).
                    append(" ct order by ct.name");
            Session sess = PersistenceManager.getRequestDBSession();
            retVal = sess.createQuery(queryStr.toString()).list();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return retVal;
    }
    
    public static IatiCodeType getIatiCodeTypeByName(IatiCodeTypeEnum codeTypeName) throws AMPException {
    	IatiCodeType codeType = null;
    	String queryStr = "select ct from " + IatiCodeType.class.getName() + " ct where ct.name=:name";
    	try {
			Query qry = PersistenceManager.getRequestDBSession().createQuery(queryStr);
			qry.setParameter("name", codeTypeName.toString());
			codeType = (IatiCodeType)qry.uniqueResult();
		} catch (HibernateException e) {
			logger.error("Cannot retrieve getIatiCodeTypeByName("+codeTypeName+"): "+e.getMessage());
			throw new AMPException(e.getCause());
		}
    	return codeType;
    }
    
    public static IatiCodeItem getIatiCodeByCode(String code, Long codeTypeId) throws AMPException {
    	return getIatiCodeByNameOrCode(null, code, codeTypeId);
    }
    
    public static IatiCodeItem getIatiCodeByName(String name, Long codeTypeId) throws AMPException {
    	return getIatiCodeByNameOrCode(name, null, codeTypeId);
    }
    
    private static IatiCodeItem getIatiCodeByNameOrCode(String name, String code, Long codeTypeId) throws AMPException {
    	IatiCodeItem retVal = null;
    	boolean and = false;
    	try {
    		String q = "select ici from "+IatiCodeItem.class.getName()+" ici where ici.type.id=:codeTypeId and";
    		if (StringUtils.isNotBlank(name)) {
    			q +=" lower(ici.name) like '"+name.toLowerCase().replace("'",  "''")+"'";
    			and  = true;
    		}
    		if (StringUtils.isNotBlank(code))
    			q += (and ? " and" : "") + " ici.code like '" + code + "'";
    		Query qry = PersistenceManager.getRequestDBSession().createQuery(q);
    		qry.setParameter("codeTypeId", BigInteger.valueOf(codeTypeId));
    		retVal = (IatiCodeItem)qry.uniqueResult();
    	} catch (NonUniqueResultException ex) {
    		logger.error("Multiple IatiCodes for name='"+name+"'"); 
    		throw new AMPException(ex);
    	} catch (Exception ex) {
			logger.error("Could not retrieve Iati Code: " + ex.getMessage());
			throw new AMPException(ex);
		}
    	return retVal;
    }
    
}
