package org.digijava.module.admin.dbentity;

public class ImportedProject {
    private Long id;
    private ImportedFilesRecord importedFilesRecord;
    private String importResponse;
    private ImportStatus importStatus;
    private boolean newProject=true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ImportedFilesRecord getImportedFilesRecord() {
        return importedFilesRecord;
    }

    public void setImportedFilesRecord(ImportedFilesRecord importedFilesRecord) {
        this.importedFilesRecord = importedFilesRecord;
    }

    public String getImportResponse() {
        return importResponse;
    }

    public void setImportResponse(String importResponse) {
        this.importResponse = importResponse;
    }

    public ImportStatus getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }

    public boolean isNewProject() {
        return newProject;
    }

    public void setNewProject(boolean newProject) {
        this.newProject = newProject;
    }

    @Override
    public String toString() {
        return "ImportedProject{" +
                "id=" + id +
                ", importedFilesRecord=" + importedFilesRecord +
                ", importResponse='" + importResponse + '\'' +
                ", importStatus=" + importStatus +
                ", newProject=" + newProject +
                '}';
    }
}
