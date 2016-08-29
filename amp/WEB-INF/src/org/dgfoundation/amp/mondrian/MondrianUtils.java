package org.dgfoundation.amp.mondrian;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

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
		long nrPledges = SQLUtils.countRows(connection, "amp_funding_pledges");
		long nrLangs = SQLUtils.getLong(connection, "select count(*) from dg_site_trans_lang_map langs where langs.site_id = 3");
			
		checkMTDAgainstPledgeCompanion(MondrianTablesRepository.MONDRIAN_RAW_DONOR_TRANSACTIONS_TABLE);
		
		for(MondrianTableDescription table:MondrianTablesRepository.MONDRIAN_ACTIVITY_DIMENSIONS) {
//			if (corNrRows > 12000 && table == MondrianTablesRepository.MONDRIAN_LONG_TEXTS)
//				continue; // postgres sucks at joining on varchars
			long nrRows = SQLUtils.countRows(connection, "v_" + table.tableName);			
			long divider = table.isFiltering ? nrLangs : 1;
			
			if (nrRows - table.supplementalRows != corNrRows * divider)
				throw new RuntimeException("the view corresponding to the Mondrian ETL table " + table.tableName + " has " + nrRows + " rows instead of " + corNrRows * divider);

			Set<String> viewColumns = SQLUtils.getTableColumns("v_" + table.tableName);			
			if (!viewColumns.iterator().next().equals("amp_activity_id"))
				throw new RuntimeException("the view corresponding to the Mondrian ETL table " + table.tableName + " does not have amp_activity_id as a first column"); 

			if (table.pledgeView != null) {
				// check the pledge view
				long nrPledgeRows = SQLUtils.countRows(connection, table.pledgeView);
				if (nrPledgeRows != nrPledges)
					throw new RuntimeException(String.format("the view corresponding to the Mondrian ETL table %s's pledge satellite has %d rows instead of %d", table.tableName, nrPledgeRows, nrPledges));
				
				checkMTDAgainstPledgeCompanion(table);
			}
		}
	}
	
	/**
	 * will crash if the table does not have a pledges companion
	 * @param table
	 */
	private static void checkMTDAgainstPledgeCompanion(MondrianTableDescription table) {
		Set<String> viewColumns = SQLUtils.getTableColumns("v_" + table.tableName);
		Set<String> pledgeViewColumns = SQLUtils.getTableColumns(table.pledgeView);
		viewColumns.remove("amp_activity_id");
		
		if (pledgeViewColumns.isEmpty())
			throw new RuntimeException(String.format("the view %s corresponding to the Mondrian ETL table %s's pledge satellite does not exist!", table.pledgeView, table.tableName));
		
		if (!pledgeViewColumns.iterator().next().equals("pledge_id"))
			throw new RuntimeException(String.format("the view corresponding to the Mondrian ETL table %s's pledge satellite does not have pledge_id as a first column!", table.tableName));
		
		pledgeViewColumns.remove("pledge_id");
		if (!viewColumns.toString().equals(pledgeViewColumns.toString()))
			throw new RuntimeException(String.format("the view corresponding to the Mondrian ETL table %s's pledge satellite has a different structure: %s instead of %s", table.tableName, pledgeViewColumns.toString(), viewColumns.toString()));

	}
}
