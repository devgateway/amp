/**
 * XLSExportAction.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.*;
import org.dgfoundation.amp.ar.view.xls.GroupReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.view.xls.XLSExportType;
import org.digijava.kernel.request.TLSUtils;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.ResponseUtil;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 * 
 */
public class XLSExportAction extends Action {

    private static Logger logger = Logger.getLogger(XLSExportAction.class) ;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws java.lang.Exception {
        
        HttpSession session = request.getSession();
        
        boolean initFromDB = false;
        TeamMember tm = (TeamMember) session.getAttribute("currentMember");
        if (tm == null || tm.getTeamId() == null )
            tm = null;
        
        if (tm == null)
        {
            initFromDB = "true".equals(request.getParameter("resetFilter"));
        }
        
        logger.info("reportContextId: " + ReportContextData.getFromRequest(true).getContextId()); // DO NOT DELETE THIS CALL - it ensures that a ReportContextMap exists
        boolean isPlain = "true".equals(request.getParameter("plainReport"));
        boolean isRich = "true".equals(request.getParameter("richReport"));
        XLSExportType exportType = XLSExportType.buildWithParams(isPlain, isRich);

        
        AmpReports report = null;
        try {
            report = ARUtil.getReferenceToReport();
        } catch (Exception e) {
            ARUtil.generateReportNotFoundPage(response);
            return null;
        }
        report.validateColumnsAndHierarchies();
        
        AmpARFilter arf = ReportContextData.getFromRequest().loadOrCreateFilter(initFromDB, report);
        
        if (tm == null){
            arf.setPublicView(true);
            }

        GroupReportData rd = ARUtil.generateReport(report, arf, true, false);

        ARUtil.cleanReportOfHtmlCodes(rd);
        
        rd.setCurrentView(GenericViews.XLS);

        String exportFileName = rd.getName() + "_" + exportType + ".xls";

        if (session.getAttribute("currentMember") != null || rd.getReportMetadata().getPublicReport()) {
            response.setContentType("application/msexcel");
            response.setHeader("Content-Disposition", ResponseUtil.encodeContentDispositionForDownload(request, exportFileName));
            AdvancedReportForm reportForm = (AdvancedReportForm) form;
            //
            AmpReports r = ReportContextData.getFromRequest().getReportMeta();
                    
        int numberOfColumns = rd.getTotalDepth();
        
        String sortBy = ReportContextData.getFromRequest().getSortBy();
        if (sortBy != null){
            rd.setSorterColumn(sortBy); 
            rd.setSortAscending(ReportContextData.getFromRequest().getSortAscending());
        }
            
        Map<Long, MetaInfo<String>> sorters = ReportContextData.getFromRequest().getReportSorters();
        //XLSExporter.resetStyles();
        if ( sorters != null && sorters.size() > 0 ) {
            rd.importLevelSorters(sorters, r.getHierarchies().size());
            rd.applyLevelSorter();
        }           
        
        HSSFWorkbook wb = new HSSFWorkbook();
        
        if (numberOfColumns > 250)
        {
            String sheetName = "ERROR Message";
            HSSFSheet sheet = wb.createSheet(sheetName);
            
            
            String[] errMsgs = {TranslatorWorker.translateText("The report has too many columns, please redo the report or export in an another format."), 
                                TranslatorWorker.translateText("Maximum supported number of columns: ") + 250,
                                TranslatorWorker.translateText("You demanded columns: ") + numberOfColumns};
            
            for(int i = 0; i < errMsgs.length; i++)
            {
                HSSFRow row = sheet.createRow(i);
                HSSFCell cell = row.createCell(0);
                cell.setCellValue(errMsgs[i]);
            }
            
            sheet.autoSizeColumn(0);

            wb.write(response.getOutputStream());
            return null;
        }
        
        String sheetName=rd.getName();
        if(sheetName.length()>31) sheetName=sheetName.substring(0,31);
        sheetName = sheetName.replace('/', '_');
        sheetName = sheetName.replace('*', '_');
        sheetName = sheetName.replace('?', '_');
        sheetName = sheetName.replace(']', '_');
        sheetName = sheetName.replace('[', '_');
        sheetName = sheetName.replace('\\', '_');
        
        HSSFSheet sheet = wb.createSheet(sheetName);
        
        
        IntWrapper rowId=new IntWrapper();
        IntWrapper colId=new IntWrapper();
        
        HSSFRow row = sheet.createRow(rowId.intValue());
        //HSSFPatriarch patriarch = sheet.createDrawingPatriarch();     
        
        HSSFFooter footer = sheet.getFooter();
        footer.setRight( "Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages() );
             

        rd.computeRowSpan(0, 0, Integer.MAX_VALUE - 100);

        String publicPortalModeParam = request.getParameter("publicPortalMode");
        Boolean isPublicPortalMode = publicPortalModeParam != null ? Boolean.valueOf(publicPortalModeParam): false;
            
        GroupReportDataXLS grdx = ARUtil.instatiateGroupReportDataXLS(request.getSession(), wb, sheet, row, rowId, colId, null, rd, exportType);
        
        grdx.setMetadata(r);
        grdx.setFilter(arf);
        
        colId.reset();
        if (isPublicPortalMode){
            rowId.reset();
            grdx.setMachineFriendlyColName(true);
        }else{
        //show title+desc+logo+statement
            grdx.makeColSpan(numberOfColumns + 3, false);   
            rowId.inc();
            
            row = sheet.createRow(rowId.shortValue());
            grdx.createHeaderLogoAndStatement(request, reportForm, getServlet().getServletContext().getRealPath("/"));
            grdx.createHeaderNameAndDescription( request );
        }
        
        grdx.generate();


        /*
         * 
         * Commented until Apache POI fixes the following bug: 
         *      https://issues.apache.org/bugzilla/show_bug.cgi?id=49188
         *  
         * Beware issue happens on large db's when generating a report with 
         * a lot of info and columns (8000 rows, 50 columns).
         * 
         *  
        try{
            sheet.autoSizeColumn((short)0);
        }
        catch (ClassCastException e) {
            throw e;
        }
        */
        rowId.inc();
        colId.reset();
        row=sheet.createRow(rowId.shortValue());
        HSSFCell cell=row.createCell(colId.intValue());
        if(reportForm!=null && reportForm.getLogoOptions() !=null)
            if (reportForm.getLogoOptions().equals("0")) {//disabled
                // do nothing 
            } else if (reportForm.getLogoOptions().equals("1")) {//enabled                                                                                                              
                if (reportForm.getLogoPositionOptions().equals("0")) {//header
                    // see startPage
                } else if (reportForm.getLogoPositionOptions().equals("1")) {//footer
                    String path = getServlet().getServletContext().getRealPath("/");
                    InputStream is = new FileInputStream(path + "/TEMPLATE/ampTemplate/images/AMPLogo.png");
                    byte[] bytes = IOUtils.toByteArray(is);
                    int idImg = wb.addPicture(bytes,  HSSFWorkbook.PICTURE_TYPE_PNG);
                    
                    // ajout de l'image sur l'ancre ( lig, col )  
                    HSSFClientAnchor ancreImg = new HSSFClientAnchor();
                    ancreImg.setCol1(colId.shortValue());
                    ancreImg.setRow1(rowId.shortValue());
                    HSSFPicture Img = sheet.createDrawingPatriarch().createPicture( ancreImg,  idImg );          
                    // redim de l'image
                    Img.resize();
                }               
            }
        if(reportForm!=null && reportForm.getStatementOptions() != null)
            if (reportForm.getStatementOptions().equals("0")) {//disabled
                // do nothing 
            } else 
                if (reportForm.getStatementOptions().equals("1")) {//enabled                                        
                    if ((reportForm.getLogoOptions().equals("1")) && (reportForm.getLogoPositionOptions().equals("1"))) { 
                        // creation d'une nouvelle cellule pour le statement    
                        grdx.makeColSpan(rd.getTotalDepth(),false); 
                        rowId.inc();
                        colId.reset();
                        row=sheet.createRow(rowId.shortValue());
                        cell=row.createCell(colId.shortValue());                        
                    }
                    String stmt = TranslatorWorker.translateText("This Report was created by AMP");
                    stmt += " " + FeaturesUtil.getCurrentCountryName();
                    if (reportForm.getDateOptions().equals("0")) {//disabled
                        // no date
                    } else if (reportForm.getDateOptions().equals("1")) {//enable       
                        stmt += " " + DateFormat.getDateInstance(DateFormat.FULL, new java.util.Locale(TLSUtils.getLangCode())).format(new Date());
                    }                                                       
                    if (reportForm.getStatementPositionOptions().equals("0")) {//header     
                        //
                    } else if (reportForm.getStatementPositionOptions().equals("1")) {//footer
                        cell.setCellValue(stmt);  
                    }               
                }
        
        wb.write(response.getOutputStream());
        
        }else{          
            session.setAttribute("sessionExpired", true);
            response.setContentType("text/html");
            OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
            PrintWriter out = new PrintWriter(outputStream, true);
            String url = FeaturesUtil.getGlobalSettingValue("Site Domain");
            String alert = TranslatorWorker.translateText("Your session has expired. Please log in again.");
            String script = "<script>opener.close();" 
                + "alert('"+ alert +"');" 
                + "window.location=('"+ url +"');"
                + "</script>";
            out.println(script);
            out.close();    
            outputStream.close();
            return null;
        }

        return null;
    }   
}
