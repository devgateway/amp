package org.digijava.module.trubudget.dbentity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
@Table(name = "amp_component_tru_sub_project")
@Cacheable
@DynamicUpdate
@Entity
public class AmpComponentTruSubProject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_component_tru_sub_project_seq")
    @SequenceGenerator(name = "amp_component_tru_sub_project_seq", sequenceName = "amp_component_tru_sub_project_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "amp_component_id")
    private Long ampComponentId;

    @Column(name = "tru_sub_project_id")
    private String truSubProjectId;

    public Long getAmpComponentId() {
        return ampComponentId;
    }

    public void setAmpComponentId(Long ampComponentId) {
        this.ampComponentId = ampComponentId;
    }

    public String getTruSubProjectId() {
        return truSubProjectId;
    }

    public void setTruSubProjectId(String truSubProjectId) {
        this.truSubProjectId = truSubProjectId;
    }
}
