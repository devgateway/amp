package org.dgfoundation.amp.test.database;

import java.sql.SQLException;

import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import junit.framework.TestCase;

/**
 * This class tests the data stored in some generic tables, if you think some of
 * this tests could fit in best on another test package please move it.
 * 
 * @author Gabriel
 * 
 */
public class DataBaseDataTest extends TestCase {
	private Session session = null;

	protected void setUp() throws Exception {
		Configuration.initConfig();
		session = PersistenceManager.getSession();
	}

	protected void tearDown() throws Exception {
		session.close();
	}

	/**
	 * Tests if some generic tables have records.
	 * 
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public void testNeededDataAvailability() throws HibernateException,
			SQLException {

		String queryString = null;
		Query qry = null;
		queryString = "from " + AmpFiscalCalendar.class.getName();
		qry = session.createQuery(queryString);
		assertFalse(
				"ERROR: The table 'amp_fiscal_calendar' cant be empty. May fail saving a new activity with fundings.",
				qry.list().size() == 0);
	}
}
