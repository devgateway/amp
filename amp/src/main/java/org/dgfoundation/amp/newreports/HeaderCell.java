/**
 * 
 */
package org.dgfoundation.amp.newreports;

import org.dgfoundation.amp.nireports.ReportHeadingCell;

/**
 * Pre-generated Report Header Cell per tabular view, that provides its positioning information, reusing {@link ReportHeadingCell}.
 * It can be used to facilitate different clients from repeatedly building their own headers structure. 
 * 
 * @author Nadejda Mandrescu
 */
public class HeaderCell extends ReportHeadingCell {
    
    /** not translated original name */
    public final String originalName;
    
    /** full not translated name of the column from the uppermost header till the current header cell level */
    public final String fullOriginalName;
    
    public final String description;
    
    public HeaderCell(int startRow, int totalRowSpan, int rowSpan, int startColumn, int colSpan, ReportOutputColumn roc) {
        super(startRow, totalRowSpan, rowSpan, startColumn, colSpan, roc.columnName);
        this.originalName = roc.originalColumnName;
        this.fullOriginalName = roc.getHierarchicalName();
        this.description  = roc.description;
    }
    
    public HeaderCell(ReportHeadingCell niHeaderCell, ReportOutputColumn roc, int colStartDelta) {
        this(niHeaderCell.getStartRow(), niHeaderCell.getTotalRowSpan(), niHeaderCell.getRowSpan(), 
                niHeaderCell.getStartColumn() + colStartDelta, niHeaderCell.getColSpan(), roc);
    }

}
