package org.dgfoundation.amp.ar.viewfetcher;

import org.dgfoundation.amp.Util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * description of an i18n view: contains a description of all the columns which should be overridden by the translation
 * @author Dolghier Constantin
 *
 */
public class I18nViewDescription {
    
    public final String viewName;
    public final Set<String> viewColumns;
    
    /**
     * Map<translated-column-name, description>
     */
    protected final Map<String, I18nViewColumnDescription> columns = new HashMap<String, I18nViewColumnDescription>();
    
    public I18nViewDescription(String viewName)
    {
        this.viewName = viewName;
        this.viewColumns = Collections.<String>unmodifiableSet(SQLUtils.getTableColumns(viewName));
    }
    
    public I18nViewDescription addColumnDef(I18nViewColumnDescription column)
    {
        this.columns.put(column.columnName, column);
        return this;
    }
    
    
    public I18nViewDescription addTrnColDef(String columnName, String idColumnName)
    {
        if (!viewColumns.contains(columnName))
            throw new RuntimeException(String.format("cannot add trn-backed translated column <%s> to view <%s>: column does not exist!", columnName, viewName));
        
        if ((idColumnName != null) && (!viewColumns.contains(idColumnName)))
            throw new RuntimeException(String.format("cannot add trn-backed translated column <%s> indexed by <%s> to view <%s>: index column does not exist!", columnName, idColumnName, viewName));
        
        return addColumnDef(new I18nViewColumnDescription(columnName, viewName, idColumnName, new ColumnValueTranslator(columnName)));
    }
    
    public I18nViewDescription addCalculatedColDef(String columnName, ColumnValueCalculator calculator) {
        return addColumnDef(new I18nViewColumnDescription(columnName, this.viewName, null, calculator));
    }
    
    public I18nViewDescription addDgEditorColumnDef(String columnName, String languageColumnName) {
        if (!viewColumns.contains(columnName))
            throw new RuntimeException(String.format("cannot add dg_editor-backed translated column <%s> to view <%s>: column does not exist!", columnName, viewName));
        if (!viewColumns.contains(languageColumnName))
            throw new RuntimeException(String.format("cannot add dg_editor-backed translated column <%s> with language column %s to view <%s>: languageColumn does not exist!", columnName, languageColumnName, viewName));
        return addColumnDef(new I18nViewColumnDescription(columnName, viewName, languageColumnName));
    }
    
    /**
     * returns the view description or null, of none found
     * @param columnName
     * @return
     */
    public I18nViewColumnDescription getColumnDescription(String columnName)
    {
        return columns.get(columnName);
    }
    
    /**
     * clones a view
     * @param newViewName
     * @return
     */
    public I18nViewDescription cloneView(String newViewName)
    {
        I18nViewDescription res = new I18nViewDescription(newViewName);
        res.columns.putAll(this.columns); // I18nViewColumnDescription is immutable, so this is a safe way to copy
        return res;
    }
    
    @Override
    public String toString()
    {
        return String.format("i18n view description of %s, columns: [%s]", this.viewName, Util.toCSString(this.columns.keySet()));
    }
}
