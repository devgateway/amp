package org.dgfoundation.amp.newreports;

import org.digijava.kernel.translator.TranslatorWorker;

import java.util.Collection;

/**
 * class holding metadata about a report-output-column. It also holds  
 * A "column" in this context might be either a Measure or a Column - anything which sits on the X axis, actually
 * @author Dolghier Constantin
 *
 */
public class ReportOutputColumn implements Comparable<ReportOutputColumn> {
    /**
     * the <strong>localized</strong> name of the column<br />
     */
    public final String columnName;
    
    /** the parent column, if any. Might be null for top-level columns, like "Funding" or "Project Title" */
    public final ReportOutputColumn parentColumn;
        
    /** the <strong> unlocalized</strong> name of the column */
    public final String originalColumnName;
    
    public final String description;
    
    /** the cell to display under a column if the contents is null (to avoid resending the same info repeatedly). never null for leaves; value is meaningless for intermediate notes */
    public final ReportCell emptyCell;
            
    public ReportOutputColumn(String columnName, ReportOutputColumn parentColumn, String originalColumnName, ReportCell emptyCell, Collection<?> flags) {
        this(columnName, parentColumn, originalColumnName, null, emptyCell, flags);
    }
    
    public ReportOutputColumn(String columnName, ReportOutputColumn parentColumn, String originalColumnName, String description, ReportCell emptyCell, Collection<?> flags) {
        this.columnName = columnName;
        
        if (columnName == null || columnName.isEmpty()) {
            throw new NullPointerException("The translated name of the column is empty. The original column name is '" + originalColumnName + "'");
        }
        
        this.parentColumn = parentColumn;
        this.originalColumnName = originalColumnName;
        this.description = description;
        this.emptyCell = emptyCell == null ? TextCell.EMPTY : emptyCell;
    }
    
    @Deprecated
    /** to delete once we are done with Mondrian */
    public static ReportOutputColumn buildTranslated(String originalColumnName, String description, String locale, ReportOutputColumn parentColumn, ReportCell emptyCell, Collection<?> flags) {
        return new ReportOutputColumn(TranslatorWorker.translateText(originalColumnName, locale, 3l), parentColumn, originalColumnName, description, emptyCell, flags);
    }
    
    @Deprecated
    /** to delete once we are done with Mondrian */ 
    public static ReportOutputColumn buildTranslated(String originalColumnName, String locale, ReportOutputColumn parentColumn, ReportCell emptyCell, Collection<?> flags){
        return buildTranslated(TranslatorWorker.translateText(originalColumnName, locale, 3l), null, locale, parentColumn, emptyCell, flags);
    }
    
    /**
     * moves up a number of levels OR until null is reached
     * @param levels
     * @return
     */
    public ReportOutputColumn moveUp(int levels) {
        if (levels == 0) return this;
        if (this.parentColumn == null) return parentColumn;
        return this.parentColumn.moveUp(levels - 1);
    }
    
    /**
     * cached result of {@link #getHierarchicalName()}
     */
    String _hierName = null;
    /**
     * computes the full name of the column like, for example, [Funding][2007][Actual Commitments]
     * <strong>unlocalized</strong>
     * @return
     */
    public String getHierarchicalName() {
        if (_hierName == null) {
            String res = String.format("[%s]", this.originalColumnName);
            if (parentColumn != null)
                res = parentColumn.getHierarchicalName() + res;
            _hierName = res;
        }
        return _hierName;
    }
    
    @Override
    public String toString() {
        return this.getHierarchicalName();
    }
    
    @Override
    public final int hashCode() {
        return getHierarchicalName().hashCode();
    }
    
    @Override public boolean equals(Object oth) {
        return this.getHierarchicalName().equals(((ReportOutputColumn) oth).getHierarchicalName());
    }
    
    @Override public int compareTo(ReportOutputColumn oth) {
        return this.getHierarchicalName().compareTo(oth.getHierarchicalName());
    }
    
    /** get the format type of the column. The information is used for displayng data in reports. 
     * 
     * @return format type of the column
     */
    public String getFormatType() {
        return this.emptyCell.extractFormatType();
    }
}
