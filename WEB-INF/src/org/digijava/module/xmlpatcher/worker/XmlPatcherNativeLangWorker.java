/**
 * XmlPatcherNativeLangWorker.java
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
import org.digijava.module.xmlpatcher.util.XmlPatcherConstants;
import org.digijava.module.xmlpatcher.util.XmlPatcherUtil;
import org.hibernate.HibernateException;

/**
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org Worker invoked for
 *         native based SQL languages
 */
public class XmlPatcherNativeLangWorker extends XmlPatcherLangWorker {

	/**
	 * @param entity
	 * @param log
	 */
	public XmlPatcherNativeLangWorker(Lang entity, AmpXmlPatchLog log) {
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
			// checks if the SQL is compatible with the server
			if (!XmlPatcherUtil.isSQLCompatible(getEntity().getValue()))
				return false;
			
			//get the jdbc connection from the Session Factory
			Connection con = XmlPatcherUtil.getConnection();
			
			con.setAutoCommit(false); //prevent auto commits. We'd like to rollback the entire portion if needed
			Statement statement = con.createStatement();
			
			//tokenize the SQL using the delimiter specified as attribute (default=";")
			StringTokenizer stok = new StringTokenizer(getEntity().getValue().trim(),
					getEntity().getDelimiter());
			while (stok.hasMoreTokens()) {
				String sqlCommand = stok.nextToken();
				if (sqlCommand.trim().equals(""))
					continue;
				statement.addBatch(sqlCommand);
			}

			//try to execute the batches and commit the whole transaction
			//if things go wrong, rollback the connection and set it back to autocommit=true
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

	/**
	 * Check if this native
	 * 
	 * @see org.digijava.module.xmlpatcher.worker.XmlPatcherWorker#runTimeCheck()
	 */
	@Override
	protected boolean runTimeCheck() throws XmlPatcherWorkerException {
		// check if the language type is supported by the xmlpatcher
		if (!super.runTimeCheck())
			return false;
		boolean langFound = false;
		for (int i = 0; i < XmlPatcherConstants.ScriptLangs.natives.length; i++) {
			if (XmlPatcherConstants.ScriptLangs.natives[i].equals(getEntity()
					.getType().value()))
				return langFound = true;
		}
		if (!langFound)
			throw new XmlPatcherLangWorkerException(
					"Unsupported native language " + getEntity().getType());

		return true;
	}

}
