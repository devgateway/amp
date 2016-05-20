package org.dgfoundation.amp.mondrian.monet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.nireports.schema.GeneratedColumnBehaviour;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.helper.Constants;

/**
 * this class wraps a connection to PostgreSQL
 * @author Dolghier Constantin
 *
 */
public class PostgresConnection extends OlapDbConnection {

    private PostgresConnection() throws SQLException {
    	super(getDirectConnection(), getMapper());
        //this.conn = DriverManager.getConnection("jdbc:monetdb://localhost/amp_moldova_210", "monetdb", "monetdb");
    }

    /**
     * fix for AMP-18357 and AMP-18352: MonetDB + Tomcat-DBCP = ( X )
     * @return
     */
    private static Connection getDirectConnection() throws SQLException {
        return PersistenceManager.getJdbcConnection();
    }

    public static PostgresConnection getConnection() {
        try {
            return new PostgresConnection();
        }
        catch(SQLException e) {throw new RuntimeException(e);}
    }

    @Override
    public LinkedHashSet<String> getTableColumns(final String tableName, boolean crashOnDuplicates){
        return SQLUtils.getTableColumns(tableName, crashOnDuplicates);
    }


    @Override
    public LinkedHashMap<String, String> getTableColumnsWithTypes(final String tableName, boolean crashOnDuplicates){
        return SQLUtils.getTableColumnsWithTypes(conn, tableName, crashOnDuplicates);
    }

    @Override
    public Set<String> getTablesWithNameMatching(String begin) {
        return SQLUtils.getTablesWithNameMatching(conn, begin);
    }

//    /**
//     * pumps the result of running a query on a database in the enclosed Monet DB
//     * @param srcConn
//     * @param srcQuery
//     * @param destTableName
//     * @throws SQLException
//     */
//    public void createTableFromQuery(java.sql.Connection srcConn, String srcQuery, String destTableName) throws SQLException {
//        if (tableExists(destTableName)) {
//            dropTable(destTableName);
//        };
//    	SQLUtils.executeQuery(conn, "CREATE TABLE " + destTableName + " AS " + srcQuery);
//    }

    @Override
    public void copyTableFromPostgres(java.sql.Connection srcConn, String tableName) throws SQLException {
        // do nothing as the table already exists
    }

    /**
     * makes a snapshot of a query, which is copied to MonetDB
     * @param tableName
     * @param columnsToIndex columns on which to create indices
     * @throws SQLException
     */
    protected void createTableFromPostgresQuery(String tableName, java.sql.Connection srcConn, String tableCreationQuery) throws SQLException {
        dropTable(tableName);
        createTableFromQuery(srcConn, tableCreationQuery, tableName);
    }

    private static DataSource getPostgreSQLDataSource() {
        try {
            Context initialContext = new InitialContext();
            //DataSource res = (javax.sql.DataSource) initialContext.lookup(Constants.MONETDB_JNDI_ALIAS);
            DataSource res = (javax.sql.DataSource) initialContext.lookup(Constants.UNIFIED_JNDI_ALIAS);
            if (res == null)
                throw new Error("could not find Monet data source!");
            return res;
        }
        catch(Exception e) {
            throw new Error(e);
        }
    }

    public static DbColumnTypesMapper getMapper() {
        return new DbColumnTypesMapper() {

            @Override
            public String mapSqlTypeToName(int rsType, int maxWidth) {
                switch(rsType) {
                    case java.sql.Types.TINYINT:
                    case java.sql.Types.SMALLINT:
                    case java.sql.Types.INTEGER:
                    case java.sql.Types.BIGINT:
                        return "integer";

                    case java.sql.Types.FLOAT:
                    case java.sql.Types.REAL:
                    case java.sql.Types.DOUBLE:
                    case java.sql.Types.NUMERIC:
                    case java.sql.Types.DECIMAL:
                        return "double precision";

                    case java.sql.Types.CHAR:
                    case java.sql.Types.VARCHAR:
                        if (maxWidth <= 255)
                            return "varchar(255)";

                        // intentional fall-through
                    case java.sql.Types.LONGVARCHAR:
                        return "text";

                    case java.sql.Types.DATE:
                    case java.sql.Types.TIME:
                    case java.sql.Types.TIMESTAMP:
                    case 2013: //java.sql.Types.TIME_WITH_TIMEZONE:
                    case 2014: //java.sql.Types.TIMESTAMP_WITH_TIMEZONE:
                        return "date";

                    case java.sql.Types.BIT:
                        return "boolean";

                    default:
                        throw new RuntimeException("don't know how to map this column type to MonetDB: " + rsType);
                }
            }
        };
    }
    
    public static EtlStrategy buildStrategy() {
    	return new EtlStrategy() {
			
			@Override
			public OlapDbConnection getOlapConnection() {
				return getConnection();
			}
			
			@Override
			public String getDataSourceString() {
				return String.format("jdbc:mondrian:DataSource=java:comp/env/ampDS");
			}

			@Override
			public DbColumnTypesMapper getColumnTypesMapper() {
				return getMapper();
			}

			@Override
			public boolean isColumnarDatabase() {
				return false;
			}
		};
    }
}
