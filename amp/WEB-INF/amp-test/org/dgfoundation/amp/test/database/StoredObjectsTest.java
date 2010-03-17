/**
 * StoredObjectsTest.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.test.database;

import java.sql.SQLException;
import java.util.List;

import org.dgfoundation.amp.test.util.Configuration;
import org.digijava.kernel.persistence.PersistenceManager;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;

import junit.framework.TestCase;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *
 */
public class StoredObjectsTest extends TestCase {

	private Session session = null;

	@Override
	protected void setUp() throws Exception {
		Configuration.initConfig();
		session = PersistenceManager.getSession();
	}
	
	@Override
	protected void tearDown() throws Exception {
		session.close();
	}

	/**
	 * This test makes sure no stored views has cross schema references,
	 * meaning they refer tables from another schema than the current schema.
	 * Developers sometimes copy/paste view definitions from the database they work on
	 * and they forget to remove the schema prefix.
	 * This test is required by MySQL-only databases
	 * @throws HibernateException
	 * @throws SQLException
	 */
	public void testCrossSchemaViewReferences() throws HibernateException,
	SQLException {
		List list = session.createSQLQuery("select table_name from information_schema.views where match (view_definition) against " +
				"( (select group_concat(distinct(table_schema) separator ' ') from information_schema.views where " +
				"table_schema!=database()) in boolean mode) and table_schema=database()").list();
		assertTrue("The following SQL views hold invalid definitions. They refer external table schemas: "+list,list.size()==0);		
	}
	
	
	/**
	 * 
	 */
	public StoredObjectsTest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public StoredObjectsTest(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
