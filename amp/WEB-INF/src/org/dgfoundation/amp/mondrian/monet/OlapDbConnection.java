package org.dgfoundation.amp.mondrian.monet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.dgfoundation.amp.algo.AlgoUtils;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.kernel.persistence.PersistenceManager;

/**
 * this class wraps a connection to MonetDB
 * @author Dolghier Constantin
 *
 */
public abstract class OlapDbConnection implements AutoCloseable {

    public final Connection conn;
    public final DbColumnTypesMapper mapper;

    protected OlapDbConnection(Connection olapConn, DbColumnTypesMapper mapper) {
    	try {
    		this.mapper = mapper;
    		this.conn = olapConn;
    		this.conn.setAutoCommit(false);
    	}
    	catch(Exception e) {
    		throw AlgoUtils.translateException(e);
    	}
    }

    public abstract String getJdbcUrl();
    
    @Override public void finalize() {
        close();
    }

    @Override public void close(){
        PersistenceManager.closeQuietly(conn);
    }

    /**
     * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
     * @param tableName - the table / view whose columns to fetch
     * @param crashOnDuplicates - whether to throw exception in case the table/view has duplicate names
     * @return
     * @throws SQLException
     */
    public abstract LinkedHashSet<String> getTableColumns(final String tableName, boolean crashOnDuplicates);

    /**
     * returns the list of all the columns of a table / view, in the same order as they appear in the table/view definition
     * @param tableName - the table / view whose columns to fetch
     * @param crashOnDuplicates - whether to throw exception in case the table/view has duplicate names
     * @return Map<ColumnName, data_type>
     */
    public abstract LinkedHashMap<String, String> getTableColumnsWithTypes(final String tableName, boolean crashOnDuplicates);

    public abstract Set<String> getTablesWithNameMatching(String begin);

    /**
     * equivalent to calling {@link #getTableColumns(String, false)}
     * @param tableName
     * @return
     */
    public LinkedHashSet<String> getTableColumns(final String tableName) {
        return getTableColumns(tableName, false);
    }

    public boolean tableExists(String tableName) {
        return !getTableColumns(tableName).isEmpty();
    }

    public void flush() {
        try {
            SQLUtils.flush(conn);
        }
        catch(Exception e) {}
    }

    public boolean dropTable(String tableName) {
    	return dropTableOrView(tableName, "TABLE");
    }
    
    public boolean dropView(String viewName) {
    	return dropTableOrView(viewName, "VIEW");
    }
    
    protected boolean dropTableOrView(String entityName, String type) {
        try {
            flush();
            if (tableExists(entityName)) {
                SQLUtils.executeQuery(this.conn, "DROP " + type + " "+ entityName + " CASCADE");
                flush();
            }
        } catch (Exception e) {return false;}
        return true;
    }

    /**
     * pumps the result of running a query on a database in the enclosed Monet DB
     * @param srcConn
     * @param srcQuery
     * @param destTableName
     * @throws SQLException
     */
    public abstract void createTableFromQuery(java.sql.Connection srcConn, String srcQuery, String destTableName) throws SQLException;

    public abstract void copyTableFromPostgres(java.sql.Connection srcConn, String tableName) throws SQLException;

    /**
     * copies entries contained in a RS to the DB
     * @param destTableName
     * @param rs
     */
    public void copyEntries(String destTableName, ResultSet rs) throws SQLException {
        List<List<Object>> rows = new ArrayList<>();
        int nrColumns = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            List<Object> line = new ArrayList<>();
            for(int i = 1; i <= nrColumns; i++)
                line.add(rs.getObject(i));
            rows.add(line);
        }
        if (rows.isEmpty())
            return; // nothing to do
        Collection<String> colNames = new ArrayList<String>(this.getTableColumns(destTableName)).subList(0, nrColumns);
        SQLUtils.insert(this.conn, destTableName, null, null, colNames, rows);
    }

    public void copyTableStructureFromPostgres(Connection srcConn, String srcTable, String destTable) throws SQLException {
        try(RsInfo rs = SQLUtils.rawRunQuery(srcConn, "select * from " + srcTable, null)) {
            DatabaseTableDescription tableDescription = DatabaseTableDescription.describeResultSet(destTable, mapper, rs.rs);
            dropTable(tableDescription.tableName);
            tableDescription.create(this.conn, false);
        }
    }

    public void executeQuery(String query){
        SQLUtils.executeQuery(conn, query);
    }
}
