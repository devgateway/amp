package org.digijava.module.trubudget.dbentity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Table(name = "amp_trubudget_subproject_component")
@Cacheable
@DynamicUpdate
@Entity
public class SubProjectComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_trubudget_subproject_component_seq")
    @SequenceGenerator(name = "amp_trubudget_subproject_component_seq", sequenceName = "amp_trubudget_subproject_component_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    private Long componentId;
    private String subProjectId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public String getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(String subProjectId) {
        this.subProjectId = subProjectId;
    }
}
