package org.digijava.module.aim.form;

import java.util.ArrayList;

import org.apache.struts.action.ActionForm;
import org.digijava.module.budget.dbentity.AmpDepartments;

public class DepartmentsManagerForm extends ActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<AmpDepartments> departments;
    private String departmentname;
    private String departmentcode;
    private Long id;
    public ArrayList<AmpDepartments> getDepartments() {
        return departments;
    }
    public void setDepartments(ArrayList<AmpDepartments> departments) {
        this.departments = departments;
    }
    public String getDepartmentname() {
        return departmentname;
    }
    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }
    public String getDepartmentcode() {
        return departmentcode;
    }
    public void setDepartmentcode(String departmentcode) {
        this.departmentcode = departmentcode;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
