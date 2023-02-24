package org.dgfoundation.amp.ar.view.xls;

import java.io.IOException;
import java.io.StringReader;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.TextCell;
import org.dgfoundation.amp.ar.workers.ComputedDateColWorker;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.Html2TextCallback;

public class ComputedDateCellXLS extends TextCellXLS {

    /**
     * @param parent
     * @param item
     */
    public ComputedDateCellXLS(Exporter parent, Viewable item) {
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
    public ComputedDateCellXLS(HSSFWorkbook wb, HSSFSheet sheet, HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId, Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Exporter#generate()
     */
    private boolean columnNeedsHTMLStripping(String columnName) {
        String s = columnName.toLowerCase();
        if ("objective".compareTo(s) == 0 || "description".compareTo(s) == 0) {
            return true;
        }
        return false;
    }

    public void generate() {
        TextCell c = (TextCell) item;
        HSSFCell cell = this.getCell(getAmountStyle());
        String indent = "";
        if (colId.value == 0)
            for (int k = 0; k < ((ReportData) c.getColumn().getParent()).getLevelDepth(); k++)
                indent = indent + Constants.excelIndexString;

        if (c.getColumn().getName().compareTo("Status") == 0) {
            String actualStatus = c.toString();

            ReportData parent = (ReportData) c.getColumn().getParent();
            while (parent.getReportMetadata() == null) {
                parent = parent.getParent();
            }
            // when we get to the top of the hierarchy we have access to
            // AmpReports

            // requirements for translation purposes
//          TranslatorWorker translator = TranslatorWorker.getInstance();
            Long siteId = parent.getReportMetadata().getSiteId();
            String locale = parent.getReportMetadata().getLocale();

            String finalStatus = new String();// the actual text to be added to
            // the column

            String translatedStatus = TranslatorWorker.translateText(actualStatus, locale, siteId);
            if (translatedStatus.compareTo("") == 0)
                translatedStatus = actualStatus;
            finalStatus += translatedStatus;

            cell.setCellValue(indent + finalStatus);
        } else if (columnNeedsHTMLStripping(c.getColumn().getName())) {
            StringReader sr = new StringReader(c.toString());
            Html2TextCallback h2t = new Html2TextCallback();
            try {
                h2t.parse(sr);
                cell.setCellValue(indent + h2t.getText());
            } catch (IOException e) {
                e.printStackTrace();
                cell.setCellValue(indent + c.toString());
            }
        } else
            cell.setCellValue(indent + c.toString());

        colId.inc();
    }

}
