package org.digijava.module.budgetexport.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.digijava.module.budgetexport.dbentity.AmpBudgetExportProject;
import org.digijava.module.budgetexport.form.BEProjectForm;
import org.digijava.module.budgetexport.util.DbUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
  * User: flyer
 * Date: 2/2/12
 * Time: 12:56 PM
 */
public class AddEditDeleteProject extends DispatchAction {
    private static Logger logger	= Logger.getLogger(AddEditDeleteProject.class);

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        return add(mapping, form, request, response);
    }

    public ActionForward add(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEProjectForm beProjectForm = (BEProjectForm) form;
        beProjectForm.setId(null);
        return mapping.findForward("forward");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEProjectForm beProjectForm = (BEProjectForm) form;

        AmpBudgetExportProject prj = null;

        if (beProjectForm.getId() == null || beProjectForm.getId().longValue() <= 0l) {
            prj = new AmpBudgetExportProject();
            prj.setCreationDate(new Date());
        } else {
            prj = DbUtil.getProjectById(beProjectForm.getId());
        }
        prj.setName(beProjectForm.getName());
        prj.setDescription(beProjectForm.getDescription());



        DbUtil.saveOrUpdateProject(prj);

        return mapping.findForward("projectList");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEProjectForm beProjectForm = (BEProjectForm) form;
        AmpBudgetExportProject prj = DbUtil.getProjectById(beProjectForm.getId());

        beProjectForm.setName(prj.getName());
        beProjectForm.setDescription(prj.getDescription());

        return mapping.findForward("forward");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEProjectForm beProjectForm = (BEProjectForm) form;
        DbUtil.deleteProjectById(beProjectForm.getId());
        return mapping.findForward("projectList");
    }
}
