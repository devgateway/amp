/**
 * XmlPatcherSQLLangWorker.java
 * (c) 2009 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.digijava.module.xmlpatcher.worker;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import org.digijava.module.xmlpatcher.dbentity.AmpXmlPatchLog;
import org.digijava.module.xmlpatcher.exception.XmlPatcherLangWorkerException;
import org.digijava.module.xmlpatcher.exception.XmlPatcherWorkerException;
import org.digijava.module.xmlpatcher.jaxb.Lang;
import org.digijava.module.xmlpatcher.util.XmlPatcherUtil;
import org.hibernate.HibernateException;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *         <p>
 *         Provides support to process generic SQL lang-type Use this with all
 *         generic SQL scripts, that do not require special server-specific
 *         queries
 */
public class XmlPatcherSQLLangWorker extends XmlPatcherLangWorker {

	/**
	 * @param entity
	 * @param log
	 */
	public XmlPatcherSQLLangWorker(Lang entity, AmpXmlPatchLog log) {
		super(entity, log);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.digijava.module.xmlpatcher.worker.XmlPatcherWorker#process()
	 */
	@Override
	protected boolean process() throws XmlPatcherWorkerException {
		try {

			// get the jdbc connection from the Session Factory
			Connection con = XmlPatcherUtil.getConnection();

			con.setAutoCommit(false); // prevent auto commits. We'd like to
			// rollback the entire portion if needed
			Statement statement = con.createStatement();

			// tokenize the SQL using the delimiter specified as attribute
			// (default=";")
			StringTokenizer stok = new StringTokenizer(getEntity().getValue()
					.trim(), getEntity().getDelimiter());
			while (stok.hasMoreTokens()) {
				String sqlCommand = stok.nextToken();
				if (sqlCommand.trim().equals(""))
					continue;
				statement.addBatch(sqlCommand);
			}

			// try to execute the batches and commit the whole transaction
			// if things go wrong, rollback the connection and set it back to
			// autocommit=true
			try {
				statement.executeBatch();
				con.commit();
			} catch (BatchUpdateException e) {
				con.rollback();
				throw new XmlPatcherLangWorkerException(e);
			} finally {
				con.setAutoCommit(true);
				con.close();
			}

			return true;

		} catch (HibernateException e) {
			throw new XmlPatcherLangWorkerException(e);
		} catch (SQLException e) {
			throw new XmlPatcherLangWorkerException(e);
		}
	}

	@Override
	protected boolean runTimeCheck() throws XmlPatcherWorkerException {
		return true;
	}

}
