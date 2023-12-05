package org.digijava.module.esrigis.form;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import javax.servlet.http.HttpServletRequest;

public class StructureTypeForm extends ActionForm{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Long ampStructureFormId;
    private String name;
    private String graphicType;
    private FormFile iconFile;
    private String action;
    private boolean reset;

    public void setAmpStructureFormId(Long ampStructureFormId) {
        this.ampStructureFormId = ampStructureFormId;
    }
    public Long getAmpStructureFormId() {
        return ampStructureFormId;
    }
    public void setGraphicType(String graphicType) {
        this.graphicType = graphicType;
    }
    public String getGraphicType() {
        return graphicType;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getAction() {
        return action;
    }
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if(isReset()){
            ampStructureFormId = null;
            name = null;
            graphicType = null;
            action = null;
            reset=false;
        }
    }
    public void setReset(boolean reset) {
        this.reset = reset;
    }
    public boolean isReset() {
        return reset;
    }
    public void setIconFile(FormFile iconFile) {
        this.iconFile = iconFile;
    }
    public FormFile getIconFile() {
        return iconFile;
    }
    

}
