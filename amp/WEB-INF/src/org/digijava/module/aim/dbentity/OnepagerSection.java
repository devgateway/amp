package org.digijava.module.aim.dbentity;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "AMP_ONEPAGER_SECTION")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class OnepagerSection implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "className")
    private String className;

    @Column(name = "folded")
    private Boolean folded;

    @Column(name = "position")
    private Integer position;
    //persisted fields


    //non persisted fields
    private String name;
    private Boolean dependent;
    private String dependentClassName;

    public OnepagerSection(){
    }

    public OnepagerSection(String name, String className, int position,
            boolean folded, boolean dependent, String dependentClassName) {
        super();
        this.name = name;
        this.className = className;
        this.position = position;
        this.folded = folded;
        this.dependent = dependent;
        this.dependentClassName = dependentClassName;
    }

    public OnepagerSection(String name, String className, int position,
            boolean folded) {
        this(name, className, position, folded, false, null);
    }

    //set the transient fields from another object
    public void load(OnepagerSection os){
        this.name = os.name;
        this.dependent = os.dependent;
        this.dependentClassName = os.dependentClassName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean getFolded() {
        return folded;
    }

    public void setFolded(Boolean folded) {
        this.folded = folded;
    }

    public Boolean getDependent() {
        return dependent;
    }

    public void setDependent(Boolean dependent) {
        this.dependent = dependent;
    }

    public String getDependentClassName() {
        return dependentClassName;
    }

    public void setDependentClassName(String dependentClassName) {
        this.dependentClassName = dependentClassName;
    }
}
