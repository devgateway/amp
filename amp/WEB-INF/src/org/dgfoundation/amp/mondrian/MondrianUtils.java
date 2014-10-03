package org.dgfoundation.amp.mondrian;

import java.sql.Connection;
import java.sql.SQLException;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

/**
 * various Mondrian ETL-related utils
 * @author Constantin Dolghier
 *
 */
public class MondrianUtils {
	
	/**
	 * called at startup to check sanity of mondrian-etl-related views
	 * @param session
	 */
	public static void checkMondrianViewsSanity(Session session) {
		session.doWork(new Work() {

			@Override
			public void execute(Connection connection) throws SQLException {
				doTheCheck(connection);
			}
			
		});
	}
	
	private static void doTheCheck(Connection connection) throws SQLException {
		long corNrRows = SQLUtils.countRows(connection, "amp_activity_version");
		long nrLangs = SQLUtils.getLong(connection, "select count(*) from dg_site_trans_lang_map langs where langs.site_id = 3");
		for(MondrianTableDescription table:MondrianTablesRepository.MONDRIAN_ACTIVITY_DIMENSIONS) {
//			if (corNrRows > 12000 && table == MondrianTablesRepository.MONDRIAN_LONG_TEXTS)
//				continue; // postgres sucks at joining on varchars
			long nrRows = SQLUtils.countRows(connection, "v_" + table.tableName);			
			long divider = table.isFiltering ? nrLangs : 1;
			if (nrRows != corNrRows * divider)
				throw new RuntimeException("the view corresponding to the Mondrian ETL table " + table.tableName + " has " + nrRows + " instead of " + corNrRows * divider);
		}
	}
}
