package org.digijava.module.aim.action.dataimporter.dbentity;

import java.io.Serializable;

public class ImportedFilesRecord implements Serializable {
private Long id;
private String fileName;
private String fileHash;

private ImportStatus importStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }

    public ImportStatus getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(ImportStatus importStatus) {
        this.importStatus = importStatus;
    }

    @Override
    public String toString() {
        return "ImportedFilesRecord{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileHash='" + fileHash + '\'' +
                ", importStatus=" + importStatus +
                '}';
    }
}
