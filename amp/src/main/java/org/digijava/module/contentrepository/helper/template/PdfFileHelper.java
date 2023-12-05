package org.digijava.module.contentrepository.helper.template;

/**
 * used to build PDF file , that is stored in JCR
 * @author Dare
 */
public class PdfFileHelper extends WordOrPdfFileHelper{
    
    public String getFileType(){ //what kind of document is this
        return TemplateConstants.DOC_TYPE_PDF;
    }   
    
    public PdfFileHelper(){
        
    }
    
    public PdfFileHelper(String docTitle,String contentType,Long documentType, byte[] pdfbody){
        super(docTitle, contentType,documentType, pdfbody);
    }
    
}
