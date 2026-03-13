package org.digijava.module.aim.action.dataimporter.dbentity;

import java.io.Serializable;

public class ImportedFilesRecord implements Serializable {
private Long id;
private String fileName;
private String fileHash;
private Long processingTimeMillis;

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

    public Long getProcessingTimeMillis() {
        return processingTimeMillis;
    }

    public void setProcessingTimeMillis(Long processingTimeMillis) {
        this.processingTimeMillis = processingTimeMillis;
    }

    public String getFormattedProcessingTime() {
        if (processingTimeMillis == null) {
            return "-";
        }
        long minutes = processingTimeMillis / 60000;
        long seconds = (processingTimeMillis % 60000) / 1000;
        return minutes + "m " + seconds + "s";
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
            ", processingTimeMillis=" + processingTimeMillis +
                ", importStatus=" + importStatus +
                '}';
    }
}
