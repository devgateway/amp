package org.digijava.module.trubudget.dbentity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "amp_component_tru_sub_project")
@Cacheable
@DynamicUpdate
@Entity
public class AmpComponentTruSubProject implements Serializable {
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

    @Column(name = "component_just_an_id")//stable across activity versions, unlike amp_component_id
    private String componentJustAnId;

    public String getComponentJustAnId() {
        return componentJustAnId;
    }

    public void setComponentJustAnId(String componentJustAnId) {
        this.componentJustAnId = componentJustAnId;
    }

    @Column(name = "sub_project_closed")
    private Boolean subProjectClosed = Boolean.FALSE;

    public Boolean getSubProjectClosed() {
        return subProjectClosed;
    }

    public void setSubProjectClosed(Boolean subProjectClosed) {
        this.subProjectClosed = subProjectClosed;
    }

    public String getTruProjectId() {
        return truProjectId;
    }

    public void setTruProjectId(String truProjectId) {
        this.truProjectId = truProjectId;
    }

    @Column(name = "tru_project_id")
    private String truProjectId;

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