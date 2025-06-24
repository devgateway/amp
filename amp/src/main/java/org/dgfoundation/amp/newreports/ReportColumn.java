package org.dgfoundation.amp.newreports;

/**
 * class describing a column to be used in a report
 * @author Dolghier Constantin
 *
 */
public class ReportColumn extends NamedTypedEntity {
    
    /**
     * equivalent to calling {@link #ReportColumn(String, ENTITY_TYPE_ALL) )}
     * @param columnName - the name of the column
     */
    public ReportColumn(String columnName) {
        super(columnName);
    }
    
    public String getColumnName() {
        return this.entityName;
    }
}
