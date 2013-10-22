package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * a class which calculates the value of a column in a row based on other values in the same row
 * @author Dolghier Constantin
 *
 */
public interface ColumnValueCalculator {
	
	/**
	 * calculates the value of the column based on other columns. <b>NEVER EVER do anything except getLong() / getString() on the resultSet!</b>
	 * @param resultSet  <b>NEVER EVER do anything except getLong() / getString() on the resultSet!</b>
	 * @return
	 */
	public String calculateValue(ResultSet resultSet) throws SQLException;
}
