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

    public String getTruWFId() {
        return truWFId;
    }

    public void setTruWFId(String truWFId) {
        this.truWFId = truWFId;
    }

    @Column(name = "amp_component_funding_id")
    private Long ampComponentFundingId;

    public Long getAmpComponentFundingId() {
        return ampComponentFundingId;
    }

    public void setAmpComponentFundingId(Long ampComponentFundingId) {
        this.ampComponentFundingId = ampComponentFundingId;
    }


}
