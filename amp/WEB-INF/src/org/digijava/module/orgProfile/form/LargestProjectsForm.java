/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.digijava.module.orgProfile.form;

import java.util.List;
import org.apache.struts.action.ActionForm;
import org.digijava.module.orgProfile.helper.Project;

/**
 *
 * @author medea
 */
public class LargestProjectsForm extends ActionForm{
     private static final long serialVersionUID = 1L;
     private List<Project>projects;

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

}
