package org.digijava.module.contentrepository.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.contentrepository.jcrentity.RootLabel;

public class LabelManagerForm extends ActionForm {
    
    private RootLabel rootLabel;
    
    private String editLabelName;
    private String editParentUUID;
    private String editUUID;
    private String editLabelColor;
    private String editLabelBackgroundColor;
    private String editLabelType;
    
    private String deleteLabelUUID;
    
    /**
     * @return the rootLabel
     */
    public RootLabel getRootLabel() {
        return rootLabel;
    }
    /**
     * @param rootLabel the rootLabel to set
     */
    public void setRootLabel(RootLabel rootLabel) {
        this.rootLabel = rootLabel;
    }
    /**
     * @return the editLabelName
     */
    public String getEditLabelName() {
        return editLabelName;
    }
    /**
     * @param editLabelName the editLabelName to set
     */
    public void setEditLabelName(String editLabelName) {
        this.editLabelName = editLabelName;
    }
    /**
     * @return the editParentUUID
     */
    public String getEditParentUUID() {
        return editParentUUID;
    }
    /**
     * @param editParentUUID the editParentUUID to set
     */
    public void setEditParentUUID(String editParentUUID) {
        this.editParentUUID = editParentUUID;
    }
    /**
     * @return the editLabelColor
     */
    public String getEditLabelColor() {
        return editLabelColor;
    }
    /**
     * @param editLabelColor the editLabelColor to set
     */
    public void setEditLabelColor(String editLabelColor) {
        this.editLabelColor = editLabelColor;
    }
    /**
     * @return the editLabelBackgroundColor
     */
    public String getEditLabelBackgroundColor() {
        return editLabelBackgroundColor;
    }
    /**
     * @param editLabelBackgroundColor the editLabelBackgroundColor to set
     */
    public void setEditLabelBackgroundColor(String editLabelBackgroundColor) {
        this.editLabelBackgroundColor = editLabelBackgroundColor;
    }
    /**
     * @return the editUUID
     */
    public String getEditUUID() {
        return editUUID;
    }
    /**
     * @param editUUID the editUUID to set
     */
    public void setEditUUID(String editUUID) {
        this.editUUID = editUUID;
    }
    /**
     * @return the editLabelType
     */
    public String getEditLabelType() {
        return editLabelType;
    }
    /**
     * @param editLabelType the editLabelType to set
     */
    public void setEditLabelType(String editLabelType) {
        this.editLabelType = editLabelType;
    }
    /**
     * @return the deleteLabelUUID
     */
    public String getDeleteLabelUUID() {
        return deleteLabelUUID;
    }
    /**
     * @param deleteLabelUUID the deleteLabelUUID to set
     */
    public void setDeleteLabelUUID(String deleteLabelUUID) {
        this.deleteLabelUUID = deleteLabelUUID;
    }

    
    
}
