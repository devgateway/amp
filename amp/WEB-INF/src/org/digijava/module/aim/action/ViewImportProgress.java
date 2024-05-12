package org.digijava.module.aim.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.dbentity.ImportedProject;
import org.digijava.module.aim.form.ImportProgressForm;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewImportProgress extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ImportProgressForm importProgressForm = (ImportProgressForm) form;

        int startPage = importProgressForm.getStartPage();
        int endPage = importProgressForm.getEndPage();
        Long importProjectId = importProgressForm.getImportProjectId();

        List<ImportedProject> importedProjects = getImportedProjects(startPage, endPage, importProjectId);

        int totalPages = getTotalPages(importedProjects.size(), endPage);

        Map<String, Object> data = new HashMap<>();
        data.put("importedProjects", importedProjects);
        data.put("startPage", startPage);
        data.put("endPage", endPage);
        data.put("totalPages", totalPages);

        response.setContentType("application/json");
        response.getWriter().write(new org.json.JSONObject(data).toString());

        return null;
    }

    private int getTotalPages(int totalRecords, int recordsPerPage) {
        return (int) Math.ceil((double) totalRecords / recordsPerPage);
    }

    private List<ImportedProject> getImportedProjects(int startPage, int endPage, Long importProjectId) {
        Session session = PersistenceManager.getRequestDBSession();

        Criteria criteria = session.createCriteria(ImportedProject.class);

        if (importProjectId!= null) {
            criteria.add(Restrictions.eq("id", importProjectId));
        }

        criteria.setFirstResult((startPage - 1) * endPage);
        criteria.setMaxResults(endPage);

        return  criteria.list();
    }
    }
