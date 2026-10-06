/**
 * CellColumnXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.Cell;
import org.digijava.module.aim.helper.GlobalSettingsConstants;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 *
 */
public class CellColumnXLS extends XLSExporter {

    /**
     * @param parent
     * @param item
     */
    public CellColumnXLS(Exporter parent, Viewable item) {
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
    public CellColumnXLS(HSSFWorkbook wb,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
            IntWrapper colId, Long ownerId, Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Exporter#generate()
     */
    public void generate() {
        CellColumn col = (CellColumn) item;
        Cell c = col.getByOwner(ownerId);
         if (c != null) {
             c.invokeExporter(this);
         }
         else {
             HSSFCell cell=this.getRegularCell();
             if (col.getClass().toString().equalsIgnoreCase("class org.dgfoundation.amp.ar.TotalAmountColumn") && FeaturesUtil.getGlobalSettingValueBoolean(GlobalSettingsConstants.REPORTS_EMPTY_VALUES_AS_ZERO_XLS)){
                 cell.setCellValue(0);
             }else{
                 cell.setCellValue(" ");
             }
             colId.inc();
         }
    }

}
