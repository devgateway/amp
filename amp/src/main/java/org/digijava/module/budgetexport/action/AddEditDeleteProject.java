package org.digijava.module.budgetexport.action;

import org.apache.log4j.Logger;
import org.apache.struts.action.*;
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
    private static Logger logger    = Logger.getLogger(AddEditDeleteProject.class);

    public ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        return add(mapping, form, request, response);
    }

    public ActionForward add(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEProjectForm beProjectForm = (BEProjectForm) form;
        beProjectForm.setAvailReports(DbUtil.getAvailReportIdNamePairs());
        beProjectForm.setId(null);
        return mapping.findForward("forward");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEProjectForm beProjectForm = (BEProjectForm) form;
        ActionErrors errors = null;

        AmpBudgetExportProject prj = null;

        if (beProjectForm.getId() == null || beProjectForm.getId().longValue() <= 0l) {
            prj = new AmpBudgetExportProject();
            prj.setCreationDate(new Date());
        } else {
            prj = DbUtil.getProjectById(beProjectForm.getId());
        }

        prj.setAmpReportId(beProjectForm.getSelReport());
        prj.setName(beProjectForm.getName());
        prj.setDescription(beProjectForm.getDescription());
        prj.setMappingImportServiceURL(beProjectForm.getMappingImportServiceURL());
        prj.setServiceActionURL(beProjectForm.getServiceActionURL());
        prj.setDataSource(beProjectForm.getDataSource());


        if (DbUtil.projectExists(beProjectForm.getName(), prj.getId())) {
            if (errors == null) errors = new ActionErrors();
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.budgetExport.notUniqueProjectName"));
        }

        ActionForward fwd = null;
        if (errors == null || errors.isEmpty()) {
            DbUtil.saveOrUpdateProject(prj);
            fwd = mapping.findForward("projectList");
        } else {
            saveErrors(request, errors);
            fwd = mapping.findForward("forward");
        }
        return fwd;
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEProjectForm beProjectForm = (BEProjectForm) form;
        AmpBudgetExportProject prj = DbUtil.getProjectById(beProjectForm.getId());
        beProjectForm.setAvailReports(DbUtil.getAvailReportIdNamePairs());
        beProjectForm.setSelReport(prj.getAmpReportId());
        beProjectForm.setName(prj.getName());
        beProjectForm.setDescription(prj.getDescription());
        beProjectForm.setMappingImportServiceURL(prj.getMappingImportServiceURL());
        beProjectForm.setServiceActionURL(prj.getServiceActionURL());
        beProjectForm.setDataSource(prj.getDataSource());

        return mapping.findForward("forward");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEProjectForm beProjectForm = (BEProjectForm) form;
        DbUtil.deleteProjectById(beProjectForm.getId());
        return mapping.findForward("projectList");
    }

    public ActionForward getDataFromService(ActionMapping mapping, ActionForm form,
                HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {
        BEProjectForm beProjectForm = (BEProjectForm) form;

        return mapping.findForward("projectList");
    }
}
