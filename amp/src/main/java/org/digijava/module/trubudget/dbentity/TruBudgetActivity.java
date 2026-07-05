package org.digijava.module.trubudget.dbentity;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "amp_trubudget_activity")
@Cacheable
@DynamicUpdate
@Entity
public class TruBudgetActivity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_trubudget_activity_seq")
    @SequenceGenerator(name = "amp_trubudget_activity_seq", sequenceName = "amp_trubudget_activity_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    @Column(name = "trubudget_id")
    private String truBudgetId;
    @Column(name = "amp_id")
    private String ampId;
    @Column(name = "amp_activity_id")
    private Long ampActivityId;
    @Column(name = "project_closed")
    private Boolean projectClosed = Boolean.FALSE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTruBudgetId() {
        return truBudgetId;
    }

    public void setTruBudgetId(String truBudgetId) {
        this.truBudgetId = truBudgetId;
    }

    public String getAmpId() {
        return ampId;
    }

    public void setAmpId(String ampId) {
        this.ampId = ampId;
    }

    public Long getAmpActivityId() {
        return ampActivityId;
    }

    public void setAmpActivityId(Long ampActivityId) {
        this.ampActivityId = ampActivityId;
    }

    public Boolean getProjectClosed() {
        return projectClosed;
    }

    public void setProjectClosed(Boolean projectClosed) {
        this.projectClosed = projectClosed;
    }
}