package org.digijava.module.contentrepository.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.contentrepository.helper.template.PossibleValueHelper;
import org.digijava.module.contentrepository.helper.template.TemplateFieldHelper;

import java.util.List;

public class ManageTemplDocFieldForm extends ActionForm {
    private List<TemplateFieldHelper> templateFields;
    private String templateDocFieldTemporaryId;
    private String selectedFieldType;
    
    private String[] preDefinedValue;
    private List<PossibleValueHelper> possibleValuesForField;
    private Boolean hasAddModeValuesRight; //whether "add new values" button should be enabled/disabled for given field type
    
    public List<TemplateFieldHelper> getTemplateFields() {
        return templateFields;
    }
    public void setTemplateFields(List<TemplateFieldHelper> templateFields) {
        this.templateFields = templateFields;
    }
    public String getTemplateDocFieldTemporaryId() {
        return templateDocFieldTemporaryId;
    }
    public void setTemplateDocFieldTemporaryId(String templateDocFieldTemporaryId) {
        this.templateDocFieldTemporaryId = templateDocFieldTemporaryId;
    }
    public String getSelectedFieldType() {
        return selectedFieldType;
    }
    public void setSelectedFieldType(String selectedFieldType) {
        this.selectedFieldType = selectedFieldType;
    }
    public String[] getPreDefinedValue() {
        return preDefinedValue;
    }
    public void setPreDefinedValue(String[] preDefinedValue) {
        this.preDefinedValue = preDefinedValue;
    }
    public List<PossibleValueHelper> getPossibleValuesForField() {
        return possibleValuesForField;
    }
    public void setPossibleValuesForField(
            List<PossibleValueHelper> possibleValuesForField) {
        this.possibleValuesForField = possibleValuesForField;
    }
    public Boolean getHasAddModeValuesRight() {
        return hasAddModeValuesRight;
    }
    public void setHasAddModeValuesRight(Boolean hasAddModeValuesRight) {
        this.hasAddModeValuesRight = hasAddModeValuesRight;
    }
}
