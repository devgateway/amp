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
import org.digijava.module.aim.dbentity.AmpReports;

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
	                "inline; filename=AMPExport.xls");

	        HttpSession session=request.getSession();
			AmpReports r=(AmpReports) session.getAttribute("reportMeta");
		
		XLSExporter.resetStyles();
	        
		
		HSSFWorkbook wb = new HSSFWorkbook();
		String sheetName=rd.getName();
		if(sheetName.length()>31) sheetName=sheetName.substring(0,31);
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
			cell.setCellValue(AmpReports.getNote(session) + "\n");
			grdx.makeColSpan(rd.getTotalDepth());
			
			
			rowId.inc();
			colId.reset();
			
			row=sheet.createRow(rowId.shortValue());
			cell=row.createCell(colId.shortValue());
			cell.setCellValue("Report Name: "+r.getName());
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
			cell.setCellValue("Report Description: "+r.getReportDescription());
			grdx.makeColSpan(rd.getTotalDepth());
			rowId.inc();
			colId.reset();
			
	
		
		grdx.generate();
	   
	    wb.write(response.getOutputStream());
	    
	    

		return null;
	}

}
