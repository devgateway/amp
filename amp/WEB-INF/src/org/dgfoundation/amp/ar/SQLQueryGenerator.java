package org.dgfoundation.amp.ar;

import java.util.Collection;
import java.util.Map;

/**
 * Interface implemented by classes that will generate a SQL query based on the ids
 * of the elements to query
 *  
 * @author eperez
 *
 */
public interface SQLQueryGenerator {
	
	/**
	 * Generates a SQL query based on the map containing the ids of the elements and
	 * whether the query should include or exclude those elements
	 * @param ids Map <String,Boolean> with each id of the elements and whether the query should
	 * include or exclude (negative filter) that element
	 * @return String, the sql query
	 */
	public String generateSQLQuery (Map<String, Boolean> ids);
	
	/**
	 * Generates a SQL query based on the map containing the ids of the elements
	 * @param ids Collection <String> with each id of the elements and w
	 * @return String, the sql query
	 */
	public String generateSQLQuery (Collection <String> ids);
	
}
