package org.digijava.module.aim.dbentity;


import javax.persistence.*;

@Entity
@Table(name = "amp_quartz_job_class")
public class AmpQuartzJobClass {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "jc_id")
    private Long id;

    @Column(name = "jc_name")
    private String name;

@Column(name = "sched_name")
    private String schedName;
@Column(name = "jc_class_fullname")
    private String classFullname;

    public AmpQuartzJobClass() {
    }

    public String getClassFullname() {
        return classFullname;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setClassFullname(String classFullname) {
        this.classFullname = classFullname;
    }
}
