/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.*;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.view.xls.ReportHeadingsXLS;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;

import java.util.Iterator;
import java.util.List;

/**
 * @author Alex Gartner
 *
 */
public class BudgetReportHeadingsXLS extends ReportHeadingsXLS {

    /**
     * @param parent
     * @param item
     */
    public BudgetReportHeadingsXLS(Exporter parent, Viewable item) {
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
    public BudgetReportHeadingsXLS(HSSFWorkbook wb, HSSFSheet sheet,
            HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
            Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void generate() {
        ColumnReportData columnReport = (ColumnReportData) item;
        
        // column headings:
        if(columnReport.getGlobalHeadingsDisplayed().booleanValue()==false) {
    
            rowId.inc();
            colId.reset();
            
            columnReport.setGlobalHeadingsDisplayed(new Boolean(true));
                row = sheet.createRow(rowId.shortValue());
                
                
                this.createHierarchyHeaderCell(0);
                
            //  this.createHeadingBorders(0);
                
                this.createMachineFriendlyHeaderCells();
                
                rowId.inc();
                colId.reset();
            
        
        }
    }
            
    
    @Override
    protected void createHierarchyHeaderCell (int curDepth) {
        if (curDepth == 0) {
            
            if ( this.getMetadata().getHierarchies() != null ) {
                for (AmpReportHierarchy arh: this.getMetadata().getHierarchies() ) {
                    String colName          = arh.getColumn().getColumnName();
                    String translColName    = null;
                    translColName   = TranslatorWorker.translateText(colName, this.getMetadata().getLocale(), this.getMetadata().getSiteId());
                    
                    if ( translColName != null && translColName.trim().length() > 0 ) {
                        HSSFCell cell1 =  this.getCell(row,this.getHighlightedStyle());
                        cell1.setCellValue( translColName.trim() );
                        colId.inc();
                    }
                }           
            }
        }
        else {
            HSSFCell cell1 =  this.getCell(row,this.getHighlightedStyle());
            cell1.setCellValue("AAA");
        }
    }
    
    @Override
    protected void prepareMachineFriendlyHeaderCellsList (List columns, String parentName, List<String> cellValues) {
        if ( columns == null ) {
            ColumnReportData columnReport = (ColumnReportData) item;
            columns         = columnReport.getItems();
        }
        
        if ( columns != null ) {
            Iterator iter   = columns.iterator();
            while (iter.hasNext()) {
                Column tempCol      = (Column) iter.next();
                String colName      = tempCol.getName(metadata.getHideActivities());
                if ( ! ArConstants.COLUMN_TOTAL.equals(colName) ) {
                    List items  = tempCol.getItems();
                    String name  = (parentName == null)?colName:parentName + " - " + colName;
                    if ( items != null && items.size() > 0 && items.get(0) instanceof Column ) {
                        this.prepareMachineFriendlyHeaderCellsList(items, name, cellValues);
                    }
                    else {
                        cellValues.add(name);
                    }
                }
            }
        }
        
    }

}
