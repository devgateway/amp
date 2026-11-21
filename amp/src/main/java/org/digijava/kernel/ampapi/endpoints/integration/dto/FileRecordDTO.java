package org.digijava.kernel.ampapi.endpoints.integration.dto;

import org.digijava.kernel.entity.integration.FileRecord;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Date;

public class FileRecordDTO {
    private Long id;
    private Long fileInformationId;
    private String content;
    private String status;
    private String error;
    private String ampId;

    private Integer row;

    private Date createdDate;

    public static FileRecordDTO fromEntity(FileRecord entity) {
        FileRecordDTO dto = new FileRecordDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.fileInformationId = entity.getFileInformation().getId();
        return dto;
    }

    public FileRecord toEntity() {
        FileRecord entity = new FileRecord();
        BeanUtils.copyProperties(this, entity);
        return entity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileInformationId() {
        return fileInformationId;
    }

    public void setFileInformationId(Long fileInformationId) {
        this.fileInformationId = fileInformationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getAmpId() {
        return ampId;
    }

    public void setAmpId(String ampId) {
        this.ampId = ampId;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
