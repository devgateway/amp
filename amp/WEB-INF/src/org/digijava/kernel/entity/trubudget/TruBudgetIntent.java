package org.digijava.kernel.entity.trubudget;

import org.digijava.kernel.user.User;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Table(name = "amp_trubudget_intent")
@Cacheable
@DynamicUpdate
@Entity
public class TruBudgetIntent implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "amp_trubudget_intent_seq")
    @SequenceGenerator(name = "amp_trubudget_intent_seq", sequenceName = "amp_trubudget_intent_seq", allocationSize = 1)
    @Column(name = "trubudget_intent_id")
    private Long id;
    @Column(name = "trubudget_intent_name")
    private String truBudgetIntentName;
    @Column(name = "trubudget_intent_display_name")

    private String truBudgetIntentDisplayName;

    private Set<User> users;

    @Transient
    private Boolean userHas;

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


    public String getTruBudgetIntentDisplayName() {
        return truBudgetIntentDisplayName;
    }

    public void setTruBudgetIntentDisplayName(String truBudgetIntentDisplayName) {
        this.truBudgetIntentDisplayName = truBudgetIntentDisplayName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Boolean getUserHas() {
        return userHas;
    }

    public void setUserHas(Boolean userHas) {
        this.userHas = userHas;
    }
}
