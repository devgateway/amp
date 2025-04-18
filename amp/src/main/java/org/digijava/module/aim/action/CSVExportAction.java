/**
 * CSVExportAction.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 *
 */
package org.digijava.module.aim.action;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ecs.xml.XML;
import org.apache.ecs.xml.XMLDocument;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.*;
import org.dgfoundation.amp.ar.view.xls.GroupReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.view.xls.XLSExportType;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.budgetexport.util.BudgetExportConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;

/**
 * CSV Export is actually using the XLS export API. The difference is only at
 * the output processing where XLS is simply converted to CSV
 *
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 7, 2006
 *
 */
public class CSVExportAction
    extends Action {

    protected static Logger logger = Logger.getLogger(CSVExportAction.class);
    
  public ActionForward execute(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request,
                               HttpServletResponse response) throws java.lang.
      Exception {

    HttpSession session = request.getSession();
    boolean asXml = request.getParameter("asXml") != null && request.getParameter("asXml").equalsIgnoreCase("true");

    if (asXml && request.getParameter("budgetExport") != null && request.getParameter("budgetExport").equalsIgnoreCase("true")) {
        request.getSession().setAttribute(BudgetExportConstants.BUDGET_EXPORT_TYPE, "sometype");
        request.getSession().setAttribute(BudgetExportConstants.BUDGET_EXPORT_PROJECT_ID, request.getParameter("projectId"));
    }

    boolean initFromDB = false;
    TeamMember tm = (TeamMember) session.getAttribute("currentMember");
    if (tm == null || tm.getTeamId() == null )
        tm = null;
    
    if (tm == null)
    {
        initFromDB = "true".equals(request.getParameter("resetFilter"));
    }
    
    logger.info("reportContextId: " + ReportContextData.getFromRequest(true).getContextId());
    
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

    rd.setCurrentView(GenericViews.XLS);
    
    //To return data, we need to check whether there's a logged user or if the report is public
    if (session.getAttribute("currentMember")!=null || rd.getReportMetadata().getPublicReport()){
        if (!asXml) {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                           "inline; filename=data.csv ");
        } else {
            response.setContentType("text/xml");
            response.setHeader("Content-Disposition",
                                       "inline; filename=data.xml ");
        }
    
        HSSFWorkbook wb = new HSSFWorkbook();
    
        String sheetName = rd.getName();
        if (sheetName.length() > 31)
          sheetName = sheetName.substring(0, 31);
    
        HSSFSheet sheet = wb.createSheet(getCleanName(sheetName));
    
        IntWrapper rowId = new IntWrapper();
        IntWrapper colId = new IntWrapper();
    
        HSSFRow row = sheet.createRow(rowId.intValue());
    
        GroupReportDataXLS grdx = ARUtil.instatiateGroupReportDataXLS(request.getSession(), wb, sheet, row, rowId, colId, null, rd, XLSExportType.SIMPLE_XLS_EXPORT);
    
        grdx.setMetadata(report);
    
        String sortBy = ReportContextData.getFromRequest().getSortBy();
        if (sortBy != null) {
          rd.setSorterColumn(sortBy);
          rd.setSortAscending( ReportContextData.getFromRequest().getSortAscending());
        }
    
//      //show title+desc
//      rowId.inc();
//      colId.reset();
//      row = sheet.createRow(rowId.shortValue());
//      HSSFCell cell = row.createCell(colId.shortValue());
//  
//      Site site = RequestUtils.getSite(request);
//      Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
//  
//      String siteId=site.getSiteId();
//      String locale=navigationLanguage.getCode(); 
//      
//      String translatedNotes = "";
//      if (FeaturesUtil.getGlobalSettingValue("Amounts in Thousands").equalsIgnoreCase("true")){
//          translatedNotes = TranslatorWorker.translateText("Amounts are in thousands (000)", locale, siteId);
//      }
//      if ("".equalsIgnoreCase(translatedNotes)) {
//          translatedNotes = AmpReports.getNote(session);
//      }
//      
//      
//      cell.setCellValue(translatedNotes + "\n");
//      
//      grdx.makeColSpan(rd.getTotalDepth(),false);
//      rowId.inc();
//      colId.reset();
//  
//      row = sheet.createRow(rowId.shortValue());
//      cell = row.createCell(colId.shortValue());
//      
//      
//      String translatedReportName="Report Name:";
//      String translatedReportDescription="Description:";
//      try{    
//          translatedReportName=TranslatorWorker.translateText("Report Name:",locale,siteId);
//          translatedReportDescription=TranslatorWorker.translateText("Description:",locale,siteId);
//      }catch (WorkerException e){;}
//  
//      cell.setCellValue(translatedReportName+": " + r.getName());
//  
//      grdx.makeColSpan(rd.getTotalDepth(),false);
//      rowId.inc();
//      colId.reset();
//  
//      row = sheet.createRow(rowId.shortValue());
//      cell = row.createCell(colId.shortValue());
//      cell.setCellValue(translatedReportDescription+": " + r.getReportDescription());
//  
//      grdx.makeColSpan(rd.getTotalDepth(),false);
//      rowId.inc();
//      colId.reset();
        
        grdx.createHeaderNameAndDescription(request);
        
        grdx.setAutoSize(false);
        grdx.generate();


        XMLDocument reportXML = null;
        XML root = null;

        if (asXml) {
            reportXML = new XMLDocument();
            reportXML.setCodeset("UTF-8");
            root = new XML("report");
            reportXML.addElement(root);
        }

        // we now iterate the rows and create the csv
    
        StringBuffer sb = new StringBuffer();
        Iterator i = sheet.rowIterator();
        while (i.hasNext()) {
          HSSFRow crow = (HSSFRow) i.next();
          XML rowTag = null;
            if (asXml) {
                rowTag = new XML("row");
              root.addElement(rowTag);
            }
          for (short ii = crow.getFirstCellNum(); ii <= crow.getLastCellNum(); ii++) {
            HSSFCell ccell = crow.getCell(ii);
            String s = "";

              XML columnTag = null;
              if (asXml) {
                  columnTag = new XML("column");
                  rowTag.addElement(columnTag);
              }

            if (ccell != null) {
              if (ccell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                s = Double.toString(ccell.getNumericCellValue());
                if (s.matches(".*[eE].*")) {
                    double value = Double.parseDouble(String.valueOf(ccell.getNumericCellValue()));
                    NumberFormat formatter = new DecimalFormat("#0.00");
                    s = formatter.format (value);
                }
                if (asXml) {
                    columnTag.addElement(FormatHelper.formatNumber(ccell.getNumericCellValue()));
                }
              } else {
                s = ccell.getStringCellValue();
                if (asXml) {
                    String escapedXml = StringEscapeUtils.escapeXml(ccell.getStringCellValue());
                    columnTag.addElement(escapedXml);
                }
              }
              sb.append("\"").append(s).append("\"");
    
              if (ii < crow.getLastCellNum())
                sb.append(",");
            }
          }
          sb.append("\n");
    
        }

        if (!asXml) {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(response.
                                    getOutputStream(), "UnicodeLittle"), true);
            out.println(sb.toString());
            out.close();
        } else {
            reportXML.setCodeset("UTF-8");
            reportXML.output(response.getOutputStream());
            response.getOutputStream().close();
        }

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

  
  private String getCleanName(String name){
      name=name.replaceAll("/", "_");
      name=name.replace("\\\\", "_");
      name=name.replaceAll("\\*", "_");
      name=name.replaceAll("\\?", "_");
      name=name.replaceAll("\\[", "_");
      name=name.replaceAll("\\]", "_");
      return name;
  }
}
