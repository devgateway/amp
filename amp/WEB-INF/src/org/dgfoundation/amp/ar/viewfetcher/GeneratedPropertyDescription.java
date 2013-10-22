package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.SQLException;
import java.util.*;


/**
 * generates a column's value for a row on an one-by-one basis, based on the values on the same row
 * @author Dolghier Constantin
 *
 */
public class GeneratedPropertyDescription implements PropertyDescription
{
	public final ColumnValueCalculator calculator;
	public final String viewName;
	public final String columnName;
	
	private final String _niceDescription;
	
	public GeneratedPropertyDescription(String viewName, String columnName, ColumnValueCalculator calculator)
	{
		this.viewName = viewName;
		this.columnName = columnName;
		
		this.calculator = calculator;
		if (this.calculator == null)
			throw new IllegalArgumentException("not allowed to supply a null Calculator here");
		
		this._niceDescription = String.format("%s.%s, calculated", viewName, columnName);
	}
	
	@Override
	public boolean isCalculated()
	{
		return true;
	}
	
	@Override
	public Map<Long, String> generateValues(java.sql.Connection connection, Collection<Long> ids, String locale) throws SQLException // will only be called for non-calculated
	{
		throw new java.lang.UnsupportedOperationException();
	}
	
	@Override
	public String getValueFor(java.sql.ResultSet currentLine) throws SQLException // will only be called for cacheable
	{
		return calculator.calculateValue(currentLine);
	}
	
	@Override
	public String getNiceDescription()
	{
		return _niceDescription;
	}
	
	@Override
	public String toString()
	{
		return _niceDescription;
	}
	
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
}
