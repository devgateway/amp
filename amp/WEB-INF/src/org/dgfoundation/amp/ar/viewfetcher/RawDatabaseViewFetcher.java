package org.dgfoundation.amp.ar.viewfetcher;

import org.dgfoundation.amp.ar.FilterParam;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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
    public RsInfo fetchRows(ArrayList<FilterParam> params) throws SQLException
    {
        StringBuilder columns = new StringBuilder();
        for(String columnName:columnNames){
            if (columns.length() > 0)
                columns.append(", ");
            columns.append("\"" + columnName + "\"");
        }
        String query = "SELECT " + columns.toString() + " FROM " + this.viewName + " " + this.condition;
        return SQLUtils.rawRunQuery(connection, query, params);
    }
}
