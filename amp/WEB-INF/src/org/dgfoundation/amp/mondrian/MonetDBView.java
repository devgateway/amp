/**
 * 
 */
package org.dgfoundation.amp.mondrian;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.mondrian.monet.MonetConnection;
import org.digijava.kernel.ampapi.mondrian.util.MoConstants;



/**
 * This is a quick solution to generate a view in MonetDB. 
 * 
 * If more views will be needed we may think of a more generic approach and remove this implementation.
 * 
 * @author Nadejda Mandrescu
 */
public class MonetDBView {
	
	public static void createFactTableViewForNoDatesFilters(MonetConnection conn) throws SQLException {
		List<String> columnsList = new ArrayList<String>(
				conn.getTableColumns(MondrianTablesRepository.FACT_TABLE.tableName));
		
		if (columnsList.size() == 0)
			throw new RuntimeException("Unable to create '" + MondrianTablesRepository.FACT_TABLE_VIEW_NO_DATE_FILTER + 
					"': missing fact table itself");
		
		// drop view first, if exists
		conn.dropView(MondrianTablesRepository.FACT_TABLE_VIEW_NO_DATE_FILTER);
		
		String mirrorTransactionType = String.format("%1$s + %2$s as \"%1$s\"", MondrianTablesRepository.TRANSACTION_TYPE, 
				MoConstants.TRANSACTION_TYPE_GAP);
		Collections.replaceAll(columnsList, MondrianTablesRepository.TRANSACTION_TYPE, mirrorTransactionType);
		String columns = Util.collectionAsString(columnsList);
		
		String viewQuery = String.format("CREATE VIEW %1$s AS "
				+ "SELECT mf.* FROM %2$s mf "
				+ "UNION ALL "
				+ "SELECT %3$s FROM %2$s",
				MondrianTablesRepository.FACT_TABLE_VIEW_NO_DATE_FILTER, MondrianTablesRepository.FACT_TABLE.tableName, 
				columns);
		
		conn.executeQuery(viewQuery);
	}
}
