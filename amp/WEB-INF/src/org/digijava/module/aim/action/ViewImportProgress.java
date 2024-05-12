package org.digijava.module.aim.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.dbentity.ImportedFilesRecord;
import org.digijava.module.admin.dbentity.ImportedProject;
import org.digijava.module.aim.form.ImportProgressForm;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewImportProgress extends Action {
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ImportProgressForm importProgressForm = (ImportProgressForm) form;
        if (Objects.equals(request.getParameter("fileRecordId"), "") || request.getParameter("fileRecordId")==null)
        {
           importProgressForm.setImportedFilesRecords(getAllImportFileRecords());
           return mapping.findForward("viewImportProgress");
        }

        int startPage =Integer.parseInt(request.getParameter("startPage"));
        int endPage = importProgressForm.getEndPage();
        Long importProjectId = importProgressForm.getImportedFileRecordId();

        List<ImportedProject> importedProjects = getImportedProjects(startPage, endPage, importProjectId);

        int totalPages = getTotalPages(importedProjects.size(), endPage);

        Map<String, Object> data = new HashMap<>();
        data.put("importedProjects", importedProjects);
        data.put("startPage", startPage);
        data.put("endPage", endPage);
        data.put("totalPages", totalPages);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        String jsonData = objectMapper.writeValueAsString(data);

        response.setContentType("application/json");
        response.getWriter().write(jsonData);

        return null;
    }

    private int getTotalPages(int totalRecords, int recordsPerPage) {
        return (int) Math.ceil((double) totalRecords / recordsPerPage);
    }

    private List<ImportedProject> getImportedProjects(int startPage, int endPage, Long importedFilesRecordId) {
        Session session = PersistenceManager.getRequestDBSession();

        Criteria criteria = session.createCriteria(ImportedProject.class);

        if (importedFilesRecordId!= null) {
            criteria.add(Restrictions.eq("importedFilesRecord", importedFilesRecordId));
        }

        criteria.setFirstResult((startPage - 1) * endPage);
        criteria.setMaxResults(endPage);

        return  criteria.list();
    }

    public List<ImportedFilesRecord> getAllImportFileRecords() {
        Session session = PersistenceManager.getRequestDBSession();

        String hql = "FROM ImportedFilesRecord";
        Query query = session.createQuery(hql);
        List<ImportedFilesRecord> importedFilesRecords = query.list();
        return importedFilesRecords;
    }

    }
