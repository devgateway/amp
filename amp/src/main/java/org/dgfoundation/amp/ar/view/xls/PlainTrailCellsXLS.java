/**
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.*;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author mmoras
 *
 */
public class PlainTrailCellsXLS extends TrailCellsXLS {

    /**
     * @param parent
     * @param item
     */
    public PlainTrailCellsXLS(Exporter parent, Viewable item) {
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
    public PlainTrailCellsXLS(HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row,
            IntWrapper rowId, IntWrapper colId, Long ownerId, Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void generate() {
        // generate totals:
        ReportData rd = (ReportData) item;
    
        row=sheet.createRow(rowId.shortValue());
            
        this.createFirstCells();
        
        this.createAmountCells();
            
        colId.reset();
        rowId.inc();
        colId.reset();
    }
    
    protected void createFirstCells () {
        //If report is not summary report, item will never be ColumnReportData
        //It is being filtered in PlainColumnReportDataXLS
        if (item instanceof ColumnReportData ){
            List<String> hierarchies = getHierarchiesList(); 
            for(String hierarchy : hierarchies){
                createCell(hierarchy);
            }
        }else{
            Integer count   = this.getHierarchiesCount();
            HSSFCell cell=this.getCell(this.getHierarchyOtherStyle());
            cell.setCellValue("");
            makeColSpan(count, true);
        }
    }
    
    private GroupReportData getBaseReport(){
        ReportData parent = ((ReportData) item).getParent();
        while(true){
            if (parent.getParent() != null){
                parent = parent.getParent();
            }else{
                break;
            }
        }
        return (GroupReportData)parent; 
    }
    
    private Set<AmpReportHierarchy> getHierarchies(){
        
        GroupReportData baseReport = (GroupReportData)this.getBaseReport();
        
        return baseReport.getReportMetadata().getHierarchies();
    }
    
    private Integer getHierarchiesCount(){
        Set<AmpReportHierarchy> hierarchies = this.getHierarchies();
        if (hierarchies != null){
            return hierarchies.size();
        }else{
            return 0;
        }
    }

    private List<String> getHierarchiesList(){
        List<String> hierarchies = new ArrayList<String>();
        
        ReportData parent = (ReportData) item;
        
        while(parent != null){
            if (parent.getLevelDepth() > 1){
                hierarchies.add(0, parent.getRepName());
                parent = parent.getParent();
            }else{
                break;
            }
        }
        return hierarchies; 
    }
    
    protected void createAmountCells () {
        ReportData grd = (ReportData) item;
        Iterator i = grd.getTrailCells().iterator();
        
        //the first column will be under the hierarchy cell
        while (i.hasNext()) {
            Cell element = (Cell) i.next();
            if (element!=null ){
                element.invokeExporter(this);
            }else {
                createCell("");
            }
        
        }
        
        colId.reset();
    }
    
    private void createCell(String text){
        HSSFCell cell2=this.getCell(this.getHierarchyOtherStyle());
        cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
        cell2.setCellValue(text);
        colId.inc();
    }
}
