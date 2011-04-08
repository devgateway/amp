/**
 * XLSExportAction.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.util.IOUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
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
import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.util.FeaturesUtil;

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
		HttpSession session = request.getSession();
		AmpARFilter arf=(AmpARFilter) session.getAttribute(ArConstants.REPORTS_FILTER);
		
		if (session.getAttribute("currentMember")!=null || arf.isPublicView()){
	     response.setContentType("application/msexcel");
	     	response.setHeader("Content-Disposition","attachment; filename="+ rd.getName().replace(" ","_") +".xls");
	        AdvancedReportForm reportForm = (AdvancedReportForm) form;
	        //
			AmpReports r=(AmpReports) session.getAttribute("reportMeta");
		
//			for translation purposes
			Site site = RequestUtils.getSite(request);
			Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
					
			String siteId=site.getId().toString();
			String locale=navigationLanguage.getCode();	
			
			
		String sortBy=(String) session.getAttribute("sortBy");
		if(sortBy!=null) rd.setSorterColumn(sortBy); 
			
		//XLSExporter.resetStyles();
	        
		
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
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();	    
	    
		HSSFFooter footer = sheet.getFooter();
		footer.setRight( "Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages() );
			 

		GroupReportDataXLS grdx=new GroupReportDataXLS(wb,sheet, row, rowId,
				colId,  null, rd);
		grdx.setMetadata(r);
			
		
		//show title+desc+logo+statement
		grdx.makeColSpan(rd.getTotalDepth(),false);	
		rowId.inc();
		colId.reset();
		row=sheet.createRow(rowId.shortValue());
		HSSFCell cell=row.createCell(colId.shortValue());
	
		if(reportForm != null && reportForm.getLogoOptions() != null)
			if (reportForm.getLogoOptions().equals("0")) {//disabled
				// do nothing 
			} else if (reportForm.getLogoOptions().equals("1")) {//enabled																		 	                	                
				if (reportForm.getLogoPositionOptions().equals("0")) {//header
					int end = request.getRequestURL().length() - "/aim/xlsExport.do".length();
					String urlPrefix = request.getRequestURL().substring(0, end);
					//								
					InputStream is = new URL(urlPrefix + "/TEMPLATE/ampTemplate/images/AMPLogo.png").openStream();
				    byte[] bytes = IOUtils.toByteArray(is);
				    int idImg = wb.addPicture(bytes,  HSSFWorkbook.PICTURE_TYPE_PNG);
				    is.close();
				    // ajout de l'image sur l'ancre ( lig, col )  
				    HSSFClientAnchor ancreImg = new HSSFClientAnchor();
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
					grdx.makeColSpan(rd.getTotalDepth(),false);	
					rowId.inc();
					colId.reset();
					row=sheet.createRow(rowId.shortValue());
					cell=row.createCell(colId.shortValue());						
				}
				String stmt = "";
				try {
					//TODO TRN: key is all right but lets use default text. Or remove this todo tag.
					stmt = TranslatorWorker.translateText("This Report was created by AMP", locale,siteId);
				} catch (WorkerException e){
				    e.printStackTrace();}
				stmt += " " + FeaturesUtil.getCurrentCountryName();
				if (reportForm.getDateOptions().equals("0")) {//disabled
					// no date
				} else if (reportForm.getDateOptions().equals("1")) {//enable		
					stmt += " " + TranslatorWorker.translateText("on", locale,siteId)+ " " + DateFormat.getDateInstance(DateFormat.FULL, new java.util.Locale(locale)).format(new Date());
				}				 	                	                
				if (reportForm.getStatementPositionOptions().equals("0")) {//header		
					cell.setCellValue(stmt);  
				} else if (reportForm.getStatementPositionOptions().equals("1")) {//footer
					// 
				}				
			}
		grdx.makeColSpan(rd.getTotalDepth(),false);	
		rowId.inc();
		colId.reset();
		row=sheet.createRow(rowId.shortValue());
		cell=row.createCell(colId.shortValue());
		
			String translatedNotes="";
			String translatedReportName="Report Name:";
			String translatedReportDescription="Description:";			
			try{	
				if (FeaturesUtil.getGlobalSettingValue("Amounts in Thousands").equalsIgnoreCase("true")){
			    	translatedNotes=TranslatorWorker.translateText("Amounts are in thousands (000)",locale,siteId);
				}
				
			    if("".equalsIgnoreCase(translatedNotes)){
			    	translatedNotes=AmpReports.getNote(session);    
			    }
			    
			    translatedReportName=TranslatorWorker.translateText("Report Name:",locale,siteId);
				translatedReportDescription=TranslatorWorker.translateText("Description:",locale,siteId);
			}catch (WorkerException e){e.printStackTrace();}
			
			String translatedCurrency = "";
			String currencyCode = (String) session.getAttribute(org.dgfoundation.amp.ar.ArConstants.SELECTED_CURRENCY);
            if(currencyCode != null) {
                translatedCurrency=TranslatorWorker.translateText(currencyCode,locale,siteId);
			    translatedCurrency=("".equalsIgnoreCase(currencyCode))?currencyCode:translatedCurrency;
            }
            else
            {
                translatedCurrency=TranslatorWorker.translateText(Constants.DEFAULT_CURRENCY,locale,siteId);
            }
            translatedNotes = translatedNotes.replaceAll("\n", " ");
			cell.setCellValue(translatedNotes+translatedCurrency/*+"\n"*/);
			
			grdx.makeColSpan(rd.getTotalDepth(),false);
						
			rowId.inc();
			colId.reset();
						
			row=sheet.createRow(rowId.shortValue());
			cell=row.createCell(colId.shortValue());
			cell.setCellValue(/*translatedReportName+" "+*/r.getName());
			HSSFCellStyle cs = wb.createCellStyle();
			cs.setFillBackgroundColor(HSSFColor.BROWN.index);
			HSSFFont font = wb.createFont();
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short)18);			
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			cs.setFont(font);		
			cell.setCellStyle(cs);
			
			
			grdx.makeColSpan(rd.getTotalDepth(),false);
			
			rowId.inc();
			colId.reset();
			
			if (r.getDescription()!=null){
				row=sheet.createRow(rowId.shortValue()); 		 
				cell=row.createCell(colId.shortValue());
				translatedReportDescription = translatedReportDescription.replaceAll("\n", " ");
				cell.setCellValue(translatedReportDescription+" "+r.getReportDescription());
				grdx.makeColSpan(rd.getTotalDepth(),false);
				rowId.inc();
				colId.reset();
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
		cell=row.createCell(colId.shortValue());
		if(reportForm!=null && reportForm.getLogoOptions() !=null)
			if (reportForm.getLogoOptions().equals("0")) {//disabled
				// do nothing 
			} else if (reportForm.getLogoOptions().equals("1")) {//enabled																		 	                	                
				if (reportForm.getLogoPositionOptions().equals("0")) {//header
					// see startPage
				} else if (reportForm.getLogoPositionOptions().equals("1")) {//footer
					int end = request.getRequestURL().length() - "/aim/xlsExport.do".length();
					String urlPrefix = request.getRequestURL().substring(0, end);
					//								
					InputStream is = new URL(urlPrefix + "/TEMPLATE/ampTemplate/images/AMPLogo.png").openStream();
				    byte[] bytes = IOUtils.toByteArray(is);
				    int idImg = wb.addPicture(bytes,  HSSFWorkbook.PICTURE_TYPE_PNG);
				    is.close();
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
					String stmt = "";
					try {
						//TODO TRN: key is all right but if possible replace with default text. or delete this todo tag
						stmt = TranslatorWorker.translateText("This Report was created by AMP", locale,siteId);
					} catch (WorkerException e) {
					    e.printStackTrace();}
					stmt += " " + FeaturesUtil.getCurrentCountryName();
					if (reportForm.getDateOptions().equals("0")) {//disabled
						// no date
					} else if (reportForm.getDateOptions().equals("1")) {//enable		
						stmt += " " + DateFormat.getDateInstance(DateFormat.FULL, new java.util.Locale(locale)).format(new Date());
					}				 	                	                
					if (reportForm.getStatementPositionOptions().equals("0")) {//header		
						//
					} else if (reportForm.getStatementPositionOptions().equals("1")) {//footer
						cell.setCellValue(stmt);  
					}				
				}
	    wb.write(response.getOutputStream());
	    
		}else{
			Site site = RequestUtils.getSite(request);
			Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
			
			String siteId=site.getSiteId();
			String locale=navigationLanguage.getCode();
			
			session.setAttribute("sessionExpired", true);
			response.setContentType("text/html");
    		OutputStreamWriter outputStream = new OutputStreamWriter(response.getOutputStream());
    		PrintWriter out = new PrintWriter(outputStream, true);
    		String url = FeaturesUtil.getGlobalSettingValue("Site Domain");
    		String alert = TranslatorWorker.translateText("Your session has expired. Please log in again.",locale,siteId);
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
