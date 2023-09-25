package org.digijava.module.aim.dbentity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Table(name = "amp_component_funding_documents")
@Cacheable
@DynamicUpdate
@Entity
public class AmpComponentFundingDocuments {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_component_funding_documents_seq")
    @SequenceGenerator(name = "amp_component_funding_documents_seq", sequenceName = "amp_component_funding_documents_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "file_base_64_string")
    private String fileBase64String;
    @Column(name = "file_name")
    private String fileName;
    @ManyToOne
    @JoinColumn(name="amp_component_funding_id", nullable=false)
    private AmpComponentFunding ampComponentFunding;

    public AmpComponentFunding getAmpComponentFunding() {
        return ampComponentFunding;
    }

    public void setAmpComponentFunding(AmpComponentFunding ampComponentFunding) {
        this.ampComponentFunding = ampComponentFunding;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileBase64String() {
        return fileBase64String;
    }

    public void setFileBase64String(String fileBase64String) {
        this.fileBase64String = fileBase64String;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
