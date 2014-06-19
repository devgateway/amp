package org.digijava.kernel.ampapi.mondrian.util;
//TODO: Add documentation to this calss;
/***
 * 
 * 
 *
 */
public class Connection {

	private static String separator = System.getProperty("file.separator");
	private static String connection;

	/**
	 * 
	 * @param path
	 * @return
	 */
	
	public static String getConnection(String path) {
		connection = "jdbc:mondrian:Datasource=java:comp/env/ampDS;"
				+ "Catalog=file:" + path + separator + "queries" + separator
				+ "AMP.xml;";
		return connection;

	}
}
