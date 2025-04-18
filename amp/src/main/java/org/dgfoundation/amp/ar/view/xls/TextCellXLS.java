/**
 * TextCellXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.Html2TextCallback;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 *
 */
public class TextCellXLS extends XLSExporter {

    /**
     * @param parent
     * @param item
     */
    public TextCellXLS(Exporter parent, Viewable item) {
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
    public TextCellXLS(HSSFWorkbook wb ,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
            IntWrapper colId, Long ownerId, Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }
    
    public final static Set<String> DG_EDITOR_COLUMNS = new HashSet<>(Arrays.asList("Objective", "Results", "Purpose", "Project Description", 
            "Project Impact", "Project Comments", "Equal Opportunity", "Environment", "Minorities"));

    /* (non-Javadoc)
     * @see org.dgfoundation.amp.ar.Exporter#generate()
     */
    private boolean columnNeedsHTMLStripping(String columnName){
        return DG_EDITOR_COLUMNS.contains(columnName);
    }
    
    @Override
    public void generate() {
        TextCell c = (TextCell) item;
        HSSFCell cell = c.isDisquisedPledgeCellWhichShouldBeHighlited() ? this.getPledgeDisguisedAsAnActivityCell() : this.getRegularCell();
        String indent = "";
    
        //PLEASE USE TRNTEXTCELL.JAVA IF YOU NEED A TRANSLATION-ENABLED CELL.
        //PLEASE DO NOT PUT ANY TranslatorWorker INVOCATIONS HERE, OR ANYWHRE ELSE IN THE EXPORTS, IT WILL BE ROLLED BACK
        
        if (colId.value == 0)
            for (int k = 0; k < ((ReportData)c.getColumn().getParent()).getLevelDepth(); k++)
                indent = indent + Constants.excelIndexString;
        
        if (columnNeedsHTMLStripping(c.getColumn().getName())){
            StringReader sr = new StringReader(c.toString());
            Html2TextCallback h2t = new Html2TextCallback();
            try {
                h2t.parse(sr);
                cell.setCellValue(indent + h2t.getText());
            } catch (IOException e) {
                e.printStackTrace();
                cell.setCellValue(indent + c.toString());
            }
        } else {
            cell.setCellValue(indent + c.toString());
        }
        
        colId.inc();
    }

}
