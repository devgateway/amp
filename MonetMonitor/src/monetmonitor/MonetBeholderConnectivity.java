package monetmonitor;

import java.sql.Connection;

import java.sql.SQLException;




/**
 * 
 * @author acartaleanu
 * derived from MonetBeholder
 *
 */
public class MonetBeholderConnectivity extends MonetBeholder{

	
	
	public MonetBeholderConnectivity() {
		super();
	}
	


	
	
	/**
	 * runs a select that will always return something (table names)
	 * @throws SQLException
	 */
	protected void runQuery(Connection conn) throws nl.cwi.monetdb.mcl.MCLException, java.sql.SQLException{
		String tableListQuery = "select tables.name from tables";
//		String tableListQuerySELECT c.name, c.type FROM sys.columns c WHERE c.table_id = (SELECT t.id FROM sys.tables t WHERE t.name='etl_fingerprints') ORDER BY c.number
		runSelect(conn, tableListQuery);
	}
	
	

}
