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
public class SessionImportLogDAO extends ImportLogDAO {

	/**
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public SessionImportLogDAO() throws HibernateException, SQLException, DgException {
		this.hbSession	= PersistenceManager.getRequestDBSession();
	}
	
	@Override
	protected void releaseSession() {
		;
	}

}
