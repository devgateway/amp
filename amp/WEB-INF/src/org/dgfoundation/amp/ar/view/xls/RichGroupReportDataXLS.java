/**
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.ReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.cell.AmountCell;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author Dolghier Constantin
 *
 */
public class RichGroupReportDataXLS extends GroupReportDataXLS {

    /**
     * @param parent
     * @param item
     */
    public RichGroupReportDataXLS(Exporter parent, Viewable item) {
        super(parent, item);
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
    public RichGroupReportDataXLS(HSSFWorkbook wb, HSSFSheet sheet,
            HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
            Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
    }
    
    @Override
    protected void showHeadings () {
        GroupReportData grd = (GroupReportData) item;
        //show Headings:        
        PlainReportHeadingsXLS headings = new PlainReportHeadingsXLS(this,grd.getFirstColumnReport()); // heading is ok from plain
        headings.setAutoSize(this.isAutoSize());
        headings.generate();
    }
    
    @Override
    protected void createPrologueTrailCellsForGRD () {
        //super.createPrologueTrailCellsForGRD();
    }
    
    @Override
    protected void createConcludingTrailCellsForReport() {
        GroupReportData grd = (GroupReportData) item;       
        colId.set(grd.getLevelDepth() - 1);
        for(int i = 0; i < metadata.getNumOfHierarchies() + 1 - grd.getLevelDepth(); i++)
            createCell(grd.getLevelDepth(), i == 0 && grd.getLevelDepth() == 1 ? getDisplayedName(grd) : null);
        //the first column will be under the hierarchy cell
        for(AmountCell element:grd.getTrailCells()){
            createCell(grd.getLevelDepth(), element == null ? null : element.getAmount());
        }
    }
    
    private void createCell(int depth, Object contents){
        
        HSSFCell cell = this.getCell(getGRDAmountStyle(depth));
        if (contents == null || (contents instanceof String)){
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            cell.setCellValue(contents == null ? "" : contents.toString());
        }
        else{
            // According to AMP-15607 the Excel export will contain the values formatted as numbers             
            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            cell.setCellValue(Math.floor((Double) contents * 1000) / 1000.0);
        }
        colId.inc();
    }

    @Override
    protected void invokeChildExporter( Viewable element) {
        element.invokeExporter(this, false, XLSExportType.RICH_XLS_EXPORT);
    }
    
    /**
     * gets the bg-color to fill smth
     * @param grd
     * @return
     */
    protected void setStyleFor(HSSFCellStyle style, int depth){
        switch(depth){
            case 1:
                style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                style.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
                return;
            case 2:
                style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
                return;
            case 3:
                style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
                return;
            default:
                style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
                return;
        }
    }
    
    Map<Integer, HSSFCellStyle> grdStyles = new HashMap<Integer, HSSFCellStyle>();
    
    protected HSSFCellStyle getGRDStyle(int depth) {
        if (!grdStyles.containsKey(depth))
        {
            HSSFCellStyle cs = wb.createCellStyle();
            HSSFFont font= wb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setColor( HSSFColor.BLACK.index );
            cs.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            cs.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            cs.setBorderRight(HSSFCellStyle.BORDER_NONE);
            cs.setBorderTop(HSSFCellStyle.BORDER_THIN);
            
            cs.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            cs.setAlignment(CellStyle.ALIGN_CENTER);
            
            setStyleFor(cs, depth);
            cs.setWrapText(true);
            /*cs.setFillPattern(HSSFCellStyle.SPARSE_DOTS);
            cs.setFillBackgroundColor(HSSFColor.GREY_80_PERCENT.index);*/

            HSSFDataFormat df = wb.createDataFormat();
            cs.setDataFormat(df.getFormat("General"));
        
            cs.setFont(font);
            grdStyles.put(depth, cs);
        }
        return grdStyles.get(depth);
    }

    Map<Integer, HSSFCellStyle> grdAmountStyles = new HashMap<Integer, HSSFCellStyle>();
    protected HSSFCellStyle getGRDAmountStyle(int depth) {
        if (!grdAmountStyles.containsKey(depth)){
            HSSFCellStyle cs = wb.createCellStyle();
            cs.cloneStyleFrom(getGRDStyle(depth));
            cs.setAlignment(CellStyle.ALIGN_RIGHT);
            cs.setBorderRight(CellStyle.BORDER_NONE);
            cs.setBorderLeft(CellStyle.BORDER_NONE);
            grdAmountStyles.put(depth, cs);
        }
        return grdAmountStyles.get(depth);
    }
            
    @Override
    public void generate() {
        GroupReportData grd = (GroupReportData) item;
        this.showHeadings();

        //System.out.format("creating a GRD with level = %d, rowspan = %2d and name = %s\n", grd.getLevelDepth(), grd.getRowSpan(), grd.getName());
        if (grd.getLevelDepth() == 2){
            // first-level hierarchy: start a new row
            row = sheet.createRow(rowId.shortValue());
        }
        
        if (grd.getLevelDepth() >= 2){
            colId.set(grd.getLevelDepth() - 2);
            createCenteredCell(getDisplayedName(grd), getGRDStyle(grd.getLevelDepth()), false, grd.getRowSpan(), 1);            
        }
        
        colId.set(metadata.getNumOfHierarchies() > 1 ? grd.getLevelDepth() - 1 : 0);
        
        for(ReportData<?> element:grd.getItems()){
            this.invokeChildExporter(element);
        }       
        
        if (grd.getLevelDepth() >= 1){
            row = sheet.createRow(rowId.shortValue());
            this.createConcludingTrailCellsForReport();
            rowId.inc(); // trailCells ended
        }

        if (this.getParent() == null)
            setColumnWidths();
    }

    
}
