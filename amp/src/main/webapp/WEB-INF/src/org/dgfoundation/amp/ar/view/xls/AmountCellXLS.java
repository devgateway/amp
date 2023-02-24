/**
 * AmountCellXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.digijava.module.aim.helper.FormatHelper;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 *
 */
public class AmountCellXLS extends XLSExporter {

    
    /** 
     * @param parent
     * @param item
     */
    public AmountCellXLS(Exporter parent, Viewable item) {
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
    public AmountCellXLS(HSSFWorkbook wb ,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
            IntWrapper colId, Long ownerId, Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Exporter#generate()
     */
    public void generate() {
        AmountCell ac=(AmountCell) item;
        HSSFCellStyle amountStyle;
        
        if(parent instanceof TrailCellsXLS)
        if(parent.getItem().getNearestReportData().getLevelDepth()==2) 
            amountStyle = this.getAmountHierarchyLevel1Style();
         else amountStyle = this.getAmountHierarchyOtherStyle();
        else
            amountStyle=this.getAmountStyle();
        
        HSSFCell cell=this.getCell(amountStyle);
        double tempAm = ac.getAmount();
            
        // According to AMP-15607 the Excel export will contain the values formatted as numbers      
        
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cell.setCellValue(((long)(tempAm * 1000)) / 1000.0);

        colId.inc();
    }

}
