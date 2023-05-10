package org.digijava.kernel.entity.integration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class FileRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "file_information_id", referencedColumnName = "id")
    private FileInformation fileInformation;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private String status;

    @Column(name = "error")
    private String error;

    @Column(name = "amp_id")
    private String ampId;

    @Column(name = "row")
    private Integer row;

    private Date createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileInformation getFileInformation() {
        return fileInformation;
    }

    public void setFileInformation(FileInformation fileInformation) {
        this.fileInformation = fileInformation;
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

    public void setAmpId(String activityId) {
        this.ampId = activityId;
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