/**
 * PermissionUtil.java
 * (c) 2007 Development Gateway Foundation
 */
package org.digijava.module.gateperm.util;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

import org.apache.log4j.Logger;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.core.PermissionMap;

/**
 * PermissionUtil.java
 * TODO description here
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
			Query query = session
					.createQuery(" from " + Permission.class.getName());
			List list = query.list();
			
			return list;
		} catch (HibernateException e) {
			logger.error(e);
			throw new RuntimeException(
					"HibernateException Exception encountered", e);
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException("SQLException Exception encountered", e);
		}

	}

	public static Collection<Permission> getPermissionsForObjectOfCategory(Object object,String categoryName) {
		Session session;
		try {
			session = PersistenceManager.getSession();
		
		Query query = session.createQuery("SELECT p from "
				+ PermissionMap.class.getName() + " p WHERE p.categoryName=:categoryName AND (p.objectId == null OR p.objectId=:objectId");
		query.setParameter("objectId", object).toString();
		query.setParameter("categoryName", categoryName);
		List col = query.list();
		Set<Permission> ret=new TreeSet<Permission>();
		Iterator i=col.iterator();
		while (i.hasNext()) {
			PermissionMap element = (PermissionMap) i.next();
			ret.add(element.getPermission());
		}
		
		return ret;
		
		} catch (HibernateException e) {
			logger.error(e);
			throw new RuntimeException( "HibernateException Exception encountered", e);
		} catch (SQLException e) {
			logger.error(e);
			throw new RuntimeException( "SQLException Exception encountered", e);
		}
		
	}
}
