package org.digijava.module.help.helper;

import java.util.List;

import org.digijava.module.editor.dbentity.Editor;
import org.digijava.module.help.dbentity.HelpTopic;
import org.digijava.module.help.util.HelpUtil;

/**
 * Helper class for Help topic.
 * Initially used in help lucene indexing.
 * @author Irakli Kobiashvili
 * @see HelpUtil#getHelpItems(String, String, org.digijava.module.translation.lucene.LangSupport[], Boolean)
 *
 */
public class HelpTopicHelper {
    private Long id;
    private String title;
    private String body;
    private Long parentId;
    private HelpTopicHelper parent;
    private List<HelpTopicHelper> children;
    private String siteId;
    private String moduleInstance;
    private String langIso;
    private String bodyKey;
    private String titleKey;
    private Float sortIndex; 
    
    /**
     * Default constructor.
     */
    public HelpTopicHelper(){
        
    }
    
    /**
     * Not very useful one but still can reduce coding by seting some of the fields.
     * @param topic
     */
    public HelpTopicHelper(HelpTopic topic){
        this.id = topic.getHelpTopicId();
        this.bodyKey = topic.getBodyEditKey();
        this.titleKey = topic.getTitleTrnKey();
        this.moduleInstance = topic.getModuleInstance();
        this.siteId = topic.getSiteId();
    }
    
    public HelpTopicHelper(HelpTopic topic, String title){
        this(topic);
        this.title = title;
    }

    public HelpTopicHelper(HelpTopic topic, String title, String lang){
        this(topic, title);
        
        List<Editor> editorList = HelpUtil.getEditor(topic.getBodyEditKey(), lang);
        if (editorList != null && !editorList.isEmpty()){
            this.body = editorList.get(0).getBody();
        }
        this.langIso = lang;
        
    }
    
    public HelpTopicHelper(
            Long topicId,
            String title, 
            String body, 
            String siteId, 
            String moduleInstance, 
            String lang, 
            String titleTrnKey, 
            String bodyEditorKey
            ){
        this.id = topicId;
        this.title = title;
        this.body = body;
        this.siteId = siteId;
        this.moduleInstance = moduleInstance;
        this.langIso = lang;
        this.titleKey = titleTrnKey;
        this.bodyKey = bodyEditorKey;
    }
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public HelpTopicHelper getParent() {
        return parent;
    }
    public void setParent(HelpTopicHelper parent) {
        this.parent = parent;
    }
    public List<HelpTopicHelper> getChildren() {
        return children;
    }
    public void setChildren(List<HelpTopicHelper> children) {
        this.children = children;
    }
    public String getSiteId() {
        return siteId;
    }
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    public String getModuleInstance() {
        return moduleInstance;
    }
    public void setModuleInstance(String moduleInstance) {
        this.moduleInstance = moduleInstance;
    }
    public String getLangIso() {
        return langIso;
    }
    public void setLangIso(String langIso) {
        this.langIso = langIso;
    }
    public String getBodyKey() {
        return bodyKey;
    }
    public void setBodyKey(String bodyKey) {
        this.bodyKey = bodyKey;
    }
    public String getTitleKey() {
        return titleKey;
    }
    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    public void setSortIndex(Float sortIndex) {
        this.sortIndex = sortIndex;
    }

    public Float getSortIndex() {
        return sortIndex;
    }

}
