package monetmonitor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.cwi.monetdb.mcl.MCLException;



/**
 * 
 * @author acartaleanu
 * derived from MonetBeholder
 *
 */
public class MonetBeholderConnectivity extends MonetBeholder{

	
	
	public MonetBeholderConnectivity() {
//		internalMonetTables = new HashSet<String>();
		super();
//		initInternalMonetTables();
	}
	


	
	
	/**
	 * runs a select that will always return something (table names)
	 * @throws SQLException
	 */
	protected void runQuery() throws nl.cwi.monetdb.mcl.MCLException, java.sql.SQLException{
		String tableListQuery = "select tables.name from tables";
		String url = "jdbc:monetdb://localhost/"+ Constants.getDbName();
		List<Object> tableNames = runSelect(DriverManager.getConnection(url, "monetdb", "monetdb"), tableListQuery);
	}
	
	

}
