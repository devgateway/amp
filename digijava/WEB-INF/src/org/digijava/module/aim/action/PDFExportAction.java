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

import net.sf.hibernate.Session;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dgfoundation.amp.ar.AmpNewFilter;
import org.dgfoundation.amp.ar.AmpReportGenerator;
import org.dgfoundation.amp.ar.GenericViews;
import org.dgfoundation.amp.ar.GroupReportData;
import org.dgfoundation.amp.ar.view.pdf.GroupReportDataPDF;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpReports;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
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

		String ampReportId = request.getParameter("ampReportId");

	
		Session session = PersistenceManager.getSession();
		HttpSession hs = request.getSession();

		AmpReports r = (AmpReports) session.get(AmpReports.class, new Long(
				ampReportId));

		AmpNewFilter af = ViewNewAdvancedReport.createFilter(r, mapping, form, request, response);
		if (af == null)
			mapping.findForward("index");

		AmpReportGenerator arg = new AmpReportGenerator(r, af);

		arg.generate();

		GroupReportData rd = arg.getReport();
		rd.setCurrentView(GenericViews.PDF);

		hs.setAttribute("report", rd);

		session.close();

		Document document = new Document(PageSize.A0.rotate(),10, 10, 10, 10);
	
	     response.setContentType("application/pdf");
	        response.setHeader("Content-Disposition",
	                "inline; filename=AMPExport.pdf");
	   
		PdfWriter.getInstance(document,
				response.getOutputStream());

		
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
		
		
		
		
		
		GroupReportDataPDF grdp=new GroupReportDataPDF(table,rd,null);
		
		
		
		
		grdp.generate();
		
		
		document.add(table);
		document.close();
		
		

		return null;

	}
}
