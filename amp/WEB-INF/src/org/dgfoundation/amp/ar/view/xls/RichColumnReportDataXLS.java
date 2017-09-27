/**
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.AmountCell;

/**
 * @author Dolghier Constantin
 *
 */
public class RichColumnReportDataXLS extends ColumnReportDataXLS {

    /**
     * @param parent
     * @param item
     */
    public RichColumnReportDataXLS(Exporter parent, Viewable item) {
        super(parent, item);
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
    public RichColumnReportDataXLS(HSSFWorkbook wb, HSSFSheet sheet,
            HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
            Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
    }
    
    @Override
    protected void createFirstCells () {
        
    }
        
    protected HSSFCellStyle crdStyle = null;
    protected HSSFCellStyle getCRDStyle() {
        if (crdStyle == null) {
            HSSFCellStyle cs = wb.createCellStyle();
            HSSFFont font= wb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setColor( HSSFColor.BLACK.index );
            cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cs.setBorderRight(HSSFCellStyle.BORDER_NONE);
            cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
            
            cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cs.setAlignment(CellStyle.ALIGN_CENTER);
            
            cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            cs.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);

            HSSFDataFormat df = wb.createDataFormat();
            cs.setDataFormat(df.getFormat("General"));
        
            cs.setFont(font);
            cs.setWrapText(true);
            crdStyle = cs;
        }
        return crdStyle;
    }
    
    protected HSSFCellStyle crdAmountStyle = null;
    protected HSSFCellStyle getCRDAmountStyle(){
        if (crdAmountStyle == null){
            crdAmountStyle = wb.createCellStyle();
            crdAmountStyle.cloneStyleFrom(getCRDStyle());
            crdAmountStyle.setAlignment(CellStyle.ALIGN_RIGHT);
            crdAmountStyle.setBorderRight(CellStyle.BORDER_NONE);
            crdAmountStyle.setBorderLeft(CellStyle.BORDER_NONE);
        }
        return crdAmountStyle;
    }
    
    @Override
    protected void invokeChildExporter( Viewable element) {
            element.invokeExporter(this, false, XLSExportType.RICH_XLS_EXPORT);
    }
        
    @Override public void generate() {
        ColumnReportData columnReport = (ColumnReportData) item;
        // add data
        int startCol = colId.intValue();
        //System.out.format("creating a ColumnRD with rowspan = %2d and name = %s\n", columnReport.getRowSpan(), columnReport.getName());
        
        if (metadata.getNumOfHierarchies() > 0)
            createCenteredCell(getDisplayedName(columnReport), getCRDStyle(), false, columnReport.getRowSpan(), 1);
        
        int hierDisplacement = metadata.getNumOfHierarchies() > 0 ? 1 : 0;
        if (metadata.getHideActivities() == null || metadata.getHideActivities().booleanValue() == false) {
            for (Long element:columnReport.getOwnerIds()) {
                this.setOwnerId(element);
                row = getOrCreateRow();
                //System.out.format("CRD export: getting a cell for row = %2d, col = %2d\n", rowId.shortValue(), colId.shortValue());
                if ( this.regularStyle == null )
                    this.getRegularStyle();
                                
                colId.set(startCol + hierDisplacement);
                for (Column column:columnReport.getItems()) {
                    this.invokeChildExporter(column);
                }
                rowId.inc(); // dummy placeholder for future row
            }
        }
        row = getOrCreateRow();
        colId.set(startCol + hierDisplacement); 
        this.createTrailCells();
        rowId.inc(); // trailCells ended
        colId.set(startCol); // get back to old colId - for next CRD
    }

    @Override
    protected void createTrailCells () {
        ReportData<?> grd = (ReportData<?>) item;
        
        //the first column will be under the hierarchy cell
        for(AmountCell element:grd.getTrailCells()){
            createCell(element == null ? null : element.getAmount());
        }
    }

    private void createCell(Double amount){
        
        HSSFCell cell = this.getCell(getCRDAmountStyle());
        if (amount == null){
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue("");
        }
        else{
            // According to AMP-15607 the Excel export will contain the values formatted as numbers             
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(Math.floor(amount * 1000) / 1000.0);
        }
        colId.inc();
    }

}
