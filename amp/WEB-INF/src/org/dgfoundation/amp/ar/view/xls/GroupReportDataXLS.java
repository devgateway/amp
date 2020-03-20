/**
 * GroupReportDataXLS.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar.view.xls;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.Exporter;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.ReportContextData;
import org.dgfoundation.amp.ar.Viewable;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;


/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 31, 2006
 * 
 */
public class GroupReportDataXLS extends XLSExporter{
    
    /**
     * the maximum allowable width of a column in the Excel file, in characters 
     */
    public final static int MAX_COLUMN_WIDTH = 55;

    /**
     * the column widths computed while generating the report heading - in "1/256 of a char"
     */
    protected TreeMap<Integer, Integer> columnWidths = new TreeMap<Integer, Integer>();
    
    private boolean machineFriendlyColName = false;
    
    /**
     * only makes sense for the top-level parent
     */
    private XLSExportType exportType;
    
    /**
     * @param parent
     * @param item
     */
    public GroupReportDataXLS(Exporter parent, Viewable item) {
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
    public GroupReportDataXLS(HSSFWorkbook wb,HSSFSheet sheet, HSSFRow row, IntWrapper rowId,
            IntWrapper colId, Long ownerId, Viewable item) {
        super(wb, sheet, row, rowId, colId, ownerId, item);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.dgfoundation.amp.ar.Exporter#generate()
     */
    public void generate() {
        GroupReportData grd = (GroupReportData) item;
    
        
        this.showHeadings();
        
        this.createPrologueTrailCellsForGRD();
        //iterate the data
        Iterator i = grd.getItems().iterator();
        while (i.hasNext()) {
            Viewable element = (Viewable) i.next();
            this.invokeChildExporter(element);
        }
        
        this.createConcludingTrailCellsForReport();
        
        if (this.getParent() == null)
            setColumnWidths();  
    }
    
    /**
     * sets the column widths for all the columns; for the columns which do not have it set, makes an autosize
     */
    protected void setColumnWidths()
    {
        int nrColumns = columnWidths.size()>0 ? columnWidths.lastKey() :0;
        for(int i = 0; i <= nrColumns; i++){
            try{
                if (columnWidths.containsKey(i)){
                    sheet.setColumnWidth(i, Math.min(columnWidths.get(i), MAX_COLUMN_WIDTH * 256));
                }
                else{
                    sheet.autoSizeColumn(i, true);
                    if (sheet.getColumnWidth(i) > MAX_COLUMN_WIDTH * 256)
                        sheet.setColumnWidth(i, MAX_COLUMN_WIDTH * 256);                
                }
            }
            catch(Exception e){
                logger.error(e.getMessage(), e);
                // autoSizeColumn() or setColumnWidth() sometime fail
            }
        }       
    }
    
    protected void invokeChildExporter( Viewable element) {
        element.invokeExporter(this);
    }
    
    protected void showHeadings () {
        GroupReportData grd = (GroupReportData) item;
        //show Headings:        
        ReportHeadingsXLS headings = new ReportHeadingsXLS(this, grd.getFirstColumnReport());
        headings.setAutoSize(this.isAutoSize());
        headings.setMachineFriendlyColName(this.machineFriendlyColName);
        headings.generate();
    }
    
    protected void createPrologueTrailCellsForGRD () {
        //      trail cells:
        GroupReportData grd = (GroupReportData) item;
        if ((grd != null) && (grd.getParent() != null) && (grd.getParent().getLevelDepth() != 0)){
            TrailCellsXLS trails2=new TrailCellsXLS(this,grd);
            trails2.generate();
        }
        
    }
    protected void createConcludingTrailCellsForReport () {
        GroupReportData grd = (GroupReportData) item;
        if ((grd.getParent() == null) || (grd.getParent().getLevelDepth() == 0)) {
            TrailCellsXLS trails2 = new TrailCellsXLS(this,grd);
            trails2.generate();
        }
    }
    
    public void createHeaderLogoAndStatement(HttpServletRequest request, AdvancedReportForm reportForm, String realPath) throws Exception {
        

        //for translation purposes
        String locale = RequestUtils.getNavigationLanguage(request).getCode();
        GroupReportData rd = (GroupReportData) item;
        
        HSSFCell cell           = row.createCell(colId.intValue());
        
        if(reportForm != null && reportForm.getLogoOptions() != null)
            if (reportForm.getLogoOptions().equals("0")) {//disabled
                // do nothing 
            } else if (reportForm.getLogoOptions().equals("1")) {//enabled                                                                                                              
                if (reportForm.getLogoPositionOptions().equals("0")) {//header
                    //String path = getServlet().getServletContext().getRealPath("/");
                    InputStream is = new FileInputStream(realPath + "/TEMPLATE/ampTemplate/images/AMPLogo.png");
                    byte[] bytes = IOUtils.toByteArray(is);
                    int idImg = wb.addPicture(bytes,  HSSFWorkbook.PICTURE_TYPE_PNG);
                   
                    // ajout de l'image sur l'ancre ( lig, col )  
                    HSSFClientAnchor ancreImg = new HSSFClientAnchor();
                    
                    //Quick fix for the logo position 
                    colId.reset();
                    rowId.reset();
                    ancreImg.setCol1(colId.shortValue());
                    ancreImg.setRow1(rowId.shortValue());
                    HSSFPicture Img = sheet.createDrawingPatriarch().createPicture( ancreImg,  idImg );          
                    // redim de l'image
                    Img.resize();
                } else if (reportForm.getLogoPositionOptions().equals("1")) {//footer
                    // see endPage function
                }               
            }
        if(reportForm != null && reportForm.getStatementOptions() != null)
            if (reportForm.getStatementOptions().equals("0")) {//disabled
                // do nothing 
            } else if (reportForm.getStatementOptions().equals("1")) {//enabled                                     
                if ((reportForm.getLogoOptions().equals("1")) && (reportForm.getLogoPositionOptions().equals("0"))) { 
                    // creation d'une nouvelle cellule pour le statement    
                    this.makeColSpan(rd.getTotalDepth(),false); 
                    rowId.inc();
                    colId.reset();
                    row=sheet.createRow(rowId.shortValue());
                    cell=row.createCell(colId.intValue());                      
                }
                String stmt = "";
                stmt = TranslatorWorker.translateText("This Report was created in AMP");
                stmt += " " + FeaturesUtil.getCurrentCountryName();
                if (reportForm.getDateOptions().equals("0")) {//disabled
                    // no date
                } else if (reportForm.getDateOptions().equals("1")) {//enable       
                    stmt += " " + TranslatorWorker.translateText("on")+ " " + DateFormat.getDateInstance(DateFormat.FULL, new java.util.Locale(locale)).format(new Date());
                }                                                       
                if (reportForm.getStatementPositionOptions().equals("0")) {//header     
                    cell.setCellValue(stmt);  
                } else if (reportForm.getStatementPositionOptions().equals("1")) {//footer
                    // 
                }               
            }
    }
    
    public void createHeaderNameAndDescription(HttpServletRequest request) throws Exception {
        
        HttpSession session     =  request.getSession();
        //for translation purposes
//      Site site = RequestUtils.getSite(request);
//      Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);        

        GroupReportData rd = (GroupReportData) item;
        AmpARFilter arf = ReportContextData.getFromRequest().getFilter();
        
        boolean isPlain = getArchExportType() == XLSExportType.PLAIN_XLS_EXPORT;

        if (!isPlain)
            this.makeColSpan(rd.getTotalDepth(), false);
        
        rowId.inc();
        colId.reset();
        row = sheet.createRow(rowId.shortValue());
        HSSFCell cell=row.createCell(colId.shortValue());
        
            String translatedNotes = TranslatorWorker.translateText(arf.getUnitsOptions().userMessage);
            String translatedReportDescription = "Description:";
                            
            String translatedCurrency = TranslatorWorker.translateText(ReportContextData.getFromRequest().getSelectedCurrencyName());
            translatedNotes = translatedNotes.replaceAll("\n", " ");
            cell.setCellValue(translatedNotes+translatedCurrency/*+"\n"*/);

            if (!isPlain)
                this.makeColSpan(rd.getTotalDepth(),false);
                        
            rowId.inc();
            colId.reset();
                        
            row=sheet.createRow(rowId.shortValue());
            cell=row.createCell(colId.shortValue());
            cell.setCellValue(/*translatedReportName+" "+*/this.metadata.getName());
            HSSFCellStyle cs = wb.createCellStyle();
            cs.setFillBackgroundColor(HSSFColor.BROWN.index);
            HSSFFont font = wb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short)18);          
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            cs.setFont(font);       
            cell.setCellStyle(cs);
            row.setHeightInPoints(30);

            if (!isPlain)
                this.makeColSpan(rd.getTotalDepth(),false);
            
            rowId.inc();
            colId.reset();
            
            if (this.metadata.getDescription()!=null){
                row=sheet.createRow(rowId.shortValue());         
                cell=row.createCell(colId.shortValue());
                translatedReportDescription = translatedReportDescription.replaceAll("\n", " ");
                cell.setCellValue(translatedReportDescription+" "+this.metadata.getReportDescription());
                if (!isPlain)
                    this.makeColSpan(rd.getTotalDepth(),false);
                rowId.inc();
                colId.reset();
            }
    }

    public void setMachineFriendlyColName(boolean machineFriendlyColName) {
        this.machineFriendlyColName = machineFriendlyColName;
    }

    /**
     * use this instead of directly accessing {@link #exportType} - the field will be null for non-parents!
     * @return
     */
    public XLSExportType getArchExportType(){
        return ((GroupReportDataXLS) this.getArchParent()).exportType;
    }
    
    public void setExportType(XLSExportType exportType){
        if (this.parent != null)
            throw new RuntimeException("only allowed to set exportType for archParent!");
        this.exportType = exportType;
    }
}
