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
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Aug 28, 2006
 * 
 */
public class PDFExportAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws java.lang.Exception {
		
		GroupReportData rd=ARUtil.generateReport(mapping,form,request,response);
		
		rd.setCurrentView(GenericViews.PDF);

		Document document = new Document(PageSize.A0.rotate(),10, 10, 10, 10);
	
	     response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition",
	                "inline; filename=AMPExport.pdf");
	   
		PdfWriter.getInstance(document,
				response.getOutputStream());

		HttpSession session=request.getSession();
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
		
		document.add(table);
		document.close();

		return null;

	}
}
