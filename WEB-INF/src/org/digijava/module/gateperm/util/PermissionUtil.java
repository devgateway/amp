/**
 * PermissionUtil.java (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.util;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.gateperm.core.Permissible;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;

/**
 * PermissionUtil.java TODO description here
 * 
 * @author mihai
 * @package org.digijava.module.gateperm.util
 * @since 05.09.2007
 */
public final class PermissionUtil {
    private static Logger logger = Logger.getLogger(PermissionUtil.class);

    public static List<Permission> getAllPermissions() {
	Session session;

	try {
	    session = PersistenceManager.getSession();
	    Query query = session.createQuery(" from " + Permission.class.getName());
	    List list = query.list();

	    return list;
	} catch (HibernateException e) {
	    logger.error(e);
	    throw new RuntimeException("HibernateException Exception encountered", e);
	} catch (SQLException e) {
	    logger.error(e);
	    throw new RuntimeException("SQLException Exception encountered", e);
	}

    }

    public static Map<Long, PermissionMap> getAllPermissionMapsForPermissibleClass(Class permClass) {
	Session session;
	try {
	    session = PersistenceManager.getSession();

	    Query query = session.createQuery("SELECT p from " + PermissionMap.class.getName()
		    + " p WHERE p.permissibleCategory=:categoryName AND p.objectIdentifier is not null");
	    query.setParameter("categoryName", permClass.getSimpleName());
	    List col = query.list();
	    Map<Long, PermissionMap> ret = new HashMap<Long, PermissionMap>();
	    Iterator i = col.iterator();
	    while (i.hasNext()) {
		PermissionMap element = (PermissionMap) i.next();
		ret.put(element.getObjectIdentifier(), element);
	    }
	    return ret;
	} catch (HibernateException e) {
	    logger.error(e);
	    throw new RuntimeException("HibernateException Exception encountered", e);
	} catch (SQLException e) {
	    logger.error(e);
	    // TODO Auto-generated catch block
	    throw new RuntimeException("SQLException Exception encountered", e);
	}

    }
    
    public static PermissionMap getGlobalPermissionForPermissibleClass(Class permClass) {
	  try {
	    Session session = PersistenceManager.getSession();
	    Query query = session.createQuery("SELECT p from " + PermissionMap.class.getName()
		    + " p WHERE p.permissibleCategory=:categoryName AND p.objectIdentifier is null");
	    query.setParameter("categoryName", permClass.getSimpleName());
	    List col = query.list();
	    PersistenceManager.releaseSession(session);
	    if(col.size()==0) return null;
	    return (PermissionMap) col.get(0);
	} catch (HibernateException e) {
	    logger.error(e);
	    throw new RuntimeException( "HibernateException Exception encountered", e);
	} catch (SQLException e) {
	    logger.error(e);
	    throw new RuntimeException( "SQLException Exception encountered", e);
	}	  
	  
    }
    

    public static PermissionMap getPermissionMapForPermissible(Permissible obj) {
	Session session;
	try {
	    session = PersistenceManager.getSession();

	    Query query = session.createQuery("SELECT p from " + PermissionMap.class.getName()
		    + " p WHERE p.permissibleCategory=:categoryName AND (p.objectIdentifier is null OR p.objectIdentifier=:objectId) ORDER BY p.objectId");
	    query.setParameter("objectId", obj.getIdentifier());
	    query.setParameter("categoryName", obj.getPermissibleCategory().getSimpleName());
	    List col = query.list();

	    if (col.size() == 0)
		return null;

	    if (col.size() == 1)
		return (PermissionMap) col.get(0);
	    Iterator i = col.iterator();
	    while (i.hasNext()) {
		PermissionMap element = (PermissionMap) i.next();
		if (element.getObjectIdentifier() != null)
		    return element;
	    }

	    return null;

	} catch (HibernateException e) {
	    logger.error(e);
	    throw new RuntimeException("HibernateException Exception encountered", e);
	} catch (SQLException e) {
	    logger.error(e);
	    throw new RuntimeException("SQLException Exception encountered", e);
	}

    }

    
    
    
    public static Map<Long, String> getAllPermissibleObjectLabelsForPermissibleClass(Class permClass) {
	Map<Long, String> ret = new HashMap<Long, String>();
	Session session = null;
	try {
	    session = PersistenceManager.getSession();
	    String idProperty = Permissible.getPermissiblePropertyName(permClass,
		    Permissible.PermissibleProperty.PROPERTY_TYPE_ID);
	    String labelProperty = Permissible.getPermissiblePropertyName(permClass,
		    Permissible.PermissibleProperty.PROPERTY_TYPE_LABEL);
	    Query q = session.createQuery("SELECT p." + idProperty + ",p." + labelProperty + " from "
		    + permClass.getName() + " p");
	    List list = q.list();
	    Iterator i = list.iterator();
	    while (i.hasNext()) {
		Object[] element = (Object[]) i.next();
		ret.put((Long) element[0], (String) element[1]);
	    }
	    return ret;

	} catch (HibernateException e) {

	    throw new RuntimeException("HibernateException Exception encountered", e);
	} catch (SQLException e) {
	    logger.error(e);
	    throw new RuntimeException("SQLException Exception encountered", e);
	} finally {
	    try {
		session.flush();
		session.close();
		PersistenceManager.releaseSession(session);
	    } catch (HibernateException e) {
		logger.error(e);
		throw new RuntimeException("HibernateException Exception encountered", e);
	    } catch (SQLException e) {
		logger.error(e);
		throw new RuntimeException("SQLException Exception encountered", e);
	    }

	}

    }

}
