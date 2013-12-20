package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * simple delegating ColumnValueCalculator which strips the common stuff off the (one-liner) concrete classes
 * @author Dolghier Constantin
 *
 */
public abstract class SimpleColumnValueCalculator implements ColumnValueCalculator {
	
	public String calculateValue(ResultSet resultSet, java.sql.ResultSet rawCurrentLine) throws SQLException
	{
		return calculateValue(resultSet);
	}
	
	protected abstract String calculateValue(ResultSet resultSet) throws SQLException;
	
	@Override
	public boolean getDeleteOriginal()
	{
		return true;
	}
}
