package org.digijava.module.aim.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpQuartzJobClass;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

public class QuartzJobClassManagerForm extends ActionForm{
    private Long id;
    private String name;
    private String classFullname;
    private String action;
    /*
     * 1 for invalid class
     */
    private String errorCode;
    private Collection<AmpQuartzJobClass> clsCol;

    public QuartzJobClassManagerForm() {
    }

    public void reset(ActionMapping mapping, HttpServletRequest request){
        id=null;
        name=null;
        classFullname=null;
        clsCol=null;
        action=null;
        errorCode=null;
    }

    public String getClassFullname() {
        return classFullname;
    }

    public Collection<AmpQuartzJobClass> getClsCol() {
        return clsCol;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }

    public void setClassFullname(String classFullname) {
        this.classFullname = classFullname;
    }

    public void setClsCol(Collection<AmpQuartzJobClass> clsCol) {
        this.clsCol = clsCol;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
}
