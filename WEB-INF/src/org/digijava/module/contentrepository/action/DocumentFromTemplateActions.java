package org.digijava.module.contentrepository.action;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.contentrepository.dbentity.CrDocumentNodeAttributes;
import org.digijava.module.contentrepository.dbentity.NodeLastApprovedVersion;
import org.digijava.module.contentrepository.dbentity.template.PossibleValue;
import org.digijava.module.contentrepository.dbentity.template.StaticTextField;
import org.digijava.module.contentrepository.dbentity.template.TemplateDoc;
import org.digijava.module.contentrepository.dbentity.template.TemplateField;
import org.digijava.module.contentrepository.form.CreateDocFromTemplateForm;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.template.PdfFileHelper;
import org.digijava.module.contentrepository.helper.template.SubmittedValueHolder;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.contentrepository.util.TemplateDocsUtil;

import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class DocumentFromTemplateActions extends DispatchAction {
	
	public ActionForward loadTemplates(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		CreateDocFromTemplateForm myForm=(CreateDocFromTemplateForm)form;
		clearForm(myForm);
		List<TemplateDoc> tempDocs=TemplateDocsUtil.getTemplateDocs();
		if(tempDocs!=null && tempDocs.size()>0){
			Collections.sort(tempDocs, new TemplateDocsUtil.HelperTempDocNameComparator());			
		}
		myForm.setTemplates(tempDocs);
		return mapping.findForward("forward");
	}
	
	public ActionForward getTemplate(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		CreateDocFromTemplateForm myForm=(CreateDocFromTemplateForm)form;
		if(myForm.getTemplateId()!= null && ! myForm.getTemplateId().equals(new Long(-1))){
			TemplateDoc tempDoc= TemplateDocsUtil.getTemplateDoc(myForm.getTemplateId());
			myForm.setSelectedTemplate(tempDoc);
			List<TemplateField> fields= new ArrayList<TemplateField>(tempDoc.getFields());
			Collections.sort(fields, new TemplateDocsUtil.TempDocFieldOrdinaryNumberComparator());
			for (TemplateField templateField : fields) {
				if(templateField.getPossibleValues()!=null){
					List<PossibleValue> posVals= new ArrayList<PossibleValue>(templateField.getPossibleValues()) ;
					Collections.sort(posVals, new TemplateDocsUtil.PossibleValuesValueComparator());
					templateField.setPossibleValuesList(posVals);
				}
			}
			myForm.setFields(fields);
			tempDoc.setFields(new HashSet<TemplateField>(fields) );
		}else{
			myForm.setDocumentName(null);
			myForm.setFields(null);
			myForm.setSelectedTemplate(null);
		}				
		return mapping.findForward("forward");
	}
	
	public ActionForward saveDocument(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception {
		CreateDocFromTemplateForm myForm=(CreateDocFromTemplateForm)form;
		SubmittedValueHolder subValHolder=null;
		List<SubmittedValueHolder> submittedValsHolder=new ArrayList<SubmittedValueHolder>();
		List<TemplateField> fields=myForm.getFields();
		for (TemplateField field : fields) {
			if(field instanceof StaticTextField){
				PossibleValue posVal=(PossibleValue)field.getPossibleValues().toArray()[0];
				subValHolder=new SubmittedValueHolder(field.getOrdinalNumber(), posVal.getValue());
				submittedValsHolder.add(subValHolder);
			}
		}
		List<String> requestParameterNames = Collections.list((Enumeration<String>)request.getParameterNames());
		for (String parameter : requestParameterNames) {
			if(parameter.startsWith("doc_")){
				//in case it's multibox and multiple select, then submitted values can be array
				String[] submittedParameterValues= request.getParameterValues(parameter);
				for(int i=0;i<submittedParameterValues.length;i++){
					if(submittedParameterValues[i].length()>0){
						if(parameter.startsWith("doc_select_") && submittedParameterValues[i].equals("-Select-")){
							continue;
						}
						Integer ordNumber=new Integer(parameter.substring(parameter.lastIndexOf("_")+1));
						subValHolder=new SubmittedValueHolder(ordNumber, submittedParameterValues[i]);
						submittedValsHolder.add(subValHolder);
					}
				}
			}
		}
		
		Collections.sort(submittedValsHolder, new TemplateDocsUtil.SubmittedValuesOrdinaryNumberComparator());		
		//create pdf from the list
		createPdf(submittedValsHolder, myForm.getDocumentName(),request);
		
		clearForm(myForm);
		return mapping.findForward("showResources");
	}
	
	
	private void createPdf(List<SubmittedValueHolder> pdfContent, String pdfName,HttpServletRequest request){
		com.lowagie.text.Document doc = new com.lowagie.text.Document(PageSize.A4);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
	    	 PdfWriter.getInstance(doc, baos);
	    	 doc.open();
	         com.lowagie.text.Font pageTitleFont = com.lowagie.text.FontFactory.getFont("Arial", 24, com.lowagie.text.Font.BOLD);
	         com.lowagie.text.Font plainFont = new Font(Font.TIMES_ROMAN, 10);
	         Paragraph pageTitle = new Paragraph(pdfName, pageTitleFont);
	         pageTitle.setAlignment(Element.ALIGN_CENTER);
	         doc.add(pageTitle);
	         doc.add(new Paragraph(" "));
	         for (SubmittedValueHolder pdfContentElement : pdfContent) {
	        	 Paragraph pdfContentEl = new Paragraph(pdfContentElement.getSubmittedValue(), plainFont);
	        	 doc.add(pdfContentEl);
		         doc.add(new Paragraph(" "));
	 		 }
	         doc.close();
	         byte[] pdfbody= baos.toByteArray();
	         String contentType="application/pdf";
	         PdfFileHelper pdfHelper=new PdfFileHelper(pdfName, contentType, pdfbody);
	         //create jcr node
	         Session jcrWriteSession		= DocumentManagerUtil.getWriteSession(request); 
	         Node userHomeNode			= DocumentManagerUtil.getUserPrivateNode(jcrWriteSession, getCurrentTeamMember(request));
	         NodeWrapper nodeWrapper		= new NodeWrapper(pdfHelper, request, userHomeNode, false, new ActionErrors());
			 if ( nodeWrapper != null && !nodeWrapper.isErrorAppeared() ){
					nodeWrapper.saveNode(jcrWriteSession);
			 }
			 //last approved version
			 String lastApprovedNodeVersionUUID=DocumentManagerUtil.getNodeOfLastVersion(nodeWrapper.getUuid(), request).getUUID();
			 NodeLastApprovedVersion lastAppVersion=new NodeLastApprovedVersion(nodeWrapper.getUuid(), lastApprovedNodeVersionUUID);
			 DbUtil.saveOrUpdateObject(lastAppVersion);
			 //public resource
			 CrDocumentNodeAttributes docAttributes=new CrDocumentNodeAttributes();
			 docAttributes.setPublicDocument(true);
			 docAttributes.setUuid(nodeWrapper.getUuid());
			 docAttributes.setPublicVersionUUID(lastApprovedNodeVersionUUID);
			 DbUtil.saveOrUpdateObject(docAttributes);
	    }catch (Exception e) {
	    	e.printStackTrace();
		}
	}
	
	private TeamMember getCurrentTeamMember( HttpServletRequest request ) {
		HttpSession httpSession		= request.getSession();
		TeamMember teamMember		= (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
		return teamMember;
	}
	
	private void clearForm(CreateDocFromTemplateForm form){
		form.setSelectedTemplate(null);
		form.setTemplateId(null);
		form.setTemplates(null);
		form.setFields(null);
		form.setDocumentName(null);
	}
}
