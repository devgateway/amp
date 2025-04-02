package org.digijava.module.contentrepository.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.contentrepository.dbentity.template.TemplateDoc;
import org.digijava.module.contentrepository.helper.template.TemplateFieldHelper;


public class TemplateDocManagerForm extends ActionForm {

    private static final long serialVersionUID = 1L;
    private List<TemplateDoc> templates;
    private String templateName;
    private Long templateId;
    private List<TemplateFieldHelper> pendingFields;    
    //for adding template fields
    private String[] fieldType; //chosen field types from dropdown submit here
    private String templateDocFieldTemporaryId;
    private String selectedFieldType;   
    private List<LabelValueBean> availableFields; //contains all template fields(checkbox,multibox,select,etc)  
    private String[] selectedFieldsIds; //which fields are selected to be removed from template 
    
    //template document field's values
    private String[] preDefinedValue;
    
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
    public List<LabelValueBean> getAvailableFields() {
        return availableFields;
    }
    public void setAvailableFields(List<LabelValueBean> availableFields) {
        this.availableFields = availableFields;
    }
    public List<TemplateDoc> getTemplates() {
        return templates;
    }
    public void setTemplates(List<TemplateDoc> templates) {
        this.templates = templates;
    }
    public String getTemplateDocFieldTemporaryId() {
        return templateDocFieldTemporaryId;
    }
    public void setTemplateDocFieldTemporaryId(String templateDocFieldTemporaryId) {
        this.templateDocFieldTemporaryId = templateDocFieldTemporaryId;
    }
    public List<TemplateFieldHelper> getPendingFields() {
        return pendingFields;
    }
    public void setPendingFields(List<TemplateFieldHelper> pendingFields) {
        this.pendingFields = pendingFields;
    }
    public String[] getFieldType() {
        return fieldType;
    }
    public void setFieldType(String[] fieldType) {
        this.fieldType = fieldType;
    }
    public String getSelectedFieldType() {
        return selectedFieldType;
    }
    public void setSelectedFieldType(String selectedFieldType) {
        this.selectedFieldType = selectedFieldType;
    }

    public String[] getSelectedFieldsIds() {
        return selectedFieldsIds;
    }
    public void setSelectedFieldsIds(String[] selectedFieldsIds) {
        this.selectedFieldsIds = selectedFieldsIds;
    }
    public String[] getPreDefinedValue() {
        return preDefinedValue;
    }
    public void setPreDefinedValue(String[] preDefinedValue) {
        this.preDefinedValue = preDefinedValue;
    }
    
}
