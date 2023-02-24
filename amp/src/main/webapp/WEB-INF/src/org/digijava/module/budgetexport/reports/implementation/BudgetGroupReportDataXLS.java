/**
 * 
 */
package org.digijava.module.budgetexport.reports.implementation;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.Viewable;
import org.dgfoundation.amp.ar.view.xls.GroupReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.view.xls.ReportHeadingsXLS;
import org.dgfoundation.amp.ar.view.xls.TrailCellsXLS;
import org.digijava.module.aim.form.AdvancedReportForm;

/**
 * @author alex
 *
 */
public class BudgetGroupReportDataXLS extends GroupReportDataXLS {

    /**
     * @param parent
     * @param item
     */
    public BudgetGroupReportDataXLS(Exporter parent, Viewable item) {
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
    public BudgetGroupReportDataXLS(HSSFWorkbook wb, HSSFSheet sheet,
            HSSFRow row, IntWrapper rowId, IntWrapper colId, Long ownerId,
            Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    protected void showHeadings () {
        GroupReportData grd = (GroupReportData) item;
        //show Headings:        
        BudgetReportHeadingsXLS headings=new BudgetReportHeadingsXLS(this,grd.getFirstColumnReport());
        headings.setAutoSize(this.isAutoSize());
        headings.generate();
    }
    
    @Override
    protected void createPrologueTrailCellsForGRD() {
        
    }
    
    @Override
    protected void createConcludingTrailCellsForReport() {
    }
    
    @Override
    protected void invokeChildExporter( Viewable element) {
        element.invokeExporter(this, true);
    }
    
    @Override
    public void createHeaderLogoAndStatement(HttpServletRequest request, AdvancedReportForm reportForm, String realPath) throws Exception {
    }
    
    @Override
    public void createHeaderNameAndDescription(HttpServletRequest request) throws Exception {
    }
}
