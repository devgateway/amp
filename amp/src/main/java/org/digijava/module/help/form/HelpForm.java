package org.digijava.module.help.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.helper.HelpTopicsTreeItem;
import org.digijava.module.sdm.dbentity.Sdm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HelpForm extends ActionForm {
    private static final long serialVersionUID = 1L;
    private List<HelpTopic> helpTopics=null;
    private Long helpTopicId=null;
    private String topicKey=null;
    private int wizardStep;
    private String bodyEditKey;
    private List<HelpTopic> firstLevelTopics=new ArrayList<HelpTopic>();
    private Long parentId;
    private String titleTrnKey;
    private String keywordsTrnKey;
    private Boolean edit=null;
    private String keywords;
    private Collection topicTree;
    private Collection adminTopicTree;
    private Collection<HelpTopicsTreeItem> glossaryTree;
    private List<String> helpErrors;
    private Boolean blankPage;
    private String title;
    private String body;
    private Long childId;
    private Collection<LabelValueBean> searched; 
    private boolean flag;
    private String page ;
    private Boolean glossaryMode;
    FormFile fileUploaded;
    
    private Sdm sdm;
    

    public Boolean getBlankPage() {
        return blankPage;
    }

    public void setBlankPage(Boolean blankPage) {
        this.blankPage = blankPage;
    }

    public List<String> getHelpErrors() {
        return helpErrors;
    }

    public void setHelpErrors(List<String> helpErrors) {
        this.helpErrors = helpErrors;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Boolean getEdit() {
        return edit;
    }

    public void setEdit(Boolean edit) {
        this.edit = edit;
    }

    public String getKeywordsTrnKey() {
        return keywordsTrnKey;
    }

    public void setKeywordsTrnKey(String keywordsTrnKey) {
        this.keywordsTrnKey = keywordsTrnKey;
    }

    public String getTitleTrnKey() {
        return titleTrnKey;
    }

    public void setTitleTrnKey(String titleTrnKey) {
        this.titleTrnKey = titleTrnKey;
    }

    

    public String getTopicKey() {
        return topicKey;
    }

    public void setTopicKey(String topicKey) {
        this.topicKey = topicKey;
    }

    public List<HelpTopic> getHelpTopics() {
        return helpTopics;
    }

    public void setHelpTopics(List<HelpTopic> helpTopics) {
        this.helpTopics = helpTopics;
    }

    public int getWizardStep() {
        return wizardStep;
    }

    public void setWizardStep(int wizardStep) {
        this.wizardStep = wizardStep;
    }

    public String getBodyEditKey() {
        return bodyEditKey;
    }

    public void setBodyEditKey(String bodyEditKey) {
        this.bodyEditKey = bodyEditKey;
    }

    public List<HelpTopic> getFirstLevelTopics() {
        return firstLevelTopics;
    }

    public void setFirstLevelTopics(List<HelpTopic> firstLevelTopics) {
        this.firstLevelTopics = firstLevelTopics;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Collection getTopicTree() {
        return topicTree;
    }

    public void setTopicTree(Collection topicTree) {
        this.topicTree = topicTree;
    }

    public Long getHelpTopicId() {
        return helpTopicId;
    }

    public void setHelpTopicId(Long helpTopicId) {
        this.helpTopicId = helpTopicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public Collection<LabelValueBean> getSearched() {
        return searched;
    }

    public void setSearched(Collection<LabelValueBean> searched) {
        this.searched = searched;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public FormFile getFileUploaded() {
        return fileUploaded;
    }

    public void setFileUploaded(FormFile fileUploaded) {
        this.fileUploaded = fileUploaded;
    }

    public String getPage() {
        return page;
    }

    public void setGlossaryMode(Boolean glossaryMode) {
        this.glossaryMode = glossaryMode;
    }

    public Boolean getGlossaryMode() {
        return glossaryMode;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Collection getAdminTopicTree() {
        return adminTopicTree;
    }

    public void setAdminTopicTree(Collection adminTopicTree) {
        this.adminTopicTree = adminTopicTree;
    }

    public void setGlossaryTree(Collection<HelpTopicsTreeItem> glossaryTree) {
        this.glossaryTree = glossaryTree;
    }

    public Collection<HelpTopicsTreeItem> getGlossaryTree() {
        return glossaryTree;
    }

    public Sdm getSdm() {
        return sdm;
    }

    public void setSdm(Sdm sdm) {
        this.sdm = sdm;
    }


}
