package org.digijava.module.contentrepository.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.contentrepository.dbentity.template.TemplateDoc;
import org.digijava.module.contentrepository.dbentity.template.TemplateField;

import java.util.List;

public class CreateDocFromTemplateForm extends ActionForm {
    
    private static final long serialVersionUID = 1L;
    private Long templateId;
    private TemplateDoc selectedTemplate;
    private List<TemplateDoc> templates;
    private List<TemplateField> fields;
    
    private String documentName;
    private String docType;
    
    private Long documentTypeCateg;
    
    private String docOwnerType;
    
    public Long getTemplateId() {
        return templateId;
    }
    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }
    public TemplateDoc getSelectedTemplate() {
        return selectedTemplate;
    }
    public void setSelectedTemplate(TemplateDoc selectedTemplate) {
        this.selectedTemplate = selectedTemplate;
    }
    public List<TemplateDoc> getTemplates() {
        return templates;
    }
    public void setTemplates(List<TemplateDoc> templates) {
        this.templates = templates;
    }
    public List<TemplateField> getFields() {
        return fields;
    }
    public void setFields(List<TemplateField> fields) {
        this.fields = fields;
    }
    public String getDocumentName() {
        return documentName;
    }
    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }
    public String getDocType() {
        return docType;
    }
    public void setDocType(String docType) {
        this.docType = docType;
    }
    public String getDocOwnerType() {
        return docOwnerType;
    }
    public void setDocOwnerType(String docOwnerType) {
        this.docOwnerType = docOwnerType;
    }
    public Long getDocumentTypeCateg() {
        return documentTypeCateg;
    }
    public void setDocumentTypeCateg(Long documentTypeCateg) {
        this.documentTypeCateg = documentTypeCateg;
    }
    
}
