package org.dgfoundation.amp.mondrian.monet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.digijava.module.aim.helper.Constants;

/**
 * this class wraps a connection to MonetDB
 * @author Dolghier Constantin
 *
 */
public class MonetConnection extends OlapDbConnection {

    public static String MONET_CFG_OVERRIDE_URL = null;

    //private static DataSource dataSource = null;

    private MonetConnection() throws SQLException {
    	super(getDirectConnection(), getMapper());
        //this.conn = DriverManager.getConnection("jdbc:monetdb://localhost/amp_moldova_210", "monetdb", "monetdb");
    }

    /**
     * fix for AMP-18357 and AMP-18352: MonetDB + Tomcat-DBCP = ( X )
     * @return
     */
    private static Connection getDirectConnection() throws SQLException {
        return DriverManager.getConnection(_getJdbcUrl(), "monetdb", "monetdb");
    }

    private static String url = null;
    
    public static String _getJdbcUrl() {
        try{Class.forName("nl.cwi.monetdb.jdbc.MonetDriver");}catch(Exception e){throw new RuntimeException(e);}

        if (MONET_CFG_OVERRIDE_URL != null)
            return MONET_CFG_OVERRIDE_URL;
        if (url == null) {
            DataSource dataSource = getMonetDataSource();
            org.apache.tomcat.jdbc.pool.DataSource src = (org.apache.tomcat.jdbc.pool.DataSource) dataSource;
            String postgresUrl = src.getUrl().substring(0, src.getUrl().indexOf('?')); // jdbc:postgresql://localhost:5432/amp_moldova_210?useUnicode=true&characterEncoding=UTF-8&jdbcCompliantTruncation=false
            String dbName = postgresUrl.substring(postgresUrl.lastIndexOf('/') + 1);
            url = String.format("jdbc:monetdb://localhost/%s", dbName);
        }
        return url;
    }

    public static MonetConnection getConnection() {
        try {
            return new MonetConnection();
        }
        catch(SQLException e) {throw new RuntimeException(e);}
    }


    @Override
    public LinkedHashSet<String> getTableColumns(final String tableName, boolean crashOnDuplicates){
        return new LinkedHashSet<>(getTableColumnsWithTypes(tableName, crashOnDuplicates).keySet());
    }


    @Override
    public LinkedHashMap<String, String> getTableColumnsWithTypes(final String tableName, boolean crashOnDuplicates){
        String query = String.format("SELECT c.name, c.type FROM sys.columns c WHERE c.table_id = (SELECT t.id FROM sys.tables t WHERE t.name='%s') ORDER BY c.number", tableName.toLowerCase());
        return SQLUtils.getStringToStringMap(this.conn, tableName, query, crashOnDuplicates);
    }

    @Override
    public Set<String> getTablesWithNameMatching(String begin) {
        return SQLUtils.getTablesWithNameMatching(this.conn, "SELECT t.name FROM sys.tables t WHERE NOT t.system", begin);
    }

    @Override
    public void copyTableFromPostgres(java.sql.Connection srcConn, String tableName) throws SQLException {
        createTableFromPostgresQuery(tableName, srcConn, "select * from " + tableName);
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

    private static DataSource getMonetDataSource() {
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
                        return "double";

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
				return String.format("jdbc:mondrian:JdbcDrivers=nl.cwi.monetdb.jdbc.MonetDriver;Jdbc=%s;JdbcUser=monetdb;JdbcPassword=monetdb;PoolNeeded=false", 
						_getJdbcUrl());
			}

			@Override
			public DbColumnTypesMapper getColumnTypesMapper() {
				return getMapper();
			}

			@Override
			public boolean isColumnarDatabase() {
				return true;
			}
		};
    }
}
