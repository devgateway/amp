package org.digijava.module.editor.action;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.editor.form.EditorForm;
import org.digijava.module.editor.util.Constants;
import org.digijava.module.editor.util.DbUtil;
import org.digijava.module.sdm.dbentity.Sdm;
import org.digijava.module.sdm.dbentity.SdmItem;

public class UploadImage extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response) throws Exception {
        EditorForm myForm = (EditorForm) form;
        FormFile attachment =myForm.getAttachment();
        
        String retVal="0";
        String newName="";
        String fileUrl="";
        String errorMessage="";
        
        if(attachment.getFileSize()<=Constants.IMG_MAX_SIZE){
            Sdm helpAttachmentsHolder=null;
            String docId = null;
            
            String editorKey= myForm.getKey();
            String chosenLang = myForm .getLang();
            //get editor with this key and language if exists
            Editor editor= DbUtil.getEditor(editorKey, chosenLang);
            
            if(editor!=null){
                // try to get document id 
                String editorBody = editor.getBody();
                String imgPart="<img\\s.*?src\\=\"/sdm/showImage\\.do\\?.*?activeParagraphOrder\\=.*\"\\s?/>" ;//<img\s.*?src\=\".*showImage\.do\?.*?activeParagraphOrder\=.*\"\s?/>;
                Pattern pattern = Pattern.compile(imgPart,Pattern.MULTILINE);
                Matcher matcher = pattern.matcher(editorBody);
                
                if (matcher.find()){                
                    String imgTag = matcher.group(0);
                    if(imgTag.contains("documentId=")){
                        docId = imgTag.substring(imgTag.indexOf("documentId=")+11);
                        if(docId.contains("&")){
                            docId = docId .substring(0,docId.indexOf("&"));
                        }else{
                            docId = docId .substring(0,docId.indexOf("\""));
                        }
                        helpAttachmentsHolder = org.digijava.module.sdm.util.DbUtil.getDocument(new Long (docId));
                    }else{ //we need to check if document exists in session
                        helpAttachmentsHolder = getAttachmentsHolder(request);
                    }
                }else{
                    helpAttachmentsHolder = getAttachmentsHolder(request);
                }
                
            }else{ // if editor doesn't exist in db, then document also doesn't exist in db. but may exist in session
                helpAttachmentsHolder = getAttachmentsHolder(request);
            }
            
            SdmItem sdmItem = new SdmItem();
            sdmItem.setContentType(attachment.getContentType());
            sdmItem.setRealType(SdmItem.TYPE_IMG);
            sdmItem.setContent(attachment.getFileData());
            sdmItem.setContentText(attachment.getFileName());
            sdmItem.setContentTitle(attachment.getFileName());
            
            if(helpAttachmentsHolder.getItems()!=null){
                //sdmItem.setParagraphOrder(new Long(helpAttachmentsHolder.getItems().size()));
                sdmItem.setParagraphOrder(org.digijava.module.sdm.util.DbUtil.getNewParagraphOrder(helpAttachmentsHolder));
                helpAttachmentsHolder.getItems().add(sdmItem);
            }else{
                HashSet items = new HashSet();
                sdmItem.setParagraphOrder(new Long(0));
                items.add(sdmItem);
                helpAttachmentsHolder.setItems(items);
            }        
            
            helpAttachmentsHolder = org.digijava.module.sdm.util.DbUtil.saveOrUpdateDocument(helpAttachmentsHolder);
            if(docId==null){
                docId = helpAttachmentsHolder.getId().toString();
            }           
            request.getSession().setAttribute("document", helpAttachmentsHolder);
            
            fileUrl="/sdm/showImage.do?activeParagraphOrder="+sdmItem.getParagraphOrder()+"&documentId="+docId;
            
        }else{
            retVal="1";
            errorMessage=TranslatorWorker.translateText("File size exceeds limit. Max size should be 2MB");
        }
        
        //generate response
        response.setContentType("text/html; charset=UTF-8");
        response.setHeader("Cache-Control","no-cache");
        PrintWriter out = response.getWriter();             
        
        out.println("<script type=\"text/javascript\">");
        out.println("window.parent.OnUploadCompleted("+retVal+",'"+fileUrl+"','"+newName+"','"+errorMessage+"');");
        out.println("</script>");
        out.flush();
        out.close();
        return null;
    }

    private Sdm getAttachmentsHolder(HttpServletRequest request) {
        Sdm helpAttachmentsHolder;
        if(request.getSession().getAttribute("document")!=null){
            helpAttachmentsHolder=(Sdm)request.getSession().getAttribute("document");
        }else{
            helpAttachmentsHolder=new Sdm();
        }
        return helpAttachmentsHolder;
    }
}
