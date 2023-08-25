package org.digijava.kernel.user;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Set;

//@Entity
//@Table(name = "trubudget_intent_group")
//@Cacheable
//@DynamicUpdate
public class TruBudgetIntentGroup {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trubudget_intent_group_seq")
//    @SequenceGenerator(name = "trubudget_intent_group_seq", sequenceName = "trubudget_intent_group_seq", allocationSize = 1)
//    @Column(name = "trubudget_intent_group_id")
    private Long id;
//    @Column(name = "trubudget_intent_group_name")
    private String name;


//    @OneToMany(mappedBy = "intentGroup")
    private Set<TruBudgetIntent> intents;

    public Set<TruBudgetIntent> getIntents() {
        return intents;
    }

    public void setIntents(Set<TruBudgetIntent> intents) {
        this.intents = intents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
