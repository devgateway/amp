package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.admin.dbentity.ImportedProject;

import java.util.List;

public class ImportProgressForm extends ActionForm {
    private List<ImportedProject> importedProjects;
    private int startPage;
    private int endPage;
    private Long totalPages;
    private Long importProjectId;

    public List<ImportedProject> getImportedProjects() {
        return importedProjects;
    }

    public void setImportedProjects(List<ImportedProject> importedProjects) {
        this.importedProjects = importedProjects;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getImportProjectId() {
        return importProjectId;
    }

    public void setImportProjectId(Long importProjectId) {
        this.importProjectId = importProjectId;
    }
}
