package org.digijava.kernel.user;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

//@Entity
//@Table(name = "trubudget_intent")
//@Cacheable
//@DynamicUpdate
public class TruBudgetIntent implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trubudget_intent_seq")
//    @SequenceGenerator(name = "trubudget_intent_seq", sequenceName = "trubudget_intent_seq", allocationSize = 1)
//    @Column(name = "trubudget_intent_id")
    private Long id;
//    @Column(name = "trubudget_intent_name")
    private String truBudgetIntentName;
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
