package org.dgfoundation.amp.ar.view.xls;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.Viewable;

public class TrnTextCellXLS  extends TextCellXLS{

    public TrnTextCellXLS(Exporter parent, Viewable item) {
        super(parent, item);
        // TODO Auto-generated constructor stub
    }
    
    public TrnTextCellXLS(HSSFWorkbook wb ,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
            IntWrapper colId, Long ownerId, Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }

}
