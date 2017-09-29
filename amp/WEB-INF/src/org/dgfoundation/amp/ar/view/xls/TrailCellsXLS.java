/**
 * TrailCellsXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.math.BigDecimal;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.Constants;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 * 
 */
public class TrailCellsXLS extends XLSExporter {

    /**
     * @param parent
     * @param item
     */
    public TrailCellsXLS(Exporter parent, Viewable item) {
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
    public TrailCellsXLS(HSSFWorkbook wb ,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
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
        // generate totals:
        ReportData grd = (ReportData) item;
    
        if (grd.getParent() != null) {
            String indent = "";
            if (colId.value == 0)
                for (int k = 0; k < ((ReportData)grd.getParent()).getLevelDepth() - 1; k++)
                    indent = indent + Constants.excelIndexString;
//          rowId.inc();
//          colId.reset();
            row=sheet.createRow(rowId.shortValue());
        
            HSSFCellStyle hierarchyStyle;
            if(grd.getLevelDepth()==2) 
                hierarchyStyle = this.getHierarchyLevel1Style();
            else hierarchyStyle=this.getHierarchyOtherStyle();
            
            
            if (grd.getReportMetadata().isSummaryReportNoHierachies()) {
              HSSFCell cell = this.getCell(hierarchyStyle);
              cell.setCellValue(" ");
              colId.inc();                    
            }
            HSSFCell cell = this.getCell(hierarchyStyle);
            
            String modifiedName = getDisplayedName(grd);
            
            if (grd.getReportMetadata().isHideActivities()!=null && !(grd.getReportMetadata().getType() == ArConstants.PLEDGES_TYPE)){
                if (grd.getReportMetadata()!=null && grd.getReportMetadata().isHideActivities())
                    //cell.setCellValue(indent + modified);
                    cell.setCellValue(indent + modifiedName+" ("+grd.getTotalUniqueRows()+")");
                else
                    cell.setCellValue(indent + modifiedName+" ("+grd.getTotalUniqueRows()+")");
            }else{
                if (!(grd.getReportMetadata().getType() == ArConstants.PLEDGES_TYPE)){
                    cell.setCellValue(indent + modifiedName+" ("+grd.getTotalUniqueRows()+")");
                }else{
                    cell.setCellValue(indent + modifiedName);
                }
            }
            colId.inc();
            Iterator i = grd.getTrailCells().iterator();
            //the first column will be under the hierarchy cell
            while (i.hasNext()) {
                Cell element = (Cell) i.next();
                if (element!=null){
                    element.invokeExporter(this);
                }else if (!metadata.getHideActivities()){
                    HSSFCell cell2=this.getCell(hierarchyStyle);
                    cell2.setCellType(HSSFCell.CELL_TYPE_STRING);
                    cell2.setCellValue("");
                    colId.inc();
                }
            
            }
            colId.reset();
            rowId.inc();
            colId.reset();
        }

    }

}
