package org.dgfoundation.amp.ar.viewfetcher;

import java.util.*;

/**
 * <b>IMMUTABLE</b>class holding the data necessary reading the i18n value of a property of a Translatable model<br />
 * <b>NEVER EVER MAKE THIS CLASS MUTABLE OR CONTAIN MUTABLE FIELDS</b>
 * there are two sources for fetching a translated value: <br />
 * 1. SELECT [modelColumnName] FROM [modelTableName] WHERE [modelTableId] = (id) -> for English
 * 2. SELECT translation FROM amp_content_translation where field_name = [propertyName] AND object_class = [className] AND locale = (locale) AND object_id = (id)
 * @author Dolghier Constantin
 *
 */
public class InternationalizedPropertyDescription 
{
	public final String propertyName;
	public final String className;
	public final String modelColumnName;
	public final String modelTableName;
	public final String modelTableId;
	
	private final String _toString;
	private final int _hashCode;
	
	public InternationalizedPropertyDescription(String propertyName, String className, String modelTableName, String modelTableId, String modelColumnName)
	{
		this.propertyName = propertyName;
		this.className = className;
		this.modelTableName = modelTableName;
		this.modelColumnName = modelColumnName;
		this.modelTableId = modelTableId;
		
		_toString = String.format("i18n IPP: %s.%s -> %s[%s]::%s", className.substring(className.lastIndexOf('.') + 1), propertyName, modelTableName, modelTableId, modelColumnName);
		_hashCode = _toString.hashCode();
	}
	
	/**
	 * take care when changing this function, as its output is part of the instance's hash!
	 */
	@Override
	public String toString()
	{
		return _toString;
	}
	
	@Override
	public int hashCode()
	{
		return _hashCode;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other == null)
			return false;
		if (!(other instanceof InternationalizedPropertyDescription))
			return false;
		return this.toString().equals(other.toString());
	}
	
	/**
	 * generates the SQL query which will fetch all the (id, translation) values for this field in the original model: those of them which have the id in (ids)
	 * @param ids
	 * @return
	 */
	public String generateEnglishQuery(Collection<Long> ids)
	{
		return String.format("SELECT %s, %s FROM %s WHERE %s IN (%s)", modelTableId, modelColumnName, modelTableName, modelTableId, ids.isEmpty() ? "-999" : DatabaseViewFetcher.generateCSV(ids));
	}
	
	/**
	 * generates the SQL query which will fetch all the (id, translation) values for this field
	 * @param ids
	 * @param locale
	 * @return
	 */
	public String generateGeneralizedQuery(Collection<Long> ids, String locale)
	{
		return String.format("SELECT object_id, translation FROM amp_content_translation where field_name = '%s' AND object_class = '%s' AND locale = '%s' AND object_id IN (%s)",
				propertyName, className, locale, ids.isEmpty() ? "-999" : DatabaseViewFetcher.generateCSV(ids));
	}
}
