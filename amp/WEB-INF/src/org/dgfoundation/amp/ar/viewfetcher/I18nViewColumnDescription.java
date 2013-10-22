package org.dgfoundation.amp.ar.viewfetcher;

/**
 * a description of a translatable column of a view which should be overwritten 
 * @author Dolghier Constantin
 *
 */
public class I18nViewColumnDescription 
{
	/**
	 * the translated column's name
	 */
	public final String columnName;
	
	/**
	 * the (id-of-translated-object-holding) column's name
	 */
	public final String indexColumnName;
	
	/**
	 * 
	 */
	public final PropertyDescription prop;
	
	public I18nViewColumnDescription(String columnName, String indexColumnName, Class<?> clazz, String propertyName)
	{
		this.columnName = columnName;
		this.indexColumnName = indexColumnName;

		this.prop = InternationalizedModelDescription.getForClass(clazz).properties.get(propertyName);
		
		if (this.prop == null)
			throw new RuntimeException("could not find a description for property " + propertyName + " of class " + clazz);		
	}
		
	//constructs a calculated instance
	public I18nViewColumnDescription(String columnName, String viewName, ColumnValueCalculator calculator)
	{
		this.columnName = columnName;
		this.indexColumnName = "(n/a)";
		this.prop = new GeneratedPropertyDescription(viewName, columnName, calculator);
	}	
	
	@Override
	public String toString()
	{
		return String.format("i18nVCD: column: <%s>, idx: <%s>, prop: %s", columnName, indexColumnName, prop.getNiceDescription());
	}
	
	public boolean isCalculated()
	{
		return prop.isCalculated();
	}
}
