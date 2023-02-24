/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.Column;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.view.xls.ColumnReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.view.xls.TrailCellsXLS;

/**
 * @author alex
 *
 */
public class BudgetColumnReportDataXLS extends ColumnReportDataXLS {

    /**
     * @param parent
     * @param item
     */
    public BudgetColumnReportDataXLS(Exporter parent, Viewable item) {
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
    public BudgetColumnReportDataXLS(HSSFWorkbook wb, HSSFSheet sheet,
            HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
            Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void createFirstCells () {
        List<String> list   = this.generateHierarchiesList();
        if ( list != null && list.size() > 0 ) {
            for (int i = 2; i < list.size(); i++) {
                String string = list.get(i);
                HSSFCell cell=this.getCell(regularStyle);
                cell.setCellValue(string);
                colId.inc();
            }
        }
    }
    
    protected List<String>  generateHierarchiesList() {
        ArrayList<String> list  = new ArrayList<String>();
        
        Exporter ex             = this;
        while ( ex!=null ) {
            ReportData rd       = (ReportData)ex.getItem();
            String modifiedName = (rd.getName()==null)?"":rd.getName();

            int pos = modifiedName.indexOf(':'); 
            if (pos >= 0)
                modifiedName = modifiedName.substring(pos + 1).trim();
            
            list.add(0, modifiedName );
            ex      = ex.getParent();
        }
    
        return list;
    }
    
    @Override
    protected void invokeChildExporter( Viewable element) {
        if ( ! isTotalCostColumn(element) )
            element.invokeExporter(this, true);
    }
    
    @Override
    protected void createTrailCells() {
        ColumnReportData crd    = (ColumnReportData) item;
        
        Boolean summary         = crd.getReportMetadata().getHideActivities(); 
        if ( summary != null && summary ) {
            ColumnReportData columnReport = (ColumnReportData) item;
            BudgetTrailCellsXLS trails = new BudgetTrailCellsXLS(this, columnReport);
            trails.generate();
        }
    }
    
    private boolean isTotalCostColumn(Viewable elem) {
        if ( elem instanceof Column ) {
            Column column       = (Column) elem;
            String colName      = column.getName(metadata.getHideActivities());
            if ( ArConstants.COLUMN_TOTAL.equals(colName) ) {
                return true;
            }
        }
        
        return false;
    }

}
