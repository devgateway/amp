/**
 * 
 */
package org.digijava.module.dataExchange.util;

import java.sql.SQLException;

import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.HibernateException;

/**
 * @author Alex Gartner
 *
 */
public class SessionSourceSettingDAO extends SourceSettingDAO {

	/**
	 * @throws HibernateException
	 * @throws SQLException
	 * @throws DgException 
	 */
	public SessionSourceSettingDAO() throws HibernateException, SQLException, DgException {
		this.hbSession	= PersistenceManager.getRequestDBSession();
	}

}
