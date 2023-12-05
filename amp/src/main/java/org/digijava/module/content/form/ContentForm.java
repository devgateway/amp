package org.digijava.module.content.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.digijava.module.content.dbentity.AmpContentItemThumbnail;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.TreeSet;

public class ContentForm extends ActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Long ampContentFormId;
    private String editKey;
    private String contentLayout;
    private String pageCode;
    private String title;
    private String description;
    private String htmlblock_1;
    private String htmlblock_2;
    private Boolean isHomepage;
    private String action;
    private Set<AmpContentItemThumbnail> contentThumbnails;
    private TreeSet<AmpContentItemThumbnail> sortedContentThumbnails;
    private int placeholder;

    private FormFile tempContentThumbnail;
    private FormFile tempContentFile;
    private String tempContentThumbnailLabel;
    

    private boolean reset;

    private Set<AmpContentItemThumbnail> contentThumbnailsRemoved;

    public ContentForm(){
        reset = true;
    }

    public void setAmpContentFormId(long ampContentFormId) {
        this.ampContentFormId = ampContentFormId;
    }
    public Long getAmpContentFormId() {
        return ampContentFormId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getAction() {
        return action;
    }
    public void setHtmlblock_1(String htmlblock_1) {
        this.htmlblock_1 = htmlblock_1;
    }
    public String getHtmlblock_1() {
        return htmlblock_1;
    }
    public void setHtmlblock_2(String htmlblock_2) {
        this.htmlblock_2 = htmlblock_2;
    }
    public String getHtmlblock_2() {
        return htmlblock_2;
    }
    public void setIsHomepage(Boolean isHomepage) {
        this.isHomepage = isHomepage;
    }
    public Boolean getIsHomepage() {
        return isHomepage;
    }
    public void setPageCode(String pageCode) {
        this.pageCode = pageCode;
    }
    public String getPageCode() {
        return pageCode;
    }
    public void setContentLayout(String contentLayout) {
        this.contentLayout = contentLayout;
    }
    public String getContentLayout() {
        return contentLayout;
    }
    public void setEditKey(String editKey) {
        this.editKey = editKey;
    }
    public String getEditKey() {
        return editKey;
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if(isReset()){
            ampContentFormId = null;
            editKey = null;
            contentLayout = null;
            pageCode = null;
            title = null;
            description = null;
            htmlblock_1 = null;
            htmlblock_2 = null;
            isHomepage = null;
            action = null;
            contentThumbnails = null;
            reset=false;
            sortedContentThumbnails = null;
            contentThumbnailsRemoved = null;
            tempContentThumbnailLabel = null;
        }
    }
    public void setReset(boolean reset) {
        this.reset = reset;
    }
    public boolean isReset() {
        return reset;
    }
    public void setContentThumbnails(Set<AmpContentItemThumbnail> contentThumbnails) {
        this.contentThumbnails = contentThumbnails;
    }
    public Set<AmpContentItemThumbnail> getContentThumbnails() {
        return contentThumbnails;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public void setTempContentThumbnail(FormFile tempContentThumbnail) {
        this.tempContentThumbnail = tempContentThumbnail;
    }

    public FormFile getTempContentThumbnail() {
        return tempContentThumbnail;
    }

    public void setTempContentFile(FormFile tempContentFile) {
        this.tempContentFile = tempContentFile;
    }

    public FormFile getTempContentFile() {
        return tempContentFile;
    }

    public void setTempContentThumbnailLabel(String tempContentThumbnailLabel) {
        this.tempContentThumbnailLabel = tempContentThumbnailLabel;
    }

    public String getTempContentThumbnailLabel() {
        return tempContentThumbnailLabel;
    }

    public TreeSet<AmpContentItemThumbnail> getSortedContentThumbnails() {
        if(this.contentThumbnails != null){
            return new TreeSet<AmpContentItemThumbnail>(this.contentThumbnails);
        }
        return sortedContentThumbnails;
    }

    public Set<AmpContentItemThumbnail> getContentThumbnailsRemoved() {
        if(contentThumbnailsRemoved == null) 
            contentThumbnailsRemoved = new TreeSet<AmpContentItemThumbnail>();
        return contentThumbnailsRemoved;
    }

}
