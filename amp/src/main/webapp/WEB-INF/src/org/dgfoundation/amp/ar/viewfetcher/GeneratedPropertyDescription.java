package org.dgfoundation.amp.ar.viewfetcher;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.translator.TranslatorWorker;


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
    public final String idColumn;
    
    private final String _niceDescription;
    
    public static long _generatedPropertyCalls = 0;
    public static long _generatedPropertyCached = 0;
    
    public GeneratedPropertyDescription(String viewName, String columnName, String idColumn, ColumnValueCalculator calculator)
    {
        this.viewName = viewName;
        this.columnName = columnName;
        this.idColumn = idColumn;
                
        this.calculator = calculator;
        if (this.calculator == null)
            throw new IllegalArgumentException("not allowed to supply a null Calculator here");
        
        sanityCheck();

        this._niceDescription = String.format("%s.%s (indexed by %s), calculated", viewName, columnName, idColumn == null ? "n/a" : idColumn);
    }
    
    /**
     * throws Exception if value is not valid
     */
    private void sanityCheck()
    {
        java.util.LinkedHashSet<String> columns = SQLUtils.getTableColumns(viewName);

        if (!columns.contains(columnName))
            throw new RuntimeException(String.format("error while configuring a GeneratedPropertyDescription instance for translatable view %s: it should contain the text column <%s>!", this.viewName, columnName));
        
        if ((idColumn != null) && (!columns.contains(idColumn)))
            throw new RuntimeException(String.format("error while configuring a GeneratedPropertyDescription instance for translatable view %s: it should contain the index column <%s>!", this.viewName, idColumn));
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
    public String getValueFor(java.sql.ResultSet currentLine, java.sql.ResultSet rawCurrentLine, ColumnValuesCacher cacher, String locale) throws SQLException // will only be called for cacheable
    {
        _generatedPropertyCalls ++;
        
        Long idValue = idColumn == null ? null : PersistenceManager.getLong(rawCurrentLine.getObject(idColumn));
        if (idValue != null && (idValue.longValue() < 0))
            idValue = null;
        
        if (idValue == null && (idColumn != null))
        {
            _generatedPropertyCached ++;
            return null;
        }
        boolean shouldTranslate = idValue == null || (!cacher.values.containsKey(idValue)); 
        String translatedValue;
        if (shouldTranslate)
        {
            translatedValue = calculator.calculateValue(currentLine, rawCurrentLine, locale);
        }
        else
        {
            translatedValue = cacher.values.get(idValue);
            _generatedPropertyCached ++;
        }
        if (idValue != null && shouldTranslate)
            cacher.values.put(idValue, translatedValue);
        return translatedValue;
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
    
    @Override
    public boolean getDeleteOriginal()
    {
        return calculator.getDeleteOriginal();
    }
    
    @Override
    public boolean allIdsHaveValues(){
        return true;
    }

}
