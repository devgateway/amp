package org.dgfoundation.amp.ar.view.xls;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

/**
 * 
 * @author Diego Dimunzio Ago 15, 2009
 */

public class ComputedMeasureCellXLS extends AmountCellXLS {

    public ComputedMeasureCellXLS(Exporter parent, Viewable item) {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }

    public ComputedMeasureCellXLS(HSSFWorkbook wb, HSSFSheet sheet,
            HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
            Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }

}
