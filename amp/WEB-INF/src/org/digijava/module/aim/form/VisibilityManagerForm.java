package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.dgfoundation.amp.visibility.AmpTreeVisibility;
import org.digijava.module.aim.dbentity.AmpTemplatesVisibility;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class VisibilityManagerForm extends ActionForm implements Serializable{

    /**
     * @author dan
     */
        private   HashMap<String,String> errors = new HashMap<String, String>();
    private   HashMap<String,String> messages = new HashMap<String, String>();
    
    
    private static final long serialVersionUID = -1859318593553304934L;
    private Collection templates;
    private Collection templateModules;
    private Collection templateModulesNotActive;
    private Collection modules;
    private Collection features;
    private Collection featuresModule;
    private Collection fields;
    private String mode;
    private Long templateId;
    private Long moduleId;
    private Long featureId;
    private Long fieldId;
    private String templateName;
    private AmpTreeVisibility ampTreeVisibility;
    private Long levelCategory;
    
    private String existSubmodules;
    
    private Collection allFields;
    private Collection allFeatures;
    private Collection allModules;
    private FormFile uploadFile = null;

    private Long fmTemplate;
    private Collection<AmpTemplatesVisibility> fmTemplateList;
    
    public FormFile getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(FormFile uploadFile) {
        this.uploadFile = uploadFile;
    }
    
    public String getTemplateName() {
        return templateName;
    }
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    public Long getTemplateId() {
        return templateId;
    }
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public Collection getTemplates() {
        return templates;
    }
    public void setTemplates(Collection templates) {
        this.templates = templates;
    }
    public Collection getFeatures() {
        return features;
    }
    public void setFeatures(Collection features) {
        this.features = features;
    }
    public Collection getFields() {
        return fields;
    }
    public void setFields(Collection fields) {
        this.fields = fields;
    }
    public Collection getModules() {
        return modules;
    }
    public void setModules(Collection modules) {
        this.modules = modules;
    }
    public Collection getTemplateModules() {
        return templateModules;
    }
    public void setTemplateModules(Collection templateModules) {
        this.templateModules = templateModules;
    }
    public Collection getTemplateModulesNotActive() {
        return templateModulesNotActive;
    }
    public void setTemplateModulesNotActive(Collection templateModulesNotActive) {
        this.templateModulesNotActive = templateModulesNotActive;
    }
    public Collection getFeaturesModule() {
        return featuresModule;
    }
    public void setFeaturesModule(Collection featuresModule) {
        this.featuresModule = featuresModule;
    }
    public Long getFeatureId() {
        return featureId;
    }
    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }
    public Long getFieldId() {
        return fieldId;
    }
    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }
    public Long getModuleId() {
        return moduleId;
    }
    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }
    public AmpTreeVisibility getAmpTreeVisibility() {
        return ampTreeVisibility;
    }
    public void setAmpTreeVisibility(AmpTreeVisibility ampTreeVisibility) {
        this.ampTreeVisibility = ampTreeVisibility;
    }
    public Collection getAllFeatures() {
        return allFeatures;
    }
    public void setAllFeatures(Collection allFeatures) {
        this.allFeatures = allFeatures;
    }
    public Collection getAllFields() {
        return allFields;
    }
    public void setAllFields(Collection allFields) {
        this.allFields = allFields;
    }
    public Collection getAllModules() {
        return allModules;
    }
    public void setAllModules(Collection allModules) {
        this.allModules = allModules;
    }
    public String getExistSubmodules() {
        return existSubmodules;
    }
    public void setExistSubmodules(String existSubmodules) {
        this.existSubmodules = existSubmodules;
    }
    public Long getLevelCategory() {
        return levelCategory;
    }
    public void setLevelCategory(Long levelCategory) {
        this.levelCategory = levelCategory;
    }
    public HashMap<String, String> getErrors() {
        return errors;
    }
    
    public HashMap<String, String> getMessages() {
        return messages;
    }
    public void addMessage(String key, String value) {
        this.messages.put(key, value) ;
    }
    
    public void addError(String key, String value) {
        this.errors.put(key, value) ;
    }
    public void clearMessages(){
        this.errors.clear();
        this.messages.clear();
    }

    public Long getFmTemplate() {
        return fmTemplate;
    }

    public void setFmTemplate(Long fmTemplate) {
        this.fmTemplate = fmTemplate;
    }

    public Collection<AmpTemplatesVisibility> getFmTemplateList() {
        return fmTemplateList;
    }

    public void setFmTemplateList(Collection<AmpTemplatesVisibility> fmTemplateList) {
        this.fmTemplateList = fmTemplateList;
    }
}
