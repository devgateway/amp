/**
 * 
 */
package org.dgfoundation.amp.permissionmanager.web;

import java.sql.SQLException;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.gateperm.core.Permission;
import org.digijava.module.gateperm.util.PermissionUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * @author dan
 *
 */
public final class PMUtil {

	public static void setGlobalPermission(Class globalPermissionMapForPermissibleClass, Permission permission,String simpleName) {
		// TODO Auto-generated method stub
		Session hs=null;
		try {
			hs = PermissionUtil.saveGlobalPermission(globalPermissionMapForPermissibleClass, permission.getId(), simpleName);
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DgException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(hs!=null){
			//pf.setPermissionId(new Long(0));
			try {
				PersistenceManager.releaseSession(hs);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	

}
