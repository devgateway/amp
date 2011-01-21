package org.dgfoundation.amp.test.workspacemanager;

import java.sql.SQLException;

import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpApplicationSettings;
import org.digijava.module.aim.dbentity.AmpFiscalCalendar;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import junit.framework.TestCase;

/**
 * @author Gabriel
 */
public class WorkspaceDataTest extends TestCase {
	private Session session = null;

	protected void setUp() throws Exception {
		Configuration.initConfig();
		session = PersistenceManager.getSession();
	}

	protected void tearDown() throws Exception {
		session.close();
	}

	public void testApplicationSettingsDataConsistency()
			throws HibernateException, SQLException {

		String queryString = null;
		Query qry = null;
		// TODO: replace SQL with HQL.
		queryString = "from " + AmpApplicationSettings.class.getName()
				+ " WHERE fis_cal_id IS NULL";
		qry = session.createQuery(queryString);
		assertFalse(
				"ERROR: The field 'amp_application_settings.fis_cal_id' cant be null. May fail saving a new activity with fundings.",
				qry.list().size() != 0);
	}
}
