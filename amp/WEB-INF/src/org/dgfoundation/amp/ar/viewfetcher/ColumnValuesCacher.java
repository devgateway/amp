package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * a class which holds the cached i18n values for a (model, field)  
 * @author Dolghier Constantin
 *
 */
public class ColumnValuesCacher {
	
	/**
	 * the property whose values are being cached
	 */
	public final InternationalizedPropertyDescription cachedProperty;
	
	protected final HashMap<Long, String> values = new HashMap<Long, String>();
	
	public ColumnValuesCacher(InternationalizedPropertyDescription cachedProperty)
	{
		this.cachedProperty = cachedProperty;
	}
	
	/**
	 * imports result generated by a query of the type "SELECT id, value FROM model". The only functions called on the ResultSet are <br />
	 * rs.getLong(1) and rs.getString(2) - this is useful if you are supplying a mock implementation
	 * @param rs
	 */
	public void importValues(ResultSet rs)
	{
		try
		{
			while (rs.next())
			{
				Long id = rs.getLong(1);
				String value = rs.getString(2);
			
				values.put(id, value);
			}
		}
		catch(SQLException e)
		{
			throw new RuntimeException("error while fetching translations", e);
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("CVC of %s[%s]::%s", cachedProperty.modelTableName, cachedProperty.modelTableId, cachedProperty.modelColumnName);
	}
}
