/**
 * PlainReportHeadingsXLS.java
 * (c) 2007 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.view.xls;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;

/**
 * @author mmoras
 *
 */
public class PlainReportHeadingsXLS extends ReportHeadingsXLS {

    /**
     * @param parent
     * @param item
     */
    public PlainReportHeadingsXLS(Exporter parent, Viewable item) {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param wb
     * @param sheet
     * @param row
     * @param rowId
     * @param colId
     * @param ownerId
     * @param item
     */
    public PlainReportHeadingsXLS(HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row,
            IntWrapper rowId, IntWrapper colId, Long ownerId, Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void createHierarchyHeaderCell (int curDepth)
    {
        ColumnReportData columnReport = (ColumnReportData) item;
        Integer rowSpan = columnReport.getMaxColumnDepth() - 1;
            
        if (this.getMetadata().getHierarchies() == null)
            return; // nothing to do

        for (AmpReportHierarchy arh: this.getMetadata().getHierarchies()) 
        {
            String colName = arh.getColumn().getColumnName();
            String translColName = getColumnDisplayName(colName);
            
            if (translColName == null)
                continue;
            
            translColName = translColName.trim();
            if (translColName.isEmpty())
                continue;
            
            if (curDepth == 0)
            {
                HSSFCell cell1 =  this.getCell(row,this.getHighlightedStyle());
                cell1.setCellValue( translColName.trim() );
                makeRowSpan(rowSpan, true);
            }
            else
            {
                HSSFCell cell1 =  this.getCell(row,this.getHighlightedStyle());
                cell1.setCellValue("");
            };
            colId.inc();
        }
    }
    
    @Override
    protected void createHeadingBorders (int curDepth)
    {       
        super.createHeadingBorders(curDepth);
        colId.dec();
    }

}
