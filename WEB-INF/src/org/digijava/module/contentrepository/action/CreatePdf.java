package org.digijava.module.contentrepository.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;

import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class CreatePdf extends Action {
@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
//    	 PdfWriter.getInstance(doc, baos);
//    	 doc.open();
//         com.lowagie.text.Font pageTitleFont = com.lowagie.text.FontFactory.getFont("Arial", 24, com.lowagie.text.Font.BOLD);
//         Paragraph pageTitle = new Paragraph(TranslatorWorker.translateText("Test PDF", request), pageTitleFont);
//         pageTitle.setAlignment(Element.ALIGN_CENTER);
//         doc.add(pageTitle);
//         doc.add(new Paragraph(" "));
//         doc.close();
//         byte[] pdfbody= baos.toByteArray();
//         InputStream stream= new ByteArrayInputStream(pdfbody);
//         String fileName="dare.pdf";
//         String contentType="application/pdf";
//         int fileSize=pdfbody.length;
//         //create jcr node
//         Session jcrWriteSession		= DocumentManagerUtil.getWriteSession(request);
//         Node userHomeNode			= DocumentManagerUtil.getUserPrivateNode(jcrWriteSession, getCurrentTeamMember(request));
//         NodeWrapper nodeWrapper		= new NodeWrapper(null, request, userHomeNode, false, new ActionErrors());
//			if ( nodeWrapper != null && !nodeWrapper.isErrorAppeared() ){
//				nodeWrapper.saveNode(jcrWriteSession);
//			}
		//aq mere last approved version unda davaupdate-o am node-is
		//public resource unda gavaketo am node-isgan
		
    }catch (Exception e) {
		// TODO: handle exception
	}
		return mapping.findForward("forward");
	}

	private TeamMember getCurrentTeamMember( HttpServletRequest request ) {
		HttpSession httpSession		= request.getSession();
		TeamMember teamMember		= (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
		return teamMember;
	}
}
