/**
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.digijava.module.aim.form.AdvancedReportForm;

/**
 * @author mmoras
 *
 */
public class PlainGroupReportDataXLS extends GroupReportDataXLS {

    /**
     * @param parent
     * @param item
     */
    public PlainGroupReportDataXLS(Exporter parent, Viewable item) {
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
    public PlainGroupReportDataXLS(HSSFWorkbook wb, HSSFSheet sheet,
            HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
            Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void showHeadings() {
        GroupReportData grd = (GroupReportData) item;
        //show Headings:        
        PlainReportHeadingsXLS headings=new PlainReportHeadingsXLS(this,grd.getFirstColumnReport());
        headings.setAutoSize(this.isAutoSize());
        headings.generate();
    }   
    @Override
    protected void createPrologueTrailCellsForGRD () {
    }
    
    @Override
    protected void createConcludingTrailCellsForReport() {
        GroupReportData grd = (GroupReportData) item;
        if (grd.getParent() != null && ((GroupReportData)grd.getParent()).getLevelDepth() == 0){
            GroupReportData groupReport = (GroupReportData) item;
            PlainTrailCellsXLS trails = new PlainTrailCellsXLS(this, groupReport);
            trails.generate();
        }
    }
    @Override
    protected void invokeChildExporter( Viewable element) {
        element.invokeExporter(this, false, XLSExportType.PLAIN_XLS_EXPORT);
    }
    
    @Override
    public void createHeaderLogoAndStatement(HttpServletRequest request, AdvancedReportForm reportForm, String realPath) throws Exception {
        // do nothing
    }
    
}
