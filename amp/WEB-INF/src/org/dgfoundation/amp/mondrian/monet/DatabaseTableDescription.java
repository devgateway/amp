package org.dgfoundation.amp.mondrian.monet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.mondrian.MondrianETL;

/**
 * holds a description of a table
 * @author Constantin Dolghier
 *
 */
public class DatabaseTableDescription {
	
	protected static Logger logger = Logger.getLogger(MondrianETL.class);
	
	public final Map<String, DatabaseTableColumn> columns;
	public final String tableName;
	
	public DatabaseTableDescription(String tableName, List<DatabaseTableColumn> _cols) {
		this.tableName = tableName;
		Map<String, DatabaseTableColumn> cols = new LinkedHashMap<String, DatabaseTableColumn>();
		for(DatabaseTableColumn col:_cols)
			cols.put(col.columnName, col);
		columns = Collections.unmodifiableMap(cols);
	}
	
	/**
	 * returns a MonetDB-suited description of a table
	 * @param tableName
	 * @param rs
	 * @return
	 */
	public static DatabaseTableDescription describeResultSet(String tableName, DbColumnTypesMapper columnTypesMapper, ResultSet rs) {
		try {
			List<DatabaseTableColumn> columns = new ArrayList<>();
			for(int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
				int columnType = rs.getMetaData().getColumnType(i); // java.sql.Types
				int columnWidth = rs.getMetaData().getColumnDisplaySize(i);
				String columnName = rs.getMetaData().getColumnLabel(i);
				String columnTypeMonetName = columnTypesMapper.mapSqlTypeToName(columnType, columnWidth);
				columns.add(new DatabaseTableColumn(columnName, columnTypeMonetName, false));
			}
			return new DatabaseTableDescription(tableName, columns);
		}
		catch(Exception e) {throw new RuntimeException(e);}
	}
	
	
	/**
	 * creates a database matching the description on the given connection
	 * @param conn
	 * @param createIndices
	 * @throws SQLException
	 */
	public void create(Connection conn, boolean createIndices) throws SQLException {
		StringBuffer query = new StringBuffer(String.format("CREATE TABLE %s (", tableName));
		boolean first = true;
		for (DatabaseTableColumn col:columns.values()) {
			if (!first) query.append(", ");
			query.append(String.format("%s %s", col.columnName, col.columnDefinition));
			first = false;
		}
		query.append(")");
		SQLUtils.executeQuery(conn, query.toString());
		
		if (createIndices) {
			logger.warn(String.format("Creating %d indices for Mondrian table %s", columns.values().stream().filter(z -> z.indexed).count(), this.tableName));
			for(DatabaseTableColumn col:columns.values())
				if (col.indexed) {
					String q = String.format("CREATE INDEX %s_%s_idx ON %s(%s)", tableName, col.columnName, tableName, col.columnName);
					SQLUtils.executeQuery(conn, q);
				}
		}
		SQLUtils.flush(conn);
	}
}
