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
		runSelect(conn, tableListQuery);
	}
	
	

}
