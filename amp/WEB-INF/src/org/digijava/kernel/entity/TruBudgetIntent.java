package org.digijava.kernel.entity;

import org.digijava.kernel.user.TruBudgetIntentGroup;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;
import java.io.Serializable;

@Table(name = "trubudget_intent")
@Cacheable
@DynamicUpdate
@Entity
public class TruBudgetIntent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trubudget_intent_seq")
    @SequenceGenerator(name = "trubudget_intent_seq", sequenceName = "trubudget_intent_seq", allocationSize = 1)
    @Column(name = "trubudget_intent_id")
    private Long id;
    @Column(name = "trubudget_intent_name")
    private String truBudgetIntentName;
    @Column(name = "trubudget_intent_display_name")

    private String truBudgetIntentDisplayName;

//    @ManyToOne
//    @JoinColumn(name = "intent_group")
    private TruBudgetIntentGroup intentGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTruBudgetIntentName() {
        return truBudgetIntentName;
    }

    public void setTruBudgetIntentName(String truBudgetIntentName) {
        this.truBudgetIntentName = truBudgetIntentName;
    }

    public TruBudgetIntentGroup getIntentGroup() {
        return intentGroup;
    }

    public void setIntentGroup(TruBudgetIntentGroup intentGroup) {
        this.intentGroup = intentGroup;
    }

    public String getTruBudgetIntentDisplayName() {
        return truBudgetIntentDisplayName;
    }

    public void setTruBudgetIntentDisplayName(String truBudgetIntentDisplayName) {
        this.truBudgetIntentDisplayName = truBudgetIntentDisplayName;
    }
}
