package org.digijava.module.aim.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedFilesRecord;
import org.digijava.module.aim.action.dataimporter.dbentity.ImportedProject;

import java.util.ArrayList;
import java.util.List;

public class ImportProgressForm extends ActionForm {
    private List<ImportedProject> importedProjects;
    private List<ImportedFilesRecord> importedFilesRecords=new ArrayList<>();
    private int startPage=1;
    private int endPage=10;
    private Long totalPages;
    private Long importedFileRecordId;

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

    public Long getImportedFileRecordId() {
        return importedFileRecordId;
    }

    public void setImportedFileRecordId(Long importedFileRecordId) {
        this.importedFileRecordId = importedFileRecordId;
    }

    public List<ImportedFilesRecord> getImportedFilesRecords() {
        return importedFilesRecords;
    }

    public void setImportedFilesRecords(List<ImportedFilesRecord> importedFilesRecords) {
        this.importedFilesRecords = importedFilesRecords;
    }
}
