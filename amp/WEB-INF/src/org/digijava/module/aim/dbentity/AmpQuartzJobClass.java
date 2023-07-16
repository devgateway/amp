package org.digijava.module.aim.dbentity;

public class AmpQuartzJobClass {
    private Long id;
    private String name;


    private String schedName;
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
