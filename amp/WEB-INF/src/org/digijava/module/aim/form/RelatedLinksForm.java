/***
 * RelatedLinksForm.java
 * @author Priyajith
 */

package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.module.aim.helper.Documents;

public class RelatedLinksForm 
extends ActionForm {
    
    private List<Documents> allDocuments;
    private Collection relatedLinks;
    private Collection pages;
    private String addDocuments;
    private String removeDocuments;
    private Long selDocuments[];
    private Integer currentPage;
    private boolean reset;
    private String activityName;
    private String title;
    private String docDescription;
    private Long activityId;
    private Long docId;
    private boolean file;
    private FormFile docFile;
    private String url;
    private boolean valuesSet;
    private int pageId;
    private String fileName;
    private boolean validLogin;
    private String[] deleteLinks;
    private String uuid;
    
    
    private Documents document;

    /**
     * @return Returns the addDocuments.
     */
    public String getAddDocuments() {
        return addDocuments;
    }
    /**
     * @param addDocuments The addDocuments to set.
     */
    public void setAddDocuments(String addDocuments) {
        this.addDocuments = addDocuments;
    }
    /**
     * @return Returns the currentPage.
     */
    public Integer getCurrentPage() {
        return currentPage;
    }
    /**
     * @param currentPage The currentPage to set.
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
    /**
     * @return Returns the pages.
     */
    public Collection getPages() {
        return pages;
    }
    /**
     * @param pages The pages to set.
     */
    public void setPages(Collection pages) {
        this.pages = pages;
    }

    public Collection getRelatedLinks() {
        return relatedLinks;
    }

    public void setRelatedLinks(Collection relatedLinks) {
        this.relatedLinks = relatedLinks;
    }
            
    /**
     * @return Returns the removeDocuments.
     */
    public String getRemoveDocuments() {
        return removeDocuments;
    }
    /**
     * @param removeDocuments The removeDocuments to set.
     */
    public void setRemoveDocuments(String removeDocuments) {
        this.removeDocuments = removeDocuments;
    }
    /**
     * @return Returns the selDocuments.
     */
    public Long[] getSelDocuments() {
        return selDocuments;
    }
    /**
     * @param selDocuments The selDocuments to set.
     */
    public void setSelDocuments(Long[] selDocuments) {
        this.selDocuments = selDocuments;
    }
    /**
     * @return Returns the allDocuments.
     */
    public List<Documents> getAllDocuments() {
        return allDocuments;
    }
    /**
     * @param allDocuments The allDocuments to set.
     */
    public void setAllDocuments(List<Documents> allDocuments) {
        this.allDocuments = allDocuments;
    }
    /**
     * @return Returns the reset.
     */
    public boolean isReset() {
        return reset;
    }
    /**
     * @param reset The reset to set.
     */
    public void setReset(boolean reset) {
        this.reset = reset;
    }
    
    public void reset(ActionMapping mapping,HttpServletRequest request) {
        if (reset) {
            allDocuments = null;
            pages = null;
            addDocuments=  null;
            removeDocuments = null;
            selDocuments = null;
            currentPage = null; 
            document = null;
            activityName = null;
            title = null;
            docDescription = null;
            activityId = null;
            docId = null;
            file = false;
            docFile = null;
            valuesSet = false;
            url = null;
        }
        
        reset = false;
    }
    /**
     * @return Returns the document.
     */
    public Documents getDocument() {
        return document;
    }
    /**
     * @param document The document to set.
     */
    public void setDocument(Documents document) {
        this.document = document;
    }
    /**
     * @return Returns the activityId.
     */
    public Long getActivityId() {
        return activityId;
    }
    /**
     * @param activityId The activityId to set.
     */
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    /**
     * @return Returns the activityName.
     */
    public String getActivityName() {
        return activityName;
    }
    /**
     * @param activityName The activityName to set.
     */
    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }
    /**
     * @return Returns the docDescription.
     */
    public String getDocDescription() {
        return docDescription;
    }
    /**
     * @param docDescription The docDescription to set.
     */
    public void setDocDescription(String docDescription) {
        this.docDescription = docDescription;
    }
    /**
     * @return Returns the docFile.
     */
    public FormFile getDocFile() {
        return docFile;
    }
    /**
     * @param docFile The docFile to set.
     */
    public void setDocFile(FormFile docFile) {
        this.docFile = docFile;
    }
    /**
     * @return Returns the docId.
     */
    public Long getDocId() {
        return docId;
    }
    /**
     * @param docId The docId to set.
     */
    public void setDocId(Long docId) {
        this.docId = docId;
    }
    /**
     * @return Returns the isFile.
     */
    public boolean isFile() {
        return file;
    }
    /**
     * @param isFile The isFile to set.
     */
    public void setFile(boolean isFile) {
        this.file = isFile;
    }
    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return Returns the valuesSet.
     */
    public boolean isValuesSet() {
        return valuesSet;
    }
    /**
     * @param valuesSet The valuesSet to set.
     */
    public void setValuesSet(boolean valuesSet) {
        this.valuesSet = valuesSet;
    }
    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return url;
    }
    /**
     * @param url The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }
    /**
     * @return Returns the pageId
     */
    public int getPageId() {
        return pageId;
    }
    /**
     * @param pageId The pageId to set.
     */
    public void setPageId(int pageId) {
        this.pageId = pageId;
    }   
    /**
     * @return Returns the fileName
     */
    public String getFileName() {
        return fileName;
    }
    /**
     * @param fileName The fileName to set.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }       

    public boolean getValidLogin() 
    {
        return validLogin;
    }

    public void setValidLogin(boolean bool) 
    {
        this.validLogin = bool ;
    }

    public String[] getDeleteLinks() {
        return deleteLinks;
    }
    public void setDeleteLinks(String[] deleteLinks) {
        this.deleteLinks = deleteLinks;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getUuid() {
        return uuid;
    }
}
