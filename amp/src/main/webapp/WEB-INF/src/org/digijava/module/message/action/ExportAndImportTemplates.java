package org.digijava.module.message.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.helper.DateConversion;
import org.digijava.module.message.dbentity.TemplateAlert;
import org.digijava.module.message.form.AmpMessageForm;
import org.digijava.module.message.jaxb.AlertTemplateType;
import org.digijava.module.message.jaxb.Messaging;
import org.digijava.module.message.jaxb.TemplatesList;
import org.digijava.module.message.util.AmpMessageUtil;
import org.digijava.kernel.translator.TranslatorWorker;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;


public class ExportAndImportTemplates extends DispatchAction {
    
    public static final String ROOT_TAG = "Messaging";
    public static final String MESSAGES_TAG = "TemplatesList";
    
    public ActionForward gotoExportImportPage (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        return mapping.findForward("gotoPage");
    }
    
    public ActionForward exportTemplates (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {        
        List<TemplateAlert> templates=AmpMessageUtil.getAllMessages(TemplateAlert.class);
        String xml=generateXMLString(templates);    
        response.setContentType("text/xml");
        response.setHeader("content-disposition", "attachment; filename=exportTemplates.xml"); // file neme will generate by date
        OutputStreamWriter outputStream = null;
            PrintWriter out=null;
        try {
          outputStream = new OutputStreamWriter(response.
                                            getOutputStream(), "UTF-8");
              out = new PrintWriter(outputStream, true);
              // return xml 
                out.println(xml);
        } catch (Exception ex) {
          try {
            StringBuffer sp = new StringBuffer("error ocure while exporting Activityes");
            sp.append("\n");
            sp.append(ex.getMessage());
            sp.append("\n");

            for (int i = 0; i < ex.getStackTrace().length; i++) {
              sp.append(ex.getStackTrace()[i].toString());
              sp.append("\n");
            }

            sp.append("\n");
            out.println(sp.toString());
          } catch (Exception ex1) {
               log.error("ImportExportManagerAction.export.error.out", ex1);
          }
                log.error("ImportExportManagerAction.export.error", ex);
        }
            out.close();        
            outputStream.close();
        return null;
    }
    
    public ActionForward importTemplates (ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        AmpMessageForm msgForm=(AmpMessageForm)form;        
        
        FormFile myFile=msgForm.getFileUploaded();
        if(myFile!=null){
            byte[] fileData    = myFile.getFileData();
            InputStream inputStream= new ByteArrayInputStream(fileData);     
            
            JAXBContext jc = JAXBContext.newInstance("org.digijava.module.message.jaxb");
            Unmarshaller m = jc.createUnmarshaller();
            Messaging item;
            try {
                item = (Messaging) m.unmarshal(inputStream);
                TemplatesList tempList=item.getTemplatesList();
                if(tempList!=null){
                    List templates=tempList.getTemplate();
                    for (Object obj : templates) {
                        AlertTemplateType tempType=(AlertTemplateType)obj;
                        TemplateAlert template=new TemplateAlert();
                        template.setName(tempType.getName());
                        template.setDescription(tempType.getMsgDetails());
                        template.setCreationDate(DateConversion.getDate(tempType.getReceived()));
                        template.setRelatedTriggerName(tempType.getRelatedTrigger());
                        AmpMessageUtil.saveOrUpdateMessage(template);
                    }
                }               
            } catch (Exception ex) {            
                ex.printStackTrace(System.out);
                ActionMessages errors = new ActionMessages();
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.aim.importErrorFileContentTemplate", TranslatorWorker.translateText("Please choose correct file to import")));               
                saveErrors(request, errors);
                return mapping.findForward("gotoPage");
            }           
        }   
        return mapping.findForward("viewTemplates");
    }
    
    
    private String generateXMLString(List<TemplateAlert> templates){
        String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xml += "<" + ROOT_TAG + ">";
        xml+="<" + MESSAGES_TAG +">";
        if (templates != null && templates.size() > 0) {
            for (TemplateAlert temp : templates) {
                xml += "<" + "template name=\"" + org.digijava.module.aim.util.DbUtil.filter(temp.getName()) + "\" ";                
                String desc=org.digijava.module.aim.util.DbUtil.filter(temp.getDescription());
                xml += " msgDetails=\"" +desc + "\"";
                xml += " received=\"" + DateConversion.convertDateToString(temp.getCreationDate()) + "\"";
                xml += " relatedTrigger=\"" +temp.getRelatedTriggerName() + "\"";                               
                xml += ">";                
                xml += "</template>";
            }
        }
        xml+="</" + MESSAGES_TAG +">";
        xml += "</" + ROOT_TAG + ">";
        return xml;
    }
    
    private void saveXMLToFile(String xml,String folder,String fileName){
        File file=new File(folder+"/"+fileName+".xml");
        FileWriter out;
        try {
            out = new FileWriter(file);
            out.write(xml);
            out.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }       
    }

}
