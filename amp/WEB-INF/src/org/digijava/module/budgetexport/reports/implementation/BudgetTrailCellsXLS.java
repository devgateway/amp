/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.*;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.view.xls.TrailCellsXLS;
import org.digijava.module.budgetexport.util.MappingEncoder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alex
 *
 */
public class BudgetTrailCellsXLS extends TrailCellsXLS {

    /**
     * @param parent
     * @param item
     */
    public BudgetTrailCellsXLS(Exporter parent, Viewable item) {
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
    public BudgetTrailCellsXLS(HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row,
            IntWrapper rowId, IntWrapper colId, Long ownerId, Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }

    
    public void generate() {
        // generate totals:
        ReportData rd = (ReportData) item;
    
        if ( ! (rd.getItem(0) instanceof ReportData) ) {
            
            row=sheet.createRow(rowId.shortValue());
            
            this.createFirstCells();
            
            this.createAmountCells();
            
            colId.reset();
            rowId.inc();
            colId.reset();
        }

    }
    
    protected void createFirstCells () {
        List<String> list   = this.generateHierarchiesList();
        if ( list != null && list.size() > 0 ) {
            for (int i = 2; i < list.size(); i++) {
                String string = list.get(i);
                HSSFCell cell=this.getCell(this.getHierarchyOtherStyle());
                cell.setCellValue(string);
                colId.inc();
            }
        }
    }
    
    protected void createAmountCells () {
        ReportData grd = (ReportData) item;
        Iterator i = grd.getTrailCells().iterator();
        //the first column will be under the hierarchy cell
        while (i.hasNext()) {
            Cell element = (Cell) i.next();
            if (element!=null && !this.isTotalCostCell(element) ){
                element.invokeExporter(this);
            }else if (!metadata.getHideActivities()){
                HSSFCell cell2=this.getCell(this.getHierarchyOtherStyle());
                cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
                cell2.setCellValue("");
                colId.inc();
            }
        
        }
        
        colId.reset();
    }
    
    protected List<String>  generateHierarchiesList() {
        ArrayList<String> list  = new ArrayList<String>();
        
        Exporter ex             = this.getParent();
        while ( ex!=null ) {
            ReportData rd       = (ReportData)ex.getItem();
            String modifiedName = (rd.getName()==null)?"":rd.getName();

            int pos = modifiedName.indexOf(':'); 
            if (pos >= 0)
                modifiedName = modifiedName.substring(pos + 1).trim();
            
            if ( rd.getSplitterCell() != null && rd.getSplitterCell().getColumn() != null 
                    && rd.getSplitterCell().getColumn().getWorker() != null ) {
                MappingEncoder enc      = rd.getSplitterCell().getColumn().getWorker().getEncoder();
                if ( enc != null) { 
                    if ( enc.overwritesEverythingWithDefaultString() )
                        modifiedName    = enc.overwriterString();
                    else if (modifiedName.toLowerCase().contains(ArConstants.UNALLOCATED.toLowerCase()) )
                        modifiedName    = enc.encode(modifiedName);
                    
                }
            }
            
            
            list.add(0, modifiedName );
            ex      = ex.getParent();
        }
    
        return list;
    }
    
    private boolean isTotalCostCell(Cell elem) {
        Object col = elem.getColumn();
        
        while ( col != null && col instanceof Column) {
            Column column       = (Column) col;
            String colName      = column.getName(metadata.getHideActivities());
            if ( ArConstants.COLUMN_TOTAL.equals(colName) ) {
                return true;
            }
            col     = column.getParent();
        }
        
        return false;
    }
}
