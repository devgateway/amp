/**
 * PDFExportAction.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.AmpARFilter;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.GenericViews;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.view.pdf.GroupReportDataPDF;
import org.dgfoundation.amp.ar.view.pdf.PDFExporter;
import org.digijava.kernel.entity.Locale;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.form.AdvancedReportForm;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEvent;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 * 
 */
public class PDFExportAction extends Action implements PdfPageEvent{

//	private HttpSession session;
	private String noteFromSession;
	
	protected static Logger logger = Logger.getLogger(PDFExportAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		
		//for translation purposes
		TranslatorWorker translator=TranslatorWorker.getInstance();
		Site site = RequestUtils.getSite(request);
		Locale navigationLanguage = RequestUtils.getNavigationLanguage(request);
		String siteId=site.getSiteId();
		String locale=navigationLanguage.getCode();
		
		
		GroupReportData rd=ARUtil.generateReport(mapping,form,request,response);
		rd.setCurrentView(GenericViews.PDF);
		
		//AdvancedReportForm formBean = (AdvancedReportForm) form;
		
		 
		/*
		 * this should not be used anymore as the page size has been included in the ARFilters.
		 * String pageSize=formBean.getPdfPageSize();
		 */
		//use the session to get the existing filters
		String pageSize=null;
		
		HttpSession httpSession = request.getSession();
		AmpARFilter arf=(AmpARFilter) httpSession.getAttribute(ArConstants.REPORTS_FILTER);
		if(arf!=null)
			pageSize=arf.getPageSize();//use the page size set in the filters 

		//the pagesize is not initialized in the filters	
		Rectangle page=null;
		
		if(pageSize==null)
			page=PageSize.A4;
		else{
			if(pageSize.equals("A0")) page=PageSize.A0;
			if(pageSize.equals("A1")) page=PageSize.A1;
			if(pageSize.equals("A2")) page=PageSize.A2;
			if(pageSize.equals("A3")) page=PageSize.A3;
			if(pageSize.equals("A4")) page=PageSize.A4;
		}
		Document document = new Document(page.rotate(),5, 5, 5, 5);		

	     response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition",
	                "attachment; filename=data.pdf");
	   
		PdfWriter writer=PdfWriter.getInstance(document,
				response.getOutputStream());
		writer.setPageEvent(new PDFExportAction());

		HttpSession session=request.getSession();
		noteFromSession=AmpReports.getNote(request.getSession());
		AmpReports r=(AmpReports) session.getAttribute("reportMeta");

		String sortBy=(String) session.getAttribute("sortBy");
		if(sortBy!=null) rd.setSorterColumn(sortBy); 
	
		
		document.open();

		//	create source cols spanning:		
		PDFExporter.widths=new float[rd.getTotalDepth()];		
		for (int k = 0; k < rd.getSourceColsCount().intValue(); k++) {
			PDFExporter.widths[k]=0.125f;
		}
		
		for (int k = rd.getSourceColsCount().intValue();k<rd.getTotalDepth() ; k++) {
			PDFExporter.widths[k]=0.05f;
		}
		
		
		PdfPTable table = new PdfPTable(PDFExporter.widths);
		table.setWidthPercentage(100);
		
		Font titleFont = new Font(Font.COURIER, 16, Font.BOLD);
		
		
		String translatedReportName="Report Name:";
		String translatedReportDescription="Description:";
		try{	
			translatedReportName=TranslatorWorker.translate("rep:pop:ReportName",locale,siteId);
			translatedReportDescription=TranslatorWorker.translate("rep:pop:Description",locale,siteId);
		}catch (WorkerException e){;}
		
		PdfPCell pdfc = new PdfPCell(new Paragraph(translatedReportName+" "+r.getName(),titleFont));
		pdfc.setColspan(rd.getTotalDepth());
		table.addCell(pdfc);
		
		pdfc = new PdfPCell(new Paragraph(translatedReportDescription+" "+r.getReportDescription()));
		pdfc.setColspan(rd.getTotalDepth());
		table.addCell(pdfc);
		
		
		
		GroupReportDataPDF grdp=new GroupReportDataPDF(table,rd,null);
		grdp.setMetadata(r);
		
		//generate a PDF output of the report structure:
		grdp.generate();		
		this.onEndPage(writer,document);
		document.add(table);
		document.setMargins(5,5,5,5);
		document.close();

		return null;

	}

	public void onOpenDocument(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onStartPage(PdfWriter writer, Document arg1) {
		arg1.setMargins(5,5,5,5);
		if(PDFExporter.headingCells==null) return;
		  PdfContentByte cb = writer.getDirectContent();
          cb.saveState();
         if(writer.getPageNumber()==1) return;
        
			Iterator i=PDFExporter.headingCells.iterator();
			PdfPTable table = new PdfPTable(PDFExporter.widths);
			table.setWidthPercentage(100);
			while (i.hasNext()) {
				PdfPCell element = (PdfPCell) i.next();
				table.addCell(element);
			}
			try {
				arg1.add(table);
			} catch (DocumentException e) {
				e.printStackTrace();
				logger.error(e);
			}
		
	}

	public void onEndPage(PdfWriter writer, Document document) {
		try {
            Rectangle page = document.getPageSize();
                     
            BaseFont helv = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            String text = "Page " + writer.getPageNumber();
            float textSize = helv.getWidthPoint(text, 12);
            float textBase = document.bottom() - 35;
            
            cb.beginText();
            cb.setFontAndSize(helv, 12);
            float adjust = helv.getWidthPoint("0", 12);
            cb.setTextMatrix(document.left(), textBase);
            cb.showText(noteFromSession);
            cb.endText();
            
            textSize = helv.getWidthPoint(text, 12);
            textBase = document.bottom() - 15;
            cb.beginText();
            cb.setFontAndSize(helv, 12);
            adjust = helv.getWidthPoint("0", 12);
            cb.setTextMatrix(document.right() - textSize - adjust, textBase);
            cb.showText(text);
            cb.endText();
            
            
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
		
	}

	public void onCloseDocument(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onParagraph(PdfWriter arg0, Document arg1, float arg2) {
		arg1.setMargins(5,5,5,5);
		
	}

	public void onParagraphEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub
		
	}

	public void onChapter(PdfWriter arg0, Document arg1, float arg2, Paragraph arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onChapterEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub
		
	}

	public void onSection(PdfWriter arg0, Document arg1, float arg2, int arg3, Paragraph arg4) {
		// TODO Auto-generated method stub
		
	}

	public void onSectionEnd(PdfWriter arg0, Document arg1, float arg2) {
		// TODO Auto-generated method stub
		
	}

	public void onGenericTag(PdfWriter arg0, Document arg1, Rectangle arg2, String arg3) {
		// TODO Auto-generated method stub
		
	}
}
