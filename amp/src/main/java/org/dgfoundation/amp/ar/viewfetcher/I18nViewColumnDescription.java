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
        
    /**
     * constructs a calculated instance <b>with no caching</b>
     * @param columnName
     * @param viewName
     * @param calculator
     */
    public I18nViewColumnDescription(String columnName, String viewName, ColumnValueCalculator calculator)
    {
        this(columnName, viewName, null, calculator);
    }   
    
    /**
     * constructs a calculated instance <b>with or without caching</b>, depending on idColumn
     * @param columnName
     * @param viewName
     * @param idColumn: null of this column is purely calculated (SLOW for non-trivial calculators!)
     * @param calculator
     */
    public I18nViewColumnDescription(String columnName, String viewName, String idColumn, ColumnValueCalculator calculator)
    {
        this.columnName = columnName;
        this.indexColumnName = idColumn;
        this.prop = new GeneratedPropertyDescription(viewName, columnName, indexColumnName, calculator);
    }       
    
    /**
     * constructs a DG_EDITOR-backed instance
     * @param columnName
     * @param viewName
     * @param property
     */
    public I18nViewColumnDescription(String columnName, String viewName, String languageColumnName)
    {
        this.columnName = columnName;
        this.indexColumnName = "amp_activity_id";
        this.prop = new DgEditorPropertyDescription(viewName, columnName, languageColumnName);
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
    
    public boolean getDeleteOriginal()
    {
        return prop.getDeleteOriginal();
    }
}
