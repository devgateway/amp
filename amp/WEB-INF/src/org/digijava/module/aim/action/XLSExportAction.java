/**
 * XLSExportAction.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.GenericViews;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.view.xls.GroupReportDataXLS;
import org.dgfoundation.amp.ar.view.xls.IntWrapper;
import org.dgfoundation.amp.ar.view.xls.XLSExporter;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.helper.Constants;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Sep 1, 2006
 * 
 */
public class XLSExportAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {

		GroupReportData rd = ARUtil.generateReport(mapping, form, request,
				response);

		rd.setCurrentView(GenericViews.XLS);
		
	     response.setContentType("application/msexcel");
	        response.setHeader("Content-Disposition",
	                "inline; filename=data.xls");

	        HttpSession session=request.getSession();
			AmpReports r=(AmpReports) session.getAttribute("reportMeta");
		
//			for translation purposes
			TranslatorWorker translator=TranslatorWorker.getInstance();
			Site site = RequestUtils.getSite(request);
			Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
					
			String siteId=site.getSiteId();
			String locale=navigationLanguage.getCode();	
			
			
		String sortBy=(String) session.getAttribute("sortBy");
		if(sortBy!=null) rd.setSorterColumn(sortBy); 
			
		XLSExporter.resetStyles();
	        
		
		HSSFWorkbook wb = new HSSFWorkbook();
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
		
	    
		HSSFFooter footer = sheet.getFooter();
		footer.setRight( "Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages() );
			 

		GroupReportDataXLS grdx=new GroupReportDataXLS(wb,sheet, row, rowId,
				colId,  null, rd);
		grdx.setMetadata(r);
			
		
		//show title+desc
			rowId.inc();
			colId.reset();
			row=sheet.createRow(rowId.shortValue());
			HSSFCell cell=row.createCell(colId.shortValue());
			
			String translatedNotes="";
			String translatedReportName="Report Name:";
			String translatedReportDescription="Description:";
			
			try{	
			    	translatedNotes=TranslatorWorker.translate("rep:pop:AllAmount",locale,siteId);
			    	if("".equalsIgnoreCase(translatedNotes)){
			    	    translatedNotes=AmpReports.getNote(session);    
			    	}
				translatedReportName=TranslatorWorker.translate("rep:pop:ReportName",locale,siteId);
				translatedReportDescription=TranslatorWorker.translate("rep:pop:Description",locale,siteId);
			}catch (WorkerException e){;}
			
			String translatedCurrency = "";
			String currencyCode = (String) session.getAttribute(org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY);
            if(currencyCode != null) {
                translatedCurrency=TranslatorWorker.translate("aim:currency:" + currencyCode.toLowerCase().replaceAll(" ", ""),locale,siteId);
			    translatedCurrency=("".equalsIgnoreCase(currencyCode))?currencyCode:translatedCurrency;
            }
            else
            {
                translatedCurrency=TranslatorWorker.translate("aim:currency:" +Constants.DEFAULT_CURRENCY.toLowerCase().replaceAll(" ", ""),locale,siteId);
            }
            
			cell.setCellValue(translatedNotes+translatedCurrency+"\n");
			
			grdx.makeColSpan(rd.getTotalDepth());
			
			
			rowId.inc();
			colId.reset();
			
			
			row=sheet.createRow(rowId.shortValue());
			cell=row.createCell(colId.shortValue());
			cell.setCellValue(translatedReportName+": "+r.getName());
			HSSFCellStyle cs = wb.createCellStyle();
			cs.setFillBackgroundColor(HSSFColor.BROWN.index);
			HSSFFont font = wb.createFont();
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short)25);			
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			cs.setFont(font);		
			cell.setCellStyle(cs);
			
			
			grdx.makeColSpan(rd.getTotalDepth());
			
			rowId.inc();
			colId.reset();
			
			
			row=sheet.createRow(rowId.shortValue()); 		 
			cell=row.createCell(colId.shortValue());
			cell.setCellValue(translatedReportDescription+": "+r.getReportDescription());
			grdx.makeColSpan(rd.getTotalDepth());
			rowId.inc();
			colId.reset();
			
	
		
		grdx.generate();
	   
	    wb.write(response.getOutputStream());
	    
	    

		return null;
	}

}
