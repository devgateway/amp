package org.digijava.module.aim.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.admin.dbentity.ImportStatus;
import org.digijava.module.admin.dbentity.ImportedFilesRecord;
import org.digijava.module.admin.dbentity.ImportedProject;
import org.digijava.module.aim.form.ImportProgressForm;
import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewImportProgress extends Action {
    private static Logger logger =LoggerFactory.getLogger(ViewImportProgress.class);
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ImportProgressForm importProgressForm = (ImportProgressForm) form;
        logger.info("Params: "+request.getParameterMap());
        request.setAttribute("importedFilesRecords",getAllImportFileRecords());


        if (request.getParameterMap().containsKey("fileRecordId") && request.getParameter("fileRecordId")!=null){

            int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
            int pageSize = Integer.parseInt(request.getParameter("pageSize"));
            Long importedFilesRecordId =Long.parseLong(request.getParameter("fileRecordId"));

            List<ImportedProject> importedProjects = getImportedProjects(pageNumber, pageSize, importedFilesRecordId);
//            Long failedProjects = importedProjects.stream().filter(project -> project.getImportStatus().equals(ImportStatus.FAILED)).count();
//            Long successfulProjects = importedProjects.stream().filter(project -> project.getImportStatus().equals(ImportStatus.SUCCESS)).count();
//            long totalPages = getTotalPages(importedProjects.size(), pageSize);
            Map<String, Long> variousCounts = getVariousCounts(pageSize, importedFilesRecordId);


            Map<String, Object> data = new HashMap<>();
            data.put("importedProjects", importedProjects);
            data.put("failedProjects", variousCounts.get("failedProjects"));
            data.put("successfulProjects", variousCounts.get("successfulProjects"));
            data.put("totalPages", variousCounts.get("totalPages"));
            data.put("totalProjects", variousCounts.get("totalProjects"));

            ObjectMapper objectMapper = new ObjectMapper();

            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            String jsonData = objectMapper.writeValueAsString(data);

            response.setContentType("application/json");
            logger.info("Json Data: "+jsonData);
            response.getWriter().write(jsonData);
            response.setCharacterEncoding("UTF-8");
            return null;
        }

        return mapping.findForward("viewImportProgress");

    }
    private Map<String, Long> getVariousCounts(int pageSize, Long importedFilesRecordId) {
        Session session = PersistenceManager.getRequestDBSession();

        String hql = "SELECT importStatus, COUNT(*) FROM ImportedProject";

        if (importedFilesRecordId!= null) {
            hql += " WHERE importedFilesRecord.id = :importedFilesRecordId";
        }

        hql += " GROUP BY importStatus";

        Query query = session.createQuery(hql);

        if (importedFilesRecordId!= null) {
            query.setParameter("importedFilesRecordId", importedFilesRecordId);
        }

        List<Object[]> resultList = query.list();

        Map<String, Long> countsMap = new HashMap<>();

        for (Object[] row : resultList) {
            ImportStatus importStatus = (ImportStatus) row[0];
            long count = ((Number) row[1]).longValue();

            if (importStatus.equals(ImportStatus.FAILED)) {
                countsMap.put("failedProjects", count);
            } else if (importStatus.equals(ImportStatus.SUCCESS)) {
                countsMap.put("successfulProjects", count);
            }
        }
        long totalProjects= getTotalProjects(importedFilesRecordId);

        countsMap.put("totalProjects", totalProjects);
        countsMap.put("totalPages", getTotalPages(totalProjects,pageSize));

        return countsMap;
    }




    private long getTotalProjects(Long importedFilesRecordId) {
        Session session = PersistenceManager.getRequestDBSession();

        String hql = "SELECT COUNT(*) FROM ImportedProject";

        if (importedFilesRecordId!= null) {
            hql += " WHERE importedFilesRecord.id = :importedFilesRecordId";
        }

        Query query = session.createQuery(hql);

        if (importedFilesRecordId!= null) {
            query.setParameter("importedFilesRecordId", importedFilesRecordId);
        }

        return ((Number) query.uniqueResult()).longValue();
    }

    private long getTotalPages(long totalRecords, int recordsPerPage) {
        return (int) Math.ceil((double) totalRecords / recordsPerPage);
    }

    private List<ImportedProject> getImportedProjects(int pageNumber, int pageSize, Long importedFilesRecordId) {
        Session session = PersistenceManager.getRequestDBSession();

        String hql = "FROM ImportedProject";

        if (importedFilesRecordId!= null) {
            hql += " WHERE importedFilesRecord.id = :importedFilesRecordId";
        }

        Query query = session.createQuery(hql);

        if (importedFilesRecordId!= null) {
            query.setParameter("importedFilesRecordId", importedFilesRecordId);
        }

        int startRecordIndex = (pageNumber - 1) * pageSize; // Calculate the start index for the query
        query.setFirstResult(startRecordIndex);
        query.setMaxResults(pageSize); // Set the maximum number of records to retrieve

        return query.list();
    }

    public List<ImportedFilesRecord> getAllImportFileRecords() {
        Session session = PersistenceManager.getRequestDBSession();

        String hql = "FROM ImportedFilesRecord";
        Query query = session.createQuery(hql);
        List<ImportedFilesRecord> importedFilesRecords = query.list();
        logger.info("importedFilesRecords: " + importedFilesRecords);
        return importedFilesRecords;
    }

    }
