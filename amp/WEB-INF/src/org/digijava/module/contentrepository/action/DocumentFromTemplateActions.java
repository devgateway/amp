package org.digijava.module.contentrepository.action;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.contentrepository.dbentity.NodeLastApprovedVersion;
import org.digijava.module.contentrepository.dbentity.template.PossibleValue;
import org.digijava.module.contentrepository.dbentity.template.StaticTextField;
import org.digijava.module.contentrepository.dbentity.template.TemplateDoc;
import org.digijava.module.contentrepository.dbentity.template.TemplateField;
import org.digijava.module.contentrepository.form.CreateDocFromTemplateForm;
import org.digijava.module.contentrepository.helper.NodeWrapper;
import org.digijava.module.contentrepository.helper.template.PdfFileHelper;
import org.digijava.module.contentrepository.helper.template.SubmittedValueHolder;
import org.digijava.module.contentrepository.helper.template.TemplateConstants;
import org.digijava.module.contentrepository.helper.template.WordDocumentHelper;
import org.digijava.module.contentrepository.util.DocumentManagerUtil;
import org.digijava.module.contentrepository.util.TemplateDocsUtil;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.List;

/**
 * contians actions , that can be done while trying to create document using Templates
 * @author Dare
 *
 */
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
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        String action = request.getParameter("actType");
        if (action.equalsIgnoreCase("saveDocument"))
        {
            return saveDocument(mapping, form, request, response);
        }
        if (action.equalsIgnoreCase("getTemplate"))
        {
            return  getTemplate(mapping,form, request, response);
        }
        return loadTemplates(mapping, form, request, response);

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
        if(fields!=null){
            for (TemplateField field : fields) {
                if(field instanceof StaticTextField){
                    PossibleValue posVal=(PossibleValue)field.getPossibleValues().toArray()[0];
                    subValHolder=new SubmittedValueHolder(field.getOrdinalNumber(), posVal.getValue());
                    subValHolder.setNeedsNewParagraph(true);
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
                            if(i==0){
                                subValHolder.setNeedsNewParagraph(true);
                            }else{
                                subValHolder.setNeedsNewParagraph(false);
                            }
                            submittedValsHolder.add(subValHolder);
                        }
                    }
                }
            }
            
            submittedValsHolder.sort(new TemplateDocsUtil.SubmittedValuesOrdinaryNumberComparator());
            //create pdf or word from the list
            String nodeuuid=null;
            if(myForm.getDocType()!=null){
                if(myForm.getDocType().equals(TemplateConstants.DOC_TYPE_PDF)){
                    nodeuuid = createPdf(submittedValsHolder, myForm.getDocumentName(), myForm.getDocumentTypeCateg(), myForm.getDocOwnerType(), request);
                }else if(myForm.getDocType().equals(TemplateConstants.DOC_TYPE_WORD)){
                    nodeuuid = createWord(submittedValsHolder, myForm.getDocumentName(),myForm.getDocumentTypeCateg(), myForm.getDocOwnerType(),request);
                }
                //last approved version only for Team Document,not Private !
                if(myForm.getDocOwnerType().equals("team")){
                    String lastApprovedNodeVersionUUID = DocumentManagerUtil.getNodeOfLastVersion(nodeuuid, request)
                            .getIdentifier();
                    NodeLastApprovedVersion lastAppVersion = new NodeLastApprovedVersion(nodeuuid,
                            lastApprovedNodeVersionUUID);
                    DbUtil.saveOrUpdateObject(lastAppVersion);
                }                                
            }           
            
            clearForm(myForm);
            request.getSession().setAttribute("resourcesTab", myForm.getDocOwnerType());

            myForm.setDocOwnerType(null);
        }
        DocumentManagerUtil.logoutJcrSessions(request); 
        return mapping.findForward("showResources");
    }
    
    /**
     * creates Pdf document and returns it's nodeUUID
     * @param pdfContent
     * @param pdfName
     * @param request 
     * @return
     */
    private String  createPdf(List<SubmittedValueHolder> pdfContent, String pdfName,Long documentType, String documentOwnerType,HttpServletRequest request){
        Document doc = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
             PdfWriter.getInstance(doc, baos);
             doc.open();
            Font pageTitleFont = FontFactory.getFont("Arial", 24, Font.BOLD);
             Font plainFont = new Font(Font.FontFamily.valueOf(BaseFont.TIMES_ROMAN), 10);
             Paragraph pageTitle = new Paragraph(pdfName, pageTitleFont);
             pageTitle.setAlignment(Element.ALIGN_CENTER);
             doc.add(pageTitle);
             doc.add(new Paragraph(" "));
             for (SubmittedValueHolder pdfContentElement : pdfContent) {
                 if(pdfContentElement.isNeedsNewParagraph()){
                     doc.add(new Paragraph(" "));
                 }
                 Paragraph pdfContentEl = new Paragraph(pdfContentElement.getSubmittedValue(), plainFont);               
                 doc.add(pdfContentEl);
             }
             doc.close();
             byte[] pdfbody= baos.toByteArray();
             String contentType="application/pdf";
             PdfFileHelper pdfHelper=new PdfFileHelper(pdfName, contentType,documentType, pdfbody);
             //create jcr node
             Session jcrWriteSession        = DocumentManagerUtil.getWriteSession(request); 
             Node userOrTeamHomeNode = null;
             TeamMember tm = getCurrentTeamMember(request);
             if (documentOwnerType.equals("team")) {
                 userOrTeamHomeNode = DocumentManagerUtil.getOrCreateTeamNode(jcrWriteSession, tm.getTeamId());
             } else if (documentOwnerType.equals("private")) {
                 userOrTeamHomeNode = DocumentManagerUtil.getOrCreateUserPrivateNode(jcrWriteSession, tm);
             }

             NodeWrapper nodeWrapper        = new NodeWrapper(pdfHelper, request, userOrTeamHomeNode, false, new ActionErrors());
             if ( nodeWrapper != null && !nodeWrapper.isErrorAppeared() ){
                    nodeWrapper.saveNode(jcrWriteSession);
             }
             return nodeWrapper.getUuid();           
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    private String createWord(List<SubmittedValueHolder> docContent, String docName, Long documentType, String documentOwnerType,HttpServletRequest request){
        Document doc = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
//            RtfWriter2.getInstance(doc, baos);
            doc.open();
            Font pageTitleFont = FontFactory.getFont("Arial", 24, Font.BOLD);
            Font plainFont = new Font(Font.FontFamily.valueOf(BaseFont.TIMES_ROMAN), 10);
            Paragraph pageTitle = new Paragraph(docName, pageTitleFont);
            pageTitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(pageTitle);
            doc.add(new Paragraph(" "));
            for (SubmittedValueHolder pdfContentElement : docContent) {
                if(pdfContentElement.isNeedsNewParagraph()){
                     doc.add(new Paragraph(" "));
                 }
                 Paragraph docContentEl = new Paragraph(pdfContentElement.getSubmittedValue(), plainFont);
                 doc.add(docContentEl);             
            }
            doc.close();
            byte[] docbody= baos.toByteArray();
            String contentType="application/msword";
            WordDocumentHelper wordDocHelper=new WordDocumentHelper(docName, contentType, documentType,  docbody);
            //create jcr node
             Session jcrWriteSession        = DocumentManagerUtil.getWriteSession(request);
             Node userOrTeamHomeNode = null;
             TeamMember tm = getCurrentTeamMember(request);
             if (documentOwnerType.equals("team")) {
                 userOrTeamHomeNode = DocumentManagerUtil.getOrCreateTeamNode(jcrWriteSession, tm.getTeamId());
             } else if (documentOwnerType.equals("private")) {
                 userOrTeamHomeNode = DocumentManagerUtil.getOrCreateUserPrivateNode(jcrWriteSession, tm);
             }

             NodeWrapper nodeWrapper        = new NodeWrapper(wordDocHelper, request, userOrTeamHomeNode, false, new ActionErrors());
             if ( nodeWrapper != null && !nodeWrapper.isErrorAppeared() ){
                    nodeWrapper.saveNode(jcrWriteSession);
             }
             return nodeWrapper.getUuid();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private TeamMember getCurrentTeamMember( HttpServletRequest request ) {
        HttpSession httpSession     = request.getSession();
        return (TeamMember)httpSession.getAttribute(Constants.CURRENT_MEMBER);
    }
    
    private void clearForm(CreateDocFromTemplateForm form){
        form.setSelectedTemplate(null);
        form.setTemplateId(null);
        form.setTemplates(null);
        form.setFields(null);
        form.setDocumentName(null);
        form.setDocType(null);      
        form.setDocumentTypeCateg( null );
    }
}
