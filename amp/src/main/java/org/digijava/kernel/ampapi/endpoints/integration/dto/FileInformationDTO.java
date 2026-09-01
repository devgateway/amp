package org.digijava.kernel.ampapi.endpoints.integration.dto;

import org.digijava.kernel.entity.integration.FileInformation;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class FileInformationDTO {
    private Long id;
    private String fileName;
    private Integer records;
    private Integer success;
    private Integer error;
    private String status;
    private Date createdDate;

    public Set<FileRecordDTO> getFileRecords() {
        return fileRecords;
    }

    public void setFileRecords(Set<FileRecordDTO> fileRecords) {
        this.fileRecords = fileRecords;
    }

    private Set<FileRecordDTO> fileRecords;

    public static FileInformationDTO fromEntity(FileInformation entity) {
        FileInformationDTO dto = new FileInformationDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getFileRecords() != null && !entity.getFileRecords().isEmpty()) {
            dto.setFileRecords(entity.getFileRecords().
                    stream().map(fileRecord -> FileRecordDTO.fromEntity(fileRecord)).collect(Collectors.toSet()));
        }
        return dto;
    }

    public FileInformation toEntity() {
        FileInformation entity = new FileInformation();
        BeanUtils.copyProperties(this, entity);
        return entity;
    }

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

    public Integer getRecords() {
        return records;
    }

    public void setRecords(Integer records) {
        this.records = records;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}