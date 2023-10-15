package org.digijava.module.trubudget.dbentity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
@Table(name = "amp_component_funding_tru_wf")
@Cacheable
@DynamicUpdate
@Entity
public class AmpComponentFundingTruWF {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_component_funding_tru_wf_seq")
    @SequenceGenerator(name = "amp_component_funding_tru_wf_seq", sequenceName = "amp_component_funding_tru_wf_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "tru_wf_id")
    private String truWFId;



    @Column(name = "tru_project_id")
    private String truProjectId;
    @Column(name = "tru_subproject_id")
    private String truSubprojectId;

    public String getTruProjectId() {
        return truProjectId;
    }

    public void setTruProjectId(String truProjectId) {
        this.truProjectId = truProjectId;
    }

    public String getTruSubprojectId() {
        return truSubprojectId;
    }

    public void setTruSubprojectId(String truSubprojectId) {
        this.truSubprojectId = truSubprojectId;
    }
    public String getTruWFId() {
        return truWFId;
    }

    public void setTruWFId(String truWFId) {
        this.truWFId = truWFId;
    }

    @Column(name = "amp_component_funding_id")//we use the justAnId field.. a unique uuid.
    private String ampComponentFundingId;

    public String getAmpComponentFundingId() {
        return ampComponentFundingId;
    }

    public void setAmpComponentFundingId(String ampComponentFundingId) {
        this.ampComponentFundingId = ampComponentFundingId;
    }


}
