/**
 * ColumnReportDataXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 * 
 */
public class ColumnReportDataXLS extends XLSExporter {

    /**
     * @param parent
     * @param item
     */
    public ColumnReportDataXLS(Exporter parent, Viewable item) {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param sheet
     * @param row
     * @param rowId
     * @param colId
     * @param ownerId
     * @param item
     */
    public ColumnReportDataXLS(HSSFWorkbook wb ,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
            IntWrapper colId, Long ownerId, Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Exporter#generate()
     */
    public void generate() {
        ColumnReportData columnReport = (ColumnReportData) item;
        this.createTrailCells();
        

        // add data
        if (metadata.getHideActivities() == null
                || metadata.getHideActivities().booleanValue() == false) {
            for (Long element:columnReport.getOwnerIds()) {
                this.setOwnerId(element);
                row = sheet.createRow(rowId.shortValue());
                if ( this.regularStyle == null )
                    this.getRegularStyle();
                this.createFirstCells();

                for (Viewable velement:columnReport.getItems()) {
                    this.invokeChildExporter(velement);
                }
                rowId.inc();
                colId.reset();
            }
        }
        


    }
    
    protected void createTrailCells () {
        ColumnReportData columnReport = (ColumnReportData) item;
        TrailCellsXLS trails = new TrailCellsXLS(this, columnReport);
        trails.generate();
    }
    
    protected void createFirstCells () {
        HSSFCell cell=this.getCell(regularStyle);
        cell.setCellValue("");
        colId.inc();
    }
    
    protected void invokeChildExporter( Viewable element) {
        element.invokeExporter(this);
    }
    

}
