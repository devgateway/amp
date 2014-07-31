package org.digijava.kernel.ampapi.endpoints.dto;

import java.util.List;

public class Sectors {
    Long id;
    String code;
    String name;
    List<Sectors> children;

    public Sectors() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Sectors> getChildren() {
        return children;
    }

    public void setChildren(List<Sectors> children) {
        this.children = children;
    }

}
