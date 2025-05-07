package org.dgfoundation.amp.ar.viewfetcher;

import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.SiteUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * a column-in-a-view translator which does a translateText() call on the input for its outputs 
 * @author Dolghier Constantin
 *
 */
public class ColumnValueTranslator implements ColumnValueCalculator {
    
    public final String columnToTranslate;
    
    private final String _toString;
    private final int _hashCode;
        
    public ColumnValueTranslator(String columnToTranslate)
    {
        if (columnToTranslate == null)
            throw new RuntimeException("cannot create ColumnValueTranslator for a null columnToTranslate!");
        this.columnToTranslate = columnToTranslate;

        _toString = String.format("DGM IPP: columnToTranslate = %s", columnToTranslate);
        _hashCode = _toString.hashCode();
    }
    
    @Override
    public String calculateValue(ResultSet resultSet, java.sql.ResultSet rawCurrentLine, String locale) throws SQLException
    {
        String translatedValue = TranslatorWorker.translateText(rawCurrentLine.getString(columnToTranslate), locale, SiteUtils.getDefaultSite());
        return translatedValue;
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
    public boolean getDeleteOriginal()
    {
        return false;
    }
}
