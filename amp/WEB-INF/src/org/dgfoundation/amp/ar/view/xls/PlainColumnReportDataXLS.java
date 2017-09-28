/**
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.ColumnReportData;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;

/**
 * @author mmoras
 *
 */
public class PlainColumnReportDataXLS extends ColumnReportDataXLS {

    /**
     * @param parent
     * @param item
     */
    public PlainColumnReportDataXLS(Exporter parent, Viewable item) {
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
    public PlainColumnReportDataXLS(HSSFWorkbook wb, HSSFSheet sheet,
            HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
            Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void createFirstCells () {
        HSSFCellStyle style = getPlainHierarchyStyle();
        List<String> list   = this.generateHierarchiesList();
        if ( list != null && list.size() > 0 ) {
            for (int i = 2; i < list.size(); i++) {
                String string = list.get(i);
                HSSFCell cell=this.getCell(style);
                cell.setCellValue(string);
                colId.inc();
            }
        }
    }
    
    private HSSFCellStyle getPlainHierarchyStyle() {
        HSSFCellStyle cs = wb.createCellStyle();
        cs.setWrapText(true);
        HSSFFont font= wb.createFont();
        font.setFontName(HSSFFont.FONT_ARIAL);
        font.setFontHeightInPoints(new Short("8"));
        cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cs.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cs.setFont(font);
        cs.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        return cs;
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
    protected void invokeChildExporter(Viewable element) {
        element.invokeExporter(this, false, XLSExportType.PLAIN_XLS_EXPORT);
    }
    
    @Override
    protected void createTrailCells() {
        ColumnReportData grd = (ColumnReportData) item;
        if (grd.getReportMetadata().getHideActivities()){
            PlainTrailCellsXLS trails = new PlainTrailCellsXLS(this, grd);
            trails.generate();
        }
    }
}
