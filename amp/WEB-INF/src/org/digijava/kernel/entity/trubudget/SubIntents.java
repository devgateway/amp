package org.digijava.kernel.entity.trubudget;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Table(name = "amp_trubudget_sub_intent")
@Cacheable
@DynamicUpdate
@Entity
public class SubIntents {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_trubudget_sub_intent_seq")
    @SequenceGenerator(name = "amp_trubudget_sub_intent_seq", sequenceName = "amp_trubudget_sub_intent_seq", allocationSize = 1)
    @Column(name = "sub_trubudget_intent_id")
    private Long id;
    @Column(name = "sub_trubudget_intent_name")
    private String subTruBudgetIntentName;
    @Column(name = "sub_trubudget_intent_display_name")

    private String subTruBudgetIntentDisplayName;
    @Column(name = "mother_intent_name")
    private String motherIntentName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getMotherIntentName() {
        return motherIntentName;
    }

    public void setMotherIntentName(String motherIntentName) {
        this.motherIntentName = motherIntentName;
    }

    public String getSubTruBudgetIntentName() {
        return subTruBudgetIntentName;
    }

    public void setSubTruBudgetIntentName(String subTruBudgetIntentName) {
        this.subTruBudgetIntentName = subTruBudgetIntentName;
    }

    public String getSubTruBudgetIntentDisplayName() {
        return subTruBudgetIntentDisplayName;
    }

    public void setSubTruBudgetIntentDisplayName(String subTruBudgetIntentDisplayName) {
        this.subTruBudgetIntentDisplayName = subTruBudgetIntentDisplayName;
    }
}
