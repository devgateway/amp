package org.digijava.module.aim.helper;

public class CountryBean {
    Long id;
    String name;
    String iso;
    String iso3;


    public CountryBean() {
    }

    public Long getId() {
        return id;
    }

    public String getIso() {
        return iso;
    }

    public String getIso3() {
        return iso3;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public void setName(String name) {
        this.name = name;
    }
}
