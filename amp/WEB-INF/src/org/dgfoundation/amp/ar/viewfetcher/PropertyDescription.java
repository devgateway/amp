package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.SQLException;
import java.util.*;

/**
 * these are indexed in the map by their hashCode() / equals() functions, so be sure to make those meaningful
 * @author simple
 *
 */
public interface PropertyDescription 
{
	public boolean isCalculated();
	public Map<Long, String> generateValues(java.sql.Connection connection, Collection<Long> ids, String locale) throws SQLException; // will only be called for non-calculated
	public String getValueFor(java.sql.ResultSet currentLine) throws SQLException; // will only be called for cacheable
	
	public String getNiceDescription();
}
