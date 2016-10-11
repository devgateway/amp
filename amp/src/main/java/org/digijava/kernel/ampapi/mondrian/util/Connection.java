package org.digijava.kernel.ampapi.mondrian.util;

import java.nio.file.Paths;
import java.sql.DriverManager;

import javax.servlet.ServletRequest;

import org.dgfoundation.amp.mondrian.MondrianETL;
import org.digijava.kernel.request.TLSUtils;
import org.olap4j.OlapConnection;

//TODO: Add documentation to this class;
/***
 * 
 * 
 *
 */
public class Connection {

	public static boolean IS_TESTING = false;
	
	private static String separator = "/";//System.getProperty("file.separator");
	private static String connection;

	/**
	 * 
	 * @param path
	 * @return
	 */
	
	public static String getDefaultConnectionPath() {
		return getConnectionBySchemaPath(MoConstants.SCHEMA_PATH);
	}
	
	public static String getConnection(String rootPath) {
		connection = MondrianETL.CONNECTION_DS + ";"
				+ "Catalog=file:" + rootPath + "/saiku/saiku-repository/AMP.xml;PoolNeeded=false";
		return connection;

	}
	public static String getConnectionBySchemaPath(String schemaPath) {
		ServletRequest request = TLSUtils.getRequest();
		if (request!=null && !IS_TESTING) 
			schemaPath = request.getServletContext().getRealPath(schemaPath);
		else 
			schemaPath = Paths.get(schemaPath).toAbsolutePath().toString();
		return MondrianETL.CONNECTION_DS +";Catalog=file:" + schemaPath + ";" + MoConstants.SCHEMA_PROCESSOR;
	}
	
	public static OlapConnection getOlapConnectionByConnPath(String connPath) throws Exception {
		// Load the driver
		Class.forName("mondrian.olap4j.MondrianOlap4jDriver");
				
		// Connect
		final java.sql.Connection connection = DriverManager.getConnection(connPath);

		// We are dealing with an olap connection. we must unwrap it.
		final OlapConnection olapConnection = connection.unwrap(OlapConnection.class);
		return olapConnection;
	}

}
