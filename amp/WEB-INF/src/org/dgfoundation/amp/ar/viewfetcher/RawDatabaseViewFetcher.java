package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import org.dgfoundation.amp.ar.FilterParam;

/**
 * a {@link ViewFetcher} which rawly fetches data from a view/table
 * @author Dolghier Constantin
 *
 */
public class RawDatabaseViewFetcher extends DatabaseViewFetcher
{
	public RawDatabaseViewFetcher(String viewName, String condition, Connection connection, String... rawColumnNames)
	{
		super(viewName, condition, connection, rawColumnNames);
	}
	
	@Override
	public ResultSet fetchRows(ArrayList<FilterParam> params) throws SQLException
	{
		String query = "SELECT " + SQLUtils.generateCSV(this.columnNames) + " FROM " + this.viewName + " " + this.condition;
		return SQLUtils.rawRunQuery(connection, query, params);
	}
}
