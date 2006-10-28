/**
 * PDFExportAction.java
 * (c) 2006 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.digijava.module.aim.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.GenericViews;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.view.pdf.GroupReportDataPDF;
import org.digijava.module.aim.dbentity.AmpReports;

import com.lowagie.text.Document;
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
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		
		GroupReportData rd=ARUtil.generateReport(mapping,form,request,response);
		rd.setCurrentView(GenericViews.PDF);

		Document document = new Document(PageSize.A0.rotate(),10, 10, 10, 40);
	
	     response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition",
	                "inline; filename=AMPExport.pdf");
	   
		PdfWriter writer=PdfWriter.getInstance(document,
				response.getOutputStream());
		writer.setPageEvent(new PDFExportAction());

		HttpSession session=request.getSession();
		noteFromSession=AmpReports.getNote(request.getSession());
		AmpReports r=(AmpReports) session.getAttribute("reportMeta");
		
		document.open();

		//	create source cols spanning:		
		float[] widths = new float[rd.getTotalDepth()];		
		for (int k = 0; k < rd.getSourceColsCount().intValue(); k++) {
			widths[k]=0.125f;
		}
		
		for (int k = rd.getSourceColsCount().intValue();k<rd.getTotalDepth() ; k++) {
			widths[k]=0.05f;
		}
		
		
		PdfPTable table = new PdfPTable(widths);
		
		Font titleFont = new Font(Font.COURIER, 16, Font.BOLD);
		
		PdfPCell pdfc = new PdfPCell(new Paragraph("Report Name: "+r.getName(),titleFont));
		pdfc.setColspan(rd.getTotalDepth());
		table.addCell(pdfc);
		
		pdfc = new PdfPCell(new Paragraph("Report Description: "+r.getReportDescription()));
		pdfc.setColspan(rd.getTotalDepth());
		table.addCell(pdfc);
		
		
		
		GroupReportDataPDF grdp=new GroupReportDataPDF(table,rd,null);
		grdp.setMetadata(r);
		
		//generate a PDF output of the report structure:
		grdp.generate();		
		this.onEndPage(writer,document);
		document.add(table);
		document.close();

		return null;

	}

	public void onOpenDocument(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onStartPage(PdfWriter arg0, Document arg1) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
