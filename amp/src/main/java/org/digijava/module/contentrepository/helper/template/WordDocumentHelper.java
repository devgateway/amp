package org.digijava.module.contentrepository.helper.template;
/**
 * used to build Word Docuemnt, which is stored in JCR
 * @author Dare
 *
 */
public class WordDocumentHelper extends WordOrPdfFileHelper{

    @Override
    public String getFileType() {
        return TemplateConstants.DOC_TYPE_WORD;
    }
    
    public WordDocumentHelper(){
        
    }
    
    public WordDocumentHelper(String docTitle,String contentType,Long documentType, byte[] docbody){
        super(docTitle, contentType, documentType, docbody);
    }

}
