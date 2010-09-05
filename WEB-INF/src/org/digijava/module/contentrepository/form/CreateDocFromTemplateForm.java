package org.digijava.module.contentrepository.form;

import java.util.List;

import org.apache.struts.action.ActionForm;
import org.digijava.module.contentrepository.dbentity.template.TemplateDoc;
import org.digijava.module.contentrepository.dbentity.template.TemplateField;

public class CreateDocFromTemplateForm extends ActionForm {
	
	private static final long serialVersionUID = 1L;
	private Long templateId;
	private TemplateDoc selectedTemplate;
	private List<TemplateDoc> templates;
	private List<TemplateField> fields;
	
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
	
}
